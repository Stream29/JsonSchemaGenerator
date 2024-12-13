package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.add
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray

@OptIn(ExperimentalSerializationApi::class)
internal val SerialDescriptor.elements
    get() = (0..<elementsCount).associate { getElementName(it) to getElementDescriptor(it) }

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