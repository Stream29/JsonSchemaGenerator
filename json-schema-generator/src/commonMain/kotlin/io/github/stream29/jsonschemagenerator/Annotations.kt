package io.github.stream29.jsonschemagenerator

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class Title(val value: String)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class Description(val value: String)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@SerialInfo
@MustBeDocumented
public annotation class Comment(val value: String)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MinLength(val value: Int)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MaxLength(val value: Int)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Pattern(val value: String)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Format(val value: String)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MultipleOf(val value: Int)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Minimum(val value: Int)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class Maximum(val value: Int)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMinimum(val value: Int)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMaximum(val value: Int)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MultipleOfDouble(val value: Double)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MinimumDouble(val value: Double)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class MaximumDouble(val value: Double)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMinimumDouble(val value: Double)

@OptIn(ExperimentalSerializationApi::class)
@Target(AnnotationTarget.PROPERTY)
@SerialInfo
@MustBeDocumented
public annotation class ExclusiveMaximumDouble(val value: Double)