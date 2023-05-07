# Terraform infrastructure-as-code with Kotlin

Terraform Language source code generation with Kotlin and execute generated code through Kotlin subshells.

## Import using Gradle

![Release](https://jitpack.io/v/masterchi3f/master-iac.svg)

1. Add this at the end of your `repositories` section.

   Groovy - `build.gradle`:

   ```groovy
   repositories {
     maven { url 'https://jitpack.io' }
   }
   ```

   Kotlin - `build.gradle.kts`:

   ```kotlin
   repositories {
     maven("https://jitpack.io")
   }
   ```

2. Add the dependency.

   Groovy - `build.gradle`:

   ```groovy
   dependencies {
     implementation 'com.github.masterchi3f:master-iac:0.1.2'
   }
   ```

   Kotlin - `build.gradle.kts`:

   ```kotlin
   dependencies {
     implementation("com.github.masterchi3f:master-iac:0.1.2")
   }
   ```

Information about this dependency on JitPack:

- You can find this dependency on JitPack
  [here](https://jitpack.io/#masterchi3f/master-iac/0.1.2)
- You can find the JitPack build logs
  [here](https://jitpack.io/com/github/masterchi3f/master-iac/0.1.2/build.log)