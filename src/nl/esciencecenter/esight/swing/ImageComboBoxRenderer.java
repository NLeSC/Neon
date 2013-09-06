package nl.esciencecenter.esight.swing;

/* From http://java.sun.com/docs/books/tutorial/index.html */

/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Modifier version of the ListCellRenderer that allows for an image to be used.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ImageComboBoxRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = -6243517743093061609L;

    private Font descriptionFont;

    private final String[] descriptions;
    private final ImageIcon[] images;

    /**
     * Default constructor, sets alignment.
     * 
     * @param descriptions
     *            The descriptions of the list elements. These are only used if
     *            images were not found.
     * @param images
     *            The images to use for the list elements.
     */
    public ImageComboBoxRenderer(String[] descriptions, ImageIcon[] images) {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);

        this.descriptions = new String[descriptions.length];
        for (int i = 0; i < descriptions.length; i++) {
            this.descriptions[i] = descriptions[i];
        }
        this.images = new ImageIcon[images.length];
        for (int i = 0; i < images.length; i++) {
            this.images[i] = images[i];
        }
    }

    /*
     * This method finds the image and text corresponding to the selected value
     * and returns the label, set up to display the text and image.
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        // Get the selected index. (The index param isn't
        // always valid, so just use the value.)
        int selectedIndex = ((Integer) value).intValue();

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        // Set the icon and text. If icon was null, say so.
        ImageIcon icon = images[selectedIndex];
        String description = descriptions[selectedIndex];
        setIcon(icon);
        if (icon != null) {
            setFont(list.getFont());
        } else {
            setDescription(description, list.getFont());
        }

        return this;
    }

    /**
     * Set the font and text when no image was found.
     * 
     * @param description
     *            The description to use.
     * @param normalFont
     *            The font to use for this description.
     */
    protected void setDescription(String description, Font normalFont) {
        if (descriptionFont == null) { // lazily create this font
            descriptionFont = normalFont.deriveFont(Font.ITALIC);
        }
        setFont(descriptionFont);
        setText(description);
    }
}
