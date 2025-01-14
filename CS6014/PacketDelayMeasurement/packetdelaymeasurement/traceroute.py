import csv
import os
from datetime import datetime
from pathlib import Path
import re
from typing import List, Optional, Generator

from pydantic import BaseModel

ROOT = Path(__file__).parents[1]

def run_traceroute(host: str) -> Path:
    now: str = datetime.now().strftime("%Y-%m-%d_%H:%M:%S")
    fname = ROOT / f"{host}_{now}.txt"
    os.system(f"traceroute {host} >> {fname}")
    return fname


class HopRecord(BaseModel):
    name: str
    ip: str
    time: float


class HopResult(BaseModel, validate_assignment=True):
    number: int
    measures: List[HopRecord]

    def add_record(self, record: HopRecord) -> None:
        self.measures.append(record)


def sanitize_ip(ip: str) -> str:
    return ip.strip(")").strip("(")


def create_hop_records(line: Generator[str, None, None], location: Optional[str] = None) -> List[HopRecord]:
    hop_location = location
    ip = sanitize_ip(next(line)) if location else None
    records = []
    try:
        while True:
            elem = next(line)
            if elem == "*":
                continue
            elif hop_location:  # location determined, only measures follow
                records.append(HopRecord(
                    name=hop_location,
                    ip=ip,
                    time=float(elem)
                ))
                next(line)  # skip unit
            else:
                hop_location = elem
                ip = sanitize_ip(next(line))
            continue
    except StopIteration:
        return records


def read_traceroute_results(file_name: Path) -> List[HopResult]:
    hop_results: List[HopResult] = []
    with open(file_name, "r") as f:
        for line in f.readlines():
            if line.endswith("* * *\n"):
                continue  # skip lines with no measurements
            split_line = (elem for elem in re.split("\s+", line.strip()))
            hop_number = next(split_line)
            # Add to prior hop result when there is a line continuation for the same hop
            if not hop_number.isdigit():
                hop_record = create_hop_records(split_line, hop_number)  # hop_number in this case is the location
                if len(hop_record) > 1:
                    raise RuntimeError("Unexpected number of measurements for a hop line continuation.")
                hop_results[-1].add_record(hop_record[0])
                continue
            else:
                hop_records = create_hop_records(split_line)
                hop_results.append(HopResult(number=hop_number, measures=hop_records))
    return hop_results

def write_traceroute_averages(hop_results: List[HopResult], fout: str) -> Path:
    outfile = ROOT / fout
    with open(outfile, "w") as f:
        writer = csv.writer(f)
        writer.writerow(["hop", "location", "average_ms"])
        for result in hop_results:
            ips = set()
            measures = []
            for record in result.measures:
                ips.add(record.ip)
                measures.append(record.time)
            writer.writerow([result.number, " | ".join(ips), sum(measures) / len(measures)])
    return outfile


def main() -> None:
    # file_name = run_traceroute("www.admin.ch")
    file_name = Path("/Users/christopherlawton/msd/MSD/CS6014/PacketDelayMeasurement/www.admin.ch_2025-01-13_15:17:32.txt")
    results: List[HopResult] = read_traceroute_results(file_name)
    outfile = file_name.stem + "_averages.csv"
    outfile = write_traceroute_averages(results, file_name.stem + "_averages.csv")


if __name__ == '__main__':
    main()
