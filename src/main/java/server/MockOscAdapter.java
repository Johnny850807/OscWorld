package server;

import world.Vector3;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class MockOscAdapter implements OscAdapter {
    @Override
    public void updateTrack(List<Vector3> vectors) {
        //System.out.println("=== Update Track (Start) ===");
        //vectors.forEach(System.out::println);
        //System.out.println("=== Update Track (End) ===");
    }
}
