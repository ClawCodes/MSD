#include <sys/time.h>
#include <stdio.h>
#include <time.h>

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
	struct timeval tv;
	printf("Time from ASM: %lu\n", tv.tv_sec);
	printHumanTime(tv);

	struct timeval tvCPP;
	struct timezone tz;
	gettimeofday(&tv, &tz);
	printf("Time from CPP: %lu\n", tvCPP.tv_sec);
	printHumanTime(tvCPP);
	return 0;
}
