package hrd.graphics;

import gltfrenzy.loader.*;
import gltfrenzy.model.*;

import static arc.Core.*;
import static mindustry.Vars.*;

// original impl @GlFolker
// https://github.com/GlennFolker/Confictura
public class HRModels{
    public static Scenes3D ship;

    public static Node shipCargo, shipBody, shipThrusters;

    public static void load(){
        assets.setLoader(Scenes3D.class, ".gltf", new Scenes3DLoader(tree, new GltfReader()));
        assets.setLoader(Scenes3D.class, ".glb", new Scenes3DLoader(tree, new GlbReader()));

        assets.setLoader(MeshSet.class, new MeshSetLoader(tree));
        assets.setLoader(Node.class, new NodeLoader(tree));

        // parsing and stuff, create the structure stuff
        Runnable loadSync = (ship = new Scenes3D()).load(tree.get("scenes/ship.gltf"), tree, null);
        shipCargo = ship.node("Cargo");
        shipBody = ship.node("Ship");
        shipThrusters = ship.node("Engines");


        // create GL resources in main thread
        app.post(() -> {
            loadSync.run();
        });
    }
}
