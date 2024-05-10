package hrd.ui.menu;

import arc.math.*;
import arc.util.noise.*;
import mindustry.content.*;
import mindustry.world.*;

public class MenuSlides{
    public static MenuSlide stone = new MenuSlide(false){
        @Override
        protected void generate(Tiles tiles){
            boolean tech = Mathf.chance(0.25);
            for(int x = 0; x < tiles.width; x++){
                for(int y = 0; y < tiles.height; y++){
                    Block floor = Blocks.basalt;
                    Block wall = Blocks.air;

                    if(Simplex.noise2d(seed + 1, 3, 0.5, 1/20.0, x, y) > 0.4){
                        floor = Blocks.stone;
                    }

                    if(Simplex.noise2d(seed + 1, 3, 0.3, 1/20.0, x, y) > 0.5){
                        wall = Blocks.stoneWall;
                    }

                    if(tech){
                        int mx = x % 10, my = y % 10;
                        int sclx = x / 10, scly = y / 10;
                        if(Simplex.noise2d(seed + 2, 2, 1f / 10f, 1f, sclx, scly) > 0.6f && (mx == 0 || my == 0 || mx == 9 || my == 9)){
                            floor = Blocks.darkPanel3;
                            if(Mathf.dst(mx, my, 5, 5) > 6f){
                                floor = Blocks.darkPanel4;
                            }

                            if(wall != Blocks.air && Mathf.chance(0.7)){
                                wall = Blocks.darkMetal;
                            }
                        }
                    }

                    setTile(x, y, wall, Blocks.air, floor);
                }
            }
        }
    };
}
