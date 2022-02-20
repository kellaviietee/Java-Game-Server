import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;



public class Core extends ApplicationAdapter {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 720;
    Screen screen;
    ServerProgram server;

    public Core(ServerProgram server) {
        this.server = server;
    }

    @Override
    public void create() {
        setScreen(new GameScreen(this,server));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
    }

    public void setScreen(Screen screen) {
        if(this.screen != null){
            this.screen.hide();
            this.screen.dispose();
        }
        this.screen = screen;
        if(this.screen != null){
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        }
    }
}
