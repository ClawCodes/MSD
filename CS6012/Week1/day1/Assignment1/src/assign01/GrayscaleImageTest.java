package assign01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GrayscaleImageTest {

    private GrayscaleImage smallSquare;
    private GrayscaleImage smallWide;
    private GrayscaleImage smallTall;

    @BeforeEach
    void setUp() {
        smallSquare = new GrayscaleImage(new double[][]{{1, 2}, {3, 4}});
        smallWide = new GrayscaleImage(new double[][]{{1, 2, 3}, {4, 5, 6}});
        smallTall = new GrayscaleImage(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}});
    }

    @Test
    void testGetPixel() {
        assertEquals(smallSquare.getPixel(0, 0), 1);
        assertEquals(smallSquare.getPixel(1, 0), 2);
        assertEquals(smallSquare.getPixel(0, 1), 3);
        assertEquals(smallSquare.getPixel(1, 1), 4);

        // corners of SmallWide
        assertEquals(smallWide.getPixel(0, 0), 1);
        assertEquals(smallWide.getPixel(2, 0), 3);
        assertEquals(smallWide.getPixel(0, 1), 4);
        assertEquals(smallWide.getPixel(2, 1), 6);

        // Corners of smallTall
        assertEquals(smallTall.getPixel(0, 0), 1);
        assertEquals(smallTall.getPixel(2, 0), 3);
        assertEquals(smallTall.getPixel(0, 3), 10);
        assertEquals(smallTall.getPixel(2, 3), 12);
    }

    @Test
    void testEquals() {
        assertEquals(smallSquare, smallSquare);
        var equivalent = new GrayscaleImage(new double[][]{{1, 2}, {3, 4}});
        assertEquals(smallSquare, equivalent);

        assertEquals(smallWide, smallWide);
        var equivalentSmallWide = new GrayscaleImage(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertEquals(smallWide, equivalentSmallWide);

        assertEquals(smallTall, smallTall);
        var equivalentSmallTall = new GrayscaleImage(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}});
        assertEquals(smallTall, equivalentSmallTall);
    }

    @Test
    void equalsWithDifferentTypes() {
        assertFalse(smallWide.equals(1));
        assertFalse(smallWide.equals("test"));
        assertFalse(smallWide.equals(null));
        assertFalse(smallWide.equals(new Object()));
    }

    @Test
    void equalsWithDifferentDimensions() {
        assertFalse(smallSquare.equals(smallWide));
        assertFalse(smallSquare.equals(smallTall));
        assertFalse(smallWide.equals(smallTall));
        assertFalse(smallTall.equals(smallWide));
    }

    @Test
    void equalsWithInequivalentPixel() {
        // TODO: change to parameterized test
        for (GrayscaleImage img : new GrayscaleImage[]{smallWide, smallTall, smallSquare}) {
            int[][] imageCorners = new int[][]{
                    {0, 0}, // First row, first column
                    {0, img.width() - 1}, // First row, last column
                    {img.height() - 1, 0}, // Last row, first column
                    {img.height() - 1, img.width() - 1} // Last row, last column
            };
            for (int[] pixel : imageCorners) {
                double[][] imgToCompare = img.getData();
                imgToCompare[pixel[0]][pixel[1]] = -100; // set in-equivalent value at specified pixel
                assertFalse(img.equals(imgToCompare));
            }
        }
    }


    @Test
    void averageBrightness() {
        assertEquals(smallSquare.averageBrightness(), 2.5, 2.5 * .001);
        var bigZero = new GrayscaleImage(new double[1000][1000]);
        assertEquals(bigZero.averageBrightness(), 0);
    }

    private static Stream<Arguments> imagesWithAvgBrightness() {
        return Stream.of(
                Arguments.of(new GrayscaleImage(new double[][]{{0.0}}), 0.0),
                Arguments.of(new GrayscaleImage(new double[][]{{1.0, 1.0}, {1.0, 1.0}}), 1.0),
                Arguments.of(new GrayscaleImage(new double[][]{{-1.0, 0.0}, {2.0, 1.0}}), 0.5),
                Arguments.of(new GrayscaleImage(new double[][]{{-1.0, -2.0}, {2.0, 1.0}}), 0.0)
        );
    }

    @ParameterizedTest
    @MethodSource("imagesWithAvgBrightness")
    void averageBrightnessFromArguments(GrayscaleImage image, double expected) {
        assertEquals(expected, image.averageBrightness(), 0.001);
    }

    @Test
    void normalized() {
        var smallNorm = smallSquare.normalized();
        assertEquals(smallNorm.averageBrightness(), 127, 127 * .001);
        var scale = 127 / 2.5;
        var expectedNorm = new GrayscaleImage(new double[][]{{scale, 2 * scale}, {3 * scale, 4 * scale}});
        for (var row = 0; row < 2; row++) {
            for (var col = 0; col < 2; col++) {
                assertEquals(smallNorm.getPixel(col, row), expectedNorm.getPixel(col, row),
                        expectedNorm.getPixel(col, row) * .001,
                        "pixel at row: " + row + " col: " + col + " incorrect");
            }
        }
    }

    @ParameterizedTest
    @MethodSource("imagesWithAvgBrightness")
    void normalizedFromExpectedBrightness(GrayscaleImage image, double expected) {
        var normalized = image.normalized();
        int actual = (expected == 0) ? 0 : 127;
        assertEquals(normalized.averageBrightness(), actual, 127 * 0.001);
    }

    @Test
    void mirrored() {
        var expected = new GrayscaleImage(new double[][]{{2, 1}, {4, 3}});
        assertEquals(smallSquare.mirrored(), expected);
    }

    private static Stream<Arguments> mirroredImages() {
        return Stream.of(
                Arguments.of(
                        // 1x1
                        new GrayscaleImage(new double[][]{{0.0}}),
                        new GrayscaleImage(new double[][]{{0.0}})
                ),
                Arguments.of(
                        // 2x1
                        new GrayscaleImage(new double[][]{{0.0}, {1.0}}),
                        new GrayscaleImage(new double[][]{{0.0}, {1.0}})
                ),
                Arguments.of(
                        // 1x2
                        new GrayscaleImage(new double[][]{{0.0, 1.0}}),
                        new GrayscaleImage(new double[][]{{1.0, 0.0}})
                ),
                Arguments.of(
                        // 3x5
                        new GrayscaleImage(new double[][]{{0.0, 1.0, 2.0, 3.0, 4.0}, {0.1, 1.1, 2.1, 3.1, 4.1}, {0.2, 1.2, 2.2, 3.2, 4.2}}),
                        new GrayscaleImage(new double[][]{{4.0, 3.0, 2.0, 1.0, 0.0}, {4.1, 3.1, 2.1, 1.1, 0.1}, {4.2, 3.2, 2.2, 1.2, 0.2}})
                )
        );
    }

    @ParameterizedTest
    @MethodSource("mirroredImages")
    void mirroredWithVaryingDimensions(GrayscaleImage image, GrayscaleImage expected) {
        assertEquals(image.mirrored(), expected);
    }

    @Test
    void cropped() {
        var cropped = smallSquare.cropped(1, 1, 1, 1);
        assertEquals(cropped, new GrayscaleImage(new double[][]{{4}}));
    }

    private static Stream<Arguments> croppedImages() {
        GrayscaleImage largeImage = new GrayscaleImage(new double[][]{{0.0, 1.0, 2.0, 3.0, 4.0}, {0.1, 1.1, 2.1, 3.1, 4.1}, {0.2, 1.2, 2.2, 3.2, 4.2}});
        return Stream.of(
                Arguments.of(
                        // 1x1
                        new GrayscaleImage(new double[][]{{0.0}}),
                        new GrayscaleImage(new double[][]{{0.0}}),
                        new int[]{0, 0, 1, 1}
                ),
                Arguments.of(
                        // 2x1
                        new GrayscaleImage(new double[][]{{0.0}, {1.0}}),
                        new GrayscaleImage(new double[][]{{1.0}}),
                        new int[]{1, 0, 1, 1}
                ),
                Arguments.of(
                        // 1x2
                        new GrayscaleImage(new double[][]{{0.0, 1.0}}),
                        new GrayscaleImage(new double[][]{{0.0}}),
                        new int[]{0, 0, 1, 1}
                ),
                Arguments.of(
                        // Crop top row
                        largeImage,
                        new GrayscaleImage(new double[][]{{0.0, 1.0, 2.0, 3.0, 4.0}}),
                        new int[]{0, 0, 5, 1}
                ),
                Arguments.of(
                        // Crop top left corner
                        largeImage,
                        new GrayscaleImage(new double[][]{{0.0, 1.0, 2.0}, {0.1, 1.1, 2.1}}),
                        new int[]{0, 0, 3, 2}
                ),
                Arguments.of(
                        // Crop top right corner
                        largeImage,
                        new GrayscaleImage(new double[][]{{2.0, 3.0, 4.0}, {2.1, 3.1, 4.1}}),
                        new int[]{0, 2, 3, 2}
                ),
                Arguments.of(
                        // Crop bottom left corner
                        largeImage,
                        new GrayscaleImage(new double[][]{{0.1, 1.1, 2.1}, {0.2, 1.2, 2.2}}),
                        new int[]{1, 0, 3, 2}
                ),
                Arguments.of(
                        // Crop bottom right corner
                        largeImage,
                        new GrayscaleImage(new double[][]{{2.1, 3.1, 4.1}, {2.2, 3.2, 4.2}}),
                        new int[]{1, 2, 3, 2}
                )
        );
    }

    @ParameterizedTest
    @MethodSource("croppedImages")
    void croppedWithVaryingDimensions(GrayscaleImage image, GrayscaleImage expected, int[] params) {
        var original = new GrayscaleImage(image.getData());
        var cropped = image.cropped(params[0], params[1], params[2], params[3]);
        assertEquals(cropped, expected);
        assertEquals(original, image); // Ensure image was not altered during crop
    }

    @Test
    void croppedThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            smallSquare.cropped(100, 1, 1, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            smallTall.cropped(0, 0, 100, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            smallWide.cropped(0, 100, 1, 1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            smallWide.cropped(0, 0, 1, 100);
        });
    }

    @Test
    void squarified() {
        // Width > height
        var squared = smallWide.squarified();
        var expected = new GrayscaleImage(new double[][]{{1, 2}, {4, 5}});
        assertEquals(squared, expected);

        // Width == height
        assertEquals(smallSquare.squarified(), smallSquare); // input is already a square

        // height > width
        assertEquals(smallTall.squarified(),
                smallTall = new GrayscaleImage(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}})
        );

    }

    @Test
    void testGetPixelThrowsOnNegativeX() {
        assertThrows(IllegalArgumentException.class, () -> {
            smallSquare.getPixel(-1, 0);
        });
    }

    @Test
    void testGetPixelThrowsOnIndexOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            smallSquare.getPixel(2, 2);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            smallSquare.getPixel(10, 15);
        });
    }
}
