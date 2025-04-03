#pragma once

#include <stdexcept>

template <typename T>
class SerialQueue {
 public:
  SerialQueue() : head_(new Node{T{}, nullptr}), size_(0) { tail_ = head_; }

  void enqueue(const T& x) {
    Node* newNode = new Node{x, nullptr};
    tail_->next = newNode;
    tail_ = newNode;
    size_++;
  }

  bool dequeue(T* ret) {
    Node* oldHead = head_;
    Node* newHead = oldHead->next;
    if (newHead == nullptr) {
      return false;
    }
    *ret = newHead->data;
    head_ = newHead;
    size_--;
    return true;
  }

  ~SerialQueue() {
    while (head_ != nullptr) {
      Node* temp = head_->next;
      delete head_;
      head_ = temp;
    }
  }

  int size() const { return size_; }

  T head() const {
    if (head_->next == nullptr) {
      throw std::out_of_range("Empty queue");
    }
    return head_->next->data;
  }

  T tail() const {
    if (head_ == tail_) {
      throw std::out_of_range("Empty queue");
    }
    return tail_->data;
  }

 private:
  struct Node {
    T data;
    Node* next;
  };

  Node* head_;
  Node* tail_;
  int size_;
};
