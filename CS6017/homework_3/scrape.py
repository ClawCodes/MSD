from bs4 import BeautifulSoup
from bs4.element import Tag
import requests
from typing import Tuple, Optional
import pandas as pd
from functools import partial

def parse_submission(submission: Tuple[Tag, Tag]) -> Optional[Tuple[str, int, str, str, str]]:
    sub, subtext = submission
    rank = sub.find(class_="rank").get_text().rstrip(".") # Rank
    title_len = len(sub.find(class_="titleline").get_text()) # Title
    age = subtext.find(class_="age").find("a").get_text() # Perform on subtext
    try:
        points = subtext.find(class_="score").get_text() # points from subtext
    except AttributeError:
        return None
    num_comments = subtext.find_all("a")[-1].get_text() # number of comments from subtext

    return rank, title_len, age, points, num_comments


def get_hn_page_page(page: int = 0):
    return requests.get(f"https://news.ycombinator.com/?p={page}").text

def clean_age(age: str) -> float:
    value, unit, _ = age.split(" ")
    if unit.startswith("hour"):
        return float(value)
    if unit.startswith("day"):
        return float(value) * 24
    if unit.startswith("minute"):
        return float(value) / 60

def extract_first_as_int(val: str, split_on: str) -> int:
    split_val = val.split(split_on)
    if len(split_val) == 1:
        return 0
    return int(split_val[0])

def main():
    all_subs = []
    for page_num in list(range(1, 6)):
        news = BeautifulSoup(get_hn_page_page(page_num), "html.parser")
        submissions = [parse_submission(sub) for sub in
                       zip(news.find_all(class_="athing submission"), news.find_all(class_="subtext"))]
        all_subs.extend(submissions)

    all_subs = list(filter(lambda x: x is not None, all_subs)) # Remove Null records (i.e. posting is an Ad)
    df = pd.DataFrame.from_records(all_subs, columns=["rank", "title_length", "age", "points", "num_comments"])

    df["rank"] = df["rank"].str.rstrip(".").astype(int)
    df["age"] = df["age"].apply(clean_age)

    df["points"] = df["points"].apply(partial(extract_first_as_int, split_on=" "))
    df["num_comments"] = df["num_comments"].apply(partial(extract_first_as_int, split_on=u"\xa0"))

    df.to_csv("data/submissions.csv", index=False)


if __name__ == '__main__':
    main()
