# Simple [Allure 3](https://github.com/allure-framework/allure3) configuration for [Gradle](https://github.com/allure-framework/allure-gradle) with [Java](https://github.com/allure-framework/allure-java)

Related to [medium post](https://medium.com/@rosolko/simple-allure-2-configuration-for-gradle-8cd3810658dd)

How to use:
* Clone repo
* Run tests: `./gradlew clean test`
* Generate allure project:
    * `./gradlew allureReport` - generate Allure report
    * `./gradlew allureServe` - generate Allure report and opens it in the default browser
