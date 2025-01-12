import os
from datetime import datetime
from pathlib import Path
import re
from typing import List, Tuple, Optional, Generator

from pydantic import BaseModel


def run_traceroute(host: str) -> None:
    now: str = datetime.now().strftime("%Y-%m-%d_%H:%M:%S")
    fname = f"{host}_{now}.txt"
    os.system(f"traceroute {host} >> {fname}")


class HopResult(BaseModel, validate_assignment=True):
    number: int
    name: str
    ip: str
    one: Optional[float] = None
    two: Optional[float] = None
    three: Optional[float] = None

    def add_measurement(self, val: Optional[float]):
        if self.one is None:
            self.one = val
        elif self.two is None:
            self.two = val
        elif self.three is None:
            self.three = val
        else:
            raise RuntimeError("All possible measurements have been populated.")


def create_hop_result(line: Generator[str, None, None], hop_number: int) -> HopResult:
    hop_location = None
    ip = None
    measures = []
    try:
        while True:
            elem = next(line)
            if elem == "*":
                measures.append(None)
                continue
            else:
                if hop_location:
                    measures.append(elem)
                else:
                    hop_location = elem
                    ip = next(line).strip(")").strip("(")
                    measures.append(next(line))
                next(line)  # skip unit
                continue
    except StopIteration:
        result = HopResult(
            number=hop_number,
            name=hop_location,
            ip=ip
        )
        for measure in measures:
            result.add_measurement(measure)
        return result


def read_traceroute_results(file_name: Path) -> List[HopResult]:
    hop_results: List[HopResult] = []
    with open(file_name, "r") as f:
        for line in f.readlines():
            if line.endswith("* * *\n"):
                continue # skip lines with no measurements
            split_line = (elem for elem in re.split("\s+", line.strip()))
            hop_number = next(split_line)
            # Add to prior hop result when there is a line continuation for the same hop
            if not hop_number.isdigit():
                next(split_line) # skip IP
                hop_results[-1].add_measurement(float(next(split_line)))
                continue
            else:
                hop_results.append(create_hop_result(split_line, hop_number))
    return hop_results


def main() -> None:
    run_traceroute("www.admin.ch")


if __name__ == '__main__':
    main()
