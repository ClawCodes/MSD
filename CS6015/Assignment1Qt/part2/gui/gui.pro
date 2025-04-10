CONFIG += c++20

QT += widgets

HEADERS += \
    filebrowser.h \
    mainwindow.h

SOURCES += \
    filebrowser.cpp \
    main.cpp \
    mainwindow.cpp \
    ../src/Env.cpp \
    ../src/expr.cpp \
    ../src/parse.cpp \
    ../src/val.cpp

INCLUDEPATH += ../src/

QMAKE_MACOSX_DEPLOYMENT_TARGET = 13.3

DISTFILES += \
    example_expression
