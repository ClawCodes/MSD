# Question 1

# Question 2
### Part A
The below graph plots the average hop times of `traceroute` packets when tracing the path to www.admin.ch (the swiss governments landing page).
In some cases 

![](traceroute_analysis.png)

### Part B

The first possible cause for this delay would be an increase in traffic at the time in which the packet with the high delay time was sent.
If there was a sudden increase in traffic at this time, then the slow packet could experience a higher queuing delay time than the other two packets.
The second possible cause for this delay would be also be due to in increase in traffic, but instead of seeing a spike in queuing time, the slow packet would have a spike in propagation delay.
During this time of increased traffic the slow packet could be sent to a router which is further away than the router where the other two packets were sent as a method of reducing congestion.
An increase in travel distance will increase the propagation delay.

### Part C
The average queuing delay time can be derived using the formula 

$$ delay_{total} = delay_{propagation} + delay_{transmission} + delay_{processing} + delay_{queuing} $$

We can consider transmission, propagation, and processing delays are constant, thus the formula reduces to

$$ delay_{total} = c + delay_{queuing} $$

Since we can assume the minimum RTT value in our ping data, 18.388, corresponds to $ delay_{queuing} = 0 $, we can then say $ 18.388 = c$.
The average queuing delay can then be solved with 

$$  (\sum\limits_{i=0}^{N} delay_{total_{i}} - 18.388 )/ N $$

In the case of `ping.txt` this results in an average delay time of $ 5.95 ms $.
