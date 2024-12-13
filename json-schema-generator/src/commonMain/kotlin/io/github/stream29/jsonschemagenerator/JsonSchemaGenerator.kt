package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.*

@OptIn(ExperimentalSerializationApi::class)
public fun SerialDescriptor.toSchema(): JsonObject {
    if (isInline) return elementDescriptors.single().toSchema()
    return when (val kind = kind) {
        is PrimitiveKind -> buildJsonObject { putType(kind.typeName, isNullable) }
        is PolymorphicKind -> throw SerializationException("Polymorphic descriptor is not supported")
        SerialKind.CONTEXTUAL -> throw SerializationException("Contextual descriptor is not supported")
        StructureKind.OBJECT -> buildJsonObject { }
        SerialKind.ENUM -> buildJsonObject {
            if (isNullable) putType("string", true)
            putJsonArray("enum") { elementNames.forEach { add(it) } }
        }

        StructureKind.CLASS -> buildJsonObject {
            putType("object", isNullable)
            putJsonObject("properties") {
                elements.forEach { (name, descriptor) ->
                    put(name, descriptor.toSchema())
                }
            }
            if ((0..elementsCount).any { !isElementOptional(it) }) {
                putJsonArray("required") {
                    elementNames.forEachIndexed { index, name ->
                        if (!isElementOptional(index)) add(name)
                    }
                }
            }
        }

        StructureKind.MAP -> buildJsonObject {
            val (keyDescriptor, valueDescriptor) = elementDescriptors.toList()
            if (keyDescriptor.kind != PrimitiveKind.STRING) throw SerializationException("Map key must be a string")
            putType("object", isNullable)
            putJsonObject("additionalProperties") { put("type", valueDescriptor.toSchema()) }
        }

        StructureKind.LIST -> buildJsonObject {
            putType("array", isNullable)
            put("items", elementDescriptors.single().toSchema())
        }
    }
}
