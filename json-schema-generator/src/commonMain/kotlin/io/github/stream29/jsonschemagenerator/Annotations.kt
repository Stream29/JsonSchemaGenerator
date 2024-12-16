package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.put


@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@SerialInfo
public annotation class Description(val text: String)

internal fun JsonObjectBuilder.putDescription(annotation: Iterable<Annotation>, annotations: Iterable<Annotation>) {
    annotation.asSequence().plus(annotations)
        .filterIsInstance<Description>()
        .firstOrNull()
        ?.let { put("description", it.text) }
}