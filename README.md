Snakes and Ladders for Kindle (KDK)

A Snakes and Ladders board game for Amazon Kindle e-reader devices, built with the Amazon Kindle Development Kit (KDK).

Copyright © 2012 Olmatech Technology

Overview

This project is a Kindlet, a Java application that runs on supported Kindle e-readers. It brings the classic Snakes and Ladders board game to the Kindle's e-ink screen.

How to Play

The standard rules of Snakes and Ladders apply:

Players take turns rolling a die and moving their token that many squares along the numbered board.
If a token lands on the bottom of a ladder, it climbs up to the top of the ladder.
If a token lands on the head of a snake, it slides down to the snake's tail.
The first player to reach the final square wins.

Repository Structure
Path	Description
src/	Java source code for the Kindlet
app_data/	Application data and resources used by the game
MANIFEST.MF	Kindlet manifest describing the application
kindlet.properties	Kindlet configuration properties
build_olga.xml	Ant build script for the project
build_all_olga.xml	Ant script that builds all targets
.gitignore	Git ignore rules
Requirements
A Kindle e-reader device that supports KDK applications
Java JDK (the KDK targets an older Java platform, so check the KDK documentation for the supported version)
Apache Ant to run the build scripts
Amazon Kindle Development Kit (KDK) libraries and tools
Building

The project uses Ant build scripts. After installing the KDK and Ant, update any KDK or signing paths in the build files for your machine, then run:

bash
# Build the Kindlet
ant -f build_olga.xml

# Or build all targets
ant -f build_all_olga.xml
<!-- TODO: Confirm default target names, output file name, and where the build writes its output. -->
Installing on a Kindle
Build the Kindlet package as described above.
Connect the Kindle to your computer over USB.
Copy the built package to the Kindle's documents folder.
Disconnect the device. The game should appear in your Kindle library.

Notes

KDK applications generally need to be signed before they can run on a device. See the Amazon KDK documentation for details on keystores and signing.

License

Copyright © 2012 Olmatech Technology. All rights reserved.