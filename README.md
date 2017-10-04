## Validate all YML files as part of your CI build

This test can be used to validate spring boot (or other) yml files as part of the compilation process.
It is primarily aimed at validating basic markup errors and does not know anything about how the
properties are actually ued. This test should work on all yml files

1. Copy the test class _ValidateSpringYml_ from [src/test/java/org/springframework/beans/factory/config](src/test/java/org/springframework/beans/factory/config)
 to the same package location in your project's test source tree.
1. Edit the @Test methods to meet your needs. 
Alternatively, retain validateAllYml() which will validate all application and test yml files.

## Test log output
Logging output does not appear on the console when running _gradle test_. Logging output is available at
 * build/reports/test/classes as html
 * buld/test-results/test as xml

## Example Notes
1. This project contains one good and one bad yml file.
The "*" test fails because int validates all yml files on the path including the bad yml file in src/test/resources
1. The test class must be in the spring package because it accesses protected code.

## Dependencies
This project was built with the following
1. Java 8
1. Gradle 4.2 
1. You can crate a gradle wrapper by running the following command `gradle wrapper --gradle-version 4.2`

## References

* This project created with [Spring starter](http://start.spring.io/)
* Information about application.properties and application.yml files available in [spring external configuration documentation](https://docs.spring.o/spring-boot/docs/current/reference/html/boot-features-external-config.html)
* Git repository created using [this git tutorial](http://kbroman.org/github_tutorial/pages/init.html)
