package matrices;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Translation extends Transformation {
    public static Translation alongX(double offsetX) {
        return new Translation(offsetX, 0, 0);
    }

    public static Translation alongY(double offsetY) {
        return new Translation(0, offsetY, 0);
    }

    public static Translation alongZ(double offsetZ) {
        return new Translation(0, 0, offsetZ);
    }

    public Translation(double offsetX, double offsetY, double offsetZ) {
        super(translationMatrix(offsetX, offsetY, offsetZ));
    }

    private static RealMatrix translationMatrix(double offsetX, double offsetY, double offsetZ) {
        RealMatrix mat = MatrixUtils.createRealIdentityMatrix(4);
        /*
          1   0   0   offsetX
          0   1   0   offsetY
          0   0   1   offsetZ
          0   0   0      1
         */
        mat.setEntry(0, 3, offsetX);
        mat.setEntry(1, 3, offsetY);
        mat.setEntry(2, 3, offsetZ);
        return mat;
    }
}
