#pragma once

#include <mutex>

template <typename T>
class ConcurrentQueue {
 public:
  ConcurrentQueue() {
    head_ = new Node{T{}, nullptr};
    tail_ = head_;
  }

  void enqueue(const T& x) {
    Node* newNode = new Node{x, nullptr};
    std::unique_lock<std::mutex> lock(tailLock_);
    tail_->next = newNode;
    tail_ = newNode;
  }

  bool dequeue(T* ret) {
    std::unique_lock<std::mutex> lock(headLock_);
    Node* oldHead = head_;
    Node* newHead = oldHead->next;
    if (newHead == nullptr) {
      return false;
    }
    *ret = newHead->data;
    head_ = newHead;
    return true;
  }

  ~ConcurrentQueue() {
    while (head_ != nullptr) {
      Node* temp = head_->next;
      delete head_;
      head_ = temp;
    }
  }

 private:
  struct Node {
    T data;
    Node* next;
  };

  Node* head_;
  Node* tail_;
  std::mutex headLock_;
  std::mutex tailLock_;
};
