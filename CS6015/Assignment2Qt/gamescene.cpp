#include "gamescene.h"

#include "constants.h"
#include "raindrop.h"

void gameScene::createRainDrop() { addItem(new RainDrop()); }

gameScene::gameScene() {
  setBackgroundBrush(QPixmap("://background"));
  setSceneRect(0, 0, BG.Width, BG.Height);

  bucket_ = new BucketMan();
  addItem(bucket_);
  bucket_->setFlag(QGraphicsItem::ItemIsFocusable);
  bucket_->setFocus();

  rainTimer_ = new QTimer();
  connect(rainTimer_, &QTimer::timeout, this, &gameScene::createRainDrop);
  rainTimer_->start(1000);
}

void gameScene::removeItem(QGraphicsPixmapItem *item) { delete item; }

BucketMan *gameScene::getBucket() { return bucket_; }
