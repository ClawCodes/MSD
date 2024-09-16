# SFML Lab
In this lab, create a program that opens an SFML window and draws a shape on the screen.

First, install homewbrew by following the instructions from the homebrew website

## Then, install Cmake and SFML using homebrew:

### brew install sfml cmake

We'll look at 2 ways of using CMake to build and run our project.

Look at the SFML + CMake sample files [files](./). Keep the same file structure (the CMakeLists.txt file should be in the top folder of your project, and the cpp file should be in the src folder).

## CMake + Xcode

CMake will generate an Xcode project for you. From the folder containing your CmakeLists.txt file:

mkdir xcode
cd xcode
cmake .. -G Xcode  # use the CmakeLists.txt file from 1 level up to generate an XCode project
open testSFML.xcodeproj #open it in xcode


If you run into CMake Error at CMakeLists.txt:25 or No CMAKE_C_COMPILER could be found, then run the following command:

sudo xcode-select --reset

As you add new files, be sure to add the .cpp and .hpp (or .h) files to the list of files in the add_executable line of the CmakeLists file.

A note about working with this stuff with git:

Your xcode folders should not contain any code that you write. When I checkout code from git, I often delete these folders and remake them to make sure I don't accidentally try to use old .o files or anything. DO NOT CHECK THEM INTO GIT!

Note these instructions are for about the easiest possible way to set up a new project... it's not unusual to waste days trying to figure this sort of thing out!

##Getting SFML to play nicely with XCode

In order for XCode to be able to see the SFML (or any other external library) you need to modify some of its settings to tell XCode where those libraries are.  To do so, follow these steps: (Note, this assumes that your homebrew code was downloaded into /opt/homebrew.)

Click on the name of your project (in the top left-hand panel of your XCode IDE)
In the main panel of the IDE you will now be able to click on the "Build Settings" tab.
Make sure the "All" button is selected (In the "Basic Customized All" bar).
In the filter (search) field, type "other linker flags"
You should now see a "Other Linker Flags" field.  
Click to the right of this field and type in:
-L/opt/homebrew/lib -lsfml-graphics -lsfml-system -lsfml-window
Finally, in the filter, type "header search paths"
Click to the right of the "Header Search Paths" field that should be shown and enter:
/opt/homebrew/include

Now your project should pull in the SFML include files and libraries and allow you to build successfully.
