import os
from datetime import datetime
from pathlib import Path
import re
from typing import List

from pydantic import BaseModel


def run_traceroute(host: str) -> None:
    now: str = datetime.now().strftime("%Y-%m-%d_%H:%M:%S")
    fname = f"{host}_{now}.txt"
    os.system(f"traceroute {host} >> {fname}")


class HopResult(BaseModel):
    number: int
    name: str
    IP: str
    one: float
    two: float
    three: float

    def add_measurement(self, val: float):
        if self.one is None:
            self.one = val
        elif self.two is None:
            self.two = val
        elif self.three is None:
            self.three = val
        else:
            raise RuntimeError("All possible measurements have been populated.")


# TODO: start here - continue parsing traceroute result. Need to handle lines like "* 108.170.248.45 (108.170.248.45)  26.030 ms *"
def read_traceroute_results(file_name: Path) -> List[HopResult]:
    hop_results: List[HopResult] = []
    with open(file_name, "r") as f:
        for line in f.readlines():
            split_line = re.split("\s+", line.strip())
            if split_line[1] == "*":
                continue  # skip lines which contain no measurements
            if not line[0].isdigit():
                # Add to prior hop result when there is a line continuation for the same hop
                hop_results[-1].add_measurement(float(split_line[1]))
            else:
                hop_results.append(HopResult(
                    number=int(split_line[0]),
                    name=split_line[1],
                    IP=split_line[2].strip(")").strip("("),
                    one=float(split_line[3]),
                    two=float(split_line[5]),
                    three=float(split_line[7]),
                ))
    return hop_results


def main() -> None:
    run_traceroute("www.admin.ch")


if __name__ == '__main__':
    main()
