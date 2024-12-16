package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.*

internal fun JsonObjectBuilder.putType(typeName: String, nullable: Boolean) {
    if (nullable) {
        putJsonArray("type") {
            add(typeName)
            add("null")
        }
    } else {
        put("type", typeName)
    }
}

internal fun JsonObjectBuilder.putEnum(enumNames: Iterable<String>, nullable: Boolean) {
    if (nullable) putType("string", true)
    putJsonArray("enum") { enumNames.forEach { add(it) } }
}

@OptIn(ExperimentalSerializationApi::class)
internal fun JsonObjectBuilder.putProperties(descriptor: SerialDescriptor)= with(descriptor) {
    putJsonObject("properties") {
        for(i in 0..<elementsCount) {
            put(getElementName(i),getElementDescriptor(i).toSchema(getElementAnnotations(i)))
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal fun JsonObjectBuilder.putRequired(descriptor: SerialDescriptor) = with(descriptor) {
    if ((0..<elementsCount).any { !isElementOptional(it) }) {
        putJsonArray("required") {
            elementNames.forEachIndexed { index, name ->
                if (!isElementOptional(index)) add(name)
            }
        }
    }
}
