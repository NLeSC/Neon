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
    public static enum TweakState {
        NONE, DATA, VISUAL, MOVIE
    }

    private final ESightExampleSettings settings = ESightExampleSettings
            .getInstance();

    private static final long serialVersionUID = 1L;

    public ESightExampleInterfaceWindow() {
        setLayout(new BorderLayout(0, 0));

        final JMenuBar menuBar = new JMenuBar();

        ImageIcon nlescIcon = GoggleSwing.createResizedImageIcon(
                "images/ESCIENCE_logo.jpg", "eScienceCenter Logo", 200, 20);
        JLabel nlesclogo = new JLabel(nlescIcon);

        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(nlesclogo);
        menuBar.add(Box.createHorizontalGlue());

        Container menuContainer = new Container();
        menuContainer.setLayout(new BoxLayout(menuContainer, BoxLayout.Y_AXIS));

        menuContainer.add(menuBar);

        add(menuContainer, BorderLayout.NORTH);

        setVisible(true);
    }
}
