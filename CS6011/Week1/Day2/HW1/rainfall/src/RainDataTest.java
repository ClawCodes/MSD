import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RainDataTest {

    static Stream<Arguments> constructorStream() {
        return Stream.of(
                Arguments.of("standard_format.txt", 6, "TestCity"),
                Arguments.of("no_data.txt", 0, "TestCity")
        );
    }

    @ParameterizedTest
    @MethodSource("constructorStream")
    public void testRainDataConstructor(String fileName, int expectedSize, String expectedCity) throws FileNotFoundException {
        RainData standardData = new RainData("test_data/" + fileName);
        Assertions.assertEquals(expectedSize, standardData.getRainData().size());
        Assertions.assertEquals(expectedCity, standardData.getCityName());
    }

    @Test
    public void testRainDataConstructorNoCity() throws FileNotFoundException {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                    RainData _ = new RainData("test_data/no_city.txt");
                }
        );

        String expectedMessage = "The provided city name January 1996\t8.26 is not valid.";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Denver", "Salt Lake City", "New york, New york", ""})
    public void testRainDataSetCityNamePasses(String city) throws FileNotFoundException {
        RainData data = new RainData("test_data/standard_format.txt");
        data.setCityName(city);
        Assertions.assertEquals(city, data.getCityName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jan 1992 1.2", "Feb \t2000\t\t 1.2333", "December 1993  3.30 "})
    public void testRainDataSetCityNameThrows(String city) throws FileNotFoundException {
        RainData data = new RainData("test_data/standard_format.txt");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            data.setCityName(city);
        });
        String expectedMessage = "The provided city name " + city + " is not valid.";
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testRainDataCalculateAverage() throws FileNotFoundException {
        RainData testData = new RainData("test_data/standard_format.txt");

        Map<String, Double> expected = Map.of(
                "January", 8.26,
                "February", 2.56,
                "March", 6.42,
                "April", 2.91,
                "May", 2.12
        );

        Map<String, Double> actual = testData.calculateAverages();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testRainDataCalculateAverageNoData() throws FileNotFoundException {
        RainData testData = new RainData("test_data/no_data.txt");
        Map<String, Double> expected = Map.of();
        Map<String, Double> actual = testData.calculateAverages();
        Assertions.assertEquals(expected, actual);
    }
}