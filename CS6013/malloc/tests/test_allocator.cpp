//
// Created by Christopher Lawton on 3/7/25.
//

#include <catch2/catch_all.hpp>
#include <iostream>

#include "../src/allocator.h"
#include "vector"

TEST_CASE("Allocator-small_objects") {
  std::vector<int *> ints;
  HashTable *table = local::allocator->get_table();
  CHECK(table->size() == 0);
  for (int i = 0; i < 1000; i++) {
    int *num = (int *)local::malloc(sizeof(int));
    *num = i;
    ints.push_back(num);
  }
  CHECK(local::allocator->get_table()->size() == 1000);
  int expected = 0;
  for (int *ptr : ints) {
    CHECK(*ptr == expected);
    local::free(ptr);
    expected++;
  }
  CHECK(local::allocator->get_table()->size() == 0);
}

TEST_CASE("Allocator-small_objects_multi_add") {
  std::vector<int *> ints;
  HashTable *table = local::allocator->get_table();
  CHECK(table->size() == 0);
  for (int i = 0; i < 1000; i++) {
    int *num = (int *)local::malloc(sizeof(int));
    *num = i;
    ints.push_back(num);
  }
  CHECK(local::allocator->get_table()->size() == 1000);
  for (int i = 1000; i < 2000; i++) {
    int *num = (int *)local::malloc(sizeof(int));
    *num = i;
    ints.push_back(num);
  }
  CHECK(local::allocator->get_table()->size() == 2000);
  int expected = 0;
  for (int *ptr : ints) {
    CHECK(*ptr == expected);
    local::free(ptr);
    expected++;
  }
  CHECK(local::allocator->get_table()->size() == 0);
}

TEST_CASE("Allocator-large_objects") {
  std::vector<char *> strPtrs;
  std::vector<std::string> strings;
  HashTable *table = local::allocator->get_table();
  CHECK(table->size() == 0);
  for (int i = 0; i < 1000; i++) {
    int size = 1000;
    char *str = (char *)local::malloc(size * sizeof(char));
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

TEST_CASE("Allocator-multi_page") {
  std::vector<char *> strPtrs;
  std::vector<std::string> strings;
  HashTable *table = local::allocator->get_table();
  CHECK(table->size() == 0);
  for (int i = 0; i < 1000; i++) {
    int size = 4000;
    char *str = (char *)local::malloc(size * sizeof(char));
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

TEST_CASE("time malloc/free") {
  constexpr int COUNT = 1000;
  // Using BENCHMARK_ADVANCED to avoid double free during free benchmarck
  // iterations
  BENCHMARK("My malloc") {
    std::vector<char *> addressesA(COUNT, nullptr);
    for (int i = 0; i < COUNT; i++) {
      addressesA[i] = (char *)local::malloc(1000 * sizeof(char));
    }
  };

  BENCHMARK("standard malloc") {
    std::vector<char *> addressesB(COUNT, nullptr);
    for (int i = 0; i < COUNT; i++) {
      addressesB[i] = (char *)malloc(1000 * sizeof(char));
    }
  };

  BENCHMARK_ADVANCED("My free") {
    std::vector<char *> addressesA(COUNT, nullptr);
    for (int i = 0; i < COUNT; i++) {
      addressesA[i] = (char *)local::malloc(1000 * sizeof(char));
    }
    for (int i = 0; i < COUNT; i++) {
      local::free(addressesA[i]);
    }
  };

  BENCHMARK_ADVANCED("standard free") {
    std::vector<char *> addressesB(COUNT, nullptr);
    for (int i = 0; i < COUNT; i++) {
      addressesB[i] = (char *)malloc(1000 * sizeof(char));
    }
    for (int i = 0; i < COUNT; i++) {
      free(addressesB[i]);
    }
  };
}
