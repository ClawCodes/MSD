#ifndef MAINWIDGET_H
#define MAINWIDGET_H

#include <QLineEdit>
#include <QPushButton>
#include <QRadioButton>
#include <QSpinBox>
#include <QTextEdit>
#include <QVBoxLayout>
#include <QWidget>

class MainWidget : public QWidget {
  // Q_OBJECT

 private:
  QLineEdit *firstName;
  QLineEdit *lastName;
  QSpinBox *age;
  QRadioButton *male;
  QRadioButton *female;
  QPushButton *refreshBtn;
  QTextEdit *freeText;
  QPushButton *finishBtn;

  QGridLayout *layout;

  QGridLayout *createGridLayout();

  QVBoxLayout *createLayout();

  void clearRadioBtn(QRadioButton *btn);

 public:
  MainWidget(QWidget *parent = nullptr);

 public slots:
  void fillSummary();
  void clearAll();
};

#endif  // MAINWIDGET_H
