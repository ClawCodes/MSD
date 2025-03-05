//
// Created by Christopher Lawton on 3/3/25.
//

#ifndef HASH_TABLE_H
#define HASH_TABLE_H

#include <unistd.h>

inline const long PAGE_SIZE = sysconf(_SC_PAGESIZE);

struct tableEntry {
  void *addr = nullptr;
  size_t size = 0;

  void makeTombStone() {
    addr = nullptr;
    size = -1;
  }

  bool isTombStone() const { return addr == nullptr && size == -1; }

  bool isEmpty() const { return addr == nullptr && size == 0; }
};

long getHash(void *address);

class HashTable {
  size_t capacity_;
  size_t size_;
  tableEntry *table_;

  void grow();

  bool isAvailable(int index);

  bool updateRecord(const int *index, void *address, const size_t *size);

 public:
  static HashTable *create(size_t size);

  int insert(void *address, size_t size);

  size_t size() { return size_; }

  size_t capacity() const { return capacity_; }

  tableEntry get(int index) { return table_[index]; }
};

#endif  // HASH_TABLE_H
