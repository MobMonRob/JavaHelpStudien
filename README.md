# JHelp - A replacement for JavaHelp on NetBeans

## Disclaimer

The project is in an early state of development, so it is not advised to use it in real world 
applications. If you have feedback or feature suggestions, please create a new
[GitHub Issue](https://github.com/MobMonRob/JavaHelpStudien/issues/new).

## Description

JHelp is a Maven NetBeans Module that should replace JavaHelp and the JavaHelp NetBeans Module.
JHelp currently only supports a limited subset of features of JavaHelp and only HelpSets from the version 2.0.

## Installation

To install JHelp in your locale maven repository run:

``` mvn clean install```

## Usage

To use the JHelp NetBeans module inside your NetBeans-Platform application, add the
maven dependency to your Project pom.xml:

```
<dependency>
    <groupId>de.dhbw.mwulle</groupId>
    <artifactId>jhelp</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

In order to create a compliant HelpSet for a module, use the NetBeans IDE Wizard and delete the ``@HelpSetRegistration``
Annotation in package-info.java.
Also remove the added ``org-netbeans-modules-javahelp`` dependency.
For each module that provides HelpSets you have to add a reference in the Services/JavaHelp 
folder inside the NetBeans Filesystem.
For that you have to manuell add this to the layer.xml of the module (currently there are issues with that):

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE filesystem PUBLIC "-//NetBeans//DTD Filesystem 1.2//EN" "http://www.netbeans.org/dtds/filesystem-1_2.dtd">
<filesystem>
    <folder name="Services">
        <folder name="JavaHelp">
            <file name="jhelp-hs.xml" url="url-to-your-helpset">
                <attr name="position" intvalue="1000"/>
            </file>
        </folder>
    </folder>
</filesystem>
```


## License

JHelp is licensed under the [Apache 2.0 License](LICENSE).