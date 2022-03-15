
import Hexgrid.HexGrid;
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.KryoObjectInput;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.KryoSerialization;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.*;

public class ServerProgram extends Listener {
    static final float TILE_SIZE = 1.025f;
    static Server server;
    static int udpPort = 8080, tcpPort = 8080;
    static HexGrid hexGrid = new HexGrid(TILE_SIZE);

    public ServerProgram() {
    }

    public void start() throws Exception {
        server = new Server();
        //register a packet.
        server.getKryo().register(PacketMessage.class);
        server.getKryo().register(com.badlogic.gdx.math.Vector3.class);
        server.getKryo().register(HashMap.class);
        server.getKryo().register(List.class);
        server.getKryo().register(ArrayList.class);
        //Can only send object as packets if they are registered.
        server.bind(tcpPort, udpPort);

        server.start();

        server.addListener(new ServerProgram());

        System.out.println("Server is operational...");
        hexGrid.startNewMap();
    }

    public void connected(Connection c) {
        System.out.println("Received a connection from..." + c.getRemoteAddressTCP().getHostString());
        //Create a messagePacket
        PacketMessage packetMessage = new PacketMessage();
        //Assign a message.
        packetMessage.message = "Hello friend the time is: " + new Date().toString();
        //Send a message.
        c.sendTCP(packetMessage);
    }

    /**
     * What server does when Connection is lost.
     *
     * @param c
     */
    public void disconnected(Connection c) {
        System.out.println(
                "Connection lost from....");
        if (server.getConnections().length == 0) {
            hexGrid = new HexGrid(TILE_SIZE);
        }
    }

    /**
     * Run when we receive a packet.
     */

    public void received(Connection c, Object o) {
        if (o instanceof PacketMessage) {
            if (Objects.equals(((PacketMessage) o).message, "TILE_LOC")) {
                if (hexGrid.isInAvailableTiles(((PacketMessage) o).tileLocation)) {
                    sendVector3((PacketMessage) o);
                    hexGrid.addTilesToMap(((PacketMessage) o).tileLocation,((PacketMessage) o).tileName);
                    List<Vector3> newFreeTiles = hexGrid.newFreeTiles(((PacketMessage) o).tileLocation);
                    for (Vector3 freeTile : newFreeTiles) {
                        hexGrid.addFreeTile(freeTile);
                        PacketMessage message = new PacketMessage();
                        message.message = "TILE_LOC";
                        message.tileLocation = freeTile;
                        message.tileName = "Free";
                        sendVector3(message);
                    }
                }
            } else {
                sendCurrentMap(c);
            }
        }

    }

    /**
     * Send the same message to all the clients that are connected.
     *
     * @param message Message to be sent over.
     */
    public void sendVector3(PacketMessage message) {
        Connection[] allConnections = server.getConnections();
        for (Connection connection : allConnections) {
            connection.sendTCP(message);
        }
    }


    /**
     * Pick a random Tile to send to Player
     *
     * @return number between 0 - 41
     */

    public int pickRandomTileForPlayer() {
        return 0;
    }



    /**
     * Generate the Initial tiles for a single player that is connected to the Server.
     *
     * @param howMany How many tiles does  player get.
     */
    public List<String> initialTilesForThePlayer(int howMany) {
        return hexGrid.initialTilesForPlayer(howMany);
    }

    /**
     * Generate the Initial tiles for all the players that are connected to the Server.
     *
     * @param howMany How many tiles does each player get.
     */
    public List<String> initialTilesForAllThePlayers(int howMany) {
        return hexGrid.initialTilesForPlayer(howMany);

    }


    /**
     * Return if the tile should be a tile that has a mission attached to it or not.
     * Use a random number generator to determine that.
     * @param tile Tile that is to be decided over if it has a mission attached with it or not
     * @return if the tile is a mission tile or not.
     */
    /*
    public boolean MissionTile(Entity tile) {
        return false;
    }
    */

    /**
     * Send all info about current Map to the player.
     *
     * @param player Player that needs the current Map.
     */
    public void sendCurrentMap(Connection player) {
        PacketMessage tileInfo = new PacketMessage();
        tileInfo.message = "NEW_MAP";
        tileInfo.serverMap = (HashMap<Vector3, String>) hexGrid.getTileLocationType();
        player.sendTCP(tileInfo);
        List<String> startTiles = initialTilesForThePlayer(10);
        PacketMessage startingTiles = new PacketMessage();
        startingTiles.message = "START_TILES";
        startingTiles.startTiles = startTiles;
        player.sendTCP(startingTiles);
    }
}

