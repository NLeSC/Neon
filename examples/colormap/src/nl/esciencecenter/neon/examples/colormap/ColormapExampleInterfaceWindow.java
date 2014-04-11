package nl.esciencecenter.neon.examples.colormap;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.esciencecenter.neon.NeonInterfacePanel;
import nl.esciencecenter.neon.swing.ColormapInterpreter;
import nl.esciencecenter.neon.swing.GoggleSwing;
import nl.esciencecenter.neon.swing.RangeSlider;
import nl.esciencecenter.neon.swing.RangeSliderUI;
import nl.esciencecenter.neon.swing.SimpleImageIcon;

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
public class ColormapExampleInterfaceWindow extends JPanel {
    private static final int INTENTIONALLY_TOO_LARGE = 1000;
    private static final int DEFAULT_HEIGHT = 25;
    private static final int DEFAULT_WIDTH = 240;

    private final static ColormapExampleSettings settings = ColormapExampleSettings.getInstance();

    // A serialVersionUID is 'needed' because we extend JPanel.
    private static final long serialVersionUID = 1L;

    private final JPanel configPanel;
    private final JPanel dataConfig;

    /**
     * Basic constructor for NeonExampleInterfaceWindow.
     */
    public ColormapExampleInterfaceWindow() {
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
        configPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, 0));
        configPanel.setMaximumSize(new Dimension(DEFAULT_WIDTH, INTENTIONALLY_TOO_LARGE));
        configPanel.setVisible(false);

        dataConfig = new JPanel();
        dataConfig.setLayout(new BoxLayout(dataConfig, BoxLayout.Y_AXIS));
        dataConfig.setMinimumSize(new Dimension(DEFAULT_WIDTH, 0));
        dataConfig.setMaximumSize(new Dimension(DEFAULT_WIDTH, INTENTIONALLY_TOO_LARGE));
        createColormapselector();

        configPanel.setVisible(true);
        configPanel.add(dataConfig, BorderLayout.WEST);

        setVisible(true);
    }

    private void createColormapselector() {
        final ArrayList<Component> screenVcomponents = new ArrayList<Component>();

        JLabel label = new JLabel("Colormap selection");
        screenVcomponents.add(label);

        final ArrayList<Component> screenHcomponents = new ArrayList<Component>();

        JComboBox<String> dataModeComboBox = new JComboBox<String>(settings.getDataModes());
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e != null && e.getSource() instanceof JComboBox
                        && e.getSource().getClass().getComponentType() == String.class) {
                    @SuppressWarnings("unchecked")
                    JComboBox<String> cb = (JComboBox<String>) e.getSource();

                    settings.selectDataMode(cb.getSelectedIndex());
                }
            }
        };
        dataModeComboBox.addActionListener(al);
        dataModeComboBox.setSelectedIndex(settings.getSelectedDataModeIndex());
        dataModeComboBox.setMinimumSize(new Dimension(50, DEFAULT_HEIGHT));
        dataModeComboBox.setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        screenHcomponents.add(dataModeComboBox);

        screenHcomponents.add(Box.createHorizontalGlue());

        final JComboBox<String> variablesComboBox = new JComboBox<String>(settings.getVariables());
        variablesComboBox.setSelectedItem(settings.getSelectedVariableIndex());
        variablesComboBox.setMinimumSize(new Dimension(50, DEFAULT_HEIGHT));
        variablesComboBox.setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        screenHcomponents.add(variablesComboBox);

        screenVcomponents.add(GoggleSwing.hBoxedComponents(screenHcomponents, true));

        final JComboBox<SimpleImageIcon> colorMapsComboBox = ColormapInterpreter.getLegendJComboBox(new Dimension(
                DEFAULT_WIDTH, DEFAULT_HEIGHT));
        colorMapsComboBox.setSelectedItem(colorMapsComboBox.getItemAt(ColormapInterpreter.getIndexOfColormap(settings
                .getSelectedColormapName())));
        colorMapsComboBox.setMinimumSize(new Dimension(100, DEFAULT_HEIGHT));
        colorMapsComboBox.setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        screenVcomponents.add(colorMapsComboBox);

        final RangeSlider selectionLegendSlider = new RangeSlider();
        ((RangeSliderUI) selectionLegendSlider.getUI()).setRangeColorMap(settings.getSelectedColormapName());
        selectionLegendSlider.setMinimum(0);
        selectionLegendSlider.setMaximum(100);
        selectionLegendSlider.setValue(settings.getRangeSliderLowerValue());
        selectionLegendSlider.setUpperValue(settings.getRangeSliderUpperValue());

        colorMapsComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                settings.setSelectedColormapIndex(colorMapsComboBox.getSelectedIndex());

                ((RangeSliderUI) selectionLegendSlider.getUI()).setRangeColorMap(settings.getSelectedColormapName());
                selectionLegendSlider.invalidate();
            }
        });

        selectionLegendSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();

                String var = settings.getSelectedVariableName();
                settings.setVariableLowerBound(var, slider.getValue());
                settings.setVariableUpperBound(var, slider.getUpperValue());
            }
        });

        variablesComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int var = ((JComboBox) e.getSource()).getSelectedIndex();

                settings.selectVariable(var);
                selectionLegendSlider.setValue(settings.getRangeSliderLowerValue());
                selectionLegendSlider.setUpperValue(settings.getRangeSliderUpperValue());
            }
        });

        screenVcomponents.add(selectionLegendSlider);

        dataConfig.add(GoggleSwing.vBoxedComponents(screenVcomponents, true));

        dataConfig.add(Box.createVerticalGlue());
    }
}
