package hr.algebra.scythe.model;



public class Tile {

    enum TileType {
        PLAIN, // Ravnice
        FOREST, // Šuma
        VILLAGE, // Selo
        HILL; // Brda
    }
    private TileType tileType;
    private Unit unit;
    private Resource resource;

    public Tile() {
    }

    public Tile(TileType tileType, Unit unit, Resource resource) {
        this.tileType = tileType;
        this.unit = unit;
        this.resource = resource;
    }
}
