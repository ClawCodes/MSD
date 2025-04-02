#pragma once

template <typename T>
class ConcurrentQueue {
 public:
  ConcurrentQueue() : head_(new Node{T{}, nullptr}), size_(0) { tail_ = head_; }

  void enqueue(const T& x) {
    if (size_ == 0) {
      head_->data = x;
      tail_ = head_;
    } else {
      Node* newNode = new Node{x, nullptr};
      tail_->next = newNode;
      tail_ = newNode;
    }
    size_++;
  }

  bool dequeue(T* ret) {
    if (size_ == 0) {
      return false;
    }
    *ret = head_->data;
    head_ = head_->next;
    size_--;
    if (size_ == 0) {
      tail_->data = T{};
      head_ = tail_;
    }
    return true;
  }

  ~ConcurrentQueue() {
    while (head_ != nullptr) {
      Node* temp = head_->next;
      delete head_;
      head_ = temp;
    }
  }

  int size() const { return size_; }

  T head() const { return head_->data; }

  T tail() const { return tail_->data; }

 private:
  struct Node {
    T data;
    Node* next;
  };

  Node* head_;
  Node* tail_;
  int size_;
};
