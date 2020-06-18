package matrices;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
// @formatter:off
public class Rotation extends Transformation {
    public static Rotation xAxis(double angle) {
        double[][] mat = {
                {1,     0,          0,       0},
                {0, cos(angle), -sin(angle), 0},
                {0, sin(angle),  cos(angle), 0},
                {0,     0,          0,       1}};

        return new Rotation(MatrixUtils.createRealMatrix(mat));
    }

    public static Rotation yAxis(double angle) {
        double[][] mat = {
                {cos(angle),   0,    sin(angle),   0},
                {   0,         1,        0,        0},
                {-sin(angle),  0,    cos(angle),   0},
                {   0,         0,        0,        1}};

        return new Rotation(MatrixUtils.createRealMatrix(mat));
    }

    public static Rotation zAxis(double angle) {
        double[][] mat = {
                {cos(angle), -sin(angle),  0,   0},
                {sin(angle),  cos(angle),  0,   0},
                {   0,            0,       1,   0},
                {   0,            0,       0,   1}};

        return new Rotation(MatrixUtils.createRealMatrix(mat));
    }

    private Rotation(RealMatrix matrix) {
        super(matrix);
    }
}
// @formatter:on
