package nl.esciencecenter.esight.examples.shadertest;

import java.util.HashMap;

import nl.esciencecenter.esight.math.Color4;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;

public class LiveShaderEditorInputHandler extends TextEditorKeyboardHandler implements MouseListener, KeyListener {

    private boolean reCompileNeeded = false;

    private final HashMap<String, Color4> syntaxColors;

    private static class SingletonHolder {
        public static final LiveShaderEditorInputHandler instance = new LiveShaderEditorInputHandler();
    }

    public static LiveShaderEditorInputHandler getInstance() {
        return SingletonHolder.instance;
    }

    protected LiveShaderEditorInputHandler() {
        super();
        syntaxColors = new HashMap<String, Color4>();

        // Vertex shader special outputs
        syntaxColors.put("gl_Position", Color4.GREEN);
        syntaxColors.put("gl_PointSize", Color4.GREEN);
        syntaxColors.put("gl_ClipVertex", Color4.GREEN);

        // Fragment shader special inputs
        syntaxColors.put("gl_FragCoord", Color4.GREEN);
        syntaxColors.put("gl_FrontFacing", Color4.GREEN);

        // Fragment shader special outputs
        syntaxColors.put("gl_FragColor", Color4.GREEN);
        syntaxColors.put("gl_FragData[gl_MaxDrawBuffers]", Color4.GREEN);
        syntaxColors.put("gl_FragDepth", Color4.GREEN);

        // keywords
        syntaxColors.put("void", Color4.MAGENTA);
        syntaxColors.put("in", Color4.MAGENTA);
        syntaxColors.put("out", Color4.MAGENTA);
        syntaxColors.put("inout", Color4.MAGENTA);
        syntaxColors.put("uniform", Color4.MAGENTA);
        syntaxColors.put("const", Color4.MAGENTA);
        syntaxColors.put("attribute", Color4.MAGENTA);
        syntaxColors.put("varying", Color4.MAGENTA);

        syntaxColors.put("if", Color4.MAGENTA);
        syntaxColors.put("for", Color4.MAGENTA);
        syntaxColors.put("else", Color4.MAGENTA);

        // Variable types
        syntaxColors.put("int", Color4.BLUE);
        syntaxColors.put("ivec2", Color4.BLUE);
        syntaxColors.put("ivec3", Color4.BLUE);
        syntaxColors.put("ivec4", Color4.BLUE);
        syntaxColors.put("float", Color4.BLUE);
        syntaxColors.put("vec2", Color4.BLUE);
        syntaxColors.put("vec3", Color4.BLUE);
        syntaxColors.put("vec4", Color4.BLUE);
        syntaxColors.put("mat3", Color4.BLUE);
        syntaxColors.put("mat4", Color4.BLUE);
        syntaxColors.put("bool", Color4.BLUE);
        syntaxColors.put("bvec2", Color4.BLUE);
        syntaxColors.put("bvec3", Color4.BLUE);
        syntaxColors.put("bvec4", Color4.BLUE);
        syntaxColors.put("sampler1D", Color4.BLUE);
        syntaxColors.put("sampler2D", Color4.BLUE);
        syntaxColors.put("sampler3D", Color4.BLUE);
        syntaxColors.put("samplerCube", Color4.BLUE);
        syntaxColors.put("sampler1DShadow", Color4.BLUE);
        syntaxColors.put("sampler2DShadow", Color4.BLUE);

        // functions
        syntaxColors.put("abs", Color4.RED);
        syntaxColors.put("ceil", Color4.RED);
        syntaxColors.put("floor", Color4.RED);
        syntaxColors.put("fract", Color4.RED);
        syntaxColors.put("min", Color4.RED);
        syntaxColors.put("max", Color4.RED);
        syntaxColors.put("mod", Color4.RED);
        syntaxColors.put("clamp", Color4.RED);
        syntaxColors.put("sign", Color4.RED);
        syntaxColors.put("smoothstep", Color4.RED);
        syntaxColors.put("step", Color4.RED);
        syntaxColors.put("mix", Color4.RED);
        syntaxColors.put("texture", Color4.RED);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN
                && e.getKeyCode() != KeyEvent.VK_LEFT && e.getKeyCode() != KeyEvent.VK_RIGHT
                && e.getKeyCode() != KeyEvent.VK_PAGE_UP && e.getKeyCode() != KeyEvent.VK_PAGE_DOWN
                && e.getKeyCode() != KeyEvent.VK_END && e.getKeyCode() != KeyEvent.VK_HOME) {

            setReCompileNeeded(true);
        }
    }

    @Override
    public void saveShader(String fileName) {
    }

    public HashMap<String, Color4> getSyntaxColors() {
        return syntaxColors;
    }

    /**
     * Getter for reCompileNeeded.
     * 
     * @return the reCompileNeeded.
     */
    public synchronized boolean isReCompileNeeded() {
        return reCompileNeeded;
    }

    /**
     * Setter for reCompileNeeded.
     * 
     * @param reCompileNeeded
     *            the reCompileNeeded to set
     */
    public synchronized void setReCompileNeeded(boolean reCompileNeeded) {
        this.reCompileNeeded = reCompileNeeded;
    }
}
