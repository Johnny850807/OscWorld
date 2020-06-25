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
public class ViewportTransformer {
    private Logger logger = LogManager.getLogger();
    private Vector3 latestPoint = new Vector3(-1, -1, -1);
    private double latestAngle = -1;

    public List<Sprite> transform(List<Sprite> sprites, Player player) {
        List<Vector3> viewPoints = viewTransform(sprites, player.getPoint(), player.getAngle());

        if (!latestPoint.equals(player.getPoint()) || latestAngle != player.getAngle()) {
            logSprites(sprites);
        }

        latestPoint = player.getPoint();
        latestAngle = player.getAngle();
        return createViewSpritesFromPoints(sprites, viewPoints);
    }

    private List<Vector3> viewTransform(List<Sprite> sprites, Vector3 newPlayerPoint, double newAngle) {
        Transformation transformation =
                new Translation(-newPlayerPoint.x, -newPlayerPoint.y, -newPlayerPoint.z);
        //.compose(Rotation.zAxis(newAngle))
        //.compose(new Translation(newPlayerPoint.x, newPlayerPoint.y, newPlayerPoint.z));

        List<Vector3> points = sprites.stream().map(Sprite::getPoint).collect(Collectors.toList());
        return transformation.transform(points);
    }

    private List<Sprite> createViewSpritesFromPoints(List<Sprite> sprites, List<Vector3> viewSpriteLocations) {
        return IntStream.range(0, sprites.size())
                .mapToObj(idx -> {
                    Sprite viewSprite = sprites.get(idx).clone();
                    viewSprite.setPoint(viewSpriteLocations.get(idx));
                    return viewSprite;
                }).collect(Collectors.toList());
    }

    private void logSprites(List<Sprite> sprites) {
        logger.trace(sprites.stream()
                .map(Object::toString).collect(Collectors.joining("\n")));
    }


}
