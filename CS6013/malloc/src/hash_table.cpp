//
// Created by Christopher Lawton on 3/3/25.
//

#include "hash_table.h"

#include <sys/mman.h>

#include <iostream>
#include <ostream>
#include <stdexcept>

#include "helper.h"

long getHash(void *address) {
  return reinterpret_cast<long>(address) >> __builtin_ctz(PAGE_SIZE);
}

HashTable *HashTable::create(size_t size) {
  auto *table = static_cast<HashTable *>(allocatePage(sizeof(HashTable)));
  table->capacity_ = size;
  table->size_ = 0;
  table->table_ =
      static_cast<tableEntry *>(allocatePage(size * sizeof(tableEntry)));
  return table;
}

bool HashTable::isAvailable(int index) {
  tableEntry entry = get(index);
  return entry.isEmpty() || entry.isTombStone();
}

bool HashTable::updateRecord(const int *index, void *address,
                             const size_t *size) {
  int i = *index;
  if (isAvailable(i)) {
    table_[i].addr = address;
    table_[i].size = *size;
    size_++;
    return true;
  }
  return false;
}

void HashTable::grow() {
  tableEntry *oldTable = table_;
  int oldCapacity = capacity_;
  capacity_ *= 2;
  table_ =
      static_cast<tableEntry *>(allocatePage(capacity_ * sizeof(tableEntry)));
  size_ = 0;
  for (int i = 0; i < oldCapacity; i++) {
    if (!oldTable[i].isEmpty()) {
      insert(oldTable[i].addr, oldTable[i].size);
    }
  }
  munmap(oldTable, oldCapacity * sizeof(tableEntry));
}

int HashTable::insert(void *address, size_t size) {
  if (size_ / static_cast<double>(capacity_) > 0.75) {
    grow();
  }
  int index = getHash(address) % capacity_;
  if (isAvailable(index)) {
    updateRecord(&index, address, &size);
    return index;
  }
  int currentIndex = index + 1;
  while (currentIndex != index) {
    if (updateRecord(&currentIndex, address, &size)) {
      return currentIndex;
    }
    // Wrap around to beginning for complete probing
    if (currentIndex == capacity_ - 1) {
      currentIndex = 0;
    } else {
      currentIndex++;
    }
  }
  throw std::runtime_error("Failed to insert record!");
}

int HashTable::find(void *address) {
  int index = getHash(address) % capacity_;
  if (table_[index].addr == address) {
    return index;
  }
  int currentIndex = index + 1;
  while (currentIndex != index) {
    if (currentIndex > capacity_ - 1) {
      currentIndex = 0;
    }
    tableEntry entry = table_[index];
    if (entry.isEmpty()) {
      return -1;  // not found
    }
    if (table_[currentIndex].addr == address) {
      return currentIndex;
    }
    currentIndex++;
  }
  throw std::runtime_error("Failed to find record!");
}

size_t HashTable::remove(void *address) {
  int index = find(address);
  size_t size = table_[index].size;
  if (index != -1) {
    table_[index].makeTombStone();
    size_--;
    return size;
  }
  return -1;
};