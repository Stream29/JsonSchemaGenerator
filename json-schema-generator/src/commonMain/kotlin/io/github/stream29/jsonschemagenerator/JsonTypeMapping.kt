package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.*

@OptIn(ExperimentalSerializationApi::class)
public val SerialDescriptor.jsonTypeName: String
    get() = when (val kind = this.kind) {
        is PrimitiveKind -> when (kind) {
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

        SerialKind.ENUM -> "string"
        StructureKind.OBJECT -> "string"
        PolymorphicKind.OPEN -> "object"
        PolymorphicKind.SEALED -> "object"
        SerialKind.CONTEXTUAL -> "object"
        StructureKind.CLASS -> "object"
        StructureKind.LIST -> "array"
        StructureKind.MAP -> "object"
    }