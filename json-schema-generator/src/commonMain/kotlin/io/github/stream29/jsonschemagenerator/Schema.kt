package io.github.stream29.jsonschemagenerator

import io.github.stream29.jsonschemagenerator.internal.toSchema
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public inline fun <reified T> schemaOf(): JsonObject = schemaOf(typeOf<T>())

public fun schemaOf(type: KType): JsonObject = schemaOf(serializer(type).descriptor)

public fun schemaOf(descriptor: SerialDescriptor): JsonObject = descriptor.toSchema(emptyList())