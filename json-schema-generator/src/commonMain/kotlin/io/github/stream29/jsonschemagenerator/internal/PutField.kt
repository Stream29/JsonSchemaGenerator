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

private inline fun <reified T> JsonObjectBuilder.findAnnotationAnd(
    annotations: Iterable<Annotation>,
    action: (T) -> Unit
) = annotations.asSequence()
    .filterIsInstance<T>()
    .firstOrNull()
    ?.also(action)


internal fun JsonObjectBuilder.putComment(annotations: Iterable<Annotation>) =
    findAnnotationAnd<Comment>(annotations) { put("${'$'}comment", it.value) }

internal fun JsonObjectBuilder.putDescription(annotations: Iterable<Annotation>) =
    findAnnotationAnd<Description>(annotations) { put("description", it.value) }

internal fun JsonObjectBuilder.putFormat(annotations: Iterable<Annotation>) =
    findAnnotationAnd<Format>(annotations) { put("format", it.value) }

internal fun JsonObjectBuilder.putPattern(annotations: Iterable<Annotation>) =
    findAnnotationAnd<Pattern>(annotations) { put("pattern", it.value) }

internal fun JsonObjectBuilder.putMaxLength(annotations: Iterable<Annotation>) =
    findAnnotationAnd<MaxLength>(annotations) { put("maxLength", it.value) }

internal fun JsonObjectBuilder.putMinLength(annotations: Iterable<Annotation>) =
    findAnnotationAnd<MinLength>(annotations) { put("minLength", it.value) }

internal fun JsonObjectBuilder.putMultipleOf(annotations: Iterable<Annotation>) =
    findAnnotationAnd<MultipleOf>(annotations) { put("multipleOf", it.value) }

internal fun JsonObjectBuilder.putMinimum(annotations: Iterable<Annotation>) =
    findAnnotationAnd<Minimum>(annotations) { put("minimum", it.value) }

internal fun JsonObjectBuilder.putMaximum(annotations: Iterable<Annotation>) =
    findAnnotationAnd<Maximum>(annotations) { put("maximum", it.value) }

internal fun JsonObjectBuilder.putExclusiveMinimum(annotations: Iterable<Annotation>) =
    findAnnotationAnd<ExclusiveMinimum>(annotations) { put("exclusiveMinimum", it.value) }

internal fun JsonObjectBuilder.putExclusiveMaximum(annotations: Iterable<Annotation>) =
    findAnnotationAnd<ExclusiveMaximum>(annotations) { put("exclusiveMaximum", it.value) }

internal fun JsonObjectBuilder.putMultipleOfDouble(annotations: Iterable<Annotation>) =
    findAnnotationAnd<MultipleOfDouble>(annotations) { put("multipleOf", it.value) }

internal fun JsonObjectBuilder.putMinimumDouble(annotations: Iterable<Annotation>) =
    findAnnotationAnd<MinimumDouble>(annotations) { put("minimum", it.value) }

internal fun JsonObjectBuilder.putMaximumDouble(annotations: Iterable<Annotation>) =
    findAnnotationAnd<MaximumDouble>(annotations) { put("maximum", it.value) }

internal fun JsonObjectBuilder.putExclusiveMinimumDouble(annotations: Iterable<Annotation>) =
    findAnnotationAnd<ExclusiveMinimumDouble>(annotations) { put("exclusiveMinimum", it.value) }

internal fun JsonObjectBuilder.putExclusiveMaximumDouble(annotations: Iterable<Annotation>) =
    findAnnotationAnd<ExclusiveMaximumDouble>(annotations) { put("exclusiveMaximum", it.value) }
