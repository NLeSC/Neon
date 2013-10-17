package nl.esciencecenter.neon.examples.shadertest;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import nl.esciencecenter.neon.NeonGLEventListener;
import nl.esciencecenter.neon.datastructures.FrameBufferObject;
import nl.esciencecenter.neon.exceptions.CompilationFailedException;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.Float3Matrix;
import nl.esciencecenter.neon.math.Float4Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Point4;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;
import nl.esciencecenter.neon.models.Axis;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.models.Quad;
import nl.esciencecenter.neon.models.Sphere;
import nl.esciencecenter.neon.noise.Perlin3D;
import nl.esciencecenter.neon.shaders.ShaderProgram;
import nl.esciencecenter.neon.shaders.ShaderProgramLoader;
import nl.esciencecenter.neon.text.MultiColorText;
import nl.esciencecenter.neon.text.jogampexperimental.Font;
import nl.esciencecenter.neon.text.jogampexperimental.FontFactory;

public class LiveShaderEditorGLEventListener extends NeonGLEventListener {
    private ShaderProgram liveShader, postprocessShader, axesShader, textShader;

    private FrameBufferObject starFBO, hudFBO, axesFBO;
    private Model FSQ_postprocess;
    private Model xAxis, yAxis, zAxis;

    private final int fontSize = 20;
    private MultiColorText myText;

    private Perlin3D noiseTex;

    private float offset = 0;

    private Sphere testModel;

    private static File vsFile;
    private static File fsFile;

    private String compilerMessage = "";

    private boolean newFragmentShader;
    private boolean newVertexShader;
    private String newShaderFileName;

    protected final ShaderProgramLoader loader;
    protected int canvasWidth, canvasHeight;

    protected int fontSet = FontFactory.UBUNTU;
    protected Font font;

    private final LiveShaderEditorInputHandler inputHandler;

    protected final float radius = 1.0f;
    protected final float ftheta = 0.0f;
    protected final float phi = 0.0f;

    protected final float fovy = 45.0f;
    private float aspect;
    protected final float zNear = 0.1f;
    protected final float zFar = 3000.0f;

    private Color4 baseColor;

    public LiveShaderEditorGLEventListener(LiveShaderEditorInputHandler inputHandler) {
        this.inputHandler = inputHandler;
        this.loader = new ShaderProgramLoader();
        this.font = FontFactory.get(fontSet).getDefault();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        contextOn(drawable);

        canvasWidth = drawable.getWidth();
        canvasHeight = drawable.getHeight();

        final GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(0, 0, canvasWidth, canvasHeight);

        if (newFragmentShader) {
            setLiveFragmentShader(gl, liveShader, new File("shaders/vs_sunsurface.vp"), newShaderFileName);
            newFragmentShader = false;
        } else if (newVertexShader) {
            setLiveVertexShader(gl, liveShader, newShaderFileName, new File("shaders/fs_animatedTurbulence.fp"));
            newVertexShader = false;
        }

        displayContext();

        contextOff(drawable);
    }

    private synchronized void displayContext() {
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        final int width = GLContext.getCurrent().getGLDrawable().getWidth();
        final int height = GLContext.getCurrent().getGLDrawable().getHeight();
        this.aspect = (float) width / (float) height;

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        final Point4 eye = new Point4((float) (radius * Math.sin(ftheta) * Math.cos(phi)), (float) (radius
                * Math.sin(ftheta) * Math.sin(phi)), (float) (radius * Math.cos(ftheta)));
        final Point4 at = new Point4(0.0f, 0.0f, 0.0f);
        final Float4Vector up = new Float4Vector(0.0f, 1.0f, 0.0f, 0.0f);

        Float4Matrix mv = FloatMatrixMath.lookAt(eye, at, up);
        mv = mv.mul(FloatMatrixMath.translate(new Float3Vector(0f, 0f, inputHandler.getViewDist())));
        mv = mv.mul(FloatMatrixMath.rotationX(inputHandler.getRotation().getX()));
        mv = mv.mul(FloatMatrixMath.rotationY(inputHandler.getRotation().getY()));

        // Vertex shader variables

        try {
            renderScene(gl, new Float4Matrix(mv));
            renderHUDText(gl, new Float4Matrix(mv));

            renderTexturesToScreen(gl, width, height);
        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    private void renderAxes(GL3 gl, Float4Matrix mv) throws UninitializedException {
        axesFBO.bind(gl);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

        final Float4Matrix p = FloatMatrixMath.perspective(fovy, aspect, zNear, zFar);
        axesShader.setUniformMatrix("PMatrix", p);
        axesShader.setUniformMatrix("MVMatrix", mv);

        axesShader.setUniformVector("Color", new Float4Vector(1f, 0f, 0f, 1f));
        axesShader.use(gl);
        xAxis.draw(gl, axesShader);

        axesShader.setUniformVector("Color", new Float4Vector(0f, 1f, 0f, 1f));
        axesShader.use(gl);
        yAxis.draw(gl, axesShader);

        axesShader.setUniformVector("Color", new Float4Vector(0f, 0f, 1f, 1f));
        axesShader.use(gl);
        zAxis.draw(gl, axesShader);

        axesFBO.unBind(gl);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        contextOn(drawable);

        final GL3 gl = drawable.getGL().getGL3();

        noiseTex.delete(gl);
        starFBO.delete(gl);
        hudFBO.delete(gl);

        myText.delete(gl);

        FSQ_postprocess.delete(gl);
        testModel.delete(gl);

        contextOff(drawable);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        contextOn(drawable);

        canvasWidth = drawable.getWidth();
        canvasHeight = drawable.getHeight();

        final GL3 gl = drawable.getGL().getGL3();

        // Anti-Aliasing
        gl.glEnable(GL3.GL_LINE_SMOOTH);
        gl.glHint(GL3.GL_LINE_SMOOTH_HINT, GL3.GL_NICEST);
        gl.glEnable(GL3.GL_POLYGON_SMOOTH);
        gl.glHint(GL3.GL_POLYGON_SMOOTH_HINT, GL3.GL_NICEST);

        // Depth testing
        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glDepthFunc(GL3.GL_LEQUAL);
        gl.glClearDepth(1.0f);

        // Culling
        gl.glEnable(GL3.GL_CULL_FACE);
        gl.glCullFace(GL3.GL_BACK);

        // Enable Blending (needed for both Transparency and
        // Anti-Aliasing
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL3.GL_BLEND);

        // Enable Vertical Sync
        gl.setSwapInterval(1);

        // Set black background
        gl.glClearColor(0f, 0f, 0f, 0f);

        // enable setting of point sizes
        gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE);

        // Load and compile shaders, then use program.
        try {
            File file1 = new File("examples/liveshadereditor/shaders/vs_multiColorTextShader.vp");
            File file2 = new File("examples/liveshadereditor/shaders/fs_multiColorTextShader.fp");

            axesShader = loader.createProgram(gl, "axes", new File("examples/liveshadereditor/shaders/vs_axes.vp"),
                    new File("examples/liveshadereditor/shaders/fs_axes.fp"));

            textShader = loader.createProgram(gl, "multiColorText", file1, file2);

            postprocessShader = loader.createProgram(gl, "postprocess", new File(
                    "examples/liveshadereditor/shaders/vs_postprocess.vp"), new File(
                    "examples/liveshadereditor/shaders/fs_postprocess.fp"));

            setLiveFragmentShader(gl, liveShader, new File("examples/liveshadereditor/shaders/vs_sunsurface.vp"),
                    "examples/liveshadereditor/shaders/fs_animatedTurbulence.fp");

            // setLiveVertexShader(gl, liveShader, "shaders/vs_sunsurface.vp",
            // new File("shaders/fs_animatedTurbulence.fp"));
        } catch (final Exception e) {
            System.err.println("Error during shader creation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // AXES
        xAxis = new Axis(new Float3Vector(-1f, 0f, 0f), new Float3Vector(1f, 0f, 0f), .1f, .02f);
        xAxis.init(gl);
        yAxis = new Axis(new Float3Vector(0f, -1f, 0f), new Float3Vector(0f, 1f, 0f), .1f, .02f);
        yAxis.init(gl);
        zAxis = new Axis(new Float3Vector(0f, 0f, -1f), new Float3Vector(0f, 0f, 1f), .1f, .02f);
        zAxis.init(gl);

        // TEST MODEL
        testModel = new Sphere(5, true);
        testModel.init(gl);

        // TEXT
        myText = new MultiColorText(font);
        baseColor = Color4.GREEN;

        final String text = "";
        myText.setString(gl, text, Color4.WHITE, fontSize);
        myText.init(gl);

        // FULL SCREEN QUADS
        FSQ_postprocess = new Quad(2f, 2f, new Float3Vector(0, 0, 0.1f));
        FSQ_postprocess.init(gl);

        // TEXTURES
        noiseTex = new Perlin3D(GL.GL_TEXTURE0, 128, 128, 128);
        noiseTex.init(gl);

        // Full screen textures (for post processing) done with FrameBufferObject's
        starFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE1);
        hudFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE2);
        axesFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE3);

        starFBO.init(gl);
        hudFBO.init(gl);
        axesFBO.init(gl);

        contextOff(drawable);
    }

    private void renderHUDText(GL3 gl, Float4Matrix mv) throws UninitializedException {
        hudFBO.bind(gl);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

        // Draw
        myText.drawHudRelative(gl, textShader, canvasWidth, canvasHeight, 30f, 2f * canvasHeight - 40f);

        hudFBO.unBind(gl);
    }

    private void setText(GL3 gl, Color4 baseColor) {
        LiveShaderEditorInputHandler myInputHandler = inputHandler;
        final String text = myInputHandler.getScreenText();

        // Set text
        myText.setString(gl, text, baseColor, fontSize);

        // Add colors
        String selection = myInputHandler.getSelectedText();
        int selectionIndex = myInputHandler.getSelectedTextIndex();
        myText.setSubstringColors(gl, inputHandler.getSyntaxColors());
        myText.setSubstringAtIndexColor(gl, selectionIndex, selection, Color4.CYAN);
        myText.finalizeColorScheme(gl);
    }

    private void renderScene(GL3 gl, Float4Matrix mv) throws UninitializedException {
        starFBO.bind(gl);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        reCompile(gl);

        liveShader.setUniformVector("Color", Color4.RED);

        noiseTex.use(gl);
        liveShader.setUniform("Noise", noiseTex.getMultitexNumber());

        final Float4Matrix p = FloatMatrixMath.perspective(fovy, aspect, zNear, zFar);
        liveShader.setUniformMatrix("PMatrix", p);
        liveShader.setUniformMatrix("MVMatrix", mv);
        liveShader.setUniformMatrix("ScaleMatrix", FloatMatrixMath.scale(1f));
        liveShader.setUniformMatrix("SMatrix", FloatMatrixMath.scale(1f));
        liveShader.setUniform("Offset", offset);
        liveShader.setUniform("OffsetRandomValue", 1f);

        offset += .001f;

        liveShader.use(gl);
        testModel.draw(gl, liveShader);

        starFBO.unBind(gl);
    }

    private void renderTexturesToScreen(GL3 gl, int width, int height) {
        postprocessShader.setUniform("axesTexture", axesFBO.getTexture().getMultitexNumber());
        postprocessShader.setUniform("starTexture", starFBO.getTexture().getMultitexNumber());
        postprocessShader.setUniform("hudTexture", hudFBO.getTexture().getMultitexNumber());

        postprocessShader.setUniform("axesBrightness", 4f);
        postprocessShader.setUniform("starBrightness", 4f);
        postprocessShader.setUniform("hudBrightness", 4f);
        postprocessShader.setUniform("overallBrightness", 4f);

        postprocessShader.setUniformMatrix("PMatrix", new Float4Matrix());
        postprocessShader.setUniformMatrix("MVMatrix", new Float4Matrix());
        postprocessShader.setUniform("scrWidth", width);
        postprocessShader.setUniform("scrHeight", height);

        try {
            postprocessShader.use(gl);

            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            FSQ_postprocess.draw(gl, postprocessShader);
        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        contextOn(drawable);

        canvasWidth = drawable.getWidth();
        canvasHeight = drawable.getHeight();

        final GL3 gl = drawable.getGL().getGL3();

        starFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE1);
        hudFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE2);
        axesFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE3);

        starFBO.init(gl);
        hudFBO.init(gl);
        axesFBO.init(gl);

        contextOff(drawable);
    }

    private void setLiveFragmentShader(GL3 gl, ShaderProgram target, File vertexShaderFile,
            String fragmentShaderFileName) {
        vsFile = vertexShaderFile;
        fsFile = null;
        try {
            liveShader = loader.createProgram(gl, "live", vertexShaderFile, new File(fragmentShaderFileName));
            inputHandler.setText(new File(fragmentShaderFileName));
            liveShader.init(gl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CompilationFailedException e) {
            e.printStackTrace();
        }
    }

    private void setLiveVertexShader(GL3 gl, ShaderProgram target, String vertexShaderFileName, File fragmentShaderFile) {
        vsFile = null;
        fsFile = fragmentShaderFile;
        try {
            liveShader = loader.createProgram(gl, "live", new File(vertexShaderFileName), fragmentShaderFile);
            inputHandler.setText(new File(vertexShaderFileName));
            liveShader.init(gl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CompilationFailedException e) {
            e.printStackTrace();
        }
    }

    public void reCompile(GL3 gl) {

        if (inputHandler.isReCompileNeeded()) {
            ShaderProgram editedShader = liveShader;
            String newCompilerMessage = "";
            try {
                if (fsFile == null) {
                    editedShader = loader.createProgram(gl, "live", vsFile, inputHandler.getText());
                } else {
                    editedShader = loader.createProgram(gl, "live", inputHandler.getText(), fsFile);
                }
                editedShader.init(gl);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (CompilationFailedException e) {
                editedShader = liveShader;
                newCompilerMessage = e.getMessage();

                baseColor = Color4.YELLOW;
            }
            if (editedShader != liveShader) {
                newCompilerMessage = "New Shader compiled succesfully!";

                ShaderProgram temp = liveShader;
                liveShader = editedShader;

                liveShader.setUniformMatrix("NormalMatrix", new Float3Matrix());
                final Float4Matrix p = FloatMatrixMath.perspective(fovy, aspect, zNear, zFar);
                liveShader.setUniformMatrix("PMatrix", p);
                liveShader.setUniformMatrix("SMatrix", FloatMatrixMath.scale(1));
                temp.delete(gl);

                baseColor = Color4.GREEN;
            }
            if (newCompilerMessage.compareTo(compilerMessage) != 0) {
                System.out.println(newCompilerMessage);
                compilerMessage = newCompilerMessage;
            }

            inputHandler.setReCompileNeeded(false);
        }

        setText(gl, baseColor);
    }

    public static boolean isFragmentShader() {
        if (fsFile == null)
            return true;
        return false;
    }

    public void openFragmentShader(String fileName) {
        newShaderFileName = fileName;
        newFragmentShader = true;
    }

    public void openVertexShader(String fileName) {
        newShaderFileName = fileName;
        newVertexShader = true;
    }
}
