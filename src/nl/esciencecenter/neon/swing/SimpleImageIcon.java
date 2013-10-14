package nl.esciencecenter.neon.swing;

import javax.swing.ImageIcon;

public class SimpleImageIcon {
    private String description;
    private ImageIcon icon;

    public SimpleImageIcon(String description, ImageIcon icon) {
        this.description = description;
        this.icon = icon;
    }

    /**
     * Getter for description.
     * 
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon.
     */
    public ImageIcon getImageIcon() {
        return icon;
    }

    /**
     * Setter for icon.
     * 
     * @param icon
     *            the icon to set
     */
    public void setImageIcon(ImageIcon image) {
        this.icon = image;
    }

}
