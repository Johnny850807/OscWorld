package utils;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class MinMax {
    public static int minmax(int val, int minX, int maxX, int minScale, int maxScale) {
        return (int) (minScale + (double) (val - minScale)
                / (maxScale - minScale) * (maxX - minX));
    }
}
