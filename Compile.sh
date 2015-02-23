#!/bin/bash

# main classes name
MainClass=$(whiptail --title "MainClass" --inputbox "What is the class name?" 10 60 Game 3>&1 1>&2 2>&3)

exitstatus=$?
if [ $exitstatus != 0 ]; then
	exit 1 #Input: Cancel
fi

# other stuff
OtherStuff=$(whiptail --title "Other Stuff" --inputbox "What needs to go in the .jar?" 10 60 "Other/* Resources/*" 3>&1 1>&2 2>&3)

exitstatus=$?
if [ $exitstatus != 0 ]; then
	exit 1 #Input: Cancel
fi

# compile
javac "$MainClass.java"
jar cvfe "$MainClass.jar" "$MainClass" *.class $OtherStuff
# clean up
rm *.class
# set executable flag
chmod +x "$MainClass.jar"
# run
java -jar "$MainClass.jar"
