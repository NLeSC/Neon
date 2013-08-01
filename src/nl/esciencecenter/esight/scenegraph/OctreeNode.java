package nl.esciencecenter.esight.scenegraph;

import java.util.ArrayList;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.input.InputHandler;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.models.Model;
import nl.esciencecenter.esight.shaders.ShaderProgram;
import nl.esciencecenter.esight.util.Settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Copyright [2013] [Netherlands eScience Center]
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
 * Node for an Octree datastructure. Think of a cube in space in which you feed
 * elements with a location. When the cube overflows with elements, it
 * subdivides into 8 children Octreenodes. The Octree can be drawn directly.
 * 
 * ATTENTION: After adding elements, the finalize() function should be called to
 * clean up and prepare for drawing. The init() function should also be called
 * before drawing.
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class OctreeNode {
    private final static Logger logger = LoggerFactory.getLogger(OctreeNode.class);
    /**
     * The maximum number of elements this node may contain before subdivision
     * occurs.
     */
    private final int maxElements;
    /** The stored elements. */
    private final ArrayList<OctreeElement> elements;
    /** The center location for this node. */
    private final VecF3 center;
    /** The size of the ribs of the cube this node represents. */
    private final float ribSize;
    /** The depth of this node in the octree. */
    private final int depth;
    /** The model to be used if this node is drawn. */
    private final Model model;
    /** The translation matrix for this node. */
    private final MatF4 TMatrix;
    /** The scale for this node's graphical representation. */
    private final float scale;

    /** The (potential) child nodes. */
    private OctreeNode ppp, ppn, pnp, pnn, npp, npn, nnp, nnn;
    /** Subdivision state holder. */
    private boolean subdivided = false;
    /** OpenGL initialization state holder. */
    private boolean initialized = false;
    /** State holder for finalization step. */
    private boolean drawable = false;
    /** The color for the drawable model. */
    private VecF4 color;

    /**
     * Basic constructor for OctreeNode
     * 
     * @param baseModel
     *            The model to use for graphic representations of this node.
     * @param maxElements
     *            The maximum amount of {@link OctreeElement}s for this node.
     * @param depth
     *            The depth for this node in the octree (root = 0).
     * @param corner
     *            The corner location for the lower X, Y, Z values of the cube
     *            represented by this node.
     * @param ribSize
     *            The rib sizes for the cube represented by this node.
     */
    public OctreeNode(Model baseModel, int maxElements, int depth, VecF3 corner, float ribSize) {
        this.model = baseModel;
        this.maxElements = maxElements;
        this.depth = depth;
        this.center = corner.add(new VecF3(.5f * ribSize, .5f * ribSize, .5f * ribSize));
        this.ribSize = ribSize;
        this.TMatrix = MatrixFMath.translate(center);
        this.scale = ribSize;
        this.elements = new ArrayList<OctreeElement>();
    }

    /**
     * Copy constructor for OctreeNode
     * 
     * @param other
     *            the node to copy.
     */
    public OctreeNode(OctreeNode other) {
        this.maxElements = other.maxElements;
        this.elements = other.elements;
        this.center = other.center;
        this.ribSize = other.ribSize;
        this.depth = other.depth;
        this.model = other.model;
        this.TMatrix = other.TMatrix;
        this.scale = other.scale;

        this.ppp = other.ppp;
        this.ppn = other.ppn;
        this.pnp = other.pnp;
        this.pnn = other.pnn;
        this.npp = other.npp;
        this.npn = other.npn;
        this.nnp = other.nnp;
        this.nnn = other.nnn;
        this.initialized = other.initialized;
        this.subdivided = other.subdivided;
        this.drawable = other.drawable;
        this.color = other.color;
    }

    /**
     * OpenGL initialization method.
     * 
     * @param gl
     *            the currently bound GL instance.
     */
    public void init(GL3 gl) {
        if (!initialized) {
            model.init(gl);

            if (subdivided) {
                ppp.init(gl);
                ppn.init(gl);
                pnp.init(gl);
                pnn.init(gl);
                npp.init(gl);
                npn.init(gl);
                nnp.init(gl);
                nnn.init(gl);
            }
        }

        initialized = true;
    }

    /**
     * Deletion method for this octree node. Cleans up any used memory. Since
     * only one model is used, this needs to delete only once.
     * 
     * @param gl
     *            the currently bound GL instance.
     */
    public void delete(GL3 gl) {
        if (initialized) {
            model.delete(gl);

            // if (subdivided) {
            // ppp.delete(gl);
            // ppn.delete(gl);
            // pnp.delete(gl);
            // pnn.delete(gl);
            // npp.delete(gl);
            // npn.delete(gl);
            // nnp.delete(gl);
            // nnn.delete(gl);
            // }
        }
    }

    /**
     * Subdivides this octree node, relocating its {@link OctreeElement}s into
     * the proper children. After this method is called, this Node only acts as
     * a throughput, delegating all calls to its children.
     */
    private void subdivide() {
        float childRibSize = ribSize / 2f;
        int newDepth = depth + 1;

        ppp = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(0f, 0f, 0f)), childRibSize);
        ppn = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(0f, 0f, -ribSize)), childRibSize);
        pnp = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(0f, -ribSize, 0f)), childRibSize);
        pnn = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(0f, -ribSize, -ribSize)), childRibSize);
        npp = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(-ribSize, 0f, 0f)), childRibSize);
        npn = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(-ribSize, 0f, -ribSize)), childRibSize);
        nnp = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(-ribSize, -ribSize, 0f)), childRibSize);
        nnn = new OctreeNode(model, maxElements, newDepth, center.add(new VecF3(-ribSize, -ribSize, -ribSize)),
                childRibSize);

        for (OctreeElement element : elements) {
            addElementSubdivided(element);
        }

        elements.clear();

        subdivided = true;
    }

    /**
     * Helper method to determine if the given location falls within the domain
     * of this node. Only needed for the root, to make sure no outside elements
     * are caught.
     * 
     * @param location
     *            The location to check.
     * @return true if the location falls inside the domain of this node.
     */
    private boolean isInThisNodesSpace(VecF3 location) {
        float halfRibSize = ribSize * 0.5f;

        if ((location.get(0) > (center.get(0) - halfRibSize)) && (location.get(1) > (center.get(1) - halfRibSize))
                && (location.get(2) > (center.get(2) - halfRibSize))
                && (location.get(0) < (center.get(0) + halfRibSize))
                && (location.get(1) < (center.get(1) + halfRibSize))
                && (location.get(2) < (center.get(2) + halfRibSize))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add an element to this node.
     * 
     * @param element
     *            The {@link OctreeElement} to add to this node.
     */
    public void addElement(OctreeElement element) {
        VecF3 location = element.getCenter();

        // If this is the root, check if the location of the element is within
        // the domain governed by this octree.
        if (depth > 0 || isInThisNodesSpace(location)) {
            // Check if we are full yet.
            if (!subdivided && (elements.size() > maxElements)) {
                if (depth < Settings.getInstance().getMaxOctreeDepth()) {
                    // If so, subdivide this node.
                    subdivide();
                } else {
                    // Or generate a warning if there is something wrong.
                    // (insane total number of elements added f.e.)
                    logger.warn("Octree max division reached.");
                }
            }

            // If this node was already divided, we delegate.
            if (subdivided) {
                addElementSubdivided(element);
            } else {
                elements.add(element);
            }
        } else {
            logger.warn("OctreeElement added that is not within governed domain of this OctreeNode.");
        }
    }

    /**
     * Finalize the addition of new elements to this node. Calculate things we
     * have to calculate for drawing (f.e. color), and clear datastructures.
     * ATTENTION: This is a placeholder, and currently sets the color to
     * transparent white based on population density compared to
     * {@link #maxElements}. Override this.
     */
    public void finalizeAdding() {
        if (subdivided) {
            ppp.finalizeAdding();
            ppn.finalizeAdding();
            pnp.finalizeAdding();
            pnn.finalizeAdding();
            npp.finalizeAdding();
            npn.finalizeAdding();
            nnp.finalizeAdding();
            nnn.finalizeAdding();
        } else {
            color = new VecF4(1f, 1f, 1f, ((float) elements.size() / (float) maxElements));
            drawable = true;
        }

        elements.clear();
    }

    /**
     * Add an element to the proper child.
     * 
     * @param element
     *            The element to add.
     */
    public void addElementSubdivided(OctreeElement element) {
        VecF3 location = element.getCenter();
        if (location.get(0) < center.get(0)) {
            if (location.get(1) < center.get(1)) {
                if (location.get(2) < center.get(2)) {
                    nnn.addElement(element);
                } else {
                    nnp.addElement(element);
                }
            } else {
                if (location.get(2) < center.get(2)) {
                    npn.addElement(element);
                } else {
                    npp.addElement(element);
                }
            }
        } else {
            if (location.get(1) < center.get(1)) {
                if (location.get(2) < center.get(2)) {
                    pnn.addElement(element);
                } else {
                    pnp.addElement(element);
                }
            } else {
                if (location.get(2) < center.get(2)) {
                    ppn.addElement(element);
                } else {
                    ppp.addElement(element);
                }
            }
        }
    }

    /**
     * OpenGL draw method.
     * 
     * @param gl
     *            the current GL instance.
     * @param program
     *            The ShaderProgram to use in the drawing process.
     * @param MVMatrix
     *            The global Modelview Matrix.
     * @throws UninitializedException
     *             if this method was called before calling the
     *             {@link #init(GL3)} method.
     */
    public void draw(GL3 gl, ShaderProgram program, MatF4 MVMatrix) throws UninitializedException {
        if (initialized) {
            if (subdivided) {
                draw_sorted(gl, program, MVMatrix);
            } else {
                if (drawable) {
                    MatF4 newM = MVMatrix.mul(TMatrix);
                    program.setUniformMatrix("MVMatrix", newM);
                    program.setUniformMatrix("SMatrix", MatrixFMath.scale(scale));
                    program.setUniformVector("Color", color);

                    program.use(gl);

                    model.draw(gl, program);
                }
            }
        } else {
            throw new UninitializedException();
        }
    }

    /**
     * Draws this node in the proper sorted order based on the current octant in
     * the {@link InputHandler}
     * 
     * @param gl
     *            the current opengl instance.
     * @param program
     *            the shaderprogram to use in drawing.
     * @param MVMatrix
     *            the global Modelview Matrix.
     */
    private void draw_sorted(GL3 gl, ShaderProgram program, MatF4 MVMatrix) {
        InputHandler inputHandler = InputHandler.getInstance();

        try {
            if (inputHandler.getCurrentViewOctant() == InputHandler.octants.NNN) {
                ppp.draw(gl, program, MVMatrix);

                npp.draw(gl, program, MVMatrix);
                pnp.draw(gl, program, MVMatrix);
                ppn.draw(gl, program, MVMatrix);

                nnp.draw(gl, program, MVMatrix);
                pnn.draw(gl, program, MVMatrix);
                npn.draw(gl, program, MVMatrix);

                nnn.draw(gl, program, MVMatrix);
            } else if (inputHandler.getCurrentViewOctant() == InputHandler.octants.NNP) {
                ppn.draw(gl, program, MVMatrix);

                npn.draw(gl, program, MVMatrix);
                pnn.draw(gl, program, MVMatrix);
                ppp.draw(gl, program, MVMatrix);

                nnn.draw(gl, program, MVMatrix);
                pnp.draw(gl, program, MVMatrix);
                npp.draw(gl, program, MVMatrix);

                nnp.draw(gl, program, MVMatrix);
            } else if (inputHandler.getCurrentViewOctant() == InputHandler.octants.NPN) {
                pnp.draw(gl, program, MVMatrix);

                nnp.draw(gl, program, MVMatrix);
                ppp.draw(gl, program, MVMatrix);
                pnn.draw(gl, program, MVMatrix);

                npp.draw(gl, program, MVMatrix);
                ppn.draw(gl, program, MVMatrix);
                nnn.draw(gl, program, MVMatrix);

                npn.draw(gl, program, MVMatrix);
            } else if (inputHandler.getCurrentViewOctant() == InputHandler.octants.NPP) {
                pnn.draw(gl, program, MVMatrix);

                nnn.draw(gl, program, MVMatrix);
                ppn.draw(gl, program, MVMatrix);
                pnp.draw(gl, program, MVMatrix);

                npn.draw(gl, program, MVMatrix);
                ppp.draw(gl, program, MVMatrix);
                nnp.draw(gl, program, MVMatrix);

                npp.draw(gl, program, MVMatrix);
            } else if (inputHandler.getCurrentViewOctant() == InputHandler.octants.PNN) {
                npp.draw(gl, program, MVMatrix);

                ppp.draw(gl, program, MVMatrix);
                nnp.draw(gl, program, MVMatrix);
                npn.draw(gl, program, MVMatrix);

                pnp.draw(gl, program, MVMatrix);
                nnn.draw(gl, program, MVMatrix);
                ppn.draw(gl, program, MVMatrix);

                pnn.draw(gl, program, MVMatrix);
            } else if (inputHandler.getCurrentViewOctant() == InputHandler.octants.PNP) {
                npn.draw(gl, program, MVMatrix);

                ppn.draw(gl, program, MVMatrix);
                nnn.draw(gl, program, MVMatrix);
                npp.draw(gl, program, MVMatrix);

                pnn.draw(gl, program, MVMatrix);
                nnp.draw(gl, program, MVMatrix);
                ppp.draw(gl, program, MVMatrix);

                pnp.draw(gl, program, MVMatrix);
            } else if (inputHandler.getCurrentViewOctant() == InputHandler.octants.PPN) {
                nnp.draw(gl, program, MVMatrix);

                pnp.draw(gl, program, MVMatrix);
                npp.draw(gl, program, MVMatrix);
                nnn.draw(gl, program, MVMatrix);

                ppp.draw(gl, program, MVMatrix);
                npn.draw(gl, program, MVMatrix);
                pnn.draw(gl, program, MVMatrix);

                ppn.draw(gl, program, MVMatrix);
            } else if (inputHandler.getCurrentViewOctant() == InputHandler.octants.PPP) {
                nnn.draw(gl, program, MVMatrix);

                pnn.draw(gl, program, MVMatrix);
                npn.draw(gl, program, MVMatrix);
                nnp.draw(gl, program, MVMatrix);

                ppn.draw(gl, program, MVMatrix);
                npp.draw(gl, program, MVMatrix);
                pnp.draw(gl, program, MVMatrix);

                ppp.draw(gl, program, MVMatrix);
            }
        } catch (UninitializedException e) {
            e.printStackTrace();
        }
    }
}
