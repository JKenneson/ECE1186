# ECE1186
University of Pittsburgh - Software Engineering Final Project

Rogue One Enterprises - Jonathan Kenneson, Kyle Monto, Dan Bednarczyk, Tyler Protivnak, Brian Stevenson, Rob Goldshear

### 1. Configuration Overview
The Rogue One Enterprises train system can be found at: 
https://github.com/JKenneson/ECE1186

From the link provided above, an option is provided to download a zipped folder of the project. This folder contains all of the source, build, and configuration files to make the project. 

### 2. Installation Prerequisites

Before the installation process can begin, the build tool Apache Ant must be installed. A link to Apache Ant can be found here: 
http://ant.apache.org

Download and install Apache Ant from the link provided above, and configure you system PATH variables for calling the command ant from the command line. 

To test if ant has been installed and configured on a system correctly, run the command:

> ant -v

If the result from the command returns a line stating the version of Apache Ant, then Apache Ant is configured correctly.

Along with Apache Ant installed, place the unzipped project directory in a known location.
The latest version of the Java JRE also needs to be installed, the link can be found here:
http://www.oracle.com/technetwork/java/javase/downloads/index.html

### 3. Building the System

In order to build the system for the first time, run the command:

> ant -f /{pathto}/TrainSystem -Dnb.internal.action.name=build jar

In order to clean and build the system, run the command:

> ant -f /{pathto}/TrainSystem -Dnb.internal.action.name=rebuild clean jar

After executing those commands, go into the dist directory of the the unzipped folder where those instructions were run. Within that dist directory, there is a TrainSystem.jar file that can be executed. Double clicking TrainSystem.jar will start the program.