/*********************************************************************
 *
 * File header comments go here... including student name, class, date, 
 * assignment, description, etc.
 *
 */

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h> // For strlen()
#include <assert.h>
#include <stdbool.h>

/*********************************************************************
 *
 * The C functions in this lab use patterns and functionality often found in
 * operating system code. Your job is to implement them.  Additionally, write some test
 * cases for each function (stick them in functions [called xyzTests(), etc or similar])
 * Call your abcTests(), etc functions in main().
 *
 * Write your own tests for each function you are testing, then share/combine
 * tests with a classmate.  Try to come up with tests that will break people's code!
 * Easy tests don't catch many bugs! [Note this is one specific case where you may
 * share code directly with another student.  The test function(s) from other students
 * must be clearly marked (add '_initials' to the function name) and commented with the
 * other student's name.
 *
 * Note: you may NOT use any global variables in your solution.
 * 
 * Errata:
 *   - You can use global vars in your testing functions (if necessary).
 *   - Don't worry about testing the free_list(), or the draw_me() functions.
 *
 * You must compile in C mode (not C++).  If you compile from the commandline
 * run clang, not clang++. Submit your solution files on Canvas.
 *
 *********************************************************************/

/*********************************************************************
 *
 * byte_sort()
 *
 * specification: byte_sort() treats its argument as a sequence of
 * 8 bytes, and returns a new unsigned long integer containing the
 * same bytes, sorted numerically, with the smaller-valued bytes in
 * the lower-order byte positions of the return value
 * 
 * EXAMPLE: byte_sort (0x0403deadbeef0201) returns 0xefdebead04030201
 * Ah, the joys of using bitwise operators!
 *
 * Hint: you may want to write helper functions for these two functions
 *
 *********************************************************************/
unsigned long generateMask(int index, int seqSize, bool zeroMask) {
    unsigned long result = 0;
    for (int i = 0; i < seqSize; i++) {
        result |= 1;
        result <<= 1;
    }
    result >>= 1;
    result <<= (64 - index - seqSize);

    return zeroMask ? ~result : result;
}

unsigned long extractBitValue(unsigned long num, int index, int seqSize) {
    unsigned long mask = generateMask(index, seqSize, false);
    return (num & mask) >> 64 - index - seqSize;
}

void updateBits(unsigned long *num, unsigned long updateVal, int index, int seqSize) {
    const unsigned long mask = generateMask(index, seqSize, true);
    *num &= mask; // zero out bits to insert value into
    updateVal <<= 64 - index - seqSize;
    // Shift updateVal to index position (assumes only first 8 bits are populated in updateVal)
    *num |= updateVal; // Insert value
}

unsigned long sortBits(unsigned long arr, int seqSize) {
    for (int i = 0; i < 64; i += seqSize) {
        unsigned long ithVal = extractBitValue(arr, i, seqSize);
        unsigned long maxVal = ithVal;
        int maxValIndex = i;
        for (int j = i + seqSize; j < 64; j += seqSize) {
            unsigned long jthVal = extractBitValue(arr, j, seqSize);
            if (jthVal > maxVal) {
                maxVal = jthVal;
                maxValIndex = j;
            }
        }
        // Swap values
        updateBits(&arr, maxVal, i, seqSize);
        updateBits(&arr, ithVal, maxValIndex, seqSize);
    }
    return arr;
}

unsigned long byte_sort(unsigned long arg) {
    return sortBits(arg, 8);
}

/*********************************************************************
 *
 * nibble_sort()
 *
 * specification: nibble_sort() treats its argument as a sequence of 16 4-bit
 * numbers, and returns a new unsigned long integer containing the same nibbles,
 * sorted numerically, with smaller-valued nibbles towards the "small end" of
 * the unsigned long value that you return
 *
 * the fact that nibbles and hex digits correspond should make it easy to
 * verify that your code is working correctly
 * 
 * EXAMPLE: nibble_sort (0x0403deadbeef0201) returns 0xfeeeddba43210000
 *
 *********************************************************************/

unsigned long nibble_sort(unsigned long arg) {
    return sortBits(arg, 4);
}

/*********************************************************************/

typedef struct elt {
    char val;
    struct elt *link;
} Elt;

/*********************************************************************/

/* Forward declaration of "free_list()"... This allows you to call   */
/* free_list() in name_list() [if you'd like].                       */
void free_list(Elt *head); // [No code goes here!]

/*********************************************************************
 *
 * name_list()
 *
 * specification: allocate and return a pointer to a linked list of
 * struct elts
 *
 * - the first element in the list should contain in its "val" field the first
 *   letter of your first name; the second element the second letter, etc.;
 *
 * - the last element of the linked list should contain in its "val" field
 *   the last letter of your first name and its "link" field should be a null
 *   pointer
 *
 * - each element must be dynamically allocated using a malloc() call
 * 
 * - you must use the "name" variable (change it to be your name).
 *
 * Note, since we're using C, not C++ you can't use new/delete!
 * The analog to delete is the free() function
 *
 * - if any call to malloc() fails, your function must return NULL and must also
 *   free any heap memory that has been allocated so far; that is, it must not
 *   leak memory when allocation fails
 *
 * Implement print_list and free_list which should do what you expect.
 * Printing or freeing a nullptr should do nothing.
 *
 * Note: free_list() might be useful for error handling for name_list()... 
 *
 *********************************************************************/

Elt *name_list(const char *name) {
    // iterate until we hit the termination char
    Elt* head = malloc(sizeof(Elt));
    head->val = name[0];
    Elt* previous = head;
    for (int i = 1; name[i] != '\0'; i++) {
        Elt* new = malloc(sizeof(Elt));
        new->val = name[i];
        previous->link = new;
        previous = new;
    }
    return head;
}

/*********************************************************************/

void print_list(Elt *head) {
    Elt* current = head;
    while (current != NULL) {
        printf("%c", current->val);
        current = current->link;
    }
    printf("\n");
}

/*********************************************************************/

void free_list(Elt *head) {
    Elt* current = head;
    while (current != NULL) {
        Elt* next = current->link;
        free(current);
        current = next;
    }
}

/*********************************************************************
 *
 * draw_me()
 *
 * This function creates a file called 'me.txt' which contains an ASCII-art
 * picture of you (it does not need to be very big).
 * 
 * Use the C stdlib functions: fopen, fclose, fprintf, etc which live in stdio.h
 * - Don't use C++ iostreams
 *
 *********************************************************************/

void draw_me() {
}

/*********************************************************************
 *
 * Test Code - Place your test functions in this section:
 */

void testGenerateMask() {
    // Mask with ones in specified span
    assert(generateMask(0, 8, false) == 0xff00000000000000);
    assert(generateMask(8, 8, false) == 0x00ff000000000000);
    assert(generateMask(16, 8, false) == 0x0000ff0000000000);
    assert(generateMask(56, 8, false) == 0x00000000000000ff);
    assert(generateMask(0, 4, false) == 0xf000000000000000);
    assert(generateMask(8, 4, false) == 0x00f0000000000000);
    assert(generateMask(16, 4, false) == 0x0000f00000000000);
    assert(generateMask(60, 4, false) == 0x000000000000000f);

    // Mask with ones in specified span
    assert(generateMask(0, 8, true) == 0x00ffffffffffffff);
    assert(generateMask(8, 8, true) == 0xff00ffffffffffff);
    assert(generateMask(16, 8, true) == 0xffff00ffffffffff);
    assert(generateMask(56, 8, true) == 0xffffffffffffff00);
    assert(generateMask(0, 4, true) == 0x0fffffffffffffff);
    assert(generateMask(8, 4, true) == 0xff0fffffffffffff);
    assert(generateMask(16, 4, true) == 0xffff0fffffffffff);
    assert(generateMask(60, 4, true) == 0xfffffffffffffff0);
}

void testExtractBitValue() {
    assert(extractBitValue(0xff00000000000000, 0, 8) == 255);
    assert(extractBitValue(0x0ff0000000000000, 4, 8) == 255);
    assert(extractBitValue(0x00000000000000ff, 56, 8) == 255);
}

void testUpdateBits() {
    unsigned long a = 0x0000000000000000;
    updateBits(&a, 255, 0, 8);
    assert(a == 0xff00000000000000);

    unsigned long b = 0x0000000000000000;
    updateBits(&b, 255, 56, 8);
    assert(b == 0x00000000000000ff);
}

void testSortBits() {
    assert(sortBits(0x0001020304050607, 8) == 0x0706050403020100);
    assert(sortBits(0x0403deadbeef0201, 8) == 0xefdebead04030201);
}

void testByteSort() {
    assert(byte_sort(0x0403deadbeef0201) == 0xefdebead04030201);
}

void testNibbleSort() {
    assert(nibble_sort(0x0403deadbeef0201) == 0xfeeeddba43210000);
}

void testNameList() {
    const char* nameList = "abc";
    Elt* abc = name_list(nameList);
    assert(abc->val == 'a');
    Elt* b = abc->link;
    assert(b->val == 'b');
    assert(abc->link->link->val == 'c');
    assert(abc->link->link->link == NULL);
    free_list(abc);
}

void testPrintList() {
    const char* nameList = "abc";
    Elt* abc = name_list(nameList);
    print_list(abc);
    free_list(abc);
}

/*********************************************************************
 *
 * main()
 *
 * The main driver program.  You can place your main() method in this
 * file to make compilation easier, or have it in a separate file.
 *
 *
 *********************************************************************/

int main() {
    // Call your test routines here...
    // testGenerateMask();
    // testExtractBitValue();
    // testUpdateBits();
    // testSortBits();
    // testByteSort();
    // testNibbleSort();
    testNameList();
    testPrintList();
    printf("All tests passed!");
    return 0;
}
