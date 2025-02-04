//
// Created by Christopher Lawton on 2/3/25.
//

#include <stdio.h>
#include <unistd.h>
#include <iostream>
#include <signal.h>


void signalHandler(int signum) {
    if (signum == SIGTERM) {
        exit(0);
    }
    std::cout << ">>> ";
}

void childHandler(int fd) {
    int wordLen = 0;
    read(fd, &wordLen, sizeof(wordLen));

    char data[wordLen];
    read(fd, data, wordLen);

    std::string out = std::string(data);
    int parentPid = getppid();

    if (out == "quit") {
        kill(parentPid, SIGTERM);
        exit(0);
    }

    std::cout << std::string(data) << '\n';

    kill(parentPid, SIGUSR1);
}

void parentHandler(int fd, std::string word, pid_t childPid) {
    // Parent
    int wordLen = word.size();
    write(fd, &wordLen, sizeof(wordLen));
    write(fd, &word, wordLen);
    pause();
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        throw std::runtime_error("Please provide a word to pipe.");
    }

    const char *param = argv[1];
    std::string word = std::string(param);

    int fds[2];
    pipe(fds);

    int writef = fds[1];
    int readf = fds[0];

    pid_t pid = fork();
    if (pid == -1) {
        perror("Fork Error");
        exit(1);
    }

    signal(SIGUSR1, signalHandler);
    signal(SIGTERM, signalHandler);

    while (true) {
        if (pid == 0) {
            childHandler(readf);
        } else {
            parentHandler(writef, word, pid);
            std::cin >> word;
        }
    }
}
