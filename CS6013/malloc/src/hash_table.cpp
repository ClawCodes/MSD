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
  for (int i = 0; i < capacity_; i++) {
    int currentIndex = (index + i) % capacity_;
    if (updateRecord(&currentIndex, address, &size)) {
      return currentIndex;
    }
  }
  throw std::runtime_error("Failed to insert record!");
}

int HashTable::find(void *address) {
  int index = getHash(address) % capacity_;

  for (int i = 0; i < capacity_; i++) {
    int currentIndex = (index + i) % capacity_;
    if (table_[currentIndex].isEmpty()) {
      return -1;  // Not found
    }
    if (!table_[currentIndex].isTombStone() &&
        table_[currentIndex].addr == address) {
      return currentIndex;
    }
  }

  return -1;
}

size_t HashTable::remove(void *address) {
  int index = find(address);
  if (index == -1) return -1;

  // A lazy deletion is a "deletion" in which you mark something as deleted
  // so it can be treated as deleted, but the data and/or object itself is not
  // deleted
  size_t size = table_[index].size;
  table_[index].makeTombStone();
  size_--;

  return size;
};