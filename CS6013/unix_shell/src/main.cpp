#include <unistd.h>

#include <iostream>
#include <string>
#include <vector>

#include "shelpers.h"

int main() {
  std::string input;
  while (std::getline(std::cin, input)) {
    std::vector<std::string> tokens = tokenize(input);
    std::vector<Command> commands = getCommands(tokens);
    for (auto command : commands) {
      pid_t pid = fork();
      if (pid == -1) {
        perror("Fork Error");
      }
      if (pid == 0) {
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
