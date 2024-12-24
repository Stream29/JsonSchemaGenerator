package io.github.stream29.jsonschemagenerator.internal

import io.github.stream29.jsonschemagenerator.*
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
internal fun JsonObjectBuilder.putProperties(descriptor: SerialDescriptor) = with(descriptor) {
    putJsonObject("properties") {
        for (i in 0..<elementsCount) {
            put(getElementName(i), getElementDescriptor(i).toSchema(getElementAnnotations(i)))
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

internal fun JsonObjectBuilder.putDescription(annotations: Iterable<Annotation>) {
    annotations.asSequence()
        .filterIsInstance<Description>()
        .firstOrNull()
        ?.let { put("description", it.value) }
}

internal fun JsonObjectBuilder.putComment(annotations: Iterable<Annotation>) {
    annotations.asSequence()
        .filterIsInstance<Comment>()
        .firstOrNull()
        ?.let { put("${'$'}comment", it.value) }
}

internal fun JsonObjectBuilder.putMinLength(annotations: Iterable<Annotation>) {
    annotations.asSequence()
        .filterIsInstance<MinLength>()
        .firstOrNull()
        ?.let { put("minLength", it.value) }
}

internal fun JsonObjectBuilder.putMaxLength(annotations: Iterable<Annotation>) {
    annotations.asSequence()
        .filterIsInstance<MaxLength>()
        .firstOrNull()
        ?.let { put("maxLength", it.value) }
}

internal fun JsonObjectBuilder.putPattern(annotations: Iterable<Annotation>) {
    annotations.asSequence()
        .filterIsInstance<Pattern>()
        .firstOrNull()
        ?.let { put("pattern", it.value) }
}

internal fun JsonObjectBuilder.putFormat(annotations: Iterable<Annotation>) {
    annotations.asSequence()
        .filterIsInstance<Format>()
        .firstOrNull()
        ?.let { put("format", it.value) }
}