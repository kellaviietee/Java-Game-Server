import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;



public class GameWorld {
    public static final float FOV = 67f;
    private ModelBatch modelBatch;
    private Environment environment;
    public PerspectiveCamera cam;
    private Engine engine;
    private AssetManager assetManager;
    private final HexGrid hexGrid;
    private GameCameraController gameCameraController;
    private final ServerProgram server;


    public GameWorld(ServerProgram server){
        this.server = server;
        initPersCamera();
        initEnviornment();
        initModelBatch();
        hexGrid = new HexGrid(1.025f);
        engine = new Engine();
        engine.addSystem(new RenderSystem(modelBatch,environment));
        assetManager = new AssetManager();
        loadAllModels();
        server.addGameWorld(this);
    }
    public void loadAllModels(){
        assetManager.load("Assets/plain.g3db",Model.class);
        assetManager.load("Assets/roadA.g3db",Model.class);
        assetManager.load("Assets/roadB.g3db",Model.class);
        assetManager.load("Assets/roadC.g3db",Model.class);
        assetManager.load("Assets/roadD.g3db",Model.class);
        assetManager.load("Assets/roadE.g3db",Model.class);
        assetManager.load("Assets/roadF.g3db",Model.class);
        assetManager.load("Assets/roadG.g3db",Model.class);
        assetManager.load("Assets/roadH.g3db",Model.class);
        assetManager.load("Assets/roadI.g3db",Model.class);
        assetManager.load("Assets/roadJ.g3db",Model.class);
        assetManager.load("Assets/roadK.g3db",Model.class);
        assetManager.load("Assets/roadL.g3db",Model.class);
        assetManager.load("Assets/roadM.g3db",Model.class);
        assetManager.load("Assets/water.g3db",Model.class);
        assetManager.load("Assets/waterA.g3db",Model.class);
        assetManager.load("Assets/waterB.g3db",Model.class);
        assetManager.load("Assets/waterC.g3db",Model.class);
        assetManager.load("Assets/waterD.g3db",Model.class);
        assetManager.finishLoading();
    }
    public Model getTestModel(){
        return assetManager.get("Assets/roadM.g3db",Model.class);
    }

    private void initPersCamera(){
        cam = new PerspectiveCamera(FOV, Core.WIDTH, Core.HEIGHT);
        cam.position.set(3f,4f,3f);
        cam.lookAt(0f,0f,0f);
        cam.near = 1f;
        cam.far = 300f;
        gameCameraController = new GameCameraController(cam);
        Gdx.input.setInputProcessor(gameCameraController);
        cam.update();
    }
    private void initEnviornment(){
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
    }
    private void initModelBatch(){
        modelBatch = new ModelBatch();
    }

    public void resize(int width, int height){
        cam.viewportWidth = width;
        cam.viewportHeight = height;
    }

    public void render(float delta){
        gameCameraController.updateDelta(delta);
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            Vector3 instanceLocation = getFloorCoordinates();
            PacketMessage packetMessage = new PacketMessage();
            packetMessage.tileLocation = instanceLocation;
            server.sendVector3(packetMessage);
            Model testModel = getTestModel();
            Entity testentity = new Entity();
            testentity.add(new ModelComponent(testModel,instanceLocation.x,instanceLocation.y,instanceLocation.z));
            engine.addEntity(testentity);

        }
        modelBatch.begin(cam);
        engine.update(delta);
        cam.update();
        modelBatch.end();
    }

    public void dispose(){
        modelBatch.dispose();
    }

    /**
    This creates an invisible XZ plane after which it returns where it hit the plane.
     */
    public Vector3 getFloorCoordinates(){
        Vector3 intersection = new Vector3();
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();
            Ray ray = cam.getPickRay(screenX, screenY);
            Plane plane = new Plane(new Vector3(0, 1, 0), Vector3.Zero);
            if (Intersector.intersectRayPlane(ray, plane, intersection)) {
                Vector3 hexClicked = hexGrid.pixelToHex(intersection);
                return hexGrid.hexToWorld(new Vector2(hexClicked.x,hexClicked.z));
            }
        }
        return new Vector3();
    }
}
