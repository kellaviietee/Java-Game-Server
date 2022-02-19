import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;


/*
This is a Rendering component. All instances, that have this component will be visible on the screen.
 */

public class ModelComponent implements Component {
    public Model model;
    public ModelInstance instance;

    public ModelComponent(Model model, float x, float y, float z){
        this.model = model;
        this.instance = new ModelInstance(model, new Matrix4().setToTranslation(x,y,z));


    }
}
