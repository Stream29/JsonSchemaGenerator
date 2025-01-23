package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.serializer

/**
 * Function type of instructions to generate a schema for certain type.
 */
public typealias SchemaGeneratingInstruction = SchemaBuildingContext.() -> JsonObject

/**
 * A generator to generate a JSON schema for a given type.
 *
 * It is a immutable class so it can be shared across multiple threads.
 *
 * @property encodePrimitive Instruction to generate schema for primitive types.
 * @property encodeEnum Instruction to generate schema for enum types.
 * @property encodeClass Instruction to generate schema for class types.
 * @property encodeArray Instruction to generate schema for array types.
 * @property encodeMap Instruction to generate schema for map types.
 * @property encodePolymorphic Instruction to generate schema for polymorphic types.
 * @property encodeContextual Instruction to generate schema for contextual types.
 * @property encodeObject Instruction to generate schema for singleton object types.
 */
@OptIn(ExperimentalSerializationApi::class)
public data class SchemaGenerator(
    val encodePrimitive: SchemaGeneratingInstruction = {
        buildJsonObject {
            putComment()
            putTitle()
            putType()
            putDescription()
            when (descriptor.kind.jsonTypeName) {
                "string" -> {
                    putFormat()
                    putPattern()
                    putMinLength()
                    putMaxLength()
                }

                "integer", "number" -> {
                    putMinimum() ?: putMinimumDouble()
                    putMaximum() ?: putMaximumDouble()
                    putExclusiveMinimum() ?: putExclusiveMinimumDouble()
                    putExclusiveMaximum() ?: putExclusiveMaximumDouble()
                    putMultipleOf() ?: putMultipleOfDouble()
                }

                else -> {}
            }
        }
    },

    val encodeEnum: SchemaGeneratingInstruction = {
        buildJsonObject {
            putComment()
            putTitle()
            putEnum()
            putDescription()
        }
    },
    val encodeClass: SchemaGeneratingInstruction = {
        buildJsonObject {
            putComment()
            putTitle()
            putType()
            putDescription()
            putProperties()
            putRequired()
        }
    },
    val encodeArray: SchemaGeneratingInstruction = {
        buildJsonObject {
            putComment()
            putTitle()
            putType()
            putDescription()
            putItems()
        }
    },
    val encodeMap: SchemaGeneratingInstruction = {
        buildJsonObject {
            val (keyDescriptor, valueDescriptor) = descriptor.elementDescriptors.toList()
            if (keyDescriptor.kind != PrimitiveKind.STRING) throw SerializationException("Map key must be a string")
            putComment()
            putTitle()
            putType()
            putDescription()
            putAdditionalProperties(valueDescriptor)
        }
    },

    val encodePolymorphic: SchemaGeneratingInstruction = {
        buildJsonObject {
            putComment()
            putTitle()
            putType()
            putDescription()
            putSealedSchemas()
        }
    },
    val encodeContextual: SchemaGeneratingInstruction = { TODO() },
    val encodeObject: SchemaGeneratingInstruction = { TODO() },
) {
    /**
     * Global default for [SchemaGenerator].
     */
    public companion object Default {
        public val default: SchemaGenerator = SchemaGenerator()
    }
}

/**
 * Generate a schema for the given type [T].
 */
public inline fun <reified T> SchemaGenerator.Default.from(): JsonObject = default.schemaOf<T>()

/**
 * Generate a schema for the given type [T].
 */
public inline fun <reified T> SchemaGenerator.schemaOf(): JsonObject =
    schemaOf(serializer<T>().descriptor, emptyList())

/**
 * Generate a schema for the given [descriptor] with [annotations].
 */
@OptIn(ExperimentalSerializationApi::class)
public fun SchemaGenerator.schemaOf(descriptor: SerialDescriptor, annotations: List<Annotation>): JsonObject {
    val context = SchemaBuildingContext(descriptor, annotations + descriptor.annotations, this)
    if (descriptor.isInline)
        return schemaOf(
            descriptor.elementDescriptors.single(),
            context.annotations + descriptor.getElementAnnotations(0)
        )
    return when (descriptor.kind) {
        is PrimitiveKind -> context.encodePrimitive()
        is PolymorphicKind -> context.encodePolymorphic()
        SerialKind.CONTEXTUAL -> context.encodeContextual()
        StructureKind.OBJECT -> context.encodeObject()
        SerialKind.ENUM -> context.encodeEnum()
        StructureKind.CLASS -> context.encodeClass()
        StructureKind.MAP -> context.encodeMap()
        StructureKind.LIST -> context.encodeArray()
    }
}
