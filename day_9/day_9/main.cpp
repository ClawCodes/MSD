//
//  main.cpp
//  day_9
//
//  Created by Christopher Lawton on 8/29/24.
//
// Scratch file for file I/O

#include <iostream>
#include <fstream>
#include <vector>


// Selection sort
void selectionSort(std::vector<int> &numbers){
    int min;
    for (int i = 0; i < numbers.size(); i++){
        min = i;
        for (int j = i+1; numbers.size(); j++){
            if (numbers[min] > numbers[j]){
                min = j;
                
                }
            }
        // Swap numbers[i] with numbers[min]
        
        }
}

int main(int argc, const char * argv[]) {
    // Create out stream
    std::ofstream out;
    // Tell the outstream where to store the data
    out.open("file.txt");
    // OR on creation of the out file stream -> "out.open("file.txt");
    
    // Check if the file is open - this ensures that the file is openable and you will correctly write
    if(!out.is_open()){
        std::cerr << "Could not open the file" << std::endl;
        return 1;
    }
    // Write to file
    // Essentially can do anything you can do with std::cout
    out << "Hello world!" << " hello" << std::endl;
    out << 34;
    
    // Anything before this function will be flushed (out.close()) This will create a new line at the end of the flush
    out.flush();
    
    // Xcode creates an active directory and this is where the file is stored (can find the file with bash "mdfind <file name>
    
    // Change the file path with produce > scheme > edit scheme > working directory > use custom working directory
    
    // Default write is to replace the file vs. append
    
    // Ensure to close the file
    out.close();
    
    // Read from file
    std::ifstream in("file.txt");
    
    if(!in.is_open()){
        std::cerr << "The file was not opened" << std::endl;
        return 1;
    }
    
    // ifstream acts just like cin
    std::string str1;
    std::string str2;
    // Read all chars delimited by space
//    in >> str1 >> str2;
    // OR read an entire line
    std::getline(in, str1);
    
    // Read ALL lines
    // The stream to str1 will always return true if successfully read data from the file
    // Will return false when there is a failure or you reach EOF
    while (in >> str1){
        std::cout << str1 << std::endl;
    }
    
    // You can also do type converstion using implicit conversion with variable assignement
//    int number;
//    while (in>>str1>>number){
//        std::cout<<str1<<std::endl;
//        std::cout<<number<<std::endl;
//    }
    
    // You can also use getline to read line by line
//    while (getline(in, str1)){
//        std::cout << str1 << std::endl;
//    }
    
    std::cout << str1 << std::endl << str2 << std::endl;


    
    return 0;
}
