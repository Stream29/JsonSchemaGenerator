package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.*

@OptIn(ExperimentalSerializationApi::class)
public data class SchemaBuildingContext(
    val descriptor: SerialDescriptor,
    val annotations: List<Annotation>,
    val generator: SchemaGenerator
) {

    public fun JsonObjectBuilder.putType() = putType(descriptor.kind.jsonTypeName)

    public fun JsonObjectBuilder.putType(typeName: String) {
        if (descriptor.isNullable) {
            putJsonArray("type") {
                add(typeName)
                add("null")
            }
        } else {
            put("type", typeName)
        }
    }

    public fun JsonObjectBuilder.putEnum() {
        putJsonArray("enum") {
            descriptor.elementNames.forEach { add(it) }
            if (descriptor.isNullable) add(null)
        }
    }

    public fun JsonObjectBuilder.putProperties() = with(descriptor) {
        putJsonObject("properties") {
            for (i in 0..<elementsCount) {
                put(getElementName(i), generator.schemaOf(getElementDescriptor(i), getElementAnnotations(i)))
            }
        }
    }


    public fun JsonObjectBuilder.putItems() = with(descriptor) {
        put("items", generator.schemaOf(elementDescriptors.single(), annotations))
    }


    public fun JsonObjectBuilder.putRequired() = with(descriptor) {
        if ((0..<elementsCount).any { !isElementOptional(it) }) {
            putJsonArray("required") {
                elementNames.forEachIndexed { index, name ->
                    if (!isElementOptional(index)) add(name)
                }
            }
        }
    }

    public fun JsonObjectBuilder.putAdditionalProperties(valueDescriptor: SerialDescriptor) {
        putJsonObject("additionalProperties") { put("type", generator.schemaOf(valueDescriptor, emptyList())) }
    }

    public inline fun <reified T> JsonObjectBuilder.findAnnotationAnd(
        annotations: Iterable<Annotation>,
        action: (T) -> Unit
    ) = annotations.asSequence()
        .filterIsInstance<T>()
        .firstOrNull()
        ?.also(action)

    public fun JsonObjectBuilder.putComment() =
        findAnnotationAnd<Comment>(annotations) { put("${'$'}comment", it.value) }

    public fun JsonObjectBuilder.putTitle() =
        findAnnotationAnd<Title>(annotations) { put("title", it.value) }

    public fun JsonObjectBuilder.putDescription() =
        findAnnotationAnd<Description>(annotations) { put("description", it.value) }

    public fun JsonObjectBuilder.putFormat() =
        findAnnotationAnd<Format>(annotations) { put("format", it.value) }

    public fun JsonObjectBuilder.putPattern() =
        findAnnotationAnd<Pattern>(annotations) { put("pattern", it.value) }

    public fun JsonObjectBuilder.putMaxLength() =
        findAnnotationAnd<MaxLength>(annotations) { put("maxLength", it.value) }

    public fun JsonObjectBuilder.putMinLength() =
        findAnnotationAnd<MinLength>(annotations) { put("minLength", it.value) }

    public fun JsonObjectBuilder.putMultipleOf() =
        findAnnotationAnd<MultipleOf>(annotations) { put("multipleOf", it.value) }

    public fun JsonObjectBuilder.putMinimum() =
        findAnnotationAnd<Minimum>(annotations) { put("minimum", it.value) }

    public fun JsonObjectBuilder.putMaximum() =
        findAnnotationAnd<Maximum>(annotations) { put("maximum", it.value) }

    public fun JsonObjectBuilder.putExclusiveMinimum() =
        findAnnotationAnd<ExclusiveMinimum>(annotations) { put("exclusiveMinimum", it.value) }

    public fun JsonObjectBuilder.putExclusiveMaximum() =
        findAnnotationAnd<ExclusiveMaximum>(annotations) { put("exclusiveMaximum", it.value) }

    public fun JsonObjectBuilder.putMultipleOfDouble() =
        findAnnotationAnd<MultipleOfDouble>(annotations) { put("multipleOf", it.value) }

    public fun JsonObjectBuilder.putMinimumDouble() =
        findAnnotationAnd<MinimumDouble>(annotations) { put("minimum", it.value) }

    public fun JsonObjectBuilder.putMaximumDouble() =
        findAnnotationAnd<MaximumDouble>(annotations) { put("maximum", it.value) }

    public fun JsonObjectBuilder.putExclusiveMinimumDouble() =
        findAnnotationAnd<ExclusiveMinimumDouble>(annotations) { put("exclusiveMinimum", it.value) }

    public fun JsonObjectBuilder.putExclusiveMaximumDouble() =
        findAnnotationAnd<ExclusiveMaximumDouble>(annotations) { put("exclusiveMaximum", it.value) }
}