import io.github.stream29.jsonschemagenerator.Description
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

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

@Serializable
@Description("TestDescription")
data class TestDescription(
    @Description("This is a long description")
    val property: String,
    @Description("This is a nested property")
    val nestedProperty: NestedDescription,
    @Description("This is a inline property")
    val valueProperty: TestInlineDescription
)

@Serializable
data class NestedDescription(
    @Description("This is a nested name")
    val nestedName: String = "",
)

@Serializable
@JvmInline
value class TestInlineDescription(
    @Description("This is a description on value class")
    val value: String
)