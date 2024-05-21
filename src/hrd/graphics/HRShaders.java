package hrd.graphics;

import arc.Core;
import arc.graphics.gl.*;
import hrd.graphics.shaders.*;

// original impl @GlFolker
// https://github.com/GlennFolker/Confictura
public class HRShaders {
    public static CelestialShader celestial;
    public static FlatShader flat;

    public static void load(){
        String prevVert = Shader.prependVertexCode, prevFrag = Shader.prependFragmentCode;
        Shader.prependVertexCode = Shader.prependFragmentCode = "";

        if(Core.graphics.getGLVersion().type == GLVersion.GlType.OpenGL){
            Shader.prependFragmentCode = "#define HAS_GL_FRAGDEPTH\n";
        }
        celestial = new CelestialShader();
        flat = new FlatShader();

        Shader.prependVertexCode = prevVert;
        Shader.prependFragmentCode = prevFrag;
    }
}
