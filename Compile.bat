@echo off
set /p MainClass=What is the class name?  
javac %MainClass%.java
set /p OtherStuff=What needs to go in the .jar?  
jar cvfe %MainClass%.jar %MainClass% *.class %OtherStuff%
del *.class
java -jar %MainClass%.jar
pause