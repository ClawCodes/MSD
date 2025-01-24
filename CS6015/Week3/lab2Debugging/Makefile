.PHONY: build asm main objects clean

main: main.cpp
	g++ -c main.cpp

asm: myAsm.s
	nasm -felf64 myAsm.s

objects: main asm

build: objects
	g++ -o main main.o myAsm.o


clean:
	rm -f main *.o
