import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public HexGrid(float tileSize) {
        this.tileSize = tileSize;
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
     * @param worldCoordinates
     * @return
     */
    public Vector2 pixelToHex(Vector3 worldCoordinates) {
        float q = (float)(sqrt(3)/3 * worldCoordinates.x - 1f/3f * worldCoordinates.z)/ tileSize;
        float r = (2f/ 3f * worldCoordinates.z) / tileSize;
        Vector2 fracAxial = new Vector2(q,r);
        Vector2 wholeAxial = AxialRound(fracAxial);
        return new Vector2(wholeAxial.x, wholeAxial.y);
    }

    public Vector3 around(Vector3 cubeCoords, int direction)
    {
        assert(direction > 0);
        assert(direction < 6);

        return new Vector3(cubeCoords.add(directions.get(direction)));
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
}
