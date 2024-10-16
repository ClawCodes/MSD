"use strict";

function findMinLocation(arr, index, compareTo) {
    let minIdx = index;
    for (let i = index; i < arr.length; i++) {
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
        let minIdx = findMinLocation(arr, i, compareTo);
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