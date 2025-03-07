//
// Created by Christopher Lawton on 3/7/25.
//

#include <catch2/catch_all.hpp>
#include <iostream>

#include "../src/allocator.h"
#include "vector"

TEST_CASE("small_objects") {
  std::vector<int*> ints;
  HashTable* table = local::allocator->get_table();
  CHECK(table->size() == 0);
  for (int i = 0; i < 1000; i++) {
    int* num = (int*)local::malloc(sizeof(int));
    *num = i;
    ints.push_back(num);
  }
  CHECK(local::allocator->get_table()->size() == 1000);
  int expected = 0;
  for (int* ptr : ints) {
    CHECK(*ptr == expected);
    local::free(ptr);
    expected++;
  }
  CHECK(local::allocator->get_table()->size() == 0);
}

TEST_CASE("large_objects") {
  std::vector<char*> strPtrs;
  std::vector<std::string> strings;
  HashTable* table = local::allocator->get_table();
  CHECK(table->size() == 0);
  for (int i = 0; i < 1000; i++) {
    int size = 1000;
    char* str = (char*)local::malloc(size * sizeof(char));
    for (int j = 0; j < size; j++) {
      str[j] = 'a' + (rand() % 26);
    }
    strPtrs.push_back(str);
    strings.push_back(std::string(str));
  }
  CHECK(local::allocator->get_table()->size() == 1000);
  for (int i = 0; i < strPtrs.size(); i++) {
    CHECK(std::string(strPtrs[i]) == strings[i]);
    local::free(strPtrs[i]);
  }
  CHECK(local::allocator->get_table()->size() == 0);
}
