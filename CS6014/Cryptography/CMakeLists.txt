find_package(Catch2 3 REQUIRED)
cmake_minimum_required(VERSION 3.30)
project(Cryptography)

enable_testing()

include(FetchContent)
FetchContent_Declare(
        Catch2
        GIT_REPOSITORY https://github.com/catchorg/Catch2.git
        GIT_TAG v3.5.2
)
FetchContent_MakeAvailable(Catch2)

add_executable(tests test_crypto.cpp)

set(CMAKE_CXX_STANDARD 20)

add_executable(main
    main.cpp
    crypto.cpp
)
