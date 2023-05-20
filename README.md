# Terraform infrastructure-as-code with Kotlin

Terraform Language source code generation with Kotlin and execute generated code through Kotlin subshells.

![Release](https://jitpack.io/v/masterchi3f/master-iac.svg)
- [Dependency builds](https://jitpack.io/#masterchi3f/master-iac/0.3.3)
- [Latest build logs](https://jitpack.io/com/github/masterchi3f/master-iac/0.3.3/build.log)

## Import using Gradle

`build.gradle`:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.masterchi3f:master-iac:0.3.3'
}
```

`build.gradle.kts`:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.masterchi3f:master-iac:0.3.3")
}
```

## Import using Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.masterchi3f</groupId>
    <artifactId>master-iac</artifactId>
    <version>0.3.3</version>
</dependency>
```

## How to use

See test file: [Terraform.kt](https://github.com/masterchi3f/master-iac/blob/master/src/test/kotlin/uks/master/thesis/Terraform.kt)

See example code in repository: [Example for Terraform infrastructure-as-code with Kotlin](https://github.com/masterchi3f/master-iac-example)
