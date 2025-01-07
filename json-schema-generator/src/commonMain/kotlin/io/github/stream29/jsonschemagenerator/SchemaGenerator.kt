package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.serializer

public typealias SchemaGeneratingInstruction = SchemaBuildingContext.() -> JsonObject

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

    val encodePolymorphic: SchemaGeneratingInstruction = { TODO() },
    val encodeContextual: SchemaGeneratingInstruction = { TODO() },
    val encodeObject: SchemaGeneratingInstruction = { TODO() },
) {
    public companion object Default {
        public val default: SchemaGenerator = SchemaGenerator()
    }
}

public inline fun <reified T> SchemaGenerator.Default.from(): JsonObject = default.schemaOf<T>()

public inline fun <reified T> SchemaGenerator.schemaOf(): JsonObject =
    schemaOf(serializer<T>().descriptor, emptyList())

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
