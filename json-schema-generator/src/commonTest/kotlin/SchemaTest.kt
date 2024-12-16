import io.github.stream29.jsonschemagenerator.toSchema
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

private val json = Json { prettyPrint = true }

fun schemaTest(descriptor: SerialDescriptor, expected: String) {
    val jsonObject = descriptor.toSchema()
    println(json.encodeToString(jsonObject))
    assertEquals(json.parseToJsonElement(expected), jsonObject)
}

class SchemaTest {
    @Test
    fun primitiveDescriptor() {
        //language=JSON
        val expected = """
            {
              "type": "string"
            }
        """.trimIndent()
        schemaTest(String.serializer().descriptor, expected)
    }

    @Test
    fun enumDescriptor() {
        //language=JSON
        val expected = """
            {
              "enum": ["A", "B", "C"]
            }
        """.trimIndent()
        schemaTest(TestEnum.serializer().descriptor, expected)
    }

    @Test
    fun classDescriptor() {
        //language=JSON
        val expected = """
            {
                "type": "object",
                "properties": {
                    "name": {
                        "type": "string"
                    },
                    "owner": {
                        "type": "string"
                    },
                    "nested": {
                        "type": "object",
                        "properties": {
                            "nestedName": {
                                "type": "string"
                            },
                            "nestedOwner": {
                                "type": "string"
                            }
                        },
                        "required": [
                            "nestedOwner"
                        ]
                    }
                },
                "required": [
                    "name",
                    "nested"
                ]
            }
        """.trimIndent()
        schemaTest(TestDataClass.serializer().descriptor, expected)
    }

    @Test
    fun mapDescriptor() {
        //language=JSON
        val expected = """
            {
                "type": "object",
                "additionalProperties": {
                    "type": {
                        "type": "integer"
                    }
                }
            }
        """.trimIndent()
        schemaTest(MapSerializer(String.serializer(), Int.serializer()).descriptor, expected)
    }

    @Test
    fun listDescriptor() {
        //language=JSON
        val expected = """
            {
                "type": "array",
                "items": {
                    "type": "string"
                }
            }
        """.trimIndent()
        schemaTest(ListSerializer(String.serializer()).descriptor, expected)
    }

    @Test
    fun inlineDescriptor() {
        //language=JSON
        val expected = """
            {
              "type": "string"
            }
        """.trimIndent()
        schemaTest(TestInlineClass.serializer().descriptor, expected)
    }

    @Test
    fun description() {
        //language=JSON
        val expected = """
            {
                "type": "object",
                "description": "TestDescription",
                "properties": {
                    "property": {
                        "type": "string",
                        "description": "This is a long description"
                    },
                    "nestedProperty": {
                        "type": "object",
                        "description": "This is a nested property",
                        "properties": {
                            "nestedName": {
                                "type": "string",
                                "description": "This is a nested name"
                            }
                        }
                    },
                    "valueProperty": {
                        "type": "string",
                        "description": "This is a inline property"
                    }
                },
                "required": [
                    "property",
                    "nestedProperty",
                    "valueProperty"
                ]
            }
        """.trimIndent()
        schemaTest(TestDescription.serializer().descriptor, expected)
    }
}