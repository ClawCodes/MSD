#include "raindrop.h"

#include <QtWidgets/qgraphicsscene.h>

#include <QPixmap>
#include <random>

#include "bucketman.h"

std::mt19937 gen(std::random_device{}());

int RainDrop::generateXPos() {
  std::uniform_int_distribution dist(xMin_, xMax_);
  return dist(gen);
}

RainDrop::RainDrop() {
  setPixmap(QPixmap("://water").scaled(20, 20));

  timer_ = new QTimer();
  connect(timer_, &QTimer::timeout, this, &RainDrop::move);
  timer_->start(100);

  setPos(generateXPos(), 0);
}

bool RainDrop::inBucket() {
  for (QGraphicsItem *item : collidingItems()) {
    BucketMan *collided = dynamic_cast<BucketMan *>(item);
    if (collided != nullptr) return true;
  }
  return false;
};

bool RainDrop::selfDestruct() {
  bool destrcut = y() > BG.Height || inBucket();
  if (destrcut) {
    scene()->removeItem(this);
    deleteLater();
  }
  return destrcut;
}

void RainDrop::move() {
  if (!selfDestruct()) setPos(x(), y() + 10);
}
