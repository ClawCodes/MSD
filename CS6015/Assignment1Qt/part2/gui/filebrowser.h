#ifndef FILEBROWSER_H
#define FILEBROWSER_H

#include <QFileSystemModel>
#include <QTreeView>
#include <QVBoxLayout>
#include <QWidget>

class FileBrowser : public QWidget {
  Q_OBJECT

 private:
  QFileSystemModel *model_;
  QTreeView *treeView_;
  QString selectedFile_;

  // Override close event to send filePath once closed
  void closeEvent(QCloseEvent *event) override;

 public:
  FileBrowser(QWidget *parent = nullptr);

 signals:
  void browserClosed(QString &filePath);

 public slots:
  void fileClicked(const QModelIndex &index);
};

#endif  // FILEBROWSER_H
