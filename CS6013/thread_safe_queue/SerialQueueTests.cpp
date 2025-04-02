//
// Created by Christopher Lawton on 4/1/25.
//

#include <catch2/catch_all.hpp>

#include "SerialQueue.hpp"

TEST_CASE("Serial Queue Constructor", "[SerialQueue::SerialQueue]") {
  SerialQueue<int>* queue = new SerialQueue<int>();
  CHECK(queue->size() == 0);
  CHECK_THROWS_WITH(queue->head(), "Empty queue");
  CHECK_THROWS_WITH(queue->tail(), "Empty queue");
}

TEST_CASE("SerialQueue enqueue", "[SerialQueue::enqueue]") {
  SECTION("Add one") {
    SerialQueue<int>* queue = new SerialQueue<int>();
    queue->enqueue(1);
    CHECK(queue->size() == 1);
    CHECK(queue->head() == 1);
    CHECK(queue->tail() == 1);
  }
  SECTION("Add many") {
    SerialQueue<int>* queue = new SerialQueue<int>();
    queue->enqueue(1);
    queue->enqueue(2);
    queue->enqueue(3);
    CHECK(queue->size() == 3);
    CHECK(queue->head() == 1);
    CHECK(queue->tail() == 3);
  }
}

TEST_CASE("SerialQueue dequeue", "[SerialQueue::dequeue]") {
  SECTION("dequeue one") {
    SerialQueue<int>* queue = new SerialQueue<int>();
    queue->enqueue(1);
    CHECK(queue->size() == 1);
    int actual = 0;
    CHECK(queue->dequeue(&actual));
    CHECK(queue->size() == 0);
    CHECK(actual == 1);
  }
  SECTION("dequeue many") {
    SerialQueue<int>* queue = new SerialQueue<int>();
    queue->enqueue(1);
    queue->enqueue(2);
    queue->enqueue(3);
    CHECK(queue->size() == 3);
    CHECK(queue->head() == 1);
    CHECK(queue->tail() == 3);

    int actual = 0;
    CHECK(queue->dequeue(&actual));
    CHECK(queue->size() == 2);
    CHECK(actual == 1);
    CHECK(queue->head() == 2);
    CHECK(queue->tail() == 3);

    CHECK(queue->dequeue(&actual));
    CHECK(queue->size() == 1);
    CHECK(actual == 2);
    CHECK(queue->head() == 3);
    CHECK(queue->tail() == 3);

    CHECK(queue->dequeue(&actual));
    CHECK(queue->size() == 0);
    CHECK(actual == 3);
    CHECK_THROWS_WITH(queue->head(), "Empty queue");
    CHECK_THROWS_WITH(queue->tail(), "Empty queue");
  }
}

TEST_CASE("Enqueue-dequeue bulk") {
  SerialQueue<int>* queue = new SerialQueue<int>();

  for (int i = 0; i < 1000; i++) {
    queue->enqueue(i);
  }
  CHECK(queue->size() == 1000);
  for (int i = 0; i < 1000; i++) {
    int actual = 0;
    CHECK(queue->dequeue(&actual));
    CHECK(actual == i);
    CHECK(queue->size() == 1000 - (i + 1));
  }
}
