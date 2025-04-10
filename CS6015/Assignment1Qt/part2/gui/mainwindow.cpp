#include "mainwindow.h"

#include <QtGui/qscreen.h>

#include <QDir>
#include <QFileSystemModel>
#include <QGroupBox>
#include <QLabel>
#include <QModelIndex>
#include <QScroller>
#include <QSize>
#include <QTreeView>
#include <fstream>

#include "Env.h"
#include "expr.h"
#include "filebrowser.h"
#include "parse.h"
#include "pointer.h"

QGridLayout *MainWindow::createLayout() {
  layout_ = new QGridLayout();

  layout_->addWidget(new QLabel("Expression:"), 0, 0);
  expression_ = new QTextEdit();
  layout_->addWidget(expression_, 0, 1);

  // Create group to hold radio buttons
  QGroupBox *radioGroup = new QGroupBox();
  QVBoxLayout *radioBox = new QVBoxLayout();
  interp_ = new QRadioButton("interp");
  radioBox->addWidget(interp_);
  print_ = new QRadioButton("pretty print");
  radioBox->addWidget(print_);
  radioGroup->setLayout(radioBox);
  layout_->addWidget(radioGroup, 1, 1);

  submit_ = new QPushButton("Submit");
  layout_->addWidget(submit_, 2, 1);

  layout_->addWidget(new QLabel("Result:"), 3, 0);
  result_ = new QTextEdit();
  layout_->addWidget(result_, 3, 1);

  reset_ = new QPushButton("Reset");
  layout_->addWidget(reset_, 4, 1);

  loadFile_ = new QPushButton("Load file");
  layout_->addWidget(loadFile_, 4, 2);

  return layout_;
}

void MainWindow::loadFile(QString &filePath) {
  std::string inFile = filePath.toUtf8().constData();
  std::ifstream file(inFile);

  if (!file) throw std::runtime_error("Could not read file: " + inFile);

  std::ostringstream buffer;
  buffer << file.rdbuf();

  expression_->setText(QString::fromStdString(buffer.str()));
}

void MainWindow::loadFileSystemView() {
  auto *fileBrowser = new FileBrowser();
  fileBrowser->setAttribute(Qt::WA_DeleteOnClose);
  fileBrowser->setWindowTitle("File Browser");
  fileBrowser->resize(600, 400);
  fileBrowser->show();

  // Listen for file browser close to then load the selected file
  connect(fileBrowser, &FileBrowser::browserClosed, this,
          &MainWindow::loadFile);
}

void MainWindow::interpExpression() {
  QString expression = expression_->toPlainText();
  std::string expr = expression.toUtf8().constData();
  PTR(Expr) parsedExpr = expr_from_string(expr);

  if (interp_->isChecked())
    expr = parsedExpr->interp(Env::empty)->to_string();
  else if (print_->isChecked())
    expr = parsedExpr->toPrettyString();
  else
    throw std::runtime_error("No mode provided");

  result_->setText(QString::fromStdString(expr));
};

void MainWindow::clearRadioBtn(QRadioButton *btn) {
  btn->setAutoExclusive(0);
  btn->setChecked(false);
  btn->setAutoExclusive(1);
}

void MainWindow::clearAll() {
  expression_->clear();
  clearRadioBtn(interp_);
  clearRadioBtn(print_);
  result_->clear();
}

MainWindow::MainWindow(QWidget *parent) : QWidget{parent} {
  setLayout(createLayout());

  // Connect signals and slots
  connect(submit_, &QPushButton::clicked, this, &MainWindow::interpExpression);
  connect(reset_, &QPushButton::clicked, this, &MainWindow::clearAll);
  connect(loadFile_, &QPushButton::clicked, this,
          &MainWindow::loadFileSystemView);
}
