from packetdelaymeasurement.main import read_traceroute_results
from pathlib import Path

ROOT = Path(__file__).parents[1]


def test_read_traceroute_results():
    # Arrange
    test_route = "test_route.txt"

    # Act
    actual = read_traceroute_results(ROOT.joinpath(test_route))

    assert len(actual) == 2
