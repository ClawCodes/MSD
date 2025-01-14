from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd

ROOT = Path(__file__).parents[1]

def merge_ip_addresses(ip_addresses: str):
    split_ip_addresses = ip_addresses.split(' | ')
    final = split_ip_addresses[0].split(".")
    for address in split_ip_addresses[1:]:
        split_address = address.split(".")
        for idx, part in enumerate(split_address):
            if final[idx].split("/")[0] != part:
                final[idx] = final[idx] + "/" + part

    return ".".join(final)

def plot(file1: Path, file2: Path, title: str, x_label: str, y_label: str, outfile: str) -> None:
    df1 = pd.read_csv(file1)
    df1["location"] = df1["location"].apply(lambda x: merge_ip_addresses(x))

    df2 = pd.read_csv(file2)
    df2["location"] = df2["location"].apply(lambda x: merge_ip_addresses(x))

    # Combine all IPs for consistent tick marks
    all_ips = sorted(set(df1['location'].unique()).union(df2['location'].unique()))

    # Map IPs to indices to help plot IPs categorically
    ip_to_index = {ip: idx for idx, ip in enumerate(all_ips)}
    df1['location_idx'] = df1['location'].map(ip_to_index)
    df2['location_idx'] = df2['location'].map(ip_to_index)

    plt.figure(figsize=(12, 6))
    plt.scatter(df1['location_idx'], df1['average_ms'], label='Trace 1', color='blue')
    plt.scatter(df2['location_idx'], df2['average_ms'], label='Trace 2', color='orange')

    plt.xticks(
        ticks=range(len(all_ips)),
        labels=all_ips,
        rotation=45,
        ha='right'  # Align text to the right for readability
    )

    plt.title(title)
    plt.xlabel(x_label)
    plt.ylabel(y_label)
    plt.legend()
    plt.tight_layout()
    plt.savefig(ROOT / f'{outfile}.png')


if __name__ == '__main__':

    file1 = ROOT / "www.admin.ch_2025-01-13_15:17:32_averages.csv"
    file2 = ROOT / "www.admin.ch_2025-01-13_20:05:23_averages.csv"

    plot(file1, file2, "Average hop time from localhost www.admin.ch", "Hop IP address(es)",
         "Average time from host to node (ms)", "traceroute_analysis")
