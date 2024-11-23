# Lab 6 Analysis

## Experiment 1 - Tree Construction

![](TreeConstructionOrderedSet.png)

Here we see the runtime of adding an entire ascending ordered list into the pqueue min heap (`ArrayQueue`) and the pqueue treeset (`TreeSetQueue`).
`ArrayQueue` outperforms `TreeSetQueue` which was expected as the `TreeSetQueue` has a guaranteed runtime of `O(nlogn)` whereas `ArrayQueue` has the following potential runtimes:
* Add with no required percolating up - `O(n)`
* Add requiring percolating up - `O(nlogn)`
* Add requiring growing the array and percolating up - `O(n^2logn)`

Given the array we are populating the heap with is sorted, thus won't require percolating up, and growing the array becomes increasingly rare, we can say the average runtime of constructing a populated `ArrayQueue` object is `O(n)`.
Thus, I expected `TreeSetQueue` to have a runtime of `O(nlogn)` and `ArrayQueue` to have a runtime of `O(n)`.
This expectation is supported as `ArrayQueue` displays a `O(n)` trend and `TreeSetQueue`, an `O(nlogn)` trend.

![](TreeConstructionPermutedSet.png)

Expect:
* ArrayQueue - slows down as we need to percolate up more -> average runtime to `O(n)` and worst to `O(nlogn)` most often
* TreeSetQueue - Stays at `O(nlogn)` - 

In the chart above, we perform the same construction of the trees as in the first chart, except we now build the trees using a permuted list.
Here I would expect `TreeSetQueue` to remain the same as it has a guaranteed runtime of `O(nlogn)` and `ArrayQueue` would slow down a bit, beause this would require percolating up more often moving it closer to a `O(nlogn)` runtime.
However, even with percolating up being required the average percolate up runtime is `O(1)`.
So, I would expect a slowdown from `ArrayQueue`, but not an entirely different time complexity.
Overall the chart above matches my expectations, besides one caveat - the trends remained, but the overall runtime increased for all data points even for the `TreeSet`.
Since the increase in runtime is applied to the entire chart (both data structures) my assumption is the experiment conflicted with other processes on my machine at time of running. 

## Experiment 3 - Tree Destruction

![](EmptyingTree.png)

This chart shows the runtime of emptying each tree (repeatedly calling `removeMin()` until no values remain).
Again, `TreeSet` guarantees runtime of `O(nlogn)`.
`ArrayQueue` now must percolated down on removal of the root node (minimum), resulting in an expected runtime of `O(nlogn)`.
While the `ArrayQueue`, does display an `O(nlogn)` trend, `TreeSet` seems to outperform `ArrayQueue` and is trending somewhere between `O(n)` and `O(nlogn)`.
My hunch on why the structures are not displaying the same performance is, my implementation of `percolateDown` is recursive and thus requires a lot of memeory overhead and additional calls, which the `TreeSet` is not making.
A follow-up experiment would be to change the recursive implementation to an iterative one and see if the runtimes between the structures are closer.
