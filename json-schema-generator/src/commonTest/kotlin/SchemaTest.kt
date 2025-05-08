import io.github.stream29.jsonschemagenerator.SchemaGenerator
import io.github.stream29.jsonschemagenerator.from
import io.github.stream29.jsonschemagenerator.schemaOf
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
    val jsonObject = SchemaGenerator.default.schemaOf(descriptor, emptyList())
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
              "type": "integer",
                "minimum": -2147483648,
                "maximum": 2147483647
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

    @Test
    fun titleAnnotation() {
        //language=JSON
        val expected = """
            {
                "${'$'}comment": "TestComment",
                "title": "TestTitle",
                "type": "string"
            }
        """.trimIndent()
        schemaTest(TitleTest.serializer().descriptor, expected)
    }

    @Test
    fun stringConstraints() {
        //language=JSON
        val expected = """
            {
                "type": "string",
                "format": "email",
                "pattern": "^[a-zA-Z0-9]+${'$'}",
                "minLength": 5,
                "maxLength": 10
            }
        """.trimIndent()
        schemaTest(StringConstraintsTest.serializer().descriptor, expected)
    }

    @Test
    fun numberConstraints() {
        //language=JSON
        val expected = """
            {
              "type": "number",
              "minimum": 1.0,
              "maximum": 10.0,
              "exclusiveMinimum": 0.0,
              "exclusiveMaximum": 11.0,
              "multipleOf": 2.0
            }
        """.trimIndent()
        schemaTest(NumberConstraintsTest.serializer().descriptor, expected)
    }

    @Test
    fun sealedType() {
        //language=JSON
        val expected = """
                    {
            "anyOf": [
                {
                    "allOf": [
                        {
                            "type": "object",
                            "properties": {
                                "name0": {
                                    "type": "string"
                                },
                                "owner0": {
                                    "type": "string"
                                }
                            },
                            "required": [
                                "name0"
                            ]
                        },
                        {
                            "properties": {
                                "type": {
                                    "enum": [
                                        "SealedClass0"
                                    ]
                                }
                            },
                            "required": [
                                "type"
                            ]
                        }
                    ]
                },
                {
                    "allOf": [
                        {
                            "type": "object",
                            "properties": {
                                "name1": {
                                    "type": "string"
                                },
                                "owner1": {
                                    "type": "string"
                                }
                            },
                            "required": [
                                "name1"
                            ]
                        },
                        {
                            "properties": {
                                "type": {
                                    "enum": [
                                        "SealedClass1"
                                    ]
                                }
                            },
                            "required": [
                                "type"
                            ]
                        }
                    ]
                }
            ]
        }
        """.trimIndent()
        schemaTest(SealedInterface.serializer().descriptor, expected)
    }

    @Test
    fun ref() {
        //language=JSON
        val ref = """
            {
                "${'$'}ref": "#/${'$'}defs/recursive",
                "${'$'}defs": {
                    "recursive": {
                        "type": "object",
                        "properties": {
                            "next": {
                                "anyOf": [
                                    {
                                        "type": "null"
                                    },
                                    {
                                        "${'$'}ref": "#/${'$'}defs/recursive"
                                    }
                                ]
                            }
                        }
                    }
                }
            }
        """.trimIndent()
        schemaTest(Recursive.serializer().descriptor, ref)
        //language=JSON
        val refWithSerialName = """
            {
                "${'$'}ref": "#/${'$'}defs/RecursiveWithSerialName",
                "${'$'}defs": {
                    "RecursiveWithSerialName": {
                        "type": "object",
                        "properties": {
                            "next": {
                                "anyOf": [
                                    {
                                        "type": "null"
                                    },
                                    {
                                        "${'$'}ref": "#/${'$'}defs/RecursiveWithSerialName"
                                    }
                                ]
                            }
                        }
                    }
                }
            }
        """.trimIndent()
        schemaTest(RecursiveWithSerialName.serializer().descriptor, refWithSerialName)
        //language=JSON
        val tree = """
            {
                "${'$'}ref": "#/${'$'}defs/Tree",
                "${'$'}defs": {
                    "Tree": {
                        "anyOf": [
                            {
                                "allOf": [
                                    {
                                        "${'$'}ref": "#/${'$'}defs/Node"
                                    },
                                    {
                                        "properties": {
                                            "type": {
                                                "enum": [
                                                    "Node"
                                                ]
                                            }
                                        },
                                        "required": [
                                            "type"
                                        ]
                                    }
                                ]
                            }
                        ]
                    },
                    "Node": {
                        "type": "object",
                        "properties": {
                            "value": {
                                "type": "string"
                            },
                            "left": {
                                "anyOf": [
                                    {
                                        "type": "null"
                                    },
                                    {
                                        "${'$'}ref": "#/${'$'}defs/Tree"
                                    }
                                ]
                            },
                            "right": {
                                "anyOf": [
                                    {
                                        "type": "null"
                                    },
                                    {
                                        "${'$'}ref": "#/${'$'}defs/Tree"
                                    }
                                ]
                            }
                        },
                        "required": [
                            "value"
                        ]
                    }
                }
            }
        """.trimIndent()
        schemaTest(Tree.serializer().descriptor, tree)
    }
}