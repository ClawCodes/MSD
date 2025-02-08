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
    for (int i = 0; i < commands.size(); i++) {
      Command command = commands[i];
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
