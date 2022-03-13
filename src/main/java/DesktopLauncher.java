import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


/**
 Just makes sure the Game actually launches.
 */
public class DesktopLauncher {
    public static void main(String[] args) throws Exception {
        ServerProgram server = new ServerProgram();
        server.start();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1024,720);
        config.setIdleFPS(60);
        config.useVsync(true);
        config.setTitle("Server");
        new Lwjgl3Application(new Core(),config);
    }
}
