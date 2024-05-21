package hrd.world;


import arc.Events;
import arc.graphics.Color;
import arc.graphics.Mesh;
import arc.graphics.g3d.Camera3D;
import arc.math.Mat;
import arc.math.geom.Mat3D;
import arc.math.geom.Quat;
import arc.math.geom.Vec3;
import arc.util.Tmp;
import gltfrenzy.model.Node;
import hrd.graphics.HRModels;
import hrd.graphics.HRShaders;
import mindustry.content.Planets;
import mindustry.game.EventType;
import mindustry.graphics.Pal;
import mindustry.maps.generators.BlankPlanetGenerator;
import mindustry.type.Planet;
import mindustry.graphics.g3d.*;
import mindustry.graphics.g3d.PlanetGrid.*;
import mindustry.type.Sector;
import mindustry.world.meta.Env;

import static mindustry.Vars.*;

// original impl @GlFolker
// https://github.com/GlennFolker/Confictura
public class Ship extends Planet{
    private static final Mat3D mat = new Mat3D();
    private static final Quat quat = new Quat();
    private static final Vec3 nor = new Vec3();

    private static float lastSpacing;

    public float scale = 0.0175f / 1.5f;

    public Ship(String name, Planet observed, float distance){
        super(name, distance(observed, distance), 0.1f);
        undistance(observed);

        updateLighting = drawOrbit = false;
        defaultEnv = Env.space;
        meshLoader = SatelliteMesh::new;
        tidalLock = true;
        camRadius = 1.3f;

        orbitOffset = 75f;

        sectors.add(new Sector(this, Ptile.empty));
        generator = new BlankPlanetGenerator();

        // Deliberately make it as the first child to get as close as possible.
        // This *might* be invasive for mods that are sensitive towards their planet children indices (which I can
        // literally find no reason for), however until something weird happens, I will keep this code.
        Events.on(EventType.ContentInitEvent.class, e -> {
            var c = parent.children;

            int index = c.indexOf(this);
            if(index == 0) return;

            float delta = orbitRadius - c.get(index - 1).orbitRadius;
            c.remove(index);
            c.insert(0, this);

            orbitRadius = parent.radius + distance + totalRadius;
            orbitTime = 150f;
            clipRadius = totalRadius + distance + parent.radius * 2f;

            for(int i = 1; i <= index; i++) c.get(i).orbitRadius += delta;
            parent.updateTotalRadius();
        });
    }

    protected static Planet distance(Planet planet, float spacing){
        lastSpacing = planet.orbitSpacing;
        planet.orbitSpacing = spacing;
        return planet;
    }

    protected static void undistance(Planet planet){
        planet.orbitSpacing = lastSpacing;
    }
    @Override
    public Mat3D getTransform(Mat3D mat){
        return super.getTransform(mat).rotate(quat.set(Vec3.X, -90f)).rotate(quat.set(Vec3.Z, 180f)).scale(scale, scale, scale);
    }

    @Override
    public void drawAtmosphere(Mesh atmosphere, Camera3D cam) {}

    public class SatelliteMesh implements GenericMesh{
        @Override
        public void render(PlanetParams params, Mat3D projection, Mat3D transform){
            Node body = HRModels.shipBody, thruster = HRModels.shipThrusters, cargo = HRModels.shipCargo;

            var shader1 = HRShaders.celestial;
            shader1.light1.set(Planets.serpulo.position);
            shader1.color1.set(Pal.spore);

            shader1.light2.set(solarSystem.position);
            shader1.color2.set(solarSystem.lightColor);

            shader1.camPos.set(renderer.planets.cam.position);
            shader1.bind();
            shader1.apply();

            shader1.setUniformMatrix4("u_proj", projection.val);
            for(var node : new Node[]{body, cargo}){
                shader1.setUniformMatrix4("u_trans", mat.set(transform).mul(node.globalTrns).val);

                float[] in = mat.val, out = Tmp.m1.val;
                System.arraycopy(in, Mat3D.M00, out, Mat.M00, 3);
                System.arraycopy(in, Mat3D.M01, out, Mat.M01, 3);
                System.arraycopy(in, Mat3D.M02, out, Mat.M02, 3);

                shader1.setUniformMatrix("u_normal", Tmp.m1.inv().transpose());

                node.mesh.containers.each(mesh -> mesh.render(shader1));
            }

            var shader2 = HRShaders.flat;
            shader2.color = Tmp.c1.set(Pal.lancerLaser).lerp(Color.white, 0.5f);
            shader2.bind();
            shader2.apply();
            shader2.setUniformMatrix4("u_proj", projection.val);

            shader2.setUniformMatrix4("u_trans", mat.set(transform).mul(thruster.globalTrns).val);

            float[] in = mat.val, out = Tmp.m1.val;
            System.arraycopy(in, Mat3D.M00, out, Mat.M00, 3);
            System.arraycopy(in, Mat3D.M01, out, Mat.M01, 3);
            System.arraycopy(in, Mat3D.M02, out, Mat.M02, 3);

            shader2.setUniformMatrix("u_normal", Tmp.m1.inv().transpose());

            thruster.mesh.containers.each(mesh -> mesh.render(shader2));
        }
    }
}
