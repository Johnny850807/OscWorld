package matrices;

import org.junit.jupiter.api.Test;
import world.Vector3;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TransformationTest {
    List<Vector3> vectors;
    List<Vector3> newVectors;

    @Test
    void testTranslation() {
        givenVectors(new Vector3(1, 0, 0),
                new Vector3(2, 0, 0));

        Transformation t = new Translation(1, 1, 2);
        Transformation transform = t.compose(t.inverse())
                                .compose(new Translation(1, 1, -1));


        newVectors = transform.transform(vectors);
        assertVector(newVectors.get(0), 2, 1, -1);
        assertVector(newVectors.get(1), 3, 1, -1);
    }

    @Test
    void testRotation() {
        givenVectors(new Vector3(1, 0, 0));

        Transformation rotation = Rotation.zAxis(90);
        newVectors = rotation.transform(vectors);

        assertNotEquals(vectors.get(0), newVectors.get(0));

        Transformation inverseRotation = Rotation.zAxis(-90);
        newVectors = inverseRotation.transform(newVectors);

        assertEquals(vectors.get(0), newVectors.get(0));
    }

    private void givenVectors(Vector3... vectors) {
        this.vectors = Arrays.asList(vectors);
    }

    private void assertVector(Vector3 vec, double x, double y, double z) {
        assertEquals(x, vec.x);
        assertEquals(y, vec.y);
        assertEquals(z, vec.z);
    }
}