cmake_minimum_required(VERSION 3.14)
project(malloc)

set(CMAKE_CXX_STANDARD 20)

# Define sources (excluding main.cpp for reusability)
set(SOURCES
        src/hash_table.cpp
        src/helper.cpp
        src/allocator.cpp
)

# Create a library from the sources
add_library(malloc_lib ${SOURCES})

# Main executable
add_executable(malloc src/main.cpp)
target_link_libraries(malloc PRIVATE malloc_lib)

# ********** TESTING **********
find_package(Catch2 3 REQUIRED)

set(TESTS tests/test_hash_table.cpp tests/test_allocator.cpp)

# testing executable
add_executable(tests ${TESTS})
target_link_libraries(tests PRIVATE malloc_lib Catch2::Catch2WithMain)

# Ensure testing is enabled
enable_testing()

include(CTest)
include(Catch)
catch_discover_tests(tests TEST_PREFIX "unittest: " DISCOVERY_MODE PRE_TEST)
