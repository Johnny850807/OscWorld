package utils;

import java.util.Random;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ArrayUtils {

    public static boolean contains(int[] array, int num) {
        for (int value : array) {
            if (value == num) {
                return true;
            }
        }
        return false;
    }

    public static int[] shuffle(int[] array) {
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            int r = random.nextInt(array.length);
            int temp = array[i];
            array[i] = array[r];
            array[r] = temp;
        }
        return array;
    }
}
