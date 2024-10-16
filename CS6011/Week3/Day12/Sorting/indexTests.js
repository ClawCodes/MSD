const { selectionSort } = require("./index.js");

// Tests
function compareArrays(arr1, arr2) {
    // Check if lengths are different
    if (arr1.length !== arr2.length) {
        return false;
    }

    // Check each element
    for (let i = 0; i < arr1.length; i++) {
        if (arr1[i] !== arr2[i]) {
            return false;
        }
    }

    // Arrays are equal
    return true;
}

// Sort integers
let intArr = Array(2,4,3,5,1);
let sortedIntArr = selectionSort(intArr);
let expectedIntArr = Array(1,2,3,4,5)
console.assert(compareArrays(expectedIntArr, sortedIntArr));

// Sort floats
let floatArr = Array(2.2,4.4,3.3,5.5,1.1);
let sortedFloatArr = selectionSort(floatArr);
let expectedFloatArr = Array(1.1,2.2,3.3,4.4,5.5)
console.assert(compareArrays(expectedFloatArr, sortedFloatArr));

// Sort strings
let strArr = Array("btest", "atest", "dtest", "ctest", "etest");
let sortedStrArr = selectionSort(strArr);
let expectedStrArr = Array("atest","btest","ctest","dtest","etest");
console.assert(compareArrays(expectedStrArr, sortedStrArr));

let strCapsArr = Array("Btest", "Atest", "Dtest", "Ctest", "Etest");
let sortedStrCapsArr = selectionSort(strCapsArr);
let expectedStrCapsArr = Array("Atest","Btest","Ctest","Dtest","Etest");
console.assert(compareArrays(expectedStrCapsArr, sortedStrCapsArr));

let strCapsAndLowerArr = Array("Aa", "aA", "cC", "Cc", "bB", "Bb");
let sortedStrCapsAndLowerArr = selectionSort(strCapsAndLowerArr);
let expectedStrCapsAndLowerArr = Array("Aa", "Bb", "Cc", "aA", "bB", "cC");
console.assert(compareArrays(expectedStrCapsAndLowerArr, sortedStrCapsAndLowerArr));

// Sort mixed types
let mixedArr = Array("btest", 1, 2.3, true, "Atest");
let sortedMixedArr = selectionSort(mixedArr);
let expectedMixedArr = Array("Atest", 1, true, 2.3, "btest");
console.assert(compareArrays(expectedMixedArr, sortedMixedArr));