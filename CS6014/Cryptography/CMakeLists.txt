cmake_minimum_required(VERSION 3.14)
project(Cryptography)

set(CMAKE_CXX_STANDARD 20)

add_executable(main
        main.cpp
        crypto.cpp
)

# TESTING
find_package(Catch2 3 REQUIRED)

add_executable(tests test_crypto.cpp crypto.cpp)
target_link_libraries(tests PRIVATE Catch2::Catch2WithMain)

include(CTest)
include(Catch)
catch_discover_tests(tests)