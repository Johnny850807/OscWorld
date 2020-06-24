package world;

import matrices.Transformation;
import matrices.Translation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ViewportProcessing {
    private Logger logger = LogManager.getLogger();
    private Game game;
    private Vector3 latestPoint = new Vector3(-1, -1, -1);
    private double latestAngle = -1;

    public ViewportProcessing(Game game) {
        this.game = game;
    }

    public void process() {
        Player p = game.getWorld().getPlayer();

        List<Vector3> viewSpriteLocations = viewTransform(p.getPoint(), p.getAngle());
        updateSpritesWithNewLocations(viewSpriteLocations);

        if (!latestPoint.equals(p.getPoint()) || latestAngle != p.getAngle()) {
            logSprites();
        }

        latestPoint = p.getPoint();
        latestAngle = p.getAngle();
    }

    private List<Vector3> viewTransform(Vector3 newPlayerPoint, double newAngle) {
        Transformation transformation =
                new Translation(-newPlayerPoint.x, -newPlayerPoint.y, -newPlayerPoint.z);
        //.compose(Rotation.zAxis(newAngle))
        //.compose(new Translation(newPlayerPoint.x, newPlayerPoint.y, newPlayerPoint.z));

        return transformation.transform(
                game.getSprites().stream()
                        .map(Sprite::getPoint).collect(Collectors.toList()));
    }

    private void updateSpritesWithNewLocations(List<Vector3> viewSpriteLocations) {
        IntStream.range(0, viewSpriteLocations.size())
                .forEach(i -> game.getSprites().get(i).setPoint(viewSpriteLocations.get(i)));
    }

    private void logSprites() {
        logger.trace(game.getSprites().stream()
                .map(Object::toString).collect(Collectors.joining("\n")));
    }


}
