//
// Created by Christopher Lawton on 4/1/25.
//

#include <barrier>
#include <iostream>
#include <thread>
#include <vector>

#include "ConcurrentQueue.hpp"

std::unique_ptr<std::barrier<>> sync_point;

void run_consumer(ConcurrentQueue<int>* queue, int num) {
  for (int i = 0; i < num; i++) {
    int val;
    queue->dequeue(&val);
  }
}

void run_producer(ConcurrentQueue<int>* queue, int num) {
  for (int i = 0; i < num; i++) {
    queue->enqueue(i);
  }
}

void run_producer_with_sync(ConcurrentQueue<int>* queue, int num) {
  sync_point->arrive_and_wait();  // Wait for consumer and producer threads to
                                  // reach sync point
  run_producer(queue, num);
}

void run_consumer_with_sync(ConcurrentQueue<int>* queue, int num) {
  sync_point->arrive_and_wait();  // Wait for consumer and producer threads to
                                  // reach sync point
  run_consumer(queue, num);
}

void run_consumer_with_sync_and_delay(ConcurrentQueue<int>* queue, int num) {
  sync_point->arrive_and_wait();
  std::this_thread::sleep_for(std::chrono::seconds(
      1));  // sleep for a second to give producer threads a head start
  run_consumer(queue, num);
}

int getSize(ConcurrentQueue<int>* queue) {
  int ret;
  bool dequeued = queue->dequeue(&ret);
  int count = 0;
  while (dequeued) {
    count++;
    dequeued = queue->dequeue(&ret);
  }
  return count;
}

/**
 * Test ConcurrentQueue when all producer and consumer threads are scheduled
 * together. This enforces true concurrent execution (i.e. all producer and
 * consumer threads are scheduled at the same time). The program not crashing is
 * a successful case.
 */
bool testQueueSimultaneousExec(int num_producers, int num_consumers,
                               int num_ints) {
  std::vector<std::thread> threads;
  static ConcurrentQueue<int>* queue = new ConcurrentQueue<int>();
  for (int i = 0; i < num_producers; ++i) {
    threads.emplace_back(run_producer_with_sync, queue, num_ints);
  }
  for (int i = 0; i < num_consumers; ++i) {
    threads.emplace_back(run_consumer_with_sync, queue, num_ints);
  }

  for (std::thread& thread : threads) {
    thread.join();
  }

  return true;
}

/**
 * Test ConcurrentQueue when all producer threads complete and then all consumer
 * threads are executed
 */
bool testQueueSerial(int num_producers, int num_consumers, int num_ints) {
  std::vector<std::thread> producers;
  std::vector<std::thread> consumers;
  static ConcurrentQueue<int>* queue = new ConcurrentQueue<int>();
  for (int i = 0; i < num_producers; ++i) {
    producers.emplace_back(run_producer, queue, num_ints);
  }

  // Ensure all producer threads complete before executing consumer threads
  for (std::thread& thread : producers) {
    thread.join();
  }

  for (int i = 0; i < num_consumers; ++i) {
    consumers.emplace_back(run_consumer, queue, num_ints);
  }

  for (std::thread& thread : consumers) {
    thread.join();
  }

  return (num_producers - num_consumers) * num_ints == getSize(queue);
}

/**
 * Test ConcurrentQueue with a second delay for consumer threads.
 * Thus, giving the producer threads a head start.
 */
bool testQueueWithDelayedConsumers(int num_producers, int num_consumers,
                                   int num_ints) {
  std::vector<std::thread> threads;
  static ConcurrentQueue<int>* queue = new ConcurrentQueue<int>();
  for (int i = 0; i < num_producers; ++i) {
    threads.emplace_back(run_producer_with_sync, queue, num_ints);
  }

  for (int i = 0; i < num_consumers; ++i) {
    threads.emplace_back(run_consumer_with_sync_and_delay, queue, num_ints);
  }

  for (std::thread& thread : threads) {
    thread.join();
  }

  return (num_producers - num_consumers) * num_ints == getSize(queue);
}

int main(int argc, char* argv[]) {
  if (argc != 4) {
    throw std::runtime_error(
        "Please enter the number of produces, consumers, and integers to "
        "queue.");
  }

  int producers = atoi(argv[1]);
  int consumers = atoi(argv[2]);
  int num_ints = atoi(argv[3]);

  sync_point = std::make_unique<std::barrier<>>(producers + consumers);
  bool res1 = testQueueSimultaneousExec(producers, consumers, num_ints);
  bool res2 = testQueueSerial(producers, consumers, num_ints);
  bool res3 = testQueueWithDelayedConsumers(producers, consumers, num_ints);

  if (res1 && res2 && res3) {
    std::cout << "All tests passed!" << std::endl;
  }

  return 0;
}