"use strict";

function findMinLocation(arr, compareTo) {
    let minIdx = 0;
    for (let i = 1; i < arr.length; i++) {
        if (compareTo(arr[i], arr[minIdx])) {
            minIdx = i;
        }
    }
    return minIdx;
}

function selectionSort(arr, compareTo = (a, b) => {
    return a < b
}) {
    for (let i = 0; i < arr.length; i++) {
        // Add i as we are passing a smaller slice of the array each iteration
        let minIdx = findMinLocation(arr.slice(i, arr.length), compareTo) + i;
        let temp = arr[minIdx];
        arr[minIdx] = arr[i];
        arr[i] = temp;
    }
    return arr;
}

class Person {
    constructor(first, last) {
        this.first = first;
        this.last = last
    }
}

function personComparator(person1, person2) {
    if (person1.last === person2.last) {
        return person1.first < person2.first;
    } else {
        return person1.last < person2.last
    }
}

module.exports = {
    selectionSort,
    Person,
    personComparator
}