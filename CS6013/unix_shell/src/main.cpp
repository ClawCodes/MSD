#include <stdlib.h>
#include <unistd.h>

#include <cstdlib>
#include <filesystem>
#include <iostream>
#include <string>
#include <vector>

#include "shelpers.h"

namespace fs = std::filesystem;

int main() {
  std::string input;
  std::cout << ">>> ";
  while (std::getline(std::cin, input)) {
    std::vector<std::string> tokens = tokenize(input);
    std::vector<Command> commands = getCommands(tokens);
    std::cout << ">>> ";
    for (int i = 0; i < commands.size(); i++) {
      Command command = commands[i];
      if (command.execName == "cd") {
        const char* path;
        if (command.argv.size() < 3) {
          path = std::getenv("HOME");
          if (path == nullptr) {
            std::cout << "HOME environment variable is empty\n";
            continue;
          }
        } else {
          path = command.argv[1];
        }
        if (chdir(path) == -1) {
          perror("chdir");
        }
        continue;
      }
      pid_t pid = fork();
      if (pid == -1) {
        perror("Fork Error");
      }
      if (pid == 0) {
        closePipes(commands, i);
        dupFileDescriptors(command);
        execvp(command.execName.c_str(),
               const_cast<char**>(command.argv.data()));
      } else {
        waitpid(pid, nullptr, 0);
        closeFileDescriptors(command);
      }
    }
  }
  return 0;
}
