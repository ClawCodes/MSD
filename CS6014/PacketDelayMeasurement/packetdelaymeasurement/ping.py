from pathlib import Path
import re
from typing import List

ROOT = Path(__file__).parents[1]

def calculate_avg_queuing_delay(times: List[float]):
    min_delay = min(times) # Assumes the min delay time has a 0 queuing delay
    queue_delays = [d - min_delay for d in times]

    return sum(queue_delays) / len(queue_delays)

def get_delay_times(fname: str)-> List[float]:
    delay_times = []
    with open(ROOT / fname, "r") as f:
        f.readline() # skip first line
        for line in f.readlines():
            if re.match(r"\d{1,2} bytes", line):
                delay_times.append(float(re.search(r"time=(.*) ms", line).group(1)))

    return delay_times


if __name__ == '__main__':
    delay_times = get_delay_times("ping.txt")
    print(f"{round(calculate_avg_queuing_delay(delay_times), 2)} ms")