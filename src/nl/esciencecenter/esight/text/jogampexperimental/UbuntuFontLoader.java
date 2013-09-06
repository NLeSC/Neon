package nl.esciencecenter.esight.text.jogampexperimental;

/**
 * Copyright 2011 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */

import java.io.IOException;
import java.net.URLConnection;

import javax.media.opengl.GLException;

import com.jogamp.common.util.IOUtil;
import com.jogamp.common.util.IntObjectHashMap;

public final class UbuntuFontLoader implements FontSet {
    final static FontSet fontLoader = new UbuntuFontLoader();

    public static FontSet get() {
        return fontLoader;
    }

    private final static int REGULAR = 0;
    private final static int REGULAR_ITALIC = 1;
    private final static int REGULAR_BOLD = 2;
    private final static int REGULAR_BOLD_ITALIC = 3;
    private final static int LIGHT = 4;
    private final static int LIGHT_ITALIC = 5;
    private final static int MEDIUM = 6;
    private final static int MEDIUM_ITALIC = 7;

    private final static int FONT_FAMILIES = 8;

    final static String availableFontFileNames[] = { "Ubuntu-R.ttf", "Ubuntu-RI.ttf", "Ubuntu-B.ttf", "Ubuntu-BI.ttf",
            "Ubuntu-L.ttf", "Ubuntu-LI.ttf", "Ubuntu-M.ttf", "Ubuntu-MI.ttf",

    };

    final static String relPath = "fonts/";

    private UbuntuFontLoader() {
        // Utility class
    }

    static final IntObjectHashMap fontMap = new IntObjectHashMap();

    static boolean is(int bits, int bit) {
        return 0 != (bits & bit);
    }

    @Override
    public Font getDefault() {
        return get(FAMILY_REGULAR, 0); // Sans Serif Regular
    }

    @Override
    public Font get(int family, int style) {
        Font font = (Font) fontMap.get((family << FONT_FAMILIES) | style);
        if (font != null) {
            return font;
        }

        switch (family) {
        case FAMILY_MONOSPACED:
        case FAMILY_CONDENSED:
        case FAMILY_REGULAR:
            if (is(style, STYLE_BOLD)) {
                if (is(style, STYLE_ITALIC)) {
                    font = abspath(availableFontFileNames[REGULAR_BOLD_ITALIC], family, style);
                } else {
                    font = abspath(availableFontFileNames[REGULAR_BOLD], family, style);
                }
            } else if (is(style, STYLE_ITALIC)) {
                font = abspath(availableFontFileNames[REGULAR_ITALIC], family, style);
            } else {
                font = abspath(availableFontFileNames[REGULAR], family, style);
            }
            break;

        case FAMILY_LIGHT:
            if (is(style, STYLE_ITALIC)) {
                font = abspath(availableFontFileNames[LIGHT_ITALIC], family, style);
            } else {
                font = abspath(availableFontFileNames[LIGHT], family, style);
            }
            break;

        case FAMILY_MEDIUM:
            if (is(style, STYLE_ITALIC)) {
                font = abspath(availableFontFileNames[MEDIUM_ITALIC], family, style);
            } else {
                font = abspath(availableFontFileNames[MEDIUM], family, style);
            }
            break;
        default:
            font = abspath(availableFontFileNames[REGULAR], family, style);
        }

        return font;
    }

    Font abspath(String fname, int family, int style) {
        final String err = "Problem loading font " + fname + ", stream " + relPath + fname;
        try {
            URLConnection urlc = IOUtil.getResource(UbuntuFontLoader.class, relPath + fname);
            if (null == urlc) {
                throw new GLException(err);
            }
            final Font f = FontFactory.get(urlc);
            if (null != f) {
                fontMap.put((family << FONT_FAMILIES) | style, f);
                return f;
            }
            throw new GLException(err + " " + urlc);
        } catch (IOException ioe) {
            throw new GLException(err, ioe);
        }
    }
}