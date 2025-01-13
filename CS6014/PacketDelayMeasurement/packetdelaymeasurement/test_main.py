from packetdelaymeasurement.main import read_traceroute_results
from pathlib import Path

ROOT = Path(__file__).parents[1]


def test_read_traceroute_results():
    # Arrange
    test_route = "test_route.txt"

    # Act
    actual = read_traceroute_results(ROOT.joinpath(test_route))
    assert len(actual) == 5
    
    first = actual[0]
    assert first.number == 1
    assert len(first.measures) == 3
    assert first.measures[0].name == "192.168.86.1"
    assert first.measures[0].ip == "192.168.86.1"
    assert first.measures[0].time == 3.394
    assert first.measures[1].name == "192.168.86.1"
    assert first.measures[1].ip == "192.168.86.1"
    assert first.measures[1].time == 3.237
    assert first.measures[2].name == "192.168.86.1"
    assert first.measures[2].ip == "192.168.86.1"
    assert first.measures[2].time == 2.556

    second = actual[1]
    assert second.number == 5
    assert len(second.measures) == 3
    assert second.measures[0].name == "23-255-225-42.googlefiber.net"
    assert second.measures[0].ip == "23.255.225.42"
    assert second.measures[0].time == 21.158
    assert second.measures[1].name == "23-255-225-42.googlefiber.net"
    assert second.measures[1].ip == "23.255.225.42"
    assert second.measures[1].time == 17.114
    assert second.measures[2].name == "23-255-225-42.googlefiber.net"
    assert second.measures[2].ip == "23.255.225.42"
    assert second.measures[2].time == 17.771

    second = actual[2]
    assert second.number == 6
    assert len(second.measures) == 3
    assert second.measures[0].name == "216-21-171-157.mci.googlefiber.net"
    assert second.measures[0].ip == "216.21.171.157"
    assert second.measures[0].time == 22.183
    assert second.measures[1].name == "216-21-171-123.mci.googlefiber.net"
    assert second.measures[1].ip == "216.21.171.123"
    assert second.measures[1].time == 17.282
    assert second.measures[2].name == "216-21-171-157.mci.googlefiber.net"
    assert second.measures[2].ip == "216.21.171.157"
    assert second.measures[2].time == 22.492

    second = actual[3]
    assert second.number == 7
    assert len(second.measures) == 1
    assert second.measures[0].name == "108.170.248.45"
    assert second.measures[0].ip == "108.170.248.45"
    assert second.measures[0].time == 26.030

    second = actual[4]
    assert second.number == 8
    assert len(second.measures) == 3
    assert second.measures[0].name == "lax17s55-in-f4.1e100.net"
    assert second.measures[0].ip == "142.251.40.36"
    assert second.measures[0].time == 30.637
    assert second.measures[1].name == "209.85.249.176"
    assert second.measures[1].ip == "209.85.249.176"
    assert second.measures[1].time == 22.408
    assert second.measures[2].name == "142.251.233.235"
    assert second.measures[2].ip == "142.251.233.235"
    assert second.measures[2].time == 16.315
