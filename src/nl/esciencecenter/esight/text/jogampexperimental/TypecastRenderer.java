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
import java.util.ArrayList;
import java.util.List;

import jogamp.graph.font.typecast.ot.OTGlyph;
import jogamp.graph.font.typecast.ot.Point;
import jogamp.graph.geom.plane.AffineTransform;
import jogamp.graph.geom.plane.Path2D;
import jogamp.graph.geom.plane.PathIterator;
import nl.esciencecenter.esight.exceptions.FontException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.graph.geom.Vertex;
import com.jogamp.graph.geom.Vertex.Factory;

/**
 * Factory to build a {@link com.jogamp.graph.geom.Path2D Path2D} from
 * {@link jogamp.graph.font.typecast.ot.OTGlyph Glyph}s.
 */
public final class TypecastRenderer {
    private static final Logger logger = LoggerFactory.getLogger(TypecastRenderer.class);

    private TypecastRenderer() {
        // Utility class
    }

    private static void getPaths(TypecastFont font, CharSequence string, float pixelSize, AffineTransform transform,
            Path2D[] p) {
        AffineTransform tmpTransform = transform;

        if (string == null) {
            return;
        }
        Font.Metrics metrics = font.getMetrics();
        float advanceTotal = 0;
        float lineGap = metrics.getLineGap(pixelSize);
        float ascent = metrics.getAscent(pixelSize);
        float descent = metrics.getDescent(pixelSize);
        if (tmpTransform == null) {
            tmpTransform = new AffineTransform();
        }
        AffineTransform t = new AffineTransform();

        float advanceY = lineGap - descent + ascent;
        float y = 0;
        for (int i = 0; i < string.length(); i++) {
            p[i] = new Path2D();
            p[i].reset();
            t.setTransform(tmpTransform);
            char character = string.charAt(i);
            if (character == '\n') {
                y += advanceY;
                advanceTotal = 0;
                continue;
            } else if (character == ' ') {
                advanceTotal += font.getFont().getHmtxTable().getAdvanceWidth(TypecastGlyph.ID_SPACE)
                        * metrics.getScale(pixelSize);
                continue;
            }
            TypecastGlyph glyph;
            try {
                glyph = (TypecastGlyph) font.getGlyph(character);
                Path2D gp = glyph.getPath();
                float scale = metrics.getScale(pixelSize);
                t.translate(advanceTotal, y);
                t.scale(scale, scale);
                p[i].append(gp.iterator(t), false);
                advanceTotal += glyph.getAdvance(pixelSize, true);
            } catch (FontException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static List<OutlineShape> getOutlineShapes(TypecastFont font, CharSequence string, float pixelSize,
            AffineTransform transform, Factory<? extends Vertex> vertexFactory) {
        Path2D[] paths = new Path2D[string.length()];
        getPaths(font, string, pixelSize, transform, paths);

        ArrayList<OutlineShape> shapes = new ArrayList<OutlineShape>();
        final int numGlyps = paths.length;
        for (int index = 0; index < numGlyps; index++) {
            if (paths[index] == null) {
                continue;
            }
            OutlineShape shape = new OutlineShape(vertexFactory);
            shapes.add(shape);
            PathIterator iterator = paths[index].iterator(transform);
            if (null != iterator) {
                while (!iterator.isDone()) {
                    float[] coords = new float[6];
                    int segmentType = iterator.currentSegment(coords);
                    addPathVertexToOutline(shape, vertexFactory, coords, segmentType);
                    iterator.next();
                }
            }
        }
        return shapes;
    }

    private static void addPathVertexToOutline(OutlineShape shape, Factory<? extends Vertex> vertexFactory,
            float[] coords, int segmentType) {
        switch (segmentType) {
        case PathIterator.SEG_MOVETO:
            shape.closeLastOutline();
            shape.addEmptyOutline();
            shape.addVertex(0, vertexFactory.create(coords, 0, 2, true));
            break;
        case PathIterator.SEG_LINETO:
            shape.addVertex(0, vertexFactory.create(coords, 0, 2, true));
            break;
        case PathIterator.SEG_QUADTO:
            shape.addVertex(0, vertexFactory.create(coords, 0, 2, false));
            shape.addVertex(0, vertexFactory.create(coords, 2, 2, true));
            break;
        case PathIterator.SEG_CUBICTO:
            shape.addVertex(0, vertexFactory.create(coords, 0, 2, false));
            shape.addVertex(0, vertexFactory.create(coords, 2, 2, false));
            shape.addVertex(0, vertexFactory.create(coords, 4, 2, true));
            break;
        case PathIterator.SEG_CLOSE:
            shape.closeLastOutline();
            break;
        default:
            throw new IllegalArgumentException("Unhandled Segment Type: " + segmentType);
        }
    }

    /**
     * Build a {@link com.jogamp.graph.geom.Path2D Path2D} from a
     * {@link jogamp.graph.font.typecast.ot.OTGlyph Glyph}. This glyph path can
     * then be transformed and rendered.
     */
    public static Path2D buildPath(OTGlyph glyph) {

        if (glyph == null) {
            return null;
        }

        Path2D glyphPath = new Path2D();

        // Iterate through all of the points in the glyph. Each time we find a
        // contour end point, add the point range to the path.
        int firstIndex = 0;
        int count = 0;
        for (int i = 0; i < glyph.getPointCount(); i++) {
            count++;
            if (glyph.getPoint(i).endOfContour) {
                addContourToPath(glyphPath, glyph, firstIndex, count);
                firstIndex = i + 1;
                count = 0;
            }
        }
        return glyphPath;
    }

    private static void addContourToPath(Path2D gp, OTGlyph glyph, int startIndex, int count) {
        int offset = 0;
        while (offset < count) {
            Point point = glyph.getPoint(startIndex + offset % count);
            Point pointPlus1 = glyph.getPoint(startIndex + (offset + 1) % count);
            Point pointPlus2 = glyph.getPoint(startIndex + (offset + 2) % count);
            if (offset == 0) {
                gp.moveTo(point.x, point.y);
            }

            if (point.onCurve) {
                if (pointPlus1.onCurve) {
                    gp.lineTo(pointPlus1.x, pointPlus1.y);
                    offset++;
                } else {
                    if (pointPlus2.onCurve) {
                        gp.quadTo(pointPlus1.x, pointPlus1.y, pointPlus2.x, pointPlus2.y);
                        offset += 2;
                    } else {
                        gp.quadTo(pointPlus1.x, pointPlus1.y, midValue(pointPlus1.x, pointPlus2.x),
                                midValue(pointPlus1.y, pointPlus2.y));
                        offset += 2;
                    }
                }
            } else {
                if (pointPlus1.onCurve) {
                    gp.quadTo(point.x, point.y, pointPlus1.x, pointPlus1.y);
                    offset++;

                } else {
                    gp.quadTo(point.x, point.y, midValue(point.x, pointPlus1.x), midValue(point.y, pointPlus1.y));
                    offset++;
                }
            }
        }
    }

    private static int midValue(int a, int b) {
        return a + (b - a) / 2;
    }
}