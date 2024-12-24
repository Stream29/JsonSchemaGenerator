package io.github.stream29.jsonschemagenerator.internal

import kotlinx.serialization.descriptors.PrimitiveKind

internal val PrimitiveKind.jsonType
    get() = when (this) {
        PrimitiveKind.STRING -> JsonPrimitiveType.STRING
        PrimitiveKind.BYTE -> JsonPrimitiveType.INTEGER
        PrimitiveKind.SHORT -> JsonPrimitiveType.INTEGER
        PrimitiveKind.INT -> JsonPrimitiveType.INTEGER
        PrimitiveKind.LONG -> JsonPrimitiveType.INTEGER
        PrimitiveKind.FLOAT -> JsonPrimitiveType.NUMBER
        PrimitiveKind.DOUBLE -> JsonPrimitiveType.NUMBER
        PrimitiveKind.BOOLEAN -> JsonPrimitiveType.BOOLEAN
        PrimitiveKind.CHAR -> JsonPrimitiveType.CHAR
    }