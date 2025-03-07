//
// Created by Christopher Lawton on 3/5/25.
//

#ifndef ALLOCATOR_H
#define ALLOCATOR_H

#include "hash_table.h"

class Allocator {
  HashTable *table_;

 public:
  static Allocator *create();
  void *allocate(size_t size) const;
  void deallocate(void *ptr) const;
  HashTable *get_table() const;
};

namespace local {
inline Allocator *allocator = Allocator::create();

inline void *malloc(size_t size) { return allocator->allocate(size); }

inline void free(void *ptr) { allocator->deallocate(ptr); }
}  // namespace local

#endif  // ALLOCATOR_H
