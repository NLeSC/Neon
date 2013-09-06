package nl.esciencecenter.esight.examples.shadertest;

import javax.swing.JFrame;

import nl.esciencecenter.esight.ESightNewtWindow;
import nl.esciencecenter.esight.util.Settings;

public class LiveShaderEditor {
    // This static definition (singleton) of a settings class ensures that these
    // global settings are useable throughout the application. This settings
    // class reads from a file named "settings.properties" to allow default
    // settings to be changed by the user.
    private final static Settings settings = Settings.getInstance();

    // This is an implementation of a 'loose' interface window. This is done to
    // allow java swing components as user interface elements. It is in a
    // seperate window because of MacOSX limitations in the JOGL library.
    private static LiveShaderEditorInterfacePanel examplePanel;

    // An example implementation of a GLEventlistener. These listeners are the
    // backbone of any OpenGL application, and provide the display cycle needed
    // for continuous updates of the screen.
    private static LiveShaderEditorGLEventListener exampleGLEventListener;

    // In this simple example, we only call the constructor. This is enough to
    // keep the program running, since the constructor will create new threads
    // for animation etc. Therefore, even though we do not use the new Object,
    // the program will not terminate.
    public static void main(String[] args) {
        new LiveShaderEditor();
    }

    public LiveShaderEditor() {
        String cmdlnfileName = null;
        String path = "";

        path = System.getProperty("user.dir");

        exampleGLEventListener = new LiveShaderEditorGLEventListener(LiveShaderEditorInputHandler.getInstance());
        examplePanel = new LiveShaderEditorInterfacePanel(exampleGLEventListener, path, cmdlnfileName);
        // }
        //
        // public void makeWindow() {
        new ESightNewtWindow(true, LiveShaderEditorInputHandler.getInstance(), exampleGLEventListener, 1920, 1080,
                "Live Shader Editor");

        // Create the frame
        final JFrame frame = new JFrame("- LSE -");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent arg0) {
                System.exit(0);
            }
        });

        frame.setSize(100, 100);

        frame.setResizable(false);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame.getContentPane().add(examplePanel);
                } catch (final Exception e) {
                    e.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        });

        frame.setVisible(true);
    }
}
