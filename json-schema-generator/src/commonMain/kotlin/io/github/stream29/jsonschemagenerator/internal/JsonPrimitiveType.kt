package io.github.stream29.jsonschemagenerator.internal

internal enum class JsonPrimitiveType(private val typeName: String) {
    STRING("string"),
    INTEGER("integer"),
    NUMBER("number"),
    BOOLEAN("boolean"),
    CHAR("char");

    override fun toString(): String = typeName
}