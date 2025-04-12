#include <QApplication>
#include <QGraphicsPixmapItem>
#include <QGraphicsView>

#include "constants.h"
#include "gamescene.h"

int main(int argc, char **argv) {
  QApplication app(argc, argv);

  gameScene *scene = new gameScene();
  QGraphicsView *view = new QGraphicsView();
  view->setHorizontalScrollBarPolicy((Qt::ScrollBarAlwaysOff));
  view->setVerticalScrollBarPolicy((Qt::ScrollBarAlwaysOff));
  view->setFixedSize(BG.Width, BG.Height);
  view->setScene(scene);
  view->show();

  return app.exec();
}
