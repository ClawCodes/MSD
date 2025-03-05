//
// Created by Christopher Lawton on 3/4/25.
//

#include "helper.h"

#include <sys/mman.h>

void *allocatePage(long numBytes) {
  return mmap(nullptr, numBytes, PROT_READ | PROT_WRITE,
              MAP_ANONYMOUS | MAP_PRIVATE, -1, 0);
}