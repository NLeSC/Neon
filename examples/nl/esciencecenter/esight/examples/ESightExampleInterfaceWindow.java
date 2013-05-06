package nl.esciencecenter.esight.examples;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import nl.esciencecenter.esight.swing.GoggleSwing;

public class ESightExampleInterfaceWindow extends JPanel {
    // A serialVersionUID is 'needed' because we extend JPanel.
    private static final long serialVersionUID = 1L;

    /**
     * Basic constructor for ESightExampleInterfaceWindow.
     */
    public ESightExampleInterfaceWindow() {
        // Set the swing layout type for this JPanel.
        setLayout(new BorderLayout(0, 0));

        // Make a menu bar
        final JMenuBar menuBar = new JMenuBar();

        // Create a swing-enabled image.
        ImageIcon nlescIcon = GoggleSwing.createResizedImageIcon(
                "images/ESCIENCE_logo.jpg", "eScienceCenter Logo", 200, 20);
        JLabel nlesclogo = new JLabel(nlescIcon);

        // Add the image to the menu bar, but create some glue around it so it
        // doesn't automatically resize the window to minimum size.
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(nlesclogo);
        menuBar.add(Box.createHorizontalGlue());

        // Add the menubar to a container, so we can apply a boxlayout. (not
        // strictly necessary, but a demonstration of what swing could do)
        Container menuContainer = new Container();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));
        menuContainer.add(menuBar);

        // Add the menu container to the JPanel
        add(menuContainer, BorderLayout.NORTH);
        setVisible(true);
    }
}
