import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class GameCameraController implements InputProcessor {
    PerspectiveCamera cam;
    private Vector3 initialLookAt;
    private float delta;
    private Vector2 previousMouse;
    private Vector3 forwardVector = new  Vector3(-10f,0f,-10f);
    private Vector3 backwardVector = new  Vector3(10f,0f,10f);
    private Vector3 leftVector = new  Vector3(-10f,0f,10f);
    private Vector3 rightVector = new  Vector3(10f,0f,-10f);

    public GameCameraController(PerspectiveCamera cam) {
        this.cam = cam;
        initialLookAt = new Vector3(0.5f, 0.7f, 0.5f);


    }

    public void updateDelta(float delta) {
        this.delta = delta;
        moveCamera(delta);
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
        PanTheCamera();
        }
        return false;
    }

    /**
     * Pans the camera around and updates the movement vectors.
     */
    private  void PanTheCamera(){
        Vector3 intersection = new Vector3();
        Ray ray = cam.getPickRay(512,360);
        Plane plane = new Plane(new Vector3(0, 1, 0), Vector3.Zero);
        Intersector.intersectRayPlane(ray,plane,intersection);
        cam.rotateAround(intersection,Vector3.Y,2);
        Vector3 dummyVector = new Vector3(cam.direction).nor();
        forwardVector = new Vector3(dummyVector.x,0,dummyVector.z).scl(10);
        backwardVector = new Vector3(-forwardVector.x,0,-forwardVector.z);
        leftVector = new Vector3(forwardVector.z,0,-forwardVector.x);
        rightVector = new Vector3(-forwardVector.z,0,forwardVector.x);
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        if (v1 > 0) {
            cam.position.lerp(cam.position.sub(new Vector3(cam.direction)), 1f);
        } else {
            cam.position.lerp(cam.position.add(new Vector3(cam.direction)), 1f);
        }
        return false;
    }

    /**
     * Move the camera around with WASD
     * @param delta
     */
    public void moveCamera(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector3 dummyVector = new Vector3(forwardVector);
            cam.translate(dummyVector.scl(delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            Vector3 dummyVector = new Vector3(backwardVector);
            cam.translate(dummyVector.scl(delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            Vector3 dummyVector = new Vector3(leftVector);
            cam.translate(dummyVector.scl(delta));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            Vector3 dummyVector = new Vector3(rightVector);
            cam.translate(dummyVector.scl(delta));
        }
    }
}
