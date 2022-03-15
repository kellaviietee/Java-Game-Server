import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PacketMessage {
    public String message;
    public Vector3 tileLocation;
    public String tileName;
    public HashMap<Vector3,String> serverMap;
    public List<String> startTiles;
}
