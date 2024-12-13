package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.descriptors.PrimitiveKind

internal val PrimitiveKind.typeName
    get() = when (this) {
        PrimitiveKind.STRING -> "string"
        PrimitiveKind.BYTE -> "integer"
        PrimitiveKind.SHORT -> "integer"
        PrimitiveKind.INT -> "integer"
        PrimitiveKind.LONG -> "integer"
        PrimitiveKind.FLOAT -> "number"
        PrimitiveKind.DOUBLE -> "number"
        PrimitiveKind.BOOLEAN -> "boolean"
        PrimitiveKind.CHAR -> "char"
    }