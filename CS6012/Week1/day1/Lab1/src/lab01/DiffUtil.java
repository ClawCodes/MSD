package lab01;

public class DiffUtil {
    /**
     * @param arr -- input array of integers
     * @return The smallest difference (absolute value of subtraction) among every
     *         pair of integers in the input array. If the array contains less
     *         than two items, returns -1.
     */
    public static int findSmallestDiff(int[] arr) {
        if (arr.length < 2) {
            return -1;
        }

        int diff = Math.abs(arr[0] - arr[1]);

        for (int i = 0; i < arr.length -1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                int tmp_diff = Math.abs(arr[i] - arr[j]);

                if (tmp_diff < diff)
                    diff = tmp_diff;
            }
        }

        return diff;    }
}
