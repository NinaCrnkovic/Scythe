package hr.algebra.scythe.model;

import hr.algebra.scythe.util.Constants;
import javafx.scene.image.Image;
import java.util.Objects;

public class Tile {

    public enum Type {
        VILLAGE(Constants.VILLAGE_IMAGE_PATH),
        FOREST(Constants.FOREST_IMAGE_PATH),
        MOUNTAIN(Constants.MOUNTAIN_IMAGE_PATH);

        private final String imagePath;
        private Image image;

        Type(String imagePath) {
            this.imagePath = imagePath;
            this.image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        }

        public Image getImage() {
            return image;
        }
    }

    private final Type type;

    public Tile(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public Image getImage() {
        return type.getImage();
    }
}

