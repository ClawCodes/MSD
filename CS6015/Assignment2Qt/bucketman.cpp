#include "bucketman.h"

#include <QKeyEvent>
#include <QPixmap>

BucketMan::BucketMan() {
  setPixmap(QPixmap("://bucket").scaled(200, 200));
  setPos(BG.Width / 2,
         BG.Height / 1.35);  // Set starting point relative to background size
}

bool BucketMan::inBounds() { return x() > xMin_ && x() < xMax_; }

void BucketMan::keyPressEvent(QKeyEvent *event) {
  if (event->key() == Qt::Key_Right) {
    int newX = x() < xMax_ ? x() + step_ : x();
    setPos(newX, y());
  }
  if (event->key() == Qt::Key_Left && x() > xMin_) {
    int newX = x() > xMin_ ? x() - step_ : x();
    setPos(newX, y());
  }
}
