package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

/**
 * Annotate to serialize the descriptor as a reference.
 *
 * @property value The name of the reference.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class Ref(val value: String)

/**
 * Annotate to serialize the descriptor as a reference with its serial name as the name in `$ref`.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class RefWithSerialName

/**
 * Annotate to add a title in the schema.
 *
 * @property value The title of the schema.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class Title(val value: String)

/**
 * Annotate to add a description in the schema.
 *
 * @property value The description of the schema.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class Description(val value: String)

/**
 * Annotate to add a comment in the schema.
 *
 * @property value The comment of the schema.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class Comment(val value: String)

/**
 * Annotate to add a min length constraint in the schema for string.
 *
 * @property value The minimum length of the string.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MinLength(val value: Int)

/**
 * Annotate to add a max length constraint in the schema for string.
 *
 * @property value The maximum length of the string.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MaxLength(val value: Int)

/**
 * Annotate to add a regex pattern constraint in the schema for string.
 *
 * @property value The regex pattern of the string.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Pattern(val value: String)

/**
 * Annotate to add a format constraint in the schema for string.
 *
 * @property value The format of the string.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Format(val value: String)

/**
 * Annotate to add a factor constraint in the schema for number.
 *
 * @property value The factor of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MultipleOf(val value: Int)

/**
 * Annotate to add a minimum value constraint in the schema for number.
 *
 * @property value The minimum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Minimum(val value: Int)

/**
 * Annotate to add a maximum value constraint in the schema for number.
 *
 * @property value The maximum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Maximum(val value: Int)

/**
 * Annotate to add a exclusive minimum value constraint in the schema for number.
 *
 * @property value The exclusive minimum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMinimum(val value: Int)

/**
 * Annotate to add a exclusive maximum value constraint in the schema for number.
 *
 * @property value The exclusive maximum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMaximum(val value: Int)

/**
 * Annotate to add a factor constraint in the schema for number.
 *
 * This is for double type.
 *
 * @property value The factor of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MultipleOfDouble(val value: Double)

/**
 * Annotate to add a minimum value constraint in the schema for number.
 *
 * This is for double type.
 *
 * @property value The minimum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MinimumDouble(val value: Double)

/**
 * Annotate to add a maximum value constraint in the schema for number.
 *
 * This is for double type.
 *
 * @property value The maximum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MaximumDouble(val value: Double)

/**
 * Annotate to add a exclusive minimum value constraint in the schema for number.
 *
 * This is for double type.
 *
 * @property value The exclusive minimum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMinimumDouble(val value: Double)

/**
 * Annotate to add a exclusive maximum value constraint in the schema for number.
 *
 * This is for double type.
 *
 * @property value The exclusive maximum value of the number.
 */
@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMaximumDouble(val value: Double)