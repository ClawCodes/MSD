//
// Created by Christopher Lawton on 3/5/25.
//

#include "allocator.h"

#include <sys/mman.h>

#include "helper.h"

Allocator *Allocator::create() {
  Allocator *allocator =
      static_cast<Allocator *>(allocatePage(sizeof(Allocator)));
  allocator->table_ = HashTable::create(17);
  return allocator;
};

void *Allocator::allocate(size_t size) const {
  void *addr = allocatePage(size);
  table_->insert(addr, size);
  return addr;
}

void Allocator::deallocate(void *ptr) const {
  size_t size = table_->remove(ptr);
  munmap(ptr, size);
}

HashTable *Allocator::get_table() const { return table_; }
