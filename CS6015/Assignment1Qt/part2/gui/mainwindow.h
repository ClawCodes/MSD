#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QFileSystemModel>
#include <QGridLayout>
#include <QPushButton>
#include <QRadioButton>
#include <QString>
#include <QTextEdit>
#include <QTreeView>
#include <QVBoxLayout>
#include <QWidget>

class MainWindow : public QWidget {
 private:
  QGridLayout *layout_;
  QTextEdit *expression_;
  QTextEdit *result_;
  QRadioButton *interp_;
  QRadioButton *print_;
  QPushButton *submit_;
  QPushButton *reset_;

  QPushButton *loadFile_;
  QFileSystemModel *model_;
  QTreeView *treeView_;
  QVBoxLayout *fileViewLayout_;

  QGridLayout *createLayout();

  void clearRadioBtn(QRadioButton *btn);

 public:
  MainWindow(QWidget *parent = nullptr);

 public slots:
  void interpExpression();
  void clearAll();
  void loadFileSystemView();
  void loadFile(QString &file);
};

#endif  // MAINWINDOW_H
