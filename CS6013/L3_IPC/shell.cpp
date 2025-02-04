//
// Created by Christopher Lawton on 2/3/25.
//

#include <stdio.h>
#include <iostream>

void signalHandler(int signum){
    static bool once = true;
    if(once){
        std::cout<<"Caught Signal: "<< signum << "\n";
        once = false;
    }
    else {
        exit(1);
    }
}

int main() {
  signal(SIGINT, signalHandler);
  const char * prompt = "shell> ";
  std::string command;

  while (true){
    std::cout << prompt;
    std::cin >> command;

    std::cout << "Running command: " << command << "\n";
   }
}