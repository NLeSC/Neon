package nl.esciencecenter.esight.examples.shadertest;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import nl.esciencecenter.esight.ESightInterfacePanel;

public class LiveShaderEditorInterfacePanel extends ESightInterfacePanel {
    private static final long serialVersionUID = 1L;
    private final LiveShaderEditorGLEventListener shaderWindow;

    public LiveShaderEditorInterfacePanel(LiveShaderEditorGLEventListener shaderWindow, String path,
            String cmdlnfileName) {
        super();

        this.shaderWindow = shaderWindow;

        // setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
        // Collections.EMPTY_SET);
        // setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
        // Collections.EMPTY_SET);

        // Make the menu bar
        final JMenuBar menuBar = new JMenuBar();
        final JMenu file = new JMenu("File");
        final JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                final File file = openFile();
                handleFile(file);
            }
        });
        file.add(open);
        menuBar.add(file);
        add(menuBar, BorderLayout.NORTH);
    }

    private File openFile() {
        final JFileChooser fileChooser = new JFileChooser(
                System.getProperty("user.dir"));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        final int result = fileChooser.showOpenDialog(this);

        // user clicked Cancel button on dialog
        if (result == JFileChooser.CANCEL_OPTION) {
            return null;
        } else {
            return fileChooser.getSelectedFile();
        }
    }

    protected void handleFile(File file) {
        if (file != null) {
            final String path = file.getPath().substring(0,
                    file.getPath().length() - file.getName().length());

            final String name = file.getName();
            final String fullPath = path + name;
            final String[] ext = fullPath.split("[.]");
            String prefix = null;
            if (ext[1].compareTo("fp") == 0 || ext[1].compareTo("vp") == 0) {
                if (ext[1].compareTo("fp") == 0) {
                    shaderWindow.openFragmentShader(file.getAbsolutePath());
                }
                if (ext[1].compareTo("vp") == 0) {
                    shaderWindow.openVertexShader(file.getAbsolutePath());
                }
            } else {
                final JOptionPane pane = new JOptionPane();
                pane.setMessage("Tried to open invalid file type.");
                final JDialog dialog = pane.createDialog("Alert");
                dialog.setVisible(true);
            }
        }
    }
}
