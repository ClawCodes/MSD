//
//  main.cpp
//  CommandLineArgs
//
//  Created by Christopher Lawton on 8/30/24.
//

#include <iostream>

int main(int argc, const char * argv[]) {
    for(int i = 0; i < argc; i++){
        std::cout << argv[i] << std::endl;
    }
    
    return 0;
}
