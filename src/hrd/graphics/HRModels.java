package hrd.graphics;

import gltfrenzy.loader.*;
import gltfrenzy.model.*;

import static arc.Core.*;
import static mindustry.Vars.*;

// original impl @GlFolker
public class HRModels{
    public static Scenes3D ship;

    public static Node shipCargo, shipBody, shipThrusters;

    public static void load(){
        assets.setLoader(Scenes3D.class, ".gltf", new Scenes3DLoader(tree, new GltfReader()));
        assets.setLoader(Scenes3D.class, ".glb", new Scenes3DLoader(tree, new GlbReader()));

        assets.setLoader(MeshSet.class, new MeshSetLoader(tree));
        assets.setLoader(Node.class, new NodeLoader(tree));

        ship = new Scenes3D();

        app.post(() -> {
            ship.load(tree.get("scenes/ship.gltf"), tree, null);
            shipCargo = ship.node("Cargo");
            shipBody = ship.node("Ship");
            shipThrusters = ship.node("Engines");
        });
    }
}
