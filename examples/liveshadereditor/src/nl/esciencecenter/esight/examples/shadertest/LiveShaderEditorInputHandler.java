package nl.esciencecenter.esight.examples.shadertest;

import java.util.HashMap;

import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.VecF3;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class LiveShaderEditorInputHandler extends TextEditorKeyboardHandler implements MouseListener, KeyListener {

    /** Initial value for the rotation in the X direction */
    protected float rotationXorigin = 0;
    /**
     * Final rotation in the X direction, translated to openGL units, stored to
     * make successive rotations smooth
     */
    protected float rotationX;

    /** Initial value for the rotation in the Y direction */
    protected float rotationYorigin = 0;
    /**
     * Final rotation in the Y direction, translated to openGL units, stored to
     * make successive rotations smooth
     */
    protected float rotationY;

    /** Initial value for the rotation in the Y direction */
    protected float rotationZorigin = 0;
    /**
     * Final rotation in the Y direction, translated to openGL units, stored to
     * make successive rotations smooth
     */
    protected float rotationZ;

    /** Mouse drag start point in X direction */
    protected float dragLeftXorigin;
    /** Mouse drag start point in Y direction */
    protected float dragLeftYorigin;

    /** Final rotation in openGL units */
    public VecF3 rotation, translation;
    /** Final view distance (translation) in openGL units */
    public float viewDist = -3f;
    /** Current direction of the view */
    private octants current_view_octant = octants.PPP;

    private float translationX = 0f;
    private float translationY = 0f;

    private float translationXorigin = 0f;
    private float translationYorigin = 0f;

    private boolean reCompileNeeded = false;

    private void reset() {
        rotationXorigin = 0;
        rotationX = 0;
        rotationYorigin = 0;
        rotationY = 0;
        rotationZorigin = 0;
        rotationZ = 0;

        dragLeftXorigin = 0;
        dragLeftYorigin = 0;

        translationX = 0f;
        translationXorigin = 0f;
        translationY = 0f;
        translationYorigin = 0f;

        rotation = new VecF3();
        translation = new VecF3();
        viewDist = -3f;
        current_view_octant = octants.PPP;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Empty - unneeded
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Empty - unneeded
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // if (e.isButtonDown(MouseEvent.BUTTON1) ||
        // e.isButtonDown(MouseEvent.BUTTON3)) {
        dragLeftXorigin = e.getX();
        dragLeftYorigin = e.getY();
        // }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        rotationXorigin = rotationX;
        rotationYorigin = rotationY;
        rotationZorigin = rotationZ;

        translationXorigin = translationX;
        translationYorigin = translationY;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.isButtonDown(MouseEvent.BUTTON1)) {
            // x/y reversed because of axis orientation. (up/down => x axis
            // rotation in OpenGL)
            if (e.isControlDown()) {
                rotationZ = ((e.getX() - dragLeftXorigin) + rotationZorigin) % 360;
            } else {
                if (e.isShiftDown()) {
                    rotationX = ((e.getX() - dragLeftXorigin) / 10f + rotationXorigin) % 360;
                    rotationY = ((e.getY() - dragLeftYorigin) / 10f + rotationYorigin) % 360;
                } else {
                    rotationX = ((e.getX() - dragLeftXorigin) + rotationXorigin) % 360;
                    rotationY = ((e.getY() - dragLeftYorigin) + rotationYorigin) % 360;
                }
            }
            // Make sure the numbers are always positive (so we can determine
            // the octant we're in more easily)
            if (rotationX < 0) {
                rotationX = 360f + rotationX % 360;
            }
            if (rotationY < 0) {
                rotationY = 360f + rotationY % 360;
            }
            if (rotationZ < 0) {
                rotationZ = 360f + rotationZ % 360;
            }

            rotation.set(0, rotationY);
            rotation.set(1, rotationX);
            rotation.set(2, rotationZ);
            setCurrentOctant(rotation);
        } else if (e.isButtonDown(MouseEvent.BUTTON3)) {
            translationX = (.01f * (e.getX() - dragLeftXorigin)) + translationXorigin;
            translationY = (-.01f * (e.getY() - dragLeftYorigin)) + translationYorigin;

            translation.set(0, translationX);
            translation.set(1, translationY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Empty - unneeded
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        float newViewDist = this.viewDist;

        if (e.isShiftDown()) {
            newViewDist -= e.getRotation()[1] * 2;
        } else {
            newViewDist -= e.getRotation()[1] * 10;
        }
        viewDist = newViewDist;
    }

    /**
     * Setter for the current viewing 'angle' by octant.
     * 
     * @param rotation
     *            The rotation from which to calculate the viewing octant.
     */
    private void setCurrentOctant(VecF3 rotation) {
        float x = rotation.get(0);
        int qx = (int) Math.floor(x / 90f);
        float y = rotation.get(1);
        int qy = (int) Math.floor(y / 90f);

        if (qx == 0 && qy == 0) {
            current_view_octant = octants.NPP;
        } else if (qx == 0 && qy == 1) {
            current_view_octant = octants.NPN;
        } else if (qx == 0 && qy == 2) {
            current_view_octant = octants.PPN;
        } else if (qx == 0 && qy == 3) {
            current_view_octant = octants.PPP;

        } else if (qx == 1 && qy == 0) {
            current_view_octant = octants.PPN;
        } else if (qx == 1 && qy == 1) {
            current_view_octant = octants.PPP;
        } else if (qx == 1 && qy == 2) {
            current_view_octant = octants.NPP;
        } else if (qx == 1 && qy == 3) {
            current_view_octant = octants.NPN;

        } else if (qx == 2 && qy == 0) {
            current_view_octant = octants.PNN;
        } else if (qx == 2 && qy == 1) {
            current_view_octant = octants.PNP;
        } else if (qx == 2 && qy == 2) {
            current_view_octant = octants.NNP;
        } else if (qx == 2 && qy == 3) {
            current_view_octant = octants.NNN;

        } else if (qx == 3 && qy == 0) {
            current_view_octant = octants.NNP;
        } else if (qx == 3 && qy == 1) {
            current_view_octant = octants.NNN;
        } else if (qx == 3 && qy == 2) {
            current_view_octant = octants.PNN;
        } else if (qx == 3 && qy == 3) {
            current_view_octant = octants.PNP;
        }
    }

    /**
     * 
     * @return the current OpenGL ModelView rotation variable
     */
    @Override
    public VecF3 getRotation() {
        return rotation;
    }

    /**
     * @param rotation
     *            the current OpenGL ModelView rotation variable to set
     */
    @Override
    public void setRotation(VecF3 rotation) {
        this.rotation = rotation;
    }

    /**
     * 
     * @return the current OpenGL ModelView translation variable
     */
    public VecF3 getTranslation() {
        return translation;
    }

    /**
     * @param rotation
     *            the OpenGL ModelView translation variable to set
     */
    public void setTranslation(VecF3 translation) {
        this.translation = translation;
    }

    /**
     * @return the current OpenGL ModelView View distance (translation) variable
     */
    @Override
    public float getViewDist() {
        return viewDist;
    }

    /**
     * @param viewDist
     *            the current OpenGL ModelView View distance (translation)
     *            variable to set
     */
    @Override
    public void setViewDist(float viewDist) {
        this.viewDist = viewDist;
    }

    /**
     * @return the current_view_octant (mainly used for octrees)
     */
    @Override
    public octants getCurrentViewOctant() {
        return current_view_octant;
    }

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
        syntaxColors.put("gl_Position", Color4.sickly_green);
        syntaxColors.put("gl_PointSize", Color4.sickly_green);
        syntaxColors.put("gl_ClipVertex", Color4.sickly_green);

        // Fragment shader special inputs
        syntaxColors.put("gl_FragCoord", Color4.sickly_green);
        syntaxColors.put("gl_FrontFacing", Color4.sickly_green);

        // Fragment shader special outputs
        syntaxColors.put("gl_FragColor", Color4.sickly_green);
        syntaxColors.put("gl_FragData[gl_MaxDrawBuffers]", Color4.sickly_green);
        syntaxColors.put("gl_FragDepth", Color4.sickly_green);

        // keywords
        syntaxColors.put("void", Color4.magenta);
        syntaxColors.put("in", Color4.magenta);
        syntaxColors.put("out", Color4.magenta);
        syntaxColors.put("inout", Color4.magenta);
        syntaxColors.put("uniform", Color4.magenta);
        syntaxColors.put("const", Color4.magenta);
        syntaxColors.put("attribute", Color4.magenta);
        syntaxColors.put("varying", Color4.magenta);

        syntaxColors.put("if", Color4.magenta);
        syntaxColors.put("for", Color4.magenta);
        syntaxColors.put("else", Color4.magenta);

        // Variable types
        syntaxColors.put("int", Color4.blue);
        syntaxColors.put("ivec2", Color4.blue);
        syntaxColors.put("ivec3", Color4.blue);
        syntaxColors.put("ivec4", Color4.blue);
        syntaxColors.put("float", Color4.blue);
        syntaxColors.put("vec2", Color4.blue);
        syntaxColors.put("vec3", Color4.blue);
        syntaxColors.put("vec4", Color4.blue);
        syntaxColors.put("mat3", Color4.blue);
        syntaxColors.put("mat4", Color4.blue);
        syntaxColors.put("bool", Color4.blue);
        syntaxColors.put("bvec2", Color4.blue);
        syntaxColors.put("bvec3", Color4.blue);
        syntaxColors.put("bvec4", Color4.blue);
        syntaxColors.put("sampler1D", Color4.blue);
        syntaxColors.put("sampler2D", Color4.blue);
        syntaxColors.put("sampler3D", Color4.blue);
        syntaxColors.put("samplerCube", Color4.blue);
        syntaxColors.put("sampler1DShadow", Color4.blue);
        syntaxColors.put("sampler2DShadow", Color4.blue);

        // functions
        syntaxColors.put("abs", Color4.sickly_magenta);
        syntaxColors.put("ceil", Color4.sickly_magenta);
        syntaxColors.put("floor", Color4.sickly_magenta);
        syntaxColors.put("fract", Color4.sickly_magenta);
        syntaxColors.put("min", Color4.sickly_magenta);
        syntaxColors.put("max", Color4.sickly_magenta);
        syntaxColors.put("mod", Color4.sickly_magenta);
        syntaxColors.put("clamp", Color4.sickly_magenta);
        syntaxColors.put("sign", Color4.sickly_magenta);
        syntaxColors.put("smoothstep", Color4.sickly_magenta);
        syntaxColors.put("step", Color4.sickly_magenta);
        syntaxColors.put("mix", Color4.sickly_magenta);
        syntaxColors.put("texture", Color4.sickly_magenta);

        reset();
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
        // String path = settings.getScreenshotPath();
        // File newDir = new File(path + "screenshots");
        // if (!newDir.exists())
        // newDir.mkdir();
        //
        // String bareName = path + "screenshots/" + fileName;
        //
        // File newFile = new File(bareName + ".txt");
        // try {
        // int attempt = 1;
        // while (newFile.exists()) {
        // String newName = bareName + " (" + attempt + ")";
        //
        // if (ShaderTestWindow.isFragmentShader()) {
        // newName += ".fp";
        // } else {
        // newName += ".vp";
        // }
        // newFile = new File(newName);
        //
        // attempt++;
        // }
        //
        // System.out.println("Writing shader: " + newFile.getAbsolutePath());
        //
        // FileWriter outFile = new FileWriter(newFile);
        // PrintWriter out = new PrintWriter(outFile);
        //
        // for (String line : getTextlines()) {
        // out.println(line);
        // }
        //
        // out.close();
        // outFile.close();
        // } catch (GLException e) {
        // e.printStackTrace();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
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
