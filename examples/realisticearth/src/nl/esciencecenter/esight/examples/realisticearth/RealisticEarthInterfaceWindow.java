package nl.esciencecenter.esight.examples.realisticearth;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import nl.esciencecenter.esight.ESightInterfacePanel;
import nl.esciencecenter.esight.swing.GoggleSwing;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Example {@link ESightInterfacePanel} implementation. Currently holds no more
 * than a logo, but could be used for all kinds of swing interface elements.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class RealisticEarthInterfaceWindow extends JPanel {
    private final static RealisticEarthSettings settings = RealisticEarthSettings.getInstance();

    // A serialVersionUID is 'needed' because we extend JPanel.
    private static final long serialVersionUID = 1L;

    /**
     * Basic constructor for ESightExampleInterfaceWindow.
     */
    public RealisticEarthInterfaceWindow() {
        // Set the swing layout type for this JPanel.
        setLayout(new BorderLayout(0, 0));

        // Make a menu bar
        final JMenuBar menuBar = new JMenuBar();

        // Create a swing-enabled image.
        ImageIcon nlescIcon = GoggleSwing.createResizedImageIcon("images/ESCIENCE_logo.jpg", "eScienceCenter Logo",
                200, 20);
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
