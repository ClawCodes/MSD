//
// Created by Christopher Lawton on 3/4/25.
//

#include <catch2/catch_all.hpp>
#include <iostream>

#include "../src/hash_table.h"
#include "../src/helper.h"

TEST_CASE("HashTable::Constructor") {
  auto table = HashTable::create(3);
  CHECK(table->size() == 0);
  CHECK(table->get(0).isEmpty());
  CHECK(table->get(1).isEmpty());
  CHECK(table->get(2).isEmpty());
}

TEST_CASE("HashTable::insert one") {
  auto table = HashTable::create(5);
  size_t size = sizeof(tableEntry);
  void *addr = allocatePage(size);
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

TEST_CASE("HashTable::insert two") {
  auto table = HashTable::create(5);
  // First
  size_t size = sizeof(tableEntry);
  void *addr = allocatePage(size);
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
  void *addrTwo = allocatePage(sizeTwo);
  CHECK(table->size() == 1);
  int indexTwo = table->insert(addrTwo, sizeTwo);
  CHECK(table->size() == 2);
  tableEntry actualTwo = table->get(indexTwo);
  CHECK(!actualTwo.isEmpty());
  CHECK(!actualTwo.isTombStone());
  CHECK(actualTwo.addr == addrTwo);
  CHECK(actualTwo.size == sizeTwo);
}

TEST_CASE("HashTable::insert duplicate record") {
  // Allow duplicates
  auto table = HashTable::create(5);
  size_t size = sizeof(tableEntry);
  void *addr = allocatePage(size);
  CHECK(table->capacity() == 5);
  CHECK(table->size() == 0);
  int aIndex = table->insert(addr, size);
  CHECK(table->size() == 1);
  int bIndex = table->insert(addr, size);
  CHECK(table->size() == 2);
  CHECK(aIndex != bIndex);
}

TEST_CASE("HashTable::Insert record with growth") {
  // Allow duplicates
  auto table = HashTable::create(5);
  size_t size = sizeof(tableEntry);
  void *addr = allocatePage(size);
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

TEST_CASE("HashTable::Remove single record") {
  auto table = HashTable::create(5);
  size_t size = sizeof(tableEntry);
  void *addr = allocatePage(size);
  CHECK(table->size() == 0);
  int index = table->insert(addr, size);
  CHECK(table->size() == 1);
  table->remove(addr);
  CHECK(table->size() == 0);
  tableEntry actual = table->get(index);
  CHECK(actual.isTombStone());
}

TEST_CASE("HashTable::Remove multi record") {
  auto table = HashTable::create(5);
  size_t size = sizeof(tableEntry);
  void *addr = allocatePage(size);
  CHECK(table->size() == 0);
  int indexA = table->insert(addr, size);
  int indexB = table->insert(addr, size);
  int indexC = table->insert(addr, size);
  int indexD = table->insert(addr, size);
  CHECK(table->size() == 4);
  table->remove(addr);
  CHECK(table->size() == 3);
  CHECK(table->get(indexA).isTombStone());
  table->remove(addr);
  CHECK(table->size() == 2);
  CHECK(table->get(indexB).isTombStone());
  table->remove(addr);
  CHECK(table->size() == 1);
  CHECK(table->get(indexC).isTombStone());
  table->remove(addr);
  CHECK(table->size() == 0);
  CHECK(table->get(indexD).isTombStone());
  // Additional remove when hashtable is expected to be empty
  int idx = table->remove(addr);
  CHECK(idx == -1);
  CHECK(table->size() == 0);
}
