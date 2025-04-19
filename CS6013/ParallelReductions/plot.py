from typing import Dict

import matplotlib.pyplot as plt
import pandas as pd
from pathlib import Path

ROOT = Path(__file__).parent

def plot_strong(filter_type: str) -> None:
    df_strong = pd.read_csv(ROOT / "strong_scaling.csv")
    fig, axes = plt.subplots(1, 3, figsize=(15, 5))

    conversions: Dict[str, str] = {"l": "Long", "i": "Integer", "f": "float"}
    df_strong["type"] = df_strong["type"].apply(lambda val: conversions.get(val))

    df_strong = df_strong[df_strong['type'] == filter_type]

    for func_idx, func_name in enumerate(["std_thread_sum", "omp1_sum", "omp_builtin_sum"]):
        for idx, size in enumerate(df_strong["input_size"].unique()):
            tmp_df = df_strong[df_strong["input_size"] == size]
            axes[func_idx].scatter(tmp_df["num_threads"], tmp_df[func_name], label=str(size))
        axes[func_idx].set_xlabel("Num threads")
        axes[func_idx].set_ylabel("Exec time (µs)")
        axes[func_idx].set_title(f"Func: {func_name}")
        axes[func_idx].legend()
    fig.suptitle(f"Strong Scaling - {filter_type}")
    fig.tight_layout()
    plt.savefig(f"strong_scaling_{filter_type}.png")

def plot_weak() -> None:
    df_weak = pd.read_csv(ROOT / "weak_scaling.csv")
    fig, axes = plt.subplots(1, 3, figsize=(15, 5))

    for idx, func_name in enumerate(["std_thread_sum", "omp1_sum", "omp_builtin_sum"]):
        axes[idx].scatter(df_weak["num_threads"], df_weak[func_name])
        axes[idx].set_xlabel("Num threads")
        axes[idx].set_ylabel("Exec time (µs)")
        axes[idx].set_title(f"{func_name}")
    fig.suptitle("Weak Scaling")
    fig.tight_layout()
    plt.savefig("weak_scaling.png")


def main():
    # plot_strong("Integer")
    plot_strong("float")
    # plot_strong("Long")
    # plot_weak()


if __name__ == '__main__':
    main()