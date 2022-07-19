# Klens

Compile-time generated functional lenses via Kotlin Symbol Processing

## Usage

First add the KSP plugin and required dependencies to build.gradle(.kts):

```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

dependencies {
    implementation("com.github.juggernaut0.klens:klens-api:0.1.0")
    ksp("com.github.juggernaut0.klens:klens-processor:0.1.0")
}
```

Then annotate a data class with the `klens.Lenses` annotation:

```kotlin
@Lenses
data class Data(val num: Int)
```

This example will generate a `DataLenses` object containing a lens for each property.

```kotlin
val myData = Data(42)
DataLenses.num.get(myData) // == 42
DataLenses.num.set(myData, 69) // == Data(num = 69)
```

Compose lenses with the `*` operator:

```kotlin
@Lenses
data class Outer(val inner: Inner)
@Lenses
data class Inner(val num: Int)

val outer = Outer(Inner(42))
val lens = OuterLenses.inner * InnerLenses.num
lens.get(outer) // == 42
lens.set(outer, 47) // == Outer(inner = Inner(num = 47))
```

Create your own lenses with the `Lens` constructor:

```kotlin
val firstItemLens = Lens<List<Int>, Int>(
    getter = { this[0] }, 
    setter = { listOf(it) + subList(1, size) }
)

val list = listOf(42, 0, 1337)
firstItemLens.get(list) // == 42
firstItemLens.set(list, 69) // == listOf(69, 0, 1337)
```