package nl.esciencecenter.esight.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for various static Swing components, for use in GUIs.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GoggleSwing {
    private final static Logger logger = LoggerFactory
                                               .getLogger(GoggleSwing.class);

    /**
     * A single item for a ButtonBox, to be used in conjuction with
     * GoggleSwing.buttonBox()
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     */
    public static class ButtonBoxItem {
        public String         label;
        public ActionListener listener;

        public ButtonBoxItem(String label,
                ActionListener listener) {
            this.label = label;
            this.listener = listener;
        }
    }

    /**
     * A single item for a RadioBox, to be used in conjuction with
     * GoggleSwing.radioBox()
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     */
    public static class RadioBoxItem {
        public String         label;
        public ActionListener listener;

        public RadioBoxItem(String label,
                ActionListener listener) {
            this.label = label;
            this.listener = listener;
        }
    }

    /**
     * A single item for a CheckBox, to be used in conjuction with
     * GoggleSwing.checkboxBox()
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     */
    public static class CheckBoxItem {
        public String       label;

        public boolean      selection;
        public ItemListener listener;

        public CheckBoxItem(String label, boolean selection,
                ItemListener listener) {
            this.label = label;
            this.selection = selection;
            this.listener = listener;
        }
    }

    /**
     * A single item for a LegendBox, to be used in conjuction with
     * GoggleSwing.legendBox()
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     */
    public static class ColoredCheckBoxItem {
        public String       label;
        public Color        color;
        public boolean      selection;
        public ItemListener listener;

        public ColoredCheckBoxItem(String label, Color color, boolean selection,
                ItemListener listener) {
            this.label = label;
            this.color = color;
            this.selection = selection;
            this.listener = listener;
        }
    }

    /**
     * A single item for a DropdownBox, to be used in conjuction with
     * GoggleSwing.dropdownBox()
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     */
    public static class DropdownBoxItem {
        public String         label;
        public ActionListener listener;

        public DropdownBoxItem(String itemLabel, ActionListener listener) {
            this.label = itemLabel;
            this.listener = listener;
        }
    }

    /**
     * A single item for a DropdownBox, to be used in conjuction with
     * GoggleSwing.dropdownBox()
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     */
    public static class DropdownBoxesBoxItem {
        public String            label;
        public String            selectedOption;
        public DropdownBoxItem[] items;

        public DropdownBoxesBoxItem(String boxLabel, String selectedOption, DropdownBoxItem... items) {
            this.label = boxLabel;
            this.selectedOption = selectedOption;
            this.items = items;
        }
    }

    /**
     * Creates a boxed list of buttons.
     * 
     * @param name
     *            The name (and label) for this box.
     * @param items
     *            One or more {@link ButtonBoxItem}s to put into this box.
     * @return the {@link Box} containing the checkboxes.
     */
    public static Box buttonBox(String name, ButtonBoxItem... items) {
        final ArrayList<Component> vcomponents = new ArrayList<Component>();

        vcomponents.add(new JLabel(name));
        vcomponents.add(Box.createHorizontalGlue());

        for (final ButtonBoxItem item : items) {
            final ArrayList<Component> hcomponents = new ArrayList<Component>();
            hcomponents.add(Box.createHorizontalGlue());

            final JButton btn = new JButton(item.label);
            btn.addActionListener(item.listener);
            btn.setPreferredSize(new Dimension(150, 10));
            hcomponents.add(btn);

            hcomponents.add(Box.createHorizontalGlue());
            vcomponents.add(GoggleSwing.hBoxedComponents(hcomponents));
        }

        vcomponents.add(GoggleSwing.verticalStrut(5));

        return GoggleSwing.vBoxedComponents(vcomponents, true);
    }

    /**
     * Creates a boxed list of checkboxes.
     * 
     * @param name
     *            The name (and label) for this box.
     * @param items
     *            One or more {@link CheckBoxItem}s to put into this box.
     * @return the {@link Box} containing the checkboxes.
     */
    public static Box checkboxBox(String name, CheckBoxItem... items) {
        final ArrayList<Component> vcomponents = new ArrayList<Component>();
        vcomponents.add(new JLabel(name));
        vcomponents.add(Box.createHorizontalGlue());

        for (final CheckBoxItem item : items) {
            final ArrayList<Component> hcomponents = new ArrayList<Component>();

            final JCheckBox btn = new JCheckBox();
            btn.setSelected(item.selection);
            btn.addItemListener(item.listener);
            hcomponents.add(btn);

            final JLabel label = new JLabel(item.label);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            hcomponents.add(label);

            vcomponents.add(GoggleSwing.hBoxedComponents(hcomponents));
        }
        return GoggleSwing.vBoxedComponents(vcomponents, true);
    }

    /**
     * Returns a JButton with an image, warns if the path was invalid.
     * 
     * @param path
     *            The path to the image.
     * @param fileName
     *            The filename of the image.
     * @param buttonText
     *            The alt-text for this button.
     * @return the newly created button with image.
     */
    public static JButton createImageButton(String path, String fileName,
            String buttonText) {
        final ImageIcon icon = GoggleSwing.createImageIcon(path, fileName);
        if (icon == null) {
            logger.warn("Icon for " + buttonText + ", with file name: " + path + fileName + ", could not be found.");
        }
        final JButton result = new JButton(buttonText, icon);
        result.setHorizontalAlignment(SwingConstants.CENTER);
        result.setMargin(new Insets(2, 2, 2, 2));
        result.setVerticalTextPosition(SwingConstants.CENTER);
        result.setHorizontalTextPosition(SwingConstants.TRAILING);
        result.setToolTipText(fileName);
        result.setFocusPainted(false);
        return result;
    }

    /**
     * Returns an {@link ImageIcon} that is resized according to the
     * specifications.
     * 
     * @param path
     *            The path to the image.
     * @param fileName
     *            The filename of the image.
     * @param width
     *            The new width for this {@link ImageIcon}
     * @param height
     *            The new height for this {@link ImageIcon}
     * @return The newly resized {@link ImageIcon}
     */
    public static ImageIcon createResizedImageIcon(String path,
            String fileName, int width, int height) {
        ImageIcon icon = createImageIcon(path, fileName);
        Image img = icon.getImage();

        Image newimg = img.getScaledInstance(width, height,
                java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);

        return newIcon;
    }

    /**
     * Returns an {@link ImageIcon}, or null if the path was invalid.
     * 
     * @param path
     *            The path to the image.
     * @param fileName
     *            The filename of the image.
     * @return The newly created {@link ImageIcon}, read from disk.
     */
    public static ImageIcon createImageIcon(String path, String fileName) {
        URL imgURL = null;

        if (path != null) {
            imgURL = ClassLoader.getSystemClassLoader().getResource(path);
        }

        if (imgURL != null) {
            return new ImageIcon(imgURL, fileName);
        } else {
            return null;
        }
    }

    /**
     * Creates a Horizontally boxed list of the given components.
     * 
     * @param components
     *            The components to be boxed.
     * @return the boxed components.
     */
    public static Box hBoxedComponents(ArrayList<Component> components) {
        final Box hrzOuterBox = Box.createHorizontalBox();
        hrzOuterBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        hrzOuterBox.add(GoggleSwing.horizontalStrut(2));

        final Box hrzInnerBox = Box.createHorizontalBox();
        hrzInnerBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (int i = 0; i < components.size(); i++) {
            final Component current = components.get(i);
            hrzInnerBox.add(current);
        }
        hrzOuterBox.add(hrzInnerBox);

        hrzOuterBox.add(GoggleSwing.horizontalStrut(2));

        return hrzOuterBox;
    }

    /**
     * Create a horizontal strut (Rigid area).
     * 
     * @param size
     *            The size of the strut.
     * @return The strut.
     */
    public static Component horizontalStrut(int size) {
        final Component verticalStrut = Box.createRigidArea(new Dimension(size,
                0));
        return verticalStrut;
    }

    /**
     * Creates a boxed list of colored checkboxes.
     * 
     * @param name
     *            The name (and label) for this box.
     * @param items
     *            One or more {@link ColoredCheckBoxItem}s to put into this box.
     * @return the {@link Box} containing the checkboxes.
     */
    public static Box coloredCheckBoxBox(String name, ColoredCheckBoxItem... items) {
        final ArrayList<Component> vcomponents = new ArrayList<Component>();
        vcomponents.add(new JLabel(name));
        vcomponents.add(Box.createHorizontalGlue());

        for (ColoredCheckBoxItem item : items) {
            final ArrayList<Component> hcomponents = new ArrayList<Component>();

            final JCheckBox btn = new JCheckBox();
            btn.setSelected(item.selection);
            btn.addItemListener(item.listener);
            hcomponents.add(btn);

            final JCheckBox icon = new JCheckBox(new ColorIcon(10, 10, item.color));
            hcomponents.add(icon);

            final JLabel label = new JLabel(item.label);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            hcomponents.add(label);

            vcomponents.add(GoggleSwing.hBoxedComponents(hcomponents));
        }
        return GoggleSwing.vBoxedComponents(vcomponents, true);
    }

    /**
     * Creates a boxed radio list.
     * 
     * @param name
     *            The name (and label) for this box.
     * @param selectedOption
     *            The label that is initially selected.
     * @param items
     *            One or more {@link DropdownBoxItem}s to put into this box.
     * @return the {@link Box} containing the checkboxes.
     */
    public static Box radioBox(String name, String selectedOption, RadioBoxItem... items) {
        final ArrayList<Component> vcomponents = new ArrayList<Component>();
        final ButtonGroup group = new ButtonGroup();

        vcomponents.add(new JLabel(name));
        vcomponents.add(Box.createHorizontalGlue());

        for (RadioBoxItem item : items) {
            final JRadioButton btn = new JRadioButton(item.label);
            group.add(btn);
            if (item.label.compareTo(selectedOption) == 0) {
                btn.setSelected(true);
            }
            btn.addActionListener(item.listener);
            vcomponents.add(btn);
        }
        return GoggleSwing.vBoxedComponents(vcomponents, true);
    }

    /**
     * Creates a horizontally boxed dropdownboxes list.
     * 
     * @param name
     *            The name (and label) for this box.
     * @param boxItems
     *            One or more {@link DropdownBoxesBoxItem}s to put into this
     *            box.
     * @return the {@link Box} containing the horizontally boxed dropdownboxes.
     */
    public static Box horizontalDropdownBoxesBox(String name, DropdownBoxesBoxItem... boxItems) {
        final ArrayList<Component> vcomponents = new ArrayList<Component>();
        vcomponents.add(new JLabel(name));
        vcomponents.add(Box.createHorizontalGlue());

        final ArrayList<Component> hcomponents = new ArrayList<Component>();

        for (DropdownBoxesBoxItem boxItem : boxItems) {
            JComboBox comboBox = new JComboBox();
            int index = 0;
            for (DropdownBoxItem optionItem : boxItem.items) {
                comboBox.addItem(optionItem.label);
                comboBox.addActionListener(optionItem.listener);

                if (optionItem.label.compareTo(boxItem.selectedOption) == 0) {
                    comboBox.setSelectedIndex(index);
                }

                index++;
            }

            hcomponents.add(comboBox);
        }
        vcomponents.add(hBoxedComponents(hcomponents));

        return GoggleSwing.vBoxedComponents(vcomponents, true);
    }

    /**
     * A box containing a single horizontal slider with Integer values.
     * 
     * @param label
     *            The name and label of this box.
     * @param listener
     *            The listener to be associated with the slider.
     * @param min
     *            The minimum value on the slider.
     * @param max
     *            The maximum value on the slider.
     * @param spacing
     *            The spacing of the ticks on the slider.
     * @param norm
     *            The initial setting for the slider.
     * @param dynamicLabel
     *            The {@link JLabel} to set when the slider value changes.
     * @return the {@link Box} containing the slider.
     */
    public static Box sliderBox(String label, ChangeListener listener, int min,
            int max, int spacing, int norm, JLabel dynamicLabel) {
        final ArrayList<Component> components = new ArrayList<Component>();
        final JLabel thresholdlabel = new JLabel(label);
        components.add(thresholdlabel);
        components.add(Box.createHorizontalGlue());

        final JSlider slider = new JSlider();
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setMinorTickSpacing(spacing);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setValue(norm);
        slider.addChangeListener(listener);
        components.add(slider);

        components.add(dynamicLabel);
        return GoggleSwing.vBoxedComponents(components, true);
    }

    /**
     * A box containing a single horizontal slider with Float values.
     * 
     * @param label
     *            The name and label of this box.
     * @param listener
     *            The listener to be associated with the slider.
     * @param fmin
     *            The minimum value on the slider.
     * @param fmax
     *            The maximum value on the slider.
     * @param fspacing
     *            The spacing of the ticks on the slider.
     * @param fnorm
     *            The initial setting for the slider.
     * @param dynamicLabel
     *            The {@link JLabel} to set when the slider value changes.
     * @return the {@link Box} containing the slider.
     */
    public static Box sliderBox(String label, ChangeListener listener,
            float fmin, float fmax, float fspacing, float fnorm,
            JLabel dynamicLabel) {
        int min = 0;
        int max = (int) ((fmax - fmin) / fspacing);
        if (fmin < 0 && fmax > 0) {
            max += 1;
        }
        int spacing = 1;
        int norm = (int) ((fnorm - fmin) / fspacing);

        final ArrayList<Component> components = new ArrayList<Component>();
        final JLabel thresholdlabel = new JLabel(label);
        components.add(thresholdlabel);
        components.add(Box.createHorizontalGlue());

        final JSlider slider = new JSlider();
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setMinorTickSpacing(spacing);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setValue(norm);
        slider.addChangeListener(listener);
        components.add(slider);

        components.add(dynamicLabel);
        return GoggleSwing.vBoxedComponents(components, true);
    }

    /**
     * A box used for titles of option panels. Also contains a close button.
     * 
     * @param label
     *            The text to be displayed as title.
     * @param listener
     *            the Itemlistener for the close button.
     * @return The title {@link Box}.
     */
    public static Box titleBox(String label, ItemListener listener) {
        final ArrayList<Component> vcomponents = new ArrayList<Component>();

        vcomponents.add(GoggleSwing.verticalStrut(5));
        final Box hrzOuterBox = Box.createHorizontalBox();
        hrzOuterBox.add(GoggleSwing.horizontalStrut(5));

        final Box hrzInnerBox = Box.createHorizontalBox();
        final JLabel panelTitle = new JLabel(label);
        hrzInnerBox.add(panelTitle);

        hrzInnerBox.add(Box.createHorizontalGlue());

        final Icon exitIcon = new ColorIcon(10, 10, new Color(0, 0, 0));
        final JCheckBox exit = new JCheckBox(exitIcon);
        exit.addItemListener(listener);
        hrzInnerBox.add(exit);
        hrzOuterBox.add(hrzInnerBox);

        hrzOuterBox.add(GoggleSwing.horizontalStrut(5));
        vcomponents.add(hrzOuterBox);

        vcomponents.add(GoggleSwing.verticalStrut(5));

        return GoggleSwing.vBoxedComponents(vcomponents, false);
    }

    /**
     * A helper method to vertically {@link Box} several components.
     * 
     * @param components
     *            the components to be boxed.
     * @param bordered
     *            option to have a bevel border yes/no.
     * @return the vertically boxed components.
     */
    public static Box vBoxedComponents(ArrayList<Component> components,
            boolean bordered) {
        final Box hrzBox = Box.createHorizontalBox();
        hrzBox.add(GoggleSwing.horizontalStrut(2));

        final Box vrtBox = Box.createVerticalBox();
        if (bordered) {
            vrtBox.setBorder(new BevelBorder(BevelBorder.RAISED));
        }

        vrtBox.add(GoggleSwing.verticalStrut(1));

        for (Component current : components) {
            vrtBox.add(current);
        }
        hrzBox.add(vrtBox);

        hrzBox.add(GoggleSwing.horizontalStrut(2));
        return hrzBox;
    }

    /**
     * A helper method to horizontally {@link Box} several components.
     * 
     * @param components
     *            the components to be boxed.
     * @param bordered
     *            option to have a bevel border yes/no.
     * @return the horizontally boxed components.
     */
    public static Box hBoxedComponents(ArrayList<Component> components,
            boolean bordered) {
        final Box vrtBox = Box.createVerticalBox();
        vrtBox.add(GoggleSwing.verticalStrut(2));

        final Box hrzBox = Box.createHorizontalBox();
        if (bordered) {
            hrzBox.setBorder(new BevelBorder(BevelBorder.RAISED));
        }

        hrzBox.add(GoggleSwing.horizontalStrut(5));

        for (Component current : components) {
            hrzBox.add(current);
        }
        vrtBox.add(hrzBox);

        vrtBox.add(GoggleSwing.verticalStrut(2));
        return vrtBox;
    }

    /**
     * Create a vertical strut (Rigid area).
     * 
     * @param size
     *            The size of the strut.
     * @return The strut.
     */
    public static Component verticalStrut(int size) {
        final Component verticalStrut = Box.createRigidArea(new Dimension(0,
                size));
        return verticalStrut;
    }
}
