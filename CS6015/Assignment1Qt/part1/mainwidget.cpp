#include "mainwidget.h"

#include <QDialogButtonBox>
#include <QGridLayout>
#include <QGroupBox>
#include <QHBoxLayout>
#include <QLabel>
#include <QSpacerItem>
#include <QString>

QGridLayout *MainWidget::createGridLayout() {
  QGridLayout *grid = new QGridLayout();
  // First row
  grid->addWidget(new QLabel("First Name"), 0, 0);
  firstName = new QLineEdit();
  grid->addWidget(firstName, 0, 1);
  grid->addWidget(new QLabel("Age"), 0, 2);
  age = new QSpinBox();
  grid->addWidget(age, 0, 3);
  // Second row
  grid->addWidget(new QLabel("Last Name"), 1, 0);
  lastName = new QLineEdit();
  grid->addWidget(lastName, 1, 1);
  // Third row
  grid->addWidget(new QLabel("Gender"), 2, 0);
  QGroupBox *radioGroup =
      new QGroupBox();  // create group to hold radio buttons
  QVBoxLayout *radioBox = new QVBoxLayout();
  male = new QRadioButton("Male");
  radioBox->addWidget(male);
  female = new QRadioButton("Female");
  radioBox->addWidget(female);
  radioGroup->setLayout(radioBox);
  grid->addWidget(radioGroup, 2, 1);
  // Fourth row
  refreshBtn = new QPushButton("Refresh");
  grid->addWidget(refreshBtn, 3, 0);
  // add spacing
  for (int row = 0; row < 4; row++) {
    for (int col = 0; col < 4; col++) {
      grid->addItem(new QSpacerItem(50, 50), row, col);
    }
  }

  return grid;
}

void MainWidget::fillSummary() {
  QString first = "First Name: " + firstName->text() + "\n";
  QString last = "Last Name: " + lastName->text() + "\n";
  QString age_ = "Age: " + age->text() + "\n";

  QString gender = "Gender: ";
  if (male->isChecked())
    gender += "Male";
  else if (female->isChecked())
    gender += "Female";
  else
    gender += "None";

  freeText->setText(first + last + age_ + gender);
}

void MainWidget::clearRadioBtn(QRadioButton *btn) {
  btn->setAutoExclusive(0);
  btn->setChecked(false);
  btn->setAutoExclusive(1);
}

void MainWidget::clearAll() {
  firstName->clear();
  lastName->clear();
  clearRadioBtn(male);
  clearRadioBtn(female);
  freeText->clear();
  age->clear();
}

QVBoxLayout *MainWidget::createLayout() {
  QVBoxLayout *vBox = new QVBoxLayout();
  vBox->addItem(createGridLayout());
  freeText = new QTextEdit();
  vBox->addWidget(freeText);
  finishBtn = new QPushButton("Finish");
  vBox->addWidget(finishBtn);

  return vBox;
}

MainWidget::MainWidget(QWidget *parent) : QWidget{parent} {
  setLayout(createLayout());
  // Connect signals and slots
  connect(refreshBtn, &QPushButton::clicked, this, &MainWidget::fillSummary);
  connect(finishBtn, &QPushButton::clicked, this, &MainWidget::clearAll);
}
