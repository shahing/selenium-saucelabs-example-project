# SauceLabs Example Project

This is an example project that shows how to run automated Selenium (in Browser) tests using SauceLabs (a cloud provider that hosts the Selenium nodes to execute on)

This project is a *Java* project, using *Maven* as the build manager. Tests are written in *Java using JUnit*, and executed using the *Maven FailSafe* plugin.

The tests are found in the `/src/test/java` directory. To implement tests, extend the `SauceLabsBaseClass` which has the configuration information, and OS/Browser combinations defined in it.

Name any created tests 'IT<testname>' so that they are automatically picked up and executed by the Maven FailSafe plugin.

## Example Test ##

The project includes a very simple sample test that loads the given URL specified by the `APP_BASE_URL` and checks to see if the Title of the page is 'Google'. This will pass if executed against `https://www.google.co.uk`, and fail against other pages.

## Run Instructions ##

In order to execute the tests, you need the following information :

1. `SAUCE_USERNAME` - the username for the SauceLabs account
1. `SAUCE_ACCESS_KEY` - the access key for the SauceLabs account
1. `APP_BASE_URL` - the base URL of the application to be tested
1. `BUILD_TAG` - a tag which describes the build (if running from Jenkins this will be automatically populated)

If you execute the tests without this information, the Maven enforcer will warn you the properties are missing and fail the build :


```
#!bash

[INFO] --- maven-enforcer-plugin:1.4.1:enforce (enforce-property) @ saucelabsexampleproject ---
[WARNING] Rule 0: org.apache.maven.plugins.enforcer.RequireProperty failed with message:
You must set the SAUCE_USERNAME system property to execute these tests. You can pass it to maven using the following parameter: -DSAUCE_USERNAME=username
[WARNING] Rule 1: org.apache.maven.plugins.enforcer.RequireProperty failed with message:
You must set the SAUCE_ACCESS_KEY system property to execute these tests. You can pass it to maven using the following parameter: -DSAUCE_ACCESS_KEY=accesskey
[WARNING] Rule 2: org.apache.maven.plugins.enforcer.RequireProperty failed with message:
You must set the APP_BASE_URL system property to execute these tests. You can pass it to maven using the following parameter: -DAPP_BASE_URL=baseurl
[WARNING] Rule 3: org.apache.maven.plugins.enforcer.RequireProperty failed with message:
You must set the BUILD_TAG system property to execute these tests. You can pass it to maven using the following parameter: -DBUILD_TAG=buildtag
```


The tests are executed using the FailSafe Maven plugin, using the following maven goals :


```
#!bash

mvn clean verify
```


To explicitly provide the required arguments above, use the following command :


```
#!bash

mvn -DSAUCE_USERNAME=username -DSAUCE_ACCESS_KEY=accesskey -DAPP_BASE_URL=https://www.google.co.uk -DBUILD_TAG='Example Manual Build Tag' clean verify
```