#include <omp.h>

#include <cassert>
#include <filesystem>
#include <format>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <random>
#include <span>
#include <thread>

namespace fs = std::filesystem;

std::random_device rd;
std::mt19937 gen(rd());
std::uniform_int_distribution int_dist(0, 10);
std::uniform_real_distribution float_dist(0.0, 10.0);
std::uniform_int_distribution long_dist(0L, 10L);

template <typename T>
struct Result {
  T sum;
  std::chrono::duration<double> time;

  void print() {
    std::string s = std::format("Sum: {} | Time: {} ms", sum, time.count());
    std::cout << s << std::endl;
  }

  std::string time_as_string() const {
    std::ostringstream oss;
    oss << std::fixed << std::setprecision(6) << time.count();
    std::string duration_string = oss.str();
    std::cout << duration_string;
    return oss.str();
  }
};

template <typename T>
Result<T> parallel_sum(T arr[], size_t N, size_t num_threads) {
  auto start = std::chrono::high_resolution_clock::now();
  double step = N / num_threads;
  std::vector<T> thread_sums(num_threads, 0);

  for (int round = 0; round < 100; round++) {
    std::vector<std::thread> threads;
    for (size_t i = 0; i < num_threads; i++) {
      size_t start_idx = i * step;
      size_t end_idx = (i == num_threads - 1) ? N : (i + 1) * step;

      std::span<T> slice(arr + start_idx, end_idx - start_idx);
      threads.emplace_back([slice, &thread_sums, i]() {
        for (T val : slice) {
          thread_sums[i] += val;
        }
      });
    }

    for (std::thread &t : threads) {
      t.join();
    }

    if (round != 99) {
      // reset sum for next round
      for (T &thread_sum : thread_sums) {
        thread_sum = 0;
      }
    }
  }

  T sum = 0;
  for (T val : thread_sums) sum += val;

  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> duration = end - start;

  return Result<T>{sum, duration};
}

template <typename T>
Result<T> parallel_sum_omp1(T arr[], size_t N, size_t num_threads) {
  auto start = std::chrono::high_resolution_clock::now();

  omp_set_num_threads(num_threads);
  std::vector<T> local_sums(num_threads, 0);

  T sum(0);
  for (int round = 0; round < 100; round++) {
#pragma omp parallel
    {
      int thread_id = omp_get_thread_num();
      int start_idx = thread_id * N / num_threads;
      int end_idx = (thread_id + 1) * N / num_threads;
      for (int i = start_idx; i < end_idx; i++) {
        local_sums[thread_id] += arr[i];
      }
    }

    for (T element : local_sums) {
      sum += element;
    }

    if (round != 99) {
      for (T &element : local_sums) {
        element = 0;
      }
      sum = 0;
    }
  }

  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> duration = end - start;

  return Result<T>{sum, duration};
}

template <typename T>
Result<T> parallel_sum_omp_builtin(T arr[], size_t N, size_t num_threads) {
  auto start = std::chrono::high_resolution_clock::now();

  omp_set_num_threads(num_threads);
  std::vector<T> local_sums(num_threads, 0);
  T sum(0);

  for (int round = 0; round < 100; round++) {
#pragma omp parallel for reduction(+ : sum)
    for (int i = 0; i < N; i++) {
      sum += arr[i];
    }
    if (round != 99) {
      sum = 0;
    }
  }

  auto end = std::chrono::high_resolution_clock::now();
  std::chrono::duration<double> duration = end - start;

  return Result<T>{sum, duration};
}

template <typename T>
T generate_value() {
  if constexpr (std::is_same_v<T, int>) return int_dist(gen);
  if constexpr (std::is_same_v<T, float>) return float_dist(gen);
  if constexpr (std::is_same_v<T, long>) return long_dist(gen);
  throw std::runtime_error("Unsupported type");
}

template <typename T>
bool approx_equal(T a, T b, double threshold = 1) {
  if constexpr (std::is_floating_point_v<T>) {
    bool result = std::abs(a - b) < threshold;
    if (!result)
      std::cout << a << ", " << b << ", " << std::abs(a - b) << " !< "
                << threshold << std::endl;
    return std::abs(a - b) < threshold;
  } else {
    return a == b;
  }
}

template <typename T>
void test_strong_scaling() {
  std::vector<int> threads{1, 2, 4, 8, 16};
  std::vector<int> input_sizes{1000, 10000, 100000, 1000000, 1000000000};

  std::vector<Result<T> > std_thread_sum;
  std::vector<Result<T> > omp1_sum;
  std::vector<Result<T> > omp_builtin_sum;
  for (int input_size : input_sizes) {
    T expected_sum = 0;
    std::vector<T> vector_under_test;
    for (int j = 0; j < input_size; j++) {
      T val = generate_value<T>();
      vector_under_test.push_back(val);
      expected_sum += val;
    }
    for (int &t : threads) {
      T *arr = vector_under_test.data();
      Result<T> res1 = parallel_sum<T>(arr, input_size, t);
      std_thread_sum.push_back(res1);
      // assert(approx_equal<T>(res1.sum, expected_sum));

      Result<T> res2 = parallel_sum_omp1<T>(arr, input_size, t);
      omp1_sum.push_back(res2);
      // assert(approx_equal<T>(res2.sum, expected_sum));

      Result<T> res3 = parallel_sum_omp_builtin<T>(arr, input_size, t);
      omp_builtin_sum.push_back(res3);
      // assert(approx_equal<T>(res3.sum, expected_sum));
    }
  }

  fs::path output_path =
      fs::path(__FILE__).parent_path() / "strong_scaling.csv";

  bool write_header =
      !fs::exists(output_path) || fs::file_size(output_path) == 0;

  std::ofstream fout(output_path);
  if (!fout.is_open()) {
    std::cerr << "Failed to open file strong_scaling.csv\n";
    exit(1);
  }

  if (write_header) {
    fout << "type,input_size,num_threads,std_thread_sum,omp1_sum,omp_builtin_"
            "sum\n";
  }

  for (int input_size : input_sizes) {
    for (int t = 0; t < threads.size(); t++) {
      fout << std::format(
          "{},{},{},{},{},{}\n", typeid(T).name(), std::to_string(input_size),
          std::to_string(threads[t]), std_thread_sum[t].time_as_string(),
          omp1_sum[t].time_as_string(), omp_builtin_sum[t].time_as_string());
    }
  }
  fout.close();
}

template <typename T>
void test_weak_scaling() {
  std::vector<int> threads{1, 2, 4, 8, 16};
  std::vector<int> input_sizes{1000000, 2000000, 4000000, 8000000, 1000000000};

  std::vector<Result<T> > std_thread_sum;
  std::vector<Result<T> > omp1_sum;
  std::vector<Result<T> > omp_builtin_sum;
  for (int i = 0; i < input_sizes.size(); i++) {
    T expected_sum = 0;
    std::vector<T> vector_under_test;
    int input_size = input_sizes[i];
    for (int j = 0; j < input_size; j++) {
      T val = generate_value<T>();
      vector_under_test.push_back(val);
      expected_sum += val;
    }
    int thread_count = threads[i];
    T *arr = vector_under_test.data();
    Result<T> res1 = parallel_sum<T>(arr, input_size, thread_count);
    std_thread_sum.push_back(res1);
    assert(approx_equal<T>(res1.sum, expected_sum));

    Result<T> res2 = parallel_sum_omp1<T>(arr, input_size, thread_count);
    omp1_sum.push_back(res2);
    assert(approx_equal<T>(res2.sum, expected_sum));

    Result<T> res3 = parallel_sum_omp_builtin<T>(arr, input_size, thread_count);
    omp_builtin_sum.push_back(res3);
    assert(approx_equal<T>(res3.sum, expected_sum));
  }

  fs::path output_path = fs::path(__FILE__).parent_path() / "weak_scaling.csv";
  std::ofstream fout(output_path);
  if (!fout.is_open()) {
    std::cerr << "Failed to open file weak_scaling.csv\n";
    exit(1);
  }
  fout << "input_size,num_threads,std_thread_sum,omp1_sum,omp_builtin_sum\n";
  for (int i = 0; i < input_sizes.size(); i++) {
    fout << std::format(
        "{},{},{},{},{}\n", std::to_string(input_sizes[i]),
        std::to_string(threads[i]), std_thread_sum[i].time_as_string(),
        omp1_sum[i].time_as_string(), omp_builtin_sum[i].time_as_string());
  }
  fout.close();
}

int main() {
  // test_strong_scaling<int>();
  // test_strong_scaling<long>();
  test_strong_scaling<float>();
  // test_weak_scaling<int>();
  return 0;
}
