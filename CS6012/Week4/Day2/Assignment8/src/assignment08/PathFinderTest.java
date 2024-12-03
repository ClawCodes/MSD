package assignment08;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class PathFinderTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "bigMaze",
            "classic",
            "demoMaze",
            "mediumMaze",
            "straight",
            "tinyMaze",
            "tinyOpen",
            "turn",
            "unsolvable"
    })
    void testMazes(String fileName) throws IOException {
        String outFile = fileName + "TestOutput.txt";
        PathFinder.solveMaze(fileName + ".txt", outFile);

        String expected = Files.readString(Paths.get(fileName + "Sol.txt"));
        String actual = Files.readString(Paths.get(outFile));

        assertEquals(expected, actual);
    }
}