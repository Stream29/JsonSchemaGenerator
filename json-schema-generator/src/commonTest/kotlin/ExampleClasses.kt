import kotlinx.serialization.Serializable

@Serializable
enum class TestEnum {
    A, B, C
}

@Serializable
data class TestDataClass(
    val name: String,
    val owner: String = "",
    val nested: NestedDataClass,
)

@Serializable
data class NestedDataClass(
    val nestedName: String = "",
    val nestedOwner: String,
)

@Serializable
@JvmInline
value class TestInlineClass(val value: String)