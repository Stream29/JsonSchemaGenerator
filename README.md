# Json Schema Generator

This library uses `kotlinx.serialization` to generate a JSON schema from a `@Serializable` type.

This library uses `SerialDescriptor` to get the structure of schema. No runtime reflection cost.

This library support Kotlin/multiplatform. Because the author Stream only have a Windows laptop, 
the following targets are not built and published: 
iosArm64, iosSimulatorArm64, iosX64, macosArm64, macosX64, tvosArm64, tvosSimulatorArm64, tvosX64, watchosArm32, watchosArm64, watchosDeviceArm64, watchosSimulatorArm64, watchosX64

Please mention that on Kotlin/JS, the string expression of float numbers may be different. 
`2.0d` may be shown in `2` and that may cause some unit tests fail.

## Get Started

To quickly learn the usage of this library, see [Kotlin Notebook](Usages.ipynb).