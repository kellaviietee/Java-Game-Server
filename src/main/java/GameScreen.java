import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
/*
This makes sure, that later we can actually implement multiple screens. One for GameWorld, another one for UI etc.
Also helps to change the scenes.
 */

public class GameScreen implements Screen {
    Core game;
    GameWorld gameWorld;
    ServerProgram server;


    public GameScreen(Core game, ServerProgram server){
        this.server = server;
        this.game = game;
        gameWorld = new GameWorld(server);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void render(float delta) {
        gameWorld.render(delta);

    }

    @Override
    public void resize(int width, int height) {
        gameWorld.resize(width,height);

    }

    @Override
    public void dispose() {
        gameWorld.dispose();

    }
    @Override
    public void show() {

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
