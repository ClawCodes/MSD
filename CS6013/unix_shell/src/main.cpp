#include <unistd.h>

#include <iostream>
#include <string>
#include <vector>
// #include <stdlib.h>
#include <filesystem>

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
        if (command.argv.size() < 2) {
          // TODO: FIX CHANGE DIRS
          auto home = std::getenv("HOME");
          if (home == nullptr) {
            std::cout << "HOME environment variable is empty\n";
            continue;
          }
          changeDir(home);
        } else {
          std::string path = std::string(command.argv[1]);
          fs::path currentPath = fs::current_path();
          if (path == ".") {
            continue;
          }
          // adjust relative dirs
          while (path.substr(0, 2) == "..") {
            path = path.substr(2, path.size());
            if (path.substr(0, 1) == "/") {
              path = path.substr(1, path.size());
            }
            currentPath = currentPath.parent_path();
          }
          std::cout << currentPath / path << std::endl;
          changeDir((currentPath / path).c_str());
        }
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
