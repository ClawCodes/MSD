//
//  main.cpp
//  PoliStructs
//
//  Created by Christopher Lawton on 8/27/24.
//

#include <iostream>
#include <string>
#include <vector>
#include <cassert>

// Define political party consts for consistent value references
const std::string javaCan = "Javacan";
const std::string cPluser = "cPluser";

// Define employment type consts for consistent value references
const std::string state = "state";
const std::string federal = "federal";

// Define politician struct

struct Politician {
    std::string name;
    std::string party;
    std::string employmentType;
};

/// <#Description#> Get all politicians that have the party Javacna
/// - Parameter politicians: vector of politicians with populated party
std::vector<Politician> javacans(std::vector<Politician> politicians){
    std::vector<Politician> javacanPoliticans;
    for (Politician politician : politicians){
        if (politician.party == javaCan)
            javacanPoliticans.push_back(politician);
    }
    return javacanPoliticans;
}

/// <#Description#> Get all politicians that have the party cPluser and are employed on the federal level
/// - Parameter politicians: vector of politicians with populated party and employmentType
std::vector<Politician> federalCplusers(std::vector<Politician> politicians){
    std::vector<Politician> fedCplusers;
    for (Politician politician : politicians){
        if (politician.party == cPluser && politician.employmentType == federal)
            fedCplusers.push_back(politician);
    }
    return fedCplusers;
}

int main(int argc, const char * argv[]) {
    // Construct and example politicians vecotr for processing
    std::vector<Politician> politicians = {
        {"John", javaCan, state},
        {"Mary", cPluser, state},
        {"Eric", javaCan, federal},
        {"jordan", javaCan, federal},
        {"Ben", cPluser, federal},
        {"Ryan", cPluser, federal},
        {"Sarah", cPluser, state},
        {"Nick", javaCan, state}
    };
    
    std::vector<Politician> javacanPoliticians = javacans(politicians);
    
    // Assertions for javacans function
    // ensure we are only getting four politicians back
    assert(javacanPoliticians.size() == 4);
    // ensrue the politicians john, Eric, Jordan, and Nick are returned and their values remain intact
    assert(javacanPoliticians[0].name == "John" && javacanPoliticians[0].party == javaCan && javacanPoliticians[0].employmentType == state);
    assert(javacanPoliticians[1].name == "Eric" && javacanPoliticians[1].party == javaCan && javacanPoliticians[1].employmentType == federal);
    assert(javacanPoliticians[2].name == "jordan" && javacanPoliticians[2].party == javaCan && javacanPoliticians[2].employmentType == federal);
    assert(javacanPoliticians[3].name == "Nick" && javacanPoliticians[3].party == javaCan && javacanPoliticians[3].employmentType == state);
    
    // Assertions for federalCplusers function
    std::vector<Politician> fedCplusers = federalCplusers(politicians);
    
    // ensure we are only getting two politicians back
    assert(fedCplusers.size() == 2);
    // ensrue the politicians Ben and Ryan are returned and their values remain intact
    assert(fedCplusers[0].name == "Ben" && fedCplusers[0].party == cPluser && fedCplusers[0].employmentType == federal);
    assert(fedCplusers[1].name == "Ryan" && fedCplusers[1].party == cPluser && fedCplusers[1].employmentType == federal);
    
    
    return 0;
}
