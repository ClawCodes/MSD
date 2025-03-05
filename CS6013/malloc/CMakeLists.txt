cmake_minimum_required(VERSION 3.14)
project(malloc)

set(CMAKE_CXX_STANDARD 20)

set(SOURCES src/hash_table.cpp src/helper.cpp)

add_executable(malloc
        ${SOURCES}
    )

# TESTING
find_package(Catch2 3 REQUIRED)

set(TESTS tests/test_hash_table.cpp)
add_executable(tests
        ${TESTS}
        ${SOURCES}
)

target_link_libraries(tests PRIVATE Catch2::Catch2WithMain)

include(CTest)
include(Catch)
catch_discover_tests(tests)