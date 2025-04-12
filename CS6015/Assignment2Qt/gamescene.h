#ifndef GAMESCENE_H
#define GAMESCENE_H

#include <QGraphicsPixmapItem>
#include <QGraphicsScene>
#include <QTimer>

#include "bucketman.h"

class gameScene : public QGraphicsScene {
 private:
  BucketMan *bucket_;
  QTimer *rainTimer_;

  void createRainDrop();

 public:
  void removeItem(QGraphicsPixmapItem *item);
  gameScene();
  BucketMan *getBucket();
};

#endif  // GAMESCENE_H
