import io.github.stream29.jsonschemagenerator.toSchema
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

private val json = Json { prettyPrint = true }

fun schemaTest(descriptor: SerialDescriptor) {
    println(json.encodeToString(descriptor.toSchema()))
}

class SchemaTest {
    @Test
    fun `primitive descriptor`() {
        schemaTest(String.serializer().descriptor)
    }

    @Test
    fun `enum descriptor`() {
        schemaTest(TestEnum.serializer().descriptor)
    }

    @Test
    fun `class descriptor`() {
        schemaTest(TestDataClass.serializer().descriptor)
    }

    @Test
    fun `map descriptor`() {
        schemaTest(MapSerializer(String.serializer(), Int.serializer()).descriptor)
    }

    @Test
    fun `list descriptor`() {
        schemaTest(ListSerializer(String.serializer()).descriptor)
    }

    @Test
    fun `inline descriptor`() {
        schemaTest(TestInlineClass.serializer().descriptor)
    }
}