package io.github.stream29.jsonschemagenerator.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonObject

@OptIn(ExperimentalSerializationApi::class)
internal fun SerialDescriptor.toSchema(propertyAnnotations: Iterable<Annotation>): JsonObject {
    val allAnnotations = propertyAnnotations + annotations
    if (isInline) return elementDescriptors.single().toSchema(allAnnotations)
    return when (val kind = kind) {
        is PrimitiveKind -> buildJsonObject {
            putComment(allAnnotations)
            val jsonType = kind.jsonType
            putType(jsonType.toString(), isNullable)
            putDescription(allAnnotations)
            when(jsonType) {
                JsonPrimitiveType.STRING -> {
                    putFormat(allAnnotations)
                    putPattern(allAnnotations)
                    putMinLength(allAnnotations)
                    putMaxLength(allAnnotations)
                }
                JsonPrimitiveType.INTEGER, JsonPrimitiveType.NUMBER -> {
                    putMinimum(allAnnotations) ?: putMinimumDouble(allAnnotations)
                    putMaximum(allAnnotations) ?: putMaximumDouble(allAnnotations)
                    putExclusiveMinimum(allAnnotations) ?: putExclusiveMinimumDouble(allAnnotations)
                    putExclusiveMaximum(allAnnotations) ?: putExclusiveMaximumDouble(allAnnotations)
                    putMultipleOf(allAnnotations) ?: putMultipleOfDouble(allAnnotations)
                }
                else -> {}
            }
        }

        is PolymorphicKind -> throw SerializationException("Polymorphic descriptor is not supported")
        SerialKind.CONTEXTUAL -> throw SerializationException("Contextual descriptor is not supported")
        StructureKind.OBJECT -> buildJsonObject { }
        SerialKind.ENUM -> buildJsonObject {
            putComment(allAnnotations)
            putEnum(elementNames, isNullable)
            putDescription(allAnnotations)
        }

        StructureKind.CLASS -> buildJsonObject {
            putComment(allAnnotations)
            putType("object", isNullable)
            putDescription(allAnnotations)
            putProperties(this@toSchema)
            putRequired(this@toSchema)
        }

        StructureKind.MAP -> buildJsonObject {
            val (keyDescriptor, valueDescriptor) = elementDescriptors.toList()
            if (keyDescriptor.kind != PrimitiveKind.STRING) throw SerializationException("Map key must be a string")
            putComment(allAnnotations)
            putType("object", isNullable)
            putDescription(allAnnotations)
            putJsonObject("additionalProperties") { put("type", valueDescriptor.toSchema(emptyList())) }
        }

        StructureKind.LIST -> buildJsonObject {
            putComment(allAnnotations)
            putType("array", isNullable)
            putDescription(allAnnotations)
            put("items", elementDescriptors.single().toSchema(emptyList()))
        }
    }
}