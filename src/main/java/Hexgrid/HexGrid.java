package Hexgrid;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.*;

import java.lang.reflect.Array;
import java.util.*;

public class HexGrid {
    private final float tileSize;
    private final static List<Vector3> directions = Arrays.asList(
            new Vector3(+1, 0, -1),
            new Vector3(+1, -1, 0),
            new Vector3(0, -1, +1),
            new Vector3(-1, 0, +1),
            new Vector3(-1, +1, 0),
            new Vector3(0, +1, -1)
    );
    private final static String[] allTileNames = {"Archery", "Barracks", "Castle", "FarmPlot", "Forest1", "Forest2",
            "Forest3", "House", "Lumbermill", "Market", "Mill", "Mine", "Mountain","Plain","Road1","Road2" ,"Road3",
            "Road4", "Road5","Road6","Road7","Road8","Road9","Road10","Road11","Road12","Road13","Rocks1", "Rocks2",
            "WallCorner", "WallGate", "WallHexCorner1", "WallHexCorner2", "WallStraight", "Watchtower", "Water1",
            "Water2", "Water3", "Water4", "Water5", "Watermill", "Well"};

    private final Map<Vector3, String> tileLocationType = new HashMap<>();
    private final Map<String, List<Vector3>> tileTypeLocation = new HashMap<>();

    public HexGrid(float tileSize) {
        this.tileSize = tileSize;
        startNewMap();
    }

    /**
     * Transforms Cube coordinates to Axial coordinates.
     * @param CubeCoordinates
     * @return
     */
    public Vector2 cubeToAxial(Vector3 CubeCoordinates) {
        float q =  CubeCoordinates.x;
        float r =  CubeCoordinates.y;
        return new Vector2(q, r);
    }

    /**
     * Transforms Axial coordinates to cube coordinates.
     * @param AxialCoordinates
     * @return
     */
    public Vector3 AxialtoCube(Vector2 AxialCoordinates) {
        float q =  AxialCoordinates.x;
        float r =  AxialCoordinates.y;
        float s = -q - r;
        return new Vector3(q, r, s);
    }

    /**
     * Converts Axial coordinates to a World coordinate.
     * @param AxialCoordinates
     * @return
     */
    public Vector3 hexToWorld(Vector2 AxialCoordinates) {
        float x = (float) (tileSize * sqrt(3) * AxialCoordinates.x + sqrt(3)/2 * AxialCoordinates.y);
        float z = tileSize *( 3f/2f * AxialCoordinates.y);
        return new Vector3(x, 0, z);
    }

    /**
     * Converts Click on the screen to an Axial coordinates.
     * @param worldCoordinates Coordinates where the Raycast hit the floor.
     * @return Axial Coordinates on the Hex Grid.
     */
    public Vector2 pixelToHex(Vector3 worldCoordinates) {
        float q = (float)(sqrt(3)/3 * worldCoordinates.x - 1f/3f * worldCoordinates.z)/ tileSize;
        float r = (2f/ 3f * worldCoordinates.z) / tileSize;
        Vector2 fracAxial = new Vector2(q,r);
        Vector2 wholeAxial = AxialRound(fracAxial);
        return new Vector2(wholeAxial.x, wholeAxial.y);
    }

    /**
     * Find all the Neighbours of a Tile location.
     * @param cubeCoords Tile location in Cube Coordinates.
     * @return List of Neighbours of a Tile.
     */
    public List<Vector3> allNeighbours(Vector3 cubeCoords) {
        List<Vector3> allNeighbours = new ArrayList<>();
        for (Vector3 direction : directions) {
            Vector3 dummyCoords = new Vector3(cubeCoords);
            Vector3 cubeNeighbour = new Vector3(dummyCoords.add(direction));
            allNeighbours.add(cubeNeighbour);
        }
        return allNeighbours;
    }

    /**
     * Helper function to help the pixelToHex coordinates.
     * @param fracAxial
     * @return
     */
    public Vector2 AxialRound(Vector2 fracAxial){
        Vector3 fracCube = AxialtoCube(fracAxial);
        float q = round(fracCube.x);
        float r = round(fracCube.y);
        float s = round(fracCube.z);
        float qDiff = abs(q- fracCube.x);
        float rDiff = abs(r - fracCube.y);
        float sDiff = abs(s-fracCube.z);
        if(qDiff > rDiff && qDiff > sDiff){
            q = -r-s;
        }
        else if(rDiff > sDiff){
            r = -q-s;
        }
        else{
            s = -q - r;
        }
        Vector3 wholeAxial = new Vector3(q,r,s);
        return cubeToAxial(wholeAxial);
    }

    /**
     * Check whether the Tile can be placed or not.
     * @param cubeCoordinates Coordinate to check.
     * @return If the tile can be placed at Cube Coordinate location.
     */
    public boolean isInAvailableTiles(Vector3 cubeCoordinates) {
        return getAvailableTiles().contains(cubeCoordinates);
    }

    /**
     * Check whether a Coordinate already have a tile on it or not.
     * @param cubeCoordinates Coordinate to check.
     * @return if a coordinate already exists there or not.
     */
    public boolean isInOccupiedTiles(Vector3 cubeCoordinates) {
        return getOccupiedTiles().contains(cubeCoordinates);
    }

    /**
     * Get all the tiles that something has been placed on.
     * @return Tiles in Cube Coordinates.
     */
    public List<Vector3> getOccupiedTiles() {
        List<Vector3> occupiedTiles = new ArrayList<>();
        for (Vector3 location : tileLocationType.keySet()) {
            if(!tileLocationType.get(location).equals("Free")) {
                occupiedTiles.add(location);
            }
        }
        return occupiedTiles;
    }

    /**
     * Get List of tiles that has nothing on them.
     * @return List of tiles in Cube Coordinates.
     */
    public List<Vector3> getAvailableTiles() {
        return tileTypeLocation.get("Free");
    }

    /**
     * Try to add Tiles to the map
     * @param cubeCoordinates Cube Coordinates where attempt is made.
     * @param tileName Type of tile that Player is trying to put down.
     * @return if tile placement was successful.
     */
    public boolean addTilesToMap(Vector3 cubeCoordinates, String tileName) {
        if (isInAvailableTiles(cubeCoordinates)) {
            tileLocationType.replace(cubeCoordinates, tileName);
            if (!tileTypeLocation.containsKey(tileName)) {
                tileTypeLocation.put(tileName, new ArrayList<>(List.of(cubeCoordinates)));
            } else {
                tileTypeLocation.get(tileName).add(cubeCoordinates);

            }
            tileTypeLocation.get("Free").remove(cubeCoordinates);
            return true;
        }
        return false;
    }

    /**
     * Get Available tiles to be added around the Map.
     * @param cubeCoordinates Last placed tile's location.
     * @return List of potential new available tiles.
     */
    public List<Vector3> newFreeTiles(Vector3 cubeCoordinates) {
        List<Vector3> neighbours = allNeighbours(cubeCoordinates);
        neighbours.removeIf(tileLocationType::containsKey);
        return neighbours;
    }

    /**
     * Adding new FreeTiles around the map.
     * @param cubeCoordinates Available tile location in Cube coordinates.
     */
    public void addFreeTile(Vector3 cubeCoordinates) {
        tileLocationType.put(cubeCoordinates,"Free");
        if (!tileTypeLocation.containsKey("Free")) {
            tileTypeLocation.put("Free", new ArrayList<>(List.of(cubeCoordinates)));
        } else if (!tileTypeLocation.get("Free").contains(cubeCoordinates)) {
            tileTypeLocation.get("Free").add(cubeCoordinates);
        }
    }

    public void startNewMap() {
        addFreeTile(new Vector3(0,0,0));
        addTilesToMap(new Vector3(0,0,0),"Plain");
        List<Vector3> newFreeTiles = newFreeTiles(new Vector3(0,0,0));
        for (Vector3 tile : newFreeTiles) {
            addFreeTile(tile);
        }
    }

    public Map<Vector3, String> getTileLocationType() {
        return tileLocationType;
    }

    /**
     * Pick a specified number of tiles for a player.
     *
     * @param howMany How many tiles will be sent to the Player.
     * @return List of integers that represent a Tile number to be sent to the Player.
     */
    public List<String> initialTilesForPlayer(int howMany) {
        List<String> initialTiles = new ArrayList<>();
        for (int i = 0; i< howMany; i++) {
        int randomint = (int) (Math.random() * allTileNames.length);
        initialTiles.add(allTileNames[randomint]);
        }
        return initialTiles;
    }
}
