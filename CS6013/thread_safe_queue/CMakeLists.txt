cmake_minimum_required(VERSION 3.30)
project(thread_safe_queue)

set(CMAKE_CXX_STANDARD 20)

add_executable(ConcurrentQueueTest ConcurrentQueueTest.cpp)

# ********** TESTING **********
find_package(Catch2 3 REQUIRED)

# testing executable
add_executable(tests SerialQueueTests.cpp)
target_link_libraries(tests PRIVATE Catch2::Catch2WithMain)

# Ensure testing is enabled
enable_testing()

include(CTest)
include(Catch)
catch_discover_tests(tests TEST_PREFIX "unittest: " DISCOVERY_MODE PRE_TEST)
