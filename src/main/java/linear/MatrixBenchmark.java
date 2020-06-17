package linear;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.*;
import java.util.Random;

/**
 * Simulate massive matrices multiplication computation
 * @author - johnny850807@gmail.com (Waterball)
 */
public class MatrixBenchmark {
    private static Random random = new Random();

    public static void main(String[] args) throws IOException {
        File communicationOverhead = new File("simulate-communication-overhead");
        communicationOverhead.deleteOnExit();
        Writer communicationOverheadWriter = new BufferedWriter(new FileWriter(communicationOverhead));

        MatrixUtils.createRealMatrix(3, 3);

        RealMatrix a = MatrixUtils.createRealMatrix(4, 4);
        RealMatrix b = MatrixUtils.createRealMatrix(4, 100);

        randomizeMatrix(a);
        randomizeMatrix(b);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            communicationOverheadWriter.write(a.multiply(b) + "\n");
        }
        long endTime = System.currentTimeMillis();


        System.out.println("Time elapsed: " + (endTime - startTime) + " ms.");
        System.out.println("Avg Time elapsed: " + (endTime - startTime) / 10000.0 + " ms.");
    }

    private static void randomizeMatrix(RealMatrix matrix) {
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                matrix.setEntry(i, j, random.nextDouble());
            }
        }
    }
}
