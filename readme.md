# buildeye
A tool for analyzing and monitoring Gradle builds

### Working with the project

#### Testing

To publish artifacts to maven local, run:
```
./gradlew publishToMavenLocal
```

Artifacts are published with 0.1-SNAPSHOT version by default 
(the version is specified via `project.version` property in `gradle.properties` file).
To publish artifacts with a custom version, specify `project.version` property via command-line:
```
./gradlew publishToMavenLocal -Pproject.version=0.1-custom-version
```