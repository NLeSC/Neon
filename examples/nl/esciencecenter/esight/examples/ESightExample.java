package nl.esciencecenter.esight.examples;

import javax.swing.JFrame;

import nl.esciencecenter.esight.ESightNewtWindow;

public class ESightExample {
    private final static ESightExampleSettings settings = ESightExampleSettings
            .getInstance();

    private static ESightExampleInterfaceWindow examplePanel;
    private static ESightExampleGLEventListener exampleWindow;

    public ESightExample() {
        // Create the Swing interface elements
        examplePanel = new ESightExampleInterfaceWindow();

        // Create the GLEventListener
        exampleWindow = new ESightExampleGLEventListener(
                ESightExampleInputHandler.getInstance());

        new ESightNewtWindow(true, exampleWindow.getInputHandler(),
                exampleWindow, settings.getDefaultScreenWidth(),
                settings.getDefaultScreenHeight(),
                "ESightExample - example Visualization Tool");

        // Create the frame
        final JFrame frame = new JFrame("- * -");
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent arg0) {
                System.exit(0);
            }
        });

        frame.setSize(settings.getInterfaceWidth(),
                settings.getInterfaceHeight());

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

    public static void main(String[] args) {
        ESightExample lib = new ESightExample();
    }
}
