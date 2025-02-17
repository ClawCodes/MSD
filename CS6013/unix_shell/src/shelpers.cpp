#include "shelpers.h"

#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>

//////////////////////////////////////////////////////////////////////////////////
//
// Author: Ben Jones (I think) with a lot of clean up by J. Davison de St.
// Germain
//
// Date:   2019?
//         Jan 2022 - Cleanup
//
// Class: CS 6013 - Systems I
//
//////////////////////////////////////////////////////////////////////////////////

using namespace std;

////////////////////////////////////////////////////////////////////////
// Example test commands you can try once your shell is up and running:
//
// ls
// ls | nl
// cd [dir]
// cat < shelpers.cpp
// cat < shelpers.cpp | nl
// cat shelpers.cpp | nl
// cat shelpers.cpp | nl | head -50 | tail -10
// cat shelpers.cpp | nl | head -50 | tail -10 > ten_lines.txt
//
// - The following two commands are equivalent.  [data.txt is sent into nl and
// the
//   output is saved to numbered_data.txt.]
//
// nl > numbered_data.txt < data.txt
// nl < data.txt > numbered_data.txt
//
// - Assuming numbered_data.txt has values in it... try running:
//   [Note this probably doesn't work like one might expect...
//    does it behave the same as your normal shell?]
//
// nl < numbered_data.txt > numbered_data.txt
//
// - The following line is an error (input redirection at end of line).
//   It should fail gracefully (ie, 1) without doing anything, 2) cleaning
//   up any file descriptors that were opened, 3) giving an appropriate
//   message to the user).
//
// cat shelpers.cpp | nl | head -50 | tail -10 > ten_lines.txt < abc
//

////////////////////////////////////////////////////////////////////////
// This routine is used by tokenize().  You do not need to modify it.

bool splitOnSymbol(vector<string>& words, int i, char c) {
  if (words[i].size() < 2) {
    return false;
  }
  int pos;
  if ((pos = words[i].find(c)) != string::npos) {
    if (pos == 0) {
      // Starts with symbol.
      words.insert(words.begin() + i + 1,
                   words[i].substr(1, words[i].size() - 1));
      words[i] = words[i].substr(0, 1);
    } else {
      // Symbol in middle or end.
      words.insert(words.begin() + i + 1, string{c});
      string after = words[i].substr(pos + 1, words[i].size() - pos - 1);
      if (!after.empty()) {
        words.insert(words.begin() + i + 2, after);
      }
      words[i] = words[i].substr(0, pos);
    }
    return true;
  } else {
    return false;
  }
}

////////////////////////////////////////////////////////////////////////
// You do not need to modify tokenize().

vector<string> tokenize(const string& s) {
  vector<string> ret;
  int pos = 0;
  int space;

  // Split on spaces:

  while ((space = s.find(' ', pos)) != string::npos) {
    string word = s.substr(pos, space - pos);
    if (!word.empty()) {
      ret.push_back(word);
    }
    pos = space + 1;
  }

  string lastWord = s.substr(pos, s.size() - pos);

  if (!lastWord.empty()) {
    ret.push_back(lastWord);
  }

  for (int i = 0; i < ret.size(); ++i) {
    for (char c : {'&', '<', '>', '|'}) {
      if (splitOnSymbol(ret, i, c)) {
        --i;
        break;
      }
    }
  }
  return ret;
}

////////////////////////////////////////////////////////////////////////

ostream& operator<<(ostream& outs, const Command& c) {
  outs << c.execName << " [argv: ";
  for (const auto& arg : c.argv) {
    if (arg) {
      outs << arg << ' ';
    } else {
      outs << "NULL ";
    }
  }
  outs << "] -- FD, in: " << c.inputFd << ", out: " << c.outputFd << " "
       << (c.background ? "(background)" : "(foreground)");
  return outs;
}

////////////////////////////////////////////////////////////////////////
//
// getCommands()
//
// Parses a vector of command line tokens and places them into (as appropriate)
// separate Command structures.
//
// Returns an empty vector if the command line (tokens) is invalid.
//
// You'll need to fill in a few gaps in this function and add appropriate error
// handling at the end.  Note, most of the gaps contain "assert( false )".
//

vector<Command> getCommands(const vector<string>& tokens) {
  vector<Command> commands(count(tokens.begin(), tokens.end(), "|") +
                           1);  // 1 + num |'s commands

  int first = 0;
  int last = find(tokens.begin(), tokens.end(), "|") - tokens.begin();

  bool error = false;

  for (int cmdNumber = 0; cmdNumber < commands.size(); ++cmdNumber) {
    const string& token = tokens[first];

    if (token == "&" || token == "<" || token == ">" || token == "|") {
      error = true;
      break;
    }

    Command& command =
        commands[cmdNumber];  // Get reference to current Command struct.
    command.execName = token;

    // Must _copy_ the token's string (otherwise, if token goes out of scope
    // (anywhere) this pointer would become bad...) Note, this fixes a security
    // hole in this code that had been here for quite a while.

    command.argv.push_back(strdup(token.c_str()));  // argv0 == program name

    command.inputFd = STDIN_FILENO;
    command.outputFd = STDOUT_FILENO;

    command.background = false;

    bool openFile = false;
    for (int j = first + 1; j < last; ++j) {
      if (openFile) {
        const char* fileName = tokens[j].c_str();
        if (tokens[j - 1] == "<") {
          if (cmdNumber !=
              0) {  // Input redirection can only occur with the first command
            error = true;
            break;
          }
          int inputFd = open(fileName, O_RDONLY);
          if (inputFd == -1) {
            error = true;
            break;
          }
          command.inputFd = inputFd;
        } else {
          if (cmdNumber !=
              commands.size() - 1) {  // Only the last command can have file
                                      // output redirection
            error = true;
            break;
          }
          int outputFd = open(fileName, O_WRONLY | O_CREAT,
                              0644);  // Create file if not exists
          if (outputFd == -1) {
            error = true;
            break;
          }
          command.outputFd = outputFd;
        }
        openFile = false;
        continue;  // Skip other processing when file name is encountered
      }
      if (tokens[j] == ">" || tokens[j] == "<") {
        openFile = true;
      } else if (tokens[j] == "&") {
        command.background = true;
      } else {
        // Otherwise this is a normal command line argument! Add to argv.
        command.argv.push_back(tokens[j].c_str());
      }
    }

    if (!error) {
      if (cmdNumber > 0) {
        int pipeFds[2];
        pipe(pipeFds);
        commands[cmdNumber - 1].outputFd =
            pipeFds[1];  // Set last command's output to write end of pipe
        command.inputFd =
            pipeFds[0];  // Set current command's input to the read end of pipe
      }

      // Exec wants argv to have a nullptr at the end!
      command.argv.push_back(nullptr);

      // Find the next pipe character
      first = last + 1;

      if (first < tokens.size()) {
        last = find(tokens.begin() + first, tokens.end(), "|") - tokens.begin();
      }
    }  // end if !error
  }    // end for( cmdNumber = 0 to commands.size )

  if (error) {
    // Close all open descriptors when error occurs
    for (const Command& command : commands) {
      closeFileDescriptors(command);
    }

    throw runtime_error("ERROR");
  }

  return commands;

}  // end getCommands()

/**
 * Duplicate the file descriptors in a command struct with dup2
 * @param command populated command struct
 */
void dupFileDescriptors(const Command& command) {
  if (command.inputFd != STDIN_FILENO) {
    if (dup2(command.inputFd, STDIN_FILENO) == -1) {
      perror("dup2 stdin");
    }
  }
  if (command.outputFd != STDOUT_FILENO) {
    if (dup2(command.outputFd, STDOUT_FILENO) == -1) {
      perror("dup2 stdout");
    }
  }
}

/**
 * Close the file descriptors in a populated command struct.
 * Note: this does not close the descriptor when STDIN or STDOUT
 * @param command populated command struct
 */
void closeFileDescriptors(const Command& command) {
  if (command.inputFd != STDIN_FILENO) {
    close(command.inputFd);
  }
  if (command.outputFd != STDOUT_FILENO) {
    close(command.outputFd);
  }
}

/**
 * Close all file descriptors in a vector of commands other than the command at
 * the provided index
 * @param commands Vector of populated command structs
 * @param index index of command to skip
 */
void closePipes(const std::vector<Command>& commands, int index) {
  for (int i = index; i < commands.size(); ++i) {
    if (i != index) {
      closeFileDescriptors(commands[i]);
    }
  }
}
