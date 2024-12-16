package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonObject

public fun SerialDescriptor.toSchema(): JsonObject = toSchema(emptyList())

@OptIn(ExperimentalSerializationApi::class)
internal fun SerialDescriptor.toSchema(propertyAnnotations: Iterable<Annotation>): JsonObject {
    if (isInline) return elementDescriptors.single().toSchema(propertyAnnotations + annotations)
    return when (val kind = kind) {
        is PrimitiveKind -> buildJsonObject {
            putType(kind.typeName, isNullable)
            putDescription(propertyAnnotations, annotations)
        }

        is PolymorphicKind -> throw SerializationException("Polymorphic descriptor is not supported")
        SerialKind.CONTEXTUAL -> throw SerializationException("Contextual descriptor is not supported")
        StructureKind.OBJECT -> buildJsonObject { }
        SerialKind.ENUM -> buildJsonObject {
            putEnum(elementNames, isNullable)
            putDescription(propertyAnnotations, annotations)
        }

        StructureKind.CLASS -> buildJsonObject {
            putType("object", isNullable)
            putDescription(propertyAnnotations, annotations)
            putProperties(this@toSchema)
            putRequired(this@toSchema)
        }

        StructureKind.MAP -> buildJsonObject {
            val (keyDescriptor, valueDescriptor) = elementDescriptors.toList()
            if (keyDescriptor.kind != PrimitiveKind.STRING) throw SerializationException("Map key must be a string")
            putType("object", isNullable)
            putDescription(propertyAnnotations, annotations)
            putJsonObject("additionalProperties") { put("type", valueDescriptor.toSchema()) }
        }

        StructureKind.LIST -> buildJsonObject {
            putType("array", isNullable)
            putDescription(propertyAnnotations, annotations)
            put("items", elementDescriptors.single().toSchema())
        }
    }
}
