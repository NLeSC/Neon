package nl.esciencecenter.esight.scenegraph;

import java.util.ArrayList;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.models.LightSource;
import nl.esciencecenter.esight.models.Model;
import nl.esciencecenter.esight.shaders.ShaderProgram;

/* Copyright [2013] [Netherlands eScience Center]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * Bsic implementation of a Scenegraph node.
 * 
 * @author Maarten van Meersbergen <m.vanmeersbergen@esciencecenter.nl>
 */
public class SGNode {

    /**
     * The translation matrix for the elements (and children) of this Scenegraph
     * node.
     */
    protected MatF4 TMatrix;
    /**
     * The rotation matrix for the elements (and children) of this Scenegraph
     * node.
     */
    protected MatF4 RMatrix;
    /**
     * The scale matrix for the elements (and children) of this Scenegraph node.
     */
    protected MatF4 SMatrix;

    /** Store for the children. */
    protected ArrayList<SGNode> children;

    /** Store for the models (elements). */
    protected ArrayList<Model> models;

    /** Store for lightsources. */
    protected ArrayList<LightSource> lightsources;

    /** State holder. */
    private boolean initialized = false;

    /**
     * Basic constructor for SGNode
     */
    public SGNode() {
        TMatrix = new MatF4();

        children = new ArrayList<SGNode>();
        models = new ArrayList<Model>();
    }

    /**
     * OpenGL initialization method.
     * 
     * @param gl
     *            the current GL instance.
     */
    public void init(GL3 gl) {
        if (!initialized) {
            for (Model m : models) {
                m.init(gl);
            }

            for (SGNode child : children) {
                child.init(gl);
            }
        }

        initialized = true;
    }

    /**
     * OpenGL cleanup method.
     * 
     * @param gl
     *            the current GL instance.
     */
    public void delete(GL3 gl) {
        for (Model m : models) {
            m.delete(gl);
        }

        for (SGNode child : children) {
            child.delete(gl);
        }
    }

    /**
     * Add a child to this node.
     * 
     * @param child
     *            the child to add.
     */
    public void addChild(SGNode child) {
        children.add(child);
    }

    /**
     * Add a model to this level of the scenegraph
     * 
     * @param model
     *            the model to add
     */
    public void addModel(Model model) {
        models.add(model);
    }

    /**
     * Set the translation for this level of the scenegraph.
     * 
     * @param translation
     *            the new translation.
     */
    public synchronized void setTranslation(VecF3 translation) {
        this.TMatrix = MatrixFMath.translate(translation);
    }

    /**
     * Translate the level (multiply with the previously entered translation
     * matrix).
     * 
     * @param translation
     *            the new translation to do.
     */
    public void translate(VecF3 translation) {
        this.TMatrix = TMatrix.mul(MatrixFMath.translate(translation));
    }

    /**
     * Rotate this level. (multiply with the previously entered translation
     * matrix).
     * 
     * @param rotation
     *            The amount of rotation to perform.
     * @param axis
     *            The axis around which to rotate.
     */
    public void rotate(float rotation, VecF3 axis) {
        this.TMatrix = TMatrix.mul(MatrixFMath.rotate(rotation, axis));
    }

    /**
     * Rotate this level. (multiply with the previously entered translation
     * matrix).
     * 
     * @param rotation
     *            The rotation to perform.
     */
    public void rotate(VecF3 rotation) {
        this.TMatrix = TMatrix.mul(MatrixFMath.rotationX(rotation.get(0)));
        this.TMatrix = TMatrix.mul(MatrixFMath.rotationY(rotation.get(1)));
        this.TMatrix = TMatrix.mul(MatrixFMath.rotationZ(rotation.get(2)));
    }

    /**
     * OpenGL draw method.
     * 
     * @param gl
     *            the current gl instance.
     * @param program
     *            The shaderProgram to use for the drawing process.
     * @param MVMatrix
     *            The global Modelview Matrix.
     * @throws UninitializedException
     *             if this method was called before the init() method.
     */
    public synchronized void draw(GL3 gl, ShaderProgram program, MatF4 MVMatrix) throws UninitializedException {
        if (!initialized) {
            throw new UninitializedException();
        }

        MatF4 newM = MVMatrix.mul(TMatrix);

        for (Model m : models) {
            program.setUniformMatrix("MVMatrix", newM);

            program.use(gl);

            m.draw(gl, program);
        }

        for (SGNode child : children) {
            child.draw(gl, program, newM);
        }
    }

}
