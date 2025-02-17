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
std::string PROMPT = ">>> ";

void prompt() { std::cout << PROMPT; }

std::map<pid_t, Command> backgroundPIDs;

/**
 * Handler for signals sent to the parent process
 * @param signum signal type (passed by OS)
 */
void signalHandler(int signum) {
  // Check if SIGCHLD sent by background process:
  // if yes, report process complete and remove from backgroundPIDs
  // otherwise continue
  if (signum == SIGCHLD) {
    std::vector<pid_t> toRemove;
    for (auto &pair : backgroundPIDs) {
      pid_t childId = waitpid(pair.first, nullptr, WNOHANG);
      if (childId == pair.first) {
        Command cmd = backgroundPIDs[childId];
        std::string msg = "\nBackground process '" + cmd.execName + "' [" +
                          std::to_string(childId) + "] completed\n";
        write(STDOUT_FILENO, msg.c_str(),
              msg.size());  // write (not std::cout) for async-safe write
        toRemove.push_back(pair.first);
      }
    }
    for (pid_t pid : toRemove) {
      backgroundPIDs.erase(pid);
    }
    if (toRemove.size() > 0) {
      write(STDOUT_FILENO, PROMPT.c_str(),
            sizeof(PROMPT) - 1);  // async-safe write
    }
  }
}

int main() {
  signal(SIGCHLD, signalHandler);

  std::string input;
  pid_t parentPid = getpid();  // Store parent process ID for future comparisons
  prompt();
  while (std::getline(std::cin, input)) {
    prompt();
    std::vector<std::string> tokens = tokenize(input);
    std::vector<Command> commands = getCommands(tokens);
    std::vector<pid_t> waitPids;
    for (int i = 0; i < commands.size(); i++) {
      Command command = commands[i];
      // If command is cd [options] then execute in main process
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
          waitPids.push_back(pid);
        }
        closeFileDescriptors(command);
      }
    }
    if (getpid() == parentPid) {
      for (pid_t pid : waitPids) {
        waitpid(pid, nullptr, 0);
      }
    }
  }
  return 0;
}
