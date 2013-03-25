package nl.esciencecenter.esight;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * @author maarten Common (extendible) class for a separate Interface Panel.
 * 
 */
public abstract class ESightInterfacePanel extends JPanel {
    private static final long serialVersionUID = -4937089344123608040L;

    /**
     * Creates a new default JPanel with a BorderLayout.
     */
    public ESightInterfacePanel() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        setLayout(new BorderLayout(0, 0));

        setVisible(true);
    }
}
