package nl.esciencecenter.esight.util;

import javax.media.opengl.GLProfile;

/**
 * Holder class for {@link GLProfile} related static functions.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GLProfileSelector {
    /**
     * Prints the available GLProfiles (hardware defined) to STDOUT
     */
    public static final void printAvailable() {
        System.out.println(GLProfile.glAvailabilityToString());
    }
}
