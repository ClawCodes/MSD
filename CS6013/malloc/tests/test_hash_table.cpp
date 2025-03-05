//
// Created by Christopher Lawton on 3/4/25.
//

#define CATCH_CONFIG_MAIN
#include <catch2/catch_all.hpp>

#include "../src/hash_table.h"
#include "../src/helper.h"

TEST_CASE("HashTable") {
  SECTION("Constructor") {
    auto table = HashTable::create(3);
    CHECK(table->size() == 0);
    CHECK(table->get(0).isEmpty());
    CHECK(table->get(1).isEmpty());
    CHECK(table->get(2).isEmpty());
  }

  SECTION("insert one") {
    auto table = HashTable::create(5);
    size_t size = sizeof(tableEntry);
    void* addr = allocatePage(size);
    CHECK(table->size() == 0);
    CHECK(table->capacity() == 5);
    int index = table->insert(addr, size);
    CHECK(table->size() == 1);
    CHECK(table->capacity() == 5);
    tableEntry actual = table->get(index);
    CHECK(!actual.isEmpty());
    CHECK(!actual.isTombStone());
    CHECK(actual.addr == addr);
    CHECK(actual.size == size);
  }

  SECTION("insert two") {
    auto table = HashTable::create(5);
    // First
    size_t size = sizeof(tableEntry);
    void* addr = allocatePage(size);
    CHECK(table->size() == 0);
    int index = table->insert(addr, size);
    CHECK(table->size() == 1);
    tableEntry actual = table->get(index);
    CHECK(!actual.isEmpty());
    CHECK(!actual.isTombStone());
    CHECK(actual.addr == addr);
    CHECK(actual.size == size);
    // Second
    size_t sizeTwo = sizeof(tableEntry);
    void* addrTwo = allocatePage(sizeTwo);
    CHECK(table->size() == 1);
    int indexTwo = table->insert(addrTwo, sizeTwo);
    CHECK(table->size() == 2);
    tableEntry actualTwo = table->get(indexTwo);
    CHECK(!actualTwo.isEmpty());
    CHECK(!actualTwo.isTombStone());
    CHECK(actualTwo.addr == addrTwo);
    CHECK(actualTwo.size == sizeTwo);
  }

  SECTION("insert duplicate record") {
    // Allow duplicates
    auto table = HashTable::create(5);
    size_t size = sizeof(tableEntry);
    void* addr = allocatePage(size);
    CHECK(table->capacity() == 5);
    CHECK(table->size() == 0);
    int aIndex = table->insert(addr, size);
    CHECK(table->size() == 1);
    int bIndex = table->insert(addr, size);
    CHECK(table->size() == 2);
    CHECK(aIndex != bIndex);
  }

  SECTION("Insert record with growth") {
    // Allow duplicates
    auto table = HashTable::create(5);
    size_t size = sizeof(tableEntry);
    void* addr = allocatePage(size);
    CHECK(table->capacity() == 5);
    CHECK(table->size() == 0);
    table->insert(addr, size);
    table->insert(addr, size);
    table->insert(addr, size);
    table->insert(addr, size);
    CHECK(table->size() == 4);
    table->insert(addr, size);  // expected growth point
    CHECK(table->size() == 5);
    CHECK(table->capacity() == 10);
  }
}