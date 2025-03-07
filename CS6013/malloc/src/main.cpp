#include <iostream>

#include "allocator.h"

int main() {
  int* numbers = (int*)local::malloc(5 * sizeof(int));
  *numbers = 5;
  std::cout << *numbers << std::endl;
  return 0;
}