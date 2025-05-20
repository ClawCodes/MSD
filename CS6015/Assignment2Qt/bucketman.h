#ifndef BUCKETMAN_H
#define BUCKETMAN_H

#include <QGraphicsPixmapItem>

#include "constants.h"

class BucketMan : public QObject, public QGraphicsPixmapItem {
 private:
  static constexpr int xMin_ = 0 - 100;
  static constexpr int xMax_ = BG.Width - 100;
  static constexpr int step_ = 20;

  void keyPressEvent(QKeyEvent *event) override;
  bool inBounds();

 public:
  BucketMan();
};

#endif  // BUCKETMAN_H
