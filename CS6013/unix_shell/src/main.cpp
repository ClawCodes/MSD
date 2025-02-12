#include <readline/history.h>
#include <readline/readline.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include <cstdlib>
#include <filesystem>
#include <iostream>
#include <map>
#include <string>
#include <vector>

#include "shelpers.h"

namespace fs = std::filesystem;

std::map<pid_t, Command> backgroundPIDs;

void signalHandler(int signum) {
  if (signum == SIGCHLD) {
    std::vector<pid_t> toRemove;
    for (auto &pair : backgroundPIDs) {
      pid_t childId = waitpid(pair.first, nullptr, WNOHANG);
      if (childId == pair.first) {
        std::cout << pair.first << "\n";
        Command cmd = backgroundPIDs[childId];
        std::cout << "Background process '" << cmd.execName << "' [" << childId
                  << "] "
                  << "completed\n";
        toRemove.push_back(pair.first);
      }
    }
    for (pid_t pid : toRemove) {
      backgroundPIDs.erase(pid);
    }
  }
}

int main() {
  signal(SIGCHLD, signalHandler);

  std::string input;
  while (std::getline(std::cin, input)) {
    std::vector<std::string> tokens = tokenize(input);
    std::vector<Command> commands = getCommands(tokens);
    for (int i = 0; i < commands.size(); i++) {
      Command command = commands[i];
      if (command.execName == "cd") {
        const char *path;
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
               const_cast<char **>(command.argv.data()));
      } else {
        if (command.background) {
          backgroundPIDs[pid] = command;
        } else {
          waitpid(pid, nullptr, 0);
        }
        closeFileDescriptors(command);
      }
    }
  }
  return 0;
}
