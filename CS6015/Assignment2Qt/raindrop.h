#ifndef RAINDROP_H
#define RAINDROP_H

#include <QGraphicsPixmapItem>
#include <QTimer>

#include "constants.h"

class RainDrop : public QObject, public QGraphicsPixmapItem {
 private:
  static constexpr int xMax_ = BG.Width - 20;
  static constexpr int xMin_ = 0;
  QTimer *timer_;

  static int generateXPos();

  bool inBucket();
  bool selfDestruct();

 public:
  RainDrop();

 private slots:
  void move();
};

#endif  // RAINDROP_H
