#include <omp.h>

#include <cassert>
#include <format>
#include <fstream>
#include <functional>
#include <iostream>
#include <span>
#include <thread>

struct Result {
  int sum;
  std::chrono::duration<double> time;

  void print() {
    std::string s = std::format("Sum: {} | Time: {} ms", sum, time.count());
    std::cout << s << std::endl;
  }
};

template <typename T>
void sum_(std::span<T> arr, std::atomic<int> &sum) {
  for (int i = 0; i < arr.size(); i++) {
    sum.fetch_add(arr[i], std::memory_order_relaxed);
  }
}

template <typename T>
Result parallel_sum(T arr[], size_t N, size_t num_threads) {
  auto start = std::chrono::high_resolution_clock::now();
  std::vector<std::thread> threads;
  std::atomic<int> sum(0);
  double step = N / num_threads;

  for (size_t i = 0; i < num_threads; i++) {
    size_t start_idx = i * step;
    size_t end_idx = (i == num_threads - 1) ? N : (i + 1) * step;

    std::span<T> slice(arr + start_idx, end_idx - start_idx);
    threads.emplace_back(sum_<T>, slice, std::ref(sum));
  }

  for (std::thread &t : threads) {
    t.join();
  }

  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> duration = end - start;

  return Result{sum, duration};
}

template <typename T>
Result parallel_sum_omp1(T arr[], size_t N, size_t num_threads) {
  auto start = std::chrono::high_resolution_clock::now();

  omp_set_num_threads(num_threads);
  std::vector<T> local_sums(num_threads, 0);

#pragma omp parallel
  {
    int thread_id = omp_get_thread_num();
    int start_idx = thread_id * N / num_threads;
    int end_idx = (thread_id + 1) * N / num_threads;
    for (int i = start_idx; i < end_idx; i++) {
      local_sums[thread_id] += arr[i];
    }
  }

  T sum(0);
  for (T element : local_sums) {
    sum += element;
  }

  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> duration = end - start;

  return Result{sum, duration};
}

template <typename T>
Result parallel_sum_omp_builtin(T arr[], size_t N, size_t num_threads) {
  auto start = std::chrono::high_resolution_clock::now();

  omp_set_num_threads(num_threads);
  std::vector<T> local_sums(num_threads, 0);
  T sum(0);
#pragma omp parallel for reduction(+ : sum)
  for (int i = 0; i < N; i++) {
    sum += arr[i];
  }

  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> duration = end - start;

  return Result{sum, duration};
}

void run_parallel_tests(Result (*func_under_test)(int[], size_t, size_t)) {
  int test_cases[5] = {10, 100, 1000, 10000, 100000};

  for (int i = 0; i < 5; i++) {
    int N = test_cases[i];
    std::vector<int> arr(N);
    int expected_sum = 0;
    for (int j = 0; j < N; j++) {
      arr[j] = j;
      expected_sum += j;
    }

    std::cout << "\n*************************************\n";
    std::cout << "Parallel sum test - Num Elements: " << N
              << " | Expected sum: " << expected_sum << "\n";
    Result result;
    for (int threads = 2; threads <= 6; threads++) {
      std::cout << "\tThread Count: " << threads << "\n\t";
      result = func_under_test(arr.data(), N, threads);
      result.print();
    }
    std::cout << "*************************************\n";
    assert(result.sum == expected_sum);
  }
}

void test_strong_scaling() {
  std::vector<int> threads{1, 2, 4, 8, 16};
  std::vector<int> input_sizes{1000, 5000, 10000, 100000, 1000000000};

  std::vector<Result> std_thread_sum;
  std::vector<Result> omp1_sum;
  std::vector<Result> omp_builtin_sum;
  for (int input_size : input_sizes) {
    int expected_sum = 0;
    std::vector<int> vector_under_test;
    for (int j = 0; j < input_size; j++) {
      vector_under_test.push_back(j);
      expected_sum += j;
    }
    for (int &t : threads) {
      auto arr = vector_under_test.data();
      Result res1 = parallel_sum(arr, input_size, t);
      assert(res1.sum == expected_sum);
      std_thread_sum.push_back(res1);

      Result res2 = parallel_sum_omp1(arr, input_size, t);
      omp1_sum.push_back(res2);
      assert(res2.sum == expected_sum);

      Result res3 = parallel_sum_omp_builtin(arr, input_size, t);
      std_thread_sum.push_back(res3);
      assert(res3.sum == expected_sum);
    }
  }

  std::ofstream fout("strong_scaling.csv");
  if (!fout.is_open()) {
    std::cerr << "Failed to open file strong_scaling.csv\n";
    exit(1);
  }
  fout << "input_size,num_threads,std_thread_sum,omp1_sum,omp_builtin_sum\n";
  for (int input_size : input_sizes) {
    for (int t : threads) {
      // TODO: fix format call
      fout << std::format("{},{},{},{},{}\n", input_size, t,
                          std_thread_sum[t].time, omp1_sum[t].time,
                          omp_builtin_sum[t].time);
    }
  }
}

int main() {
  // run_parallel_tests(parallel_sum);
  // run_parallel_tests(parallel_sum_omp1);
  // run_parallel_tests(parallel_sum_omp_builtin);
  test_strong_scaling();
  return 0;
}