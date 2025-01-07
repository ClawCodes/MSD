# TARGETS
# main: Create executable with rebuild files that changed from last compilation
# build: Intuitive command for building project (call main rule)
# Pants.o: Rebuild Pants object file and its dependencies
# Caps.o: Rebuild Caps object file and its dependencies
# Clothing.o: Rebuild Clothing object file
# main.o: Rebuild main object file and its dependencies
# clean: remove object files and executable
# execute: Rebuild project if any file changed and run executable

main: main.o
	c++ main.o Pants.o Clothing.o Caps.o -o main

build: main

Pants.o: Pants.h Pants.cpp Clothing.o
	c++ -c Pants.cpp

Caps.o: Caps.h Caps.cpp Clothing.o
	c++ -c Caps.cpp

Clothing.o: Clothing.h Clothing.cpp
	c++ -c Clothing.cpp

main.o: main.cpp Pants.o Caps.o
	c++ -c main.cpp

clean:
	rm -rf *.o main

execute: main
	./main