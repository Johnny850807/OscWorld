package matrices;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeHost;
import org.apache.commons.math3.analysis.function.Inverse;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import world.Vector3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Transformation {
    protected RealMatrix matrix;

    public Transformation(RealMatrix matrix) {
        this.matrix = matrix;
    }

    public List<Vector3> transform(List<Vector3> vectors) {
        RealMatrix m = MatrixUtils.createRealMatrix(4, vectors.size());

        // copy vectors to a matrix
        for (int col = 0; col < m.getColumnDimension(); col++) {
            m.setEntry(0, col, vectors.get(col).x);
        }
        for (int col = 0; col < m.getColumnDimension(); col++) {
            m.setEntry(1, col, vectors.get(col).y);
        }
        for (int col = 0; col < m.getColumnDimension(); col++) {
            m.setEntry(2, col, vectors.get(col).z);
        }
        for (int col = 0; col < m.getColumnDimension(); col++) {
            m.setEntry(3, col, 1);
        }

        // linear transformation
        RealMatrix resultMat = this.matrix.multiply(m);
        List<Vector3> newVectors = new ArrayList<>(vectors.size());
        for (int col = 0; col < resultMat.getColumnDimension(); col++) {
            double[] column = resultMat.getColumn(col);
            newVectors.add(new Vector3(column[0], column[1], column[2]));
        }

        return newVectors;
    }

    public Transformation compose(Transformation transformation) {
        // composite linear transformation
        return new Transformation(matrix.multiply(transformation.getMatrix()));
    }

    public Transformation inverse() {
        return new Transformation(MatrixUtils.inverse(matrix));
    }

    public RealMatrix getMatrix() {
        return matrix;
    }

}
