//
// Created by Christopher Lawton on 2/5/25.
//

#ifndef SHELPERS_H
#define SHELPERS_H

#pragma once

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

#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include <iostream>
#include <string>
#include <vector>

//////////////////////////////////////////////////////////////////////////////////
// A struct to contain a command line program information.
// You'll need to fork/exec for each one of these commands!
// To get started, initially just assume the user tries to only
// execute 1 command.

struct Command {
  std::string execName;  // The name of the executable.

  // Remember argv[0] should be the name of the program (same as execName)
  // Also, argv should end with a nullptr!
  std::vector<const char*> argv;
  int inputFd, outputFd;
  bool background;
};

//////////////////////////////////////////////////////////////////////////////////
//
// getCommands()
//
//    Takes in a list of string tokens and places the results into Command
//    structures.
//
// Read through this function.  You'll need to fill in a few parts to implement
// I/O redirection and (possibly) backgrounded commands.
// Most of the places you need to fill in contain an assert(false), so you'll
// discover them when you try to use more functionality
//
// Returns an empty vector on error.
//
std::vector<Command> getCommands(const std::vector<std::string>& tokens);

//////////////////////////////////////////////////////////////////////////////////
// Helper function for you to use.  (Already implemented for you.)

// Takes in a command line (string) and returns it broken into separate pieces.
std::vector<std::string> tokenize(const std::string& command_line_string);

// Prints out the contents of a Command structure.  Useful for debugging.
std::ostream& operator<<(std::ostream& outs, const Command& c);

// Dups stdin and/or stdout should there be redirection or piping
void dupFileDescriptors(const Command& command);

// Close non-standard file descriptors
void closeFileDescriptors(const Command& command);

// Close all pipes that the provided command will not use
void closePipes(const std::vector<Command>& commands, int index);

#endif  // SHELPERS_H
