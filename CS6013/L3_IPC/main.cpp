//
// Created by Christopher Lawton on 2/3/25.
//

#include <stdio.h>
#include <unistd.h>
#include <iostream>

int main(int argc, char *argv[]) {
    char *cmd;
    if (argc >= 2) {
        cmd = argv[1];
    }
    int fds[2];
    int rc = pipe(fds);

    int writef = fds[1];
    int readf = fds[0];

    pid_t pid = fork();
    if (pid == -1) {
        perror("Fork Error");
        exit(1);
    } else if (pid == 0) {
        // Child
        char data[6];
        read(readf, data, 6);
        std::cout << "child\n";
        std::cout << data << '\n';
    } else {
        // Parent
        std::cout << "parent\n";
        const char str[6] = "hello";
        write(writef, &str, 6);
        int status;
        waitpid(pid, &status, 0);
        return 0;
    }
}
