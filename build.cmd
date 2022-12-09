@cd src
@dir /s /b *.java > ../sources.txt
@cd ..
@javac @sources.txt -d bin/classes
@del sources.txt /q