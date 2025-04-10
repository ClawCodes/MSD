#include "filebrowser.h"

FileBrowser::FileBrowser(QWidget *parent) : QWidget(parent) {
  auto *layout = new QVBoxLayout(this);

  model_ = new QFileSystemModel(this);
  model_->setRootPath(QDir::rootPath());
  model_->setFilter(QDir::AllEntries | QDir::NoDotAndDotDot);

  treeView_ = new QTreeView(this);
  treeView_->setModel(model_);
  treeView_->setRootIndex(model_->index(QDir::homePath()));
  layout->addWidget(treeView_);

  connect(treeView_, &QTreeView::clicked, this, &FileBrowser::fileClicked);
}

void FileBrowser::fileClicked(const QModelIndex &index) {
  selectedFile_ = model_->filePath(index);
}

void FileBrowser::closeEvent(QCloseEvent *event) {
  emit browserClosed(selectedFile_);
  QWidget::closeEvent(event);
}
