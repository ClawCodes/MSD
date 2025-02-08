cmake_minimum_required(VERSION 3.30)
project(unix_shell)

set(CMAKE_CXX_STANDARD 20)

add_executable(unix_shell src/main.cpp src/shelpers.cpp)
