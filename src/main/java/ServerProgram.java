import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.util.Date;

public class ServerProgram extends Listener {
    static Server server;
    static int udpPort = 27960, tcpPort = 27960;

    public ServerProgram() {
    }

    public void start() throws Exception {
        server = new Server();
        //register a packet.
        server.getKryo().register(PacketMessage.class);
        server.getKryo().register(com.badlogic.gdx.math.Vector3.class);
        //Can only send object as packets if they are registered.
        server.bind(tcpPort,udpPort);

        server.start();

        server.addListener(new ServerProgram());

        System.out.println("Server is operational...");

    }
    public void connected(Connection c){
        System.out.println("Receinved a connection from..." + c.getRemoteAddressTCP().getHostString());
        //Create a messagePacket
        PacketMessage packetMessage = new PacketMessage();
        //Assign a message.
        packetMessage.message = "Hello friend the time is: " + new Date().toString();
        //Send a message.
        c.sendTCP(packetMessage);

    }
    public void disconnected(Connection c){
        System.out.println("Connection lost from....");
    }
    //Run when we receive a packet.
    public void received(Connection c, Object o){

    }
    public void sendVector3(PacketMessage message){
        Connection[] allConnections = server.getConnections();
        for(Connection connection:allConnections){
        message.message = "TILE_LOC";
        connection.sendTCP(message);
        }
    }
}
