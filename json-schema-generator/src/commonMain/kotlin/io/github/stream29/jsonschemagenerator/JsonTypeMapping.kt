package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind

@OptIn(ExperimentalSerializationApi::class)
public val SerialKind.jsonTypeName: String
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
        StructureKind.LIST -> "array"
        StructureKind.MAP -> "object"
        StructureKind.CLASS -> "object"
        StructureKind.OBJECT -> "string"
        SerialKind.ENUM -> "string"
        is PolymorphicKind -> "object"
        SerialKind.CONTEXTUAL -> "object"
    }