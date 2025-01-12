from packetdelaymeasurement.main import read_traceroute_results
from pathlib import Path

ROOT = Path(__file__).parents[1]


def test_read_traceroute_results():
    # Arrange
    test_route = "test_route.txt"

    # Act
    actual = read_traceroute_results(ROOT.joinpath(test_route))
    # TODO: Both hop 6 and 8 have different locations - decide if you want to capture the separate IP addresses/names
    assert len(actual) == 5
    first = actual[0]
    assert first.number == 1
    assert first.name == "192.168.86.1"
    assert first.ip == "192.168.86.1"
    assert first.one == 3.394
    assert first.two == 3.237
    assert first.three == 2.556
    second = actual[1]
    assert second.number == 5
    assert second.name == "23-255-225-42.googlefiber.net"
    assert second.ip == "23.255.225.42"
    assert second.one == 21.158
    assert second.two == 17.114
    assert second.three == 17.771
    third = actual[2]
    assert third.number == 6
    assert third.name == "216-21-171-157.mci.googlefiber.net"
    assert third.ip == "216.21.171.157"
    assert third.one == 22.183
    assert third.two == 17.282
    assert third.three == 22.492
    fourth = actual[3]
    assert fourth.number == 7
    assert fourth.name == "108.170.248.45"
    assert fourth.ip == "108.170.248.45"
    assert fourth.one == 26.030
    assert fourth.two is None
    assert fourth.three is None
    fifth = actual[4]
    assert fifth.number == 8
    assert fifth.name == "lax17s55-in-f4.1e100.net"
    assert fifth.ip == "142.251.40.36"
    assert fifth.one == 30.637
    assert fifth.two == 22.408
    assert fifth.three == 16.315
