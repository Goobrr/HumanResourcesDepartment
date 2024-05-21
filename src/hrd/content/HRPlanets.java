package hrd.content;

import hrd.world.Ship;
import mindustry.content.Planets;
import mindustry.type.Planet;

public class HRPlanets {
    public static Planet ship;

    public static void load(){
        ship = new Ship("ship", Planets.serpulo, 0.525f);
    }
}
