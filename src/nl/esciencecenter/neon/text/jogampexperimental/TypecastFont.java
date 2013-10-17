package nl.esciencecenter.neon.text.jogampexperimental;

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

import java.util.List;

import jogamp.graph.font.typecast.ot.OTFont;
import jogamp.graph.font.typecast.ot.OTFontCollection;
import jogamp.graph.font.typecast.ot.table.CmapFormat;
import jogamp.graph.font.typecast.ot.table.CmapIndexEntry;
import jogamp.graph.font.typecast.ot.table.CmapTable;
import jogamp.graph.font.typecast.ot.table.HdmxTable;
import jogamp.graph.font.typecast.ot.table.ID;
import jogamp.graph.geom.plane.AffineTransform;
import jogamp.graph.geom.plane.Path2D;
import nl.esciencecenter.neon.exceptions.FontException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.common.util.IntObjectHashMap;
import com.jogamp.graph.geom.Vertex;
import com.jogamp.graph.geom.Vertex.Factory;
import com.jogamp.opengl.math.geom.AABBox;

public class TypecastFont implements FontInt {
    private static final Logger logger = LoggerFactory.getLogger(TypecastFont.class);

    static final boolean DEBUG = false;

    private final OTFontCollection fontset;
    private final OTFont font;
    private TypecastHMetrics metrics;
    private final CmapFormat cmapFormat;
    private int cmapentries;

    // Add cache size to limit memory usage ??
    private IntObjectHashMap char2Glyph;

    public TypecastFont(OTFontCollection fontset) throws FontException {
        this.fontset = fontset;
        this.font = fontset.getFont(0);

        // Generic attempt to find the best CmapTable,
        // which is assumed to be the one with the most entries (stupid 'eh?)
        CmapTable cmapTable = font.getCmapTable();
        CmapFormat[] cmapFormatP = { null, null, null, null };
        int platform = -1;
        int platformLength = -1;
        int encoding = -1;
        for (int i = 0; i < cmapTable.getNumTables(); i++) {
            CmapIndexEntry cmapIdxEntry = cmapTable.getCmapIndexEntry(i);
            int pidx = cmapIdxEntry.getPlatformId();
            CmapFormat cf = cmapIdxEntry.getFormat();
            if (DEBUG) {
                logger.debug("CmapFormat[" + i + "]: platform " + pidx + ", encoding " + cmapIdxEntry.getEncodingId()
                        + ": " + cf);
            }
            if (cmapFormatP[pidx] == null || cmapFormatP[pidx].getLength() < cf.getLength()) {
                cmapFormatP[pidx] = cf;
                if (cf.getLength() > platformLength) {
                    platformLength = cf.getLength();
                    platform = pidx;
                    encoding = cmapIdxEntry.getEncodingId();
                }
            }
        }
        if (0 <= platform) {
            cmapFormat = cmapFormatP[platform];
            if (DEBUG) {
                logger.debug("Selected CmapFormat: platform " + platform + ", encoding " + encoding + ": " + cmapFormat);
            }
        } else {
            CmapFormat tmpCmapFormat = null;

            if (null == tmpCmapFormat) {
                // default unicode
                platform = ID.platformMicrosoft;
                encoding = ID.encodingUnicode;
                tmpCmapFormat = cmapTable.getCmapFormat((short) platform, (short) encoding);
            }
            if (null == tmpCmapFormat) {
                // maybe a symbol font ?
                platform = ID.platformMicrosoft;
                encoding = ID.encodingSymbol;
                tmpCmapFormat = cmapTable.getCmapFormat((short) platform, (short) encoding);
            }
            if (null == tmpCmapFormat) {
                throw new FontException("Cannot find a suitable cmap table for font " + font);
            }
            cmapFormat = tmpCmapFormat;
            if (DEBUG) {
                logger.debug("Selected CmapFormat (2): platform " + platform + ", encoding " + encoding + ": "
                        + cmapFormat);
            }
        }

        cmapentries = 0;
        for (int i = 0; i < cmapFormat.getRangeCount(); ++i) {
            CmapFormat.Range range = cmapFormat.getRange(i);
            cmapentries = cmapentries + (range.getEndCode() - range.getStartCode() + 1); // end
            // included
        }
        if (DEBUG) {
            logger.debug("font direction hint: " + font.getHeadTable().getFontDirectionHint());
            logger.debug("num glyphs: " + font.getNumGlyphs());
            logger.debug("num cmap entries: " + cmapentries);
            logger.debug("num cmap ranges: " + cmapFormat.getRangeCount());

            for (int i = 0; i < cmapFormat.getRangeCount(); ++i) {
                CmapFormat.Range range = cmapFormat.getRange(i);
                for (int j = range.getStartCode(); j <= range.getEndCode(); ++j) {
                    final int code = cmapFormat.mapCharCode(j);
                    if (code < 15) {
                        logger.debug(" char: " + j + " ( " + (char) j + " ) -> " + code);
                    }
                }
            }
        }
        char2Glyph = new IntObjectHashMap(cmapentries + cmapentries / 4);
    }

    @Override
    public StringBuilder getName(StringBuilder sb, int nameIndex) {
        return getFont().getName(nameIndex, sb);
    }

    @Override
    public String getName(int nameIndex) {
        return getName(null, nameIndex).toString();
    }

    @Override
    public StringBuilder getAllNames(StringBuilder sb, String separator) {
        return getFont().getAllNames(sb, separator);
    }

    @Override
    public StringBuilder getFullFamilyName(StringBuilder sb) {
        StringBuilder newSb = new StringBuilder(sb);
        newSb = getName(newSb, Font.NAME_FAMILY).append("-");
        getName(newSb, Font.NAME_SUBFAMILY);
        return newSb;
    }

    @Override
    public Metrics getMetrics() {
        if (metrics == null) {
            metrics = new TypecastHMetrics(this);
        }
        return metrics;
    }

    @Override
    public Glyph getGlyph(char symbol) throws FontException {
        TypecastGlyph result = (TypecastGlyph) getChar2Glyph().get(symbol);
        if (null == result) {
            short code = (short) getCmapFormat().mapCharCode(symbol);
            if (0 == code && 0 != symbol) {
                // reserved special glyph IDs by convention
                switch (symbol) {
                case ' ':
                    code = Glyph.ID_SPACE;
                    break;
                case '\n':
                    code = Glyph.ID_CR;
                    break;
                default:
                    code = Glyph.ID_UNKNOWN;
                }
            }

            jogamp.graph.font.typecast.ot.OTGlyph glyph = getFont().getGlyph(code);
            if (null == glyph) {
                glyph = getFont().getGlyph(Glyph.ID_UNKNOWN);
            }
            if (null == glyph) {
                throw new FontException("Could not retrieve glyph for symbol: <" + symbol + "> " + (int) symbol
                        + " -> glyph id " + code);
            }
            Path2D path = TypecastRenderer.buildPath(glyph);
            result = new TypecastGlyph(this, symbol, (short) 0, new AABBox(), 1, path);
            if (DEBUG) {
                logger.debug("New glyph: " + (int) symbol + " ( " + symbol + " ) -> " + code + ", contours "
                        + glyph.getPointCount() + ": " + path);
            }
            final HdmxTable hdmx = getFont().getHdmxTable();
            if (null != result && null != hdmx) {
                for (int i = 0; i < hdmx.getNumberOfRecords(); i++) {
                    final HdmxTable.DeviceRecord dr = hdmx.getRecord(i);
                    result.addAdvance(dr.getWidth(code), dr.getPixelSize());
                }
            }
            getChar2Glyph().put(symbol, result);
        }
        return result;
    }

    @Override
    public List<OutlineShape> getOutlineShapes(CharSequence string, float pixelSize,
            Factory<? extends Vertex> vertexFactory) {
        AffineTransform transform = new AffineTransform(vertexFactory);
        return TypecastRenderer.getOutlineShapes(this, string, pixelSize, transform, vertexFactory);
    }

    @Override
    public float getStringWidth(CharSequence string, float pixelSize) {
        float width = 0;
        final int len = string.length();
        for (int i = 0; i < len; i++) {
            char character = string.charAt(i);
            if (character == '\n') {
                width = 0;
            } else {
                Glyph glyph;
                try {
                    glyph = getGlyph(character);
                    width += glyph.getAdvance(pixelSize, false);
                } catch (FontException e) {
                    logger.error(e.getMessage());
                }
            }
        }

        return (int) (width + 0.5f);
    }

    @Override
    public float getStringHeight(CharSequence string, float pixelSize) {
        int height = 0;

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character != ' ') {
                Glyph glyph;
                try {
                    glyph = getGlyph(character);
                    AABBox bbox = glyph.getBBox(pixelSize);
                    height = (int) Math.ceil(Math.max(bbox.getHeight(), height));
                } catch (FontException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return height;
    }

    @Override
    public AABBox getStringBounds(CharSequence string, float pixelSize) {
        if (string == null) {
            return new AABBox();
        }
        final Metrics tmpMetrics = getMetrics();
        final float lineGap = tmpMetrics.getLineGap(pixelSize);
        final float ascent = tmpMetrics.getAscent(pixelSize);
        final float descent = tmpMetrics.getDescent(pixelSize);
        final float advanceY = lineGap - descent + ascent;
        float totalHeight = 0;
        float totalWidth = 0;
        float curLineWidth = 0;
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character == '\n') {
                totalWidth = Math.max(curLineWidth, totalWidth);
                curLineWidth = 0;
                totalHeight -= advanceY;
                continue;
            }
            Glyph glyph;
            try {
                glyph = getGlyph(character);
                curLineWidth += glyph.getAdvance(pixelSize, true);
            } catch (FontException e) {
                logger.error(e.getMessage());
            }
        }
        if (curLineWidth > 0) {
            totalHeight -= advanceY;
            totalWidth = Math.max(curLineWidth, totalWidth);
        }
        return new AABBox(0, 0, 0, totalWidth, totalHeight, 0);
    }

    @Override
    final public int getNumGlyphs() {
        return getFont().getNumGlyphs();
    }

    @Override
    public boolean isPrintableChar(char c) {
        return FontFactory.isPrintableChar(c);
    }

    @Override
    public String toString() {
        return getFullFamilyName(null).toString();
    }

    /**
     * Getter for fontset.
     * 
     * @return the fontset.
     */
    public OTFontCollection getFontset() {
        return fontset;
    }

    /**
     * Getter for font.
     * 
     * @return the font.
     */
    public OTFont getFont() {
        return font;
    }

    /**
     * Getter for cmapFormat.
     * 
     * @return the cmapFormat.
     */
    public CmapFormat getCmapFormat() {
        return cmapFormat;
    }

    /**
     * Getter for cmapentries.
     * 
     * @return the cmapentries.
     */
    public int getCmapentries() {
        return cmapentries;
    }

    /**
     * Setter for cmapentries.
     * 
     * @param cmapentries
     *            the cmapentries to set
     */
    public void setCmapentries(int cmapentries) {
        this.cmapentries = cmapentries;
    }

    /**
     * Getter for char2Glyph.
     * 
     * @return the char2Glyph.
     */
    public IntObjectHashMap getChar2Glyph() {
        return char2Glyph;
    }

    /**
     * Setter for char2Glyph.
     * 
     * @param char2Glyph
     *            the char2Glyph to set
     */
    public void setChar2Glyph(IntObjectHashMap char2Glyph) {
        this.char2Glyph = char2Glyph;
    }
}