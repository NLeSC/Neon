package nl.esciencecenter.esight.input;

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
 * Touch interface (Collab) point representation.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
class TouchPoint {
    private int id;
    private int state;
    private float tx, ty; // Touch coordinates

    /**
     * Getter for id.
     * 
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for id.
     * 
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for state.
     * 
     * @return the state.
     */
    public int getState() {
        return state;
    }

    /**
     * Setter for state.
     * 
     * @param state
     *            the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Getter for tx.
     * 
     * @return the tx.
     */
    public float getTx() {
        return tx;
    }

    /**
     * Setter for tx.
     * 
     * @param tx
     *            the tx to set
     */
    public void setTx(float tx) {
        this.tx = tx;
    }

    /**
     * Getter for ty.
     * 
     * @return the ty.
     */
    public float getTy() {
        return ty;
    }

    /**
     * Setter for ty.
     * 
     * @param ty
     *            the ty to set
     */
    public void setTy(float ty) {
        this.ty = ty;
    }
}
