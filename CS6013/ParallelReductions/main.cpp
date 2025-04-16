#include <omp.h>

#include <format>
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
    for (int threads = 2; threads <= 6; threads++) {
      std::cout << "\tThread Count: " << threads << "\n\t";
      Result result = func_under_test(arr.data(), N, threads);
      result.print();
    }
    std::cout << "*************************************\n";
  }
}

int main() {
  run_parallel_tests(parallel_sum);
  run_parallel_tests(parallel_sum_omp1);
  run_parallel_tests(parallel_sum_omp_builtin);
  return 0;
}