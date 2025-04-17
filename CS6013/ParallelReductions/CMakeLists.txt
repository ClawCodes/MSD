cmake_minimum_required(VERSION 3.30)
project(ParallelReductions)

set(CMAKE_CXX_STANDARD 20)

set(OPENMP_ROOT /opt/homebrew/opt/libomp)

set(OpenMP_C_FLAGS "-Xpreprocessor -fopenmp -I${OPENMP_ROOT}/include")
set(OpenMP_omp_LIBRARY ${OPENMP_ROOT}/lib/libomp.dylib)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_C_FLAGS}")
link_directories(${OPENMP_ROOT}/lib)

add_executable(ParallelReductions main.cpp)
target_link_libraries(ParallelReductions ${OpenMP_omp_LIBRARY})