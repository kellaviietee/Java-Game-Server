import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;

public class PacketMessage {
    public String message;
    public Vector3 tileLocation;
    public String tileName;
    public HashMap<Vector3,String> mapTiles;
}
