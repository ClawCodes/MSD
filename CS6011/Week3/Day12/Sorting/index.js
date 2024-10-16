function findMinLocation(arr){
    let previous = arr[0];
    let minIdx = 0;
    for (let i = 1; i < arr.length; i++) {
        if (arr[i] < previous) {
            previous = arr[i];
            minIdx = i;
        }
    }
    return minIdx;
}

function selectionSort(arr){
    for (var i = 0; i < arr.length ; i++) {
        let minIdx = findMinLocation(arr.slice(i, arr.length)) + i;
        let temp = arr[minIdx];
        arr[minIdx] = arr[i];
        arr[i] = temp;
    }
    return arr;
}

module.exports = {
    selectionSort
}