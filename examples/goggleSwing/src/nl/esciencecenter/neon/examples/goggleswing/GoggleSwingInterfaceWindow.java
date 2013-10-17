package nl.esciencecenter.neon.examples.goggleswing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.esciencecenter.neon.NeonInterfacePanel;
import nl.esciencecenter.neon.swing.GoggleSwing;
import nl.esciencecenter.neon.swing.GoggleSwing.ButtonBoxItem;
import nl.esciencecenter.neon.swing.GoggleSwing.CheckBoxItem;
import nl.esciencecenter.neon.swing.GoggleSwing.ColoredCheckBoxItem;
import nl.esciencecenter.neon.swing.GoggleSwing.DropdownBoxItem;
import nl.esciencecenter.neon.swing.GoggleSwing.DropdownBoxesBoxItem;
import nl.esciencecenter.neon.swing.GoggleSwing.RadioBoxItem;

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
 * Example {@link NeonInterfacePanel} implementation. Currently holds no more
 * than a logo, but could be used for all kinds of swing interface elements.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GoggleSwingInterfaceWindow extends JPanel {
    // A serialVersionUID is 'needed' because we extend JPanel.
    private static final long serialVersionUID = 1L;

    private final JPanel configPanel;

    /**
     * Basic constructor for NeonExampleInterfaceWindow.
     */
    public GoggleSwingInterfaceWindow() {
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

        configPanel = new JPanel();
        add(configPanel, BorderLayout.WEST);
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        configPanel.setPreferredSize(new Dimension(240, 0));
        configPanel.setMaximumSize(new Dimension(240, 1000));

        // Example implementation of a GUI box meant for a panel title.
        Box titleBox = GoggleSwing.titleBox("Title", new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                // TODO Auto-generated method stub
            }
        });
        configPanel.add(titleBox);

        // Example implementation of a GUI box with multiple buttons in it.
        Box buttonBox = GoggleSwing.buttonBox("ButtonBox", new ButtonBoxItem[] {
                new ButtonBoxItem("First Button", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        // TODO Auto-generated method stub

                    }
                }), new ButtonBoxItem("Second Button", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        // TODO Auto-generated method stub

                    }
                }) });
        configPanel.add(buttonBox);

        // Example implementation of a GUI box with colored checkboxes (for
        // example for a legend)
        Box coloredCheckBoxBox = GoggleSwing.coloredCheckBoxBox("ColoredCheckBoxBox", new ColoredCheckBoxItem[] {
                new ColoredCheckBoxItem("First Item", Color.BLUE, true, new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        // TODO Auto-generated method stub

                    }
                }), new ColoredCheckBoxItem("Second Item", Color.RED, false, new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        // TODO Auto-generated method stub

                    }
                }) });
        configPanel.add(coloredCheckBoxBox);

        // Example implementation of a GUI box with horizontally spaced
        // DropdownBoxes.
        Box horizontalDropdownBoxesBox = GoggleSwing.horizontalDropdownBoxesBox("HorizontalDropdownBoxesBox",
                new DropdownBoxesBoxItem[] {
                        new DropdownBoxesBoxItem("First dropdownBox", "First Item", new DropdownBoxItem[] {
                                new DropdownBoxItem("First Item", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // TODO Auto-generated method stub

                                    }
                                }), new DropdownBoxItem("Second Item", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // TODO Auto-generated method stub

                                    }
                                }) }),
                        new DropdownBoxesBoxItem("Second dropdownBox", "Second Item", new DropdownBoxItem[] {
                                new DropdownBoxItem("First Item", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // TODO Auto-generated method stub

                                    }
                                }), new DropdownBoxItem("Second Item", new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        // TODO Auto-generated method stub

                                    }
                                }) }) });
        horizontalDropdownBoxesBox.setPreferredSize(new Dimension(240, 0));
        horizontalDropdownBoxesBox.setMaximumSize(new Dimension(240, 50));
        configPanel.add(horizontalDropdownBoxesBox);

        // Example implementation of a GUI box with a slider.
        float MINIMUM = 0f, MAXIMUM = 100f, SPACING = 10f, DEFAULT = 10f;
        Box sliderBox = GoggleSwing.sliderBox("SliderBox", new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                // TODO Auto-generated method stub

            }
        }, MINIMUM, MAXIMUM, SPACING, DEFAULT, new JLabel("" + DEFAULT));
        configPanel.add(sliderBox);

        // Example implementation of a GUI box with checkboxes
        Box checkboxBox = GoggleSwing.checkboxBox("ColoredCheckBoxBox", new CheckBoxItem[] {
                new CheckBoxItem("First Item", true, new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        // TODO Auto-generated method stub

                    }
                }), new CheckBoxItem("Second Item", false, new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        // TODO Auto-generated method stub

                    }
                }) });
        configPanel.add(checkboxBox);

        // Example implementation of a GUI box with radio buttons
        Box radioBox = GoggleSwing.radioBox("RadioBox", "Second Item", new RadioBoxItem[] {
                new RadioBoxItem("First Item", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                    }
                }), new RadioBoxItem("Second Item", new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        // TODO Auto-generated method stub
                    }
                }) });
        configPanel.add(radioBox);

        configPanel.setVisible(true);

        setVisible(true);
    }
}
