package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.*

/**
 * All information needed for building a JSON Schema.
 *
 * Also, a receiver for a group of functions that help to build the schema as context receivers.
 *
 * @property descriptor The [SerialDescriptor] of the current type.
 * @property annotations The annotations on the current property and current type.
 * @property generator The [SchemaGenerator] that contains the full instruction to build the schema.
 */
@OptIn(ExperimentalSerializationApi::class)
public data class SchemaBuildingContext(
    val descriptor: SerialDescriptor,
    val annotations: List<Annotation>,
    val generator: SchemaGenerator
) {

    /**
     * Put the field "type" into the [JsonObject] for schema.
     *
     * The type name and nullability are inferred from the [SchemaBuildingContext].
     */
    public fun JsonObjectBuilder.putType() = putType(descriptor.kind.jsonTypeName, descriptor.isNullable)

    /**
     * Put the field "type" into the [JsonObject] for schema.
     *
     * Use it if you want to customize the type name and nullability.
     */
    public fun JsonObjectBuilder.putType(typeName: String, nullable: Boolean) {
        if (nullable) {
            putJsonArray("type") {
                add(typeName)
                add("null")
            }
        } else {
            put("type", typeName)
        }
    }

    /**
     * Put the field "enum" into the [JsonObject] for schema of enums.
     *
     * The enum values are inferred from the [SchemaBuildingContext].
     */
    public fun JsonObjectBuilder.putEnum() {
        putJsonArray("enum") {
            descriptor.elementNames.forEach { add(it) }
            if (descriptor.isNullable) add(null)
        }
    }

    /**
     * Put the field "properties" into the [JsonObject] for schema of class.
     *
     * The properties are inferred from the [SchemaBuildingContext].
     */
    public fun JsonObjectBuilder.putProperties() = with(descriptor) {
        putJsonObject("properties") {
            for (i in 0..<elementsCount) {
                put(getElementName(i), generator.schemaOf(getElementDescriptor(i), getElementAnnotations(i)))
            }
        }
    }

    /**
     * Put the field "items" into the [JsonObject] for schema of arrays.
     *
     * The item type is inferred from the [SchemaBuildingContext].
     */
    public fun JsonObjectBuilder.putItems() = with(descriptor) {
        put("items", generator.schemaOf(elementDescriptors.single(), annotations))
    }

    /**
     * Put the field "required" into the [JsonObject] for schema of class.
     *
     * The required fields are inferred from the [SchemaBuildingContext].
     */
    public fun JsonObjectBuilder.putRequired() = with(descriptor) {
        if ((0..<elementsCount).any { !isElementOptional(it) }) {
            putJsonArray("required") {
                elementNames.forEachIndexed { index, name ->
                    if (!isElementOptional(index)) add(name)
                }
            }
        }
    }

    /**
     * Return the schema of the [descriptor] and add a required field "type" with the [SerialDescriptor.serialName].
     */
    public fun SchemaGenerator.sealedSchemaOf(descriptor: SerialDescriptor) = buildJsonObject {
        schemaOf(descriptor, emptyList()).forEach { (key, value) ->
            when (key) {
                "properties" -> JsonObject(mapOf("type" to buildJsonObject {
                    putJsonArray("enum") { add(descriptor.serialName) }
                }) + value.jsonObject)

                "required" -> buildJsonArray {
                    add("type")
                    addAll(value.jsonArray)
                }

                else -> value
            }.let {
                put(key, it)
            }
        }
    }

    /**
     * Put the field "anyOf" with all the children of the sealed type inferred from [descriptor].
     */
    public fun JsonObjectBuilder.putSealedSchemas() {
        putJsonArray("anyOf") {
            descriptor.getElementDescriptor(1)
                .elementDescriptors
                .map { generator.sealedSchemaOf(it) }
                .forEach { add(it) }
        }
    }

    /**
     * Put the field "additionalProperties" into the [JsonObject] for schema of maps.
     *
     * The value type is inferred from the [SchemaBuildingContext].
     */
    public fun JsonObjectBuilder.putAdditionalProperties(valueDescriptor: SerialDescriptor) {
        putJsonObject("additionalProperties") { put("type", generator.schemaOf(valueDescriptor, emptyList())) }
    }

    /**
     * Find the first annotation of type [T] in the [annotations] and apply the [action] to it.
     *
     * Use it if you want to use your own logic to handle the annotation.
     */
    public inline fun <reified T> JsonObjectBuilder.findAnnotationAnd(
        annotations: Iterable<Annotation>,
        action: (T) -> Unit
    ) = annotations.asSequence()
        .filterIsInstance<T>()
        .firstOrNull()
        ?.also(action)

    /**
     * Put the field "comment" into the [JsonObject] for schema.
     *
     * The comment is resolved from the [annotations]. Nothing will be put if there is no [Comment].
     */
    public fun JsonObjectBuilder.putComment() =
        findAnnotationAnd<Comment>(annotations) { put("${'$'}comment", it.value) }

    /**
     * Put the field "title" into the [JsonObject] for schema.
     *
     * The title is resolved from the [annotations]. Nothing will be put if there is no [Title].
     */
    public fun JsonObjectBuilder.putTitle() =
        findAnnotationAnd<Title>(annotations) { put("title", it.value) }

    /**
     * Put the field "description" into the [JsonObject] for schema.
     *
     * The description is resolved from the [annotations]. Nothing will be put if there is no [Description].
     */
    public fun JsonObjectBuilder.putDescription() =
        findAnnotationAnd<Description>(annotations) { put("description", it.value) }

    /**
     * Put the field "format" into the [JsonObject] for schema of string.
     *
     * The format is resolved from the [annotations]. Nothing will be put if there is no [Format].
     */
    public fun JsonObjectBuilder.putFormat() =
        findAnnotationAnd<Format>(annotations) { put("format", it.value) }

    /**
     * Put the field "pattern" into the [JsonObject] for schema of string.
     *
     * The pattern is resolved from the [annotations]. Nothing will be put if there is no [Pattern].
     */
    public fun JsonObjectBuilder.putPattern() =
        findAnnotationAnd<Pattern>(annotations) { put("pattern", it.value) }

    /**
     * Put the field "maxLength" into the [JsonObject] for schema of string.
     *
     * The maxLength is resolved from the [annotations]. Nothing will be put if there is no [MaxLength].
     */
    public fun JsonObjectBuilder.putMaxLength() =
        findAnnotationAnd<MaxLength>(annotations) { put("maxLength", it.value) }

    /**
     * Put the field "minLength" into the [JsonObject] for schema of string.
     *
     * The minLength is resolved from the [annotations]. Nothing will be put if there is no [MinLength].
     */
    public fun JsonObjectBuilder.putMinLength() =
        findAnnotationAnd<MinLength>(annotations) { put("minLength", it.value) }

    /**
     * Put the field "multipleOf" into the [JsonObject] for schema of numbers.
     *
     * The multipleOf is resolved from the [annotations]. Nothing will be put if there is no [MultipleOf].
     */
    public fun JsonObjectBuilder.putMultipleOf() =
        findAnnotationAnd<MultipleOf>(annotations) { put("multipleOf", it.value) }

    /**
     * Put the field "minimum" into the [JsonObject] for schema of numbers.
     *
     * The minimum is resolved from the [annotations]. Nothing will be put if there is no [Minimum].
     */
    public fun JsonObjectBuilder.putMinimum() =
        findAnnotationAnd<Minimum>(annotations) { put("minimum", it.value) }

    /**
     * Put the field "maximum" into the [JsonObject] for schema of numbers.
     *
     * The maximum is resolved from the [annotations]. Nothing will be put if there is no [Maximum].
     */
    public fun JsonObjectBuilder.putMaximum() =
        findAnnotationAnd<Maximum>(annotations) { put("maximum", it.value) }

    /**
     * Put the field "exclusiveMinimum" into the [JsonObject] for schema of numbers.
     *
     * The exclusiveMinimum is resolved from the [annotations]. Nothing will be put if there is no [ExclusiveMinimum].
     */
    public fun JsonObjectBuilder.putExclusiveMinimum() =
        findAnnotationAnd<ExclusiveMinimum>(annotations) { put("exclusiveMinimum", it.value) }

    /**
     * Put the field "exclusiveMaximum" into the [JsonObject] for schema of numbers.
     *
     * The exclusiveMaximum is resolved from the [annotations]. Nothing will be put if there is no [ExclusiveMaximum].
     */
    public fun JsonObjectBuilder.putExclusiveMaximum() =
        findAnnotationAnd<ExclusiveMaximum>(annotations) { put("exclusiveMaximum", it.value) }

    /**
     * Put the field "multipleOf" into the [JsonObject] for schema of numbers.
     *
     * The multipleOf is resolved from the [annotations]. Nothing will be put if there is no [MultipleOfDouble].
     */
    public fun JsonObjectBuilder.putMultipleOfDouble() =
        findAnnotationAnd<MultipleOfDouble>(annotations) { put("multipleOf", it.value) }

    /**
     * Put the field "minimum" into the [JsonObject] for schema of numbers.
     *
     * The minimum is resolved from the [annotations]. Nothing will be put if there is no [MinimumDouble].
     */
    public fun JsonObjectBuilder.putMinimumDouble() =
        findAnnotationAnd<MinimumDouble>(annotations) { put("minimum", it.value) }

    /**
     * Put the field "maximum" into the [JsonObject] for schema of numbers.
     *
     * The maximum is resolved from the [annotations]. Nothing will be put if there is no [MaximumDouble].
     */
    public fun JsonObjectBuilder.putMaximumDouble() =
        findAnnotationAnd<MaximumDouble>(annotations) { put("maximum", it.value) }

    /**
     * Put the field "exclusiveMinimum" into the [JsonObject] for schema of numbers.
     *
     * The exclusiveMinimum is resolved from the [annotations]. Nothing will be put if there is no [ExclusiveMinimumDouble].
     */
    public fun JsonObjectBuilder.putExclusiveMinimumDouble() =
        findAnnotationAnd<ExclusiveMinimumDouble>(annotations) { put("exclusiveMinimum", it.value) }

    /**
     * Put the field "exclusiveMaximum" into the [JsonObject] for schema of numbers.
     *
     * The exclusiveMaximum is resolved from the [annotations]. Nothing will be put if there is no [ExclusiveMaximumDouble].
     */
    public fun JsonObjectBuilder.putExclusiveMaximumDouble() =
        findAnnotationAnd<ExclusiveMaximumDouble>(annotations) { put("exclusiveMaximum", it.value) }
}