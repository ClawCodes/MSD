#include <sys/time.h>
#include <stdio.h>
#include <time.h>
#include <cassert>
#include <iostream>

extern "C" {
	void sayHello();
	int myPuts(const char* s, int len);
	timeval myGTOD();
}

void printHumanTime(timeval tv){
	struct tm* now = localtime(&tv.tv_sec);
        printf("Human time: %d:%0d:%0d\n", now->tm_hour, now->tm_min, now->tm_sec);
}

int main(){
	sayHello();

	const char* helloCPP = "Hello from c++!\n";
	myPuts(helloCPP, 17);
	
	struct timeval tv = myGTOD();
	printf("Time from ASM: %lu\n", tv.tv_sec);
	printHumanTime(tv);

	struct timeval tvCPP;
	struct timezone tz;
	gettimeofday(&tvCPP, &tz);
	printf("Time from CPP: %lu\n", tvCPP.tv_sec);
	printHumanTime(tvCPP);

	assert((tv.tv_sec - tvCPP.tv_sec) <= 1); // Accept off by one 
	std::cout << "ASM and CPP time stamps are equivalent!\n"; 
	return 0;
}
