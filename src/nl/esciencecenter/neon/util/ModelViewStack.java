package nl.esciencecenter.neon.util;

import java.util.ArrayList;
import java.util.List;

import nl.esciencecenter.neon.math.Float4Matrix;

public class ModelViewStack {
    private final List<Float4Matrix> stack;

    public ModelViewStack() {
        stack = new ArrayList<Float4Matrix>();
    }

    public ModelViewStack(ModelViewStack old) {
        stack = new ArrayList<Float4Matrix>();
        for (Float4Matrix oMV : old.stack) {
            stack.add(oMV);
        }
    }

    public void putTop(Float4Matrix mv) {
        stack.add(mv);
    }

    public void putBottom(Float4Matrix mv) {
        stack.add(0, mv);
    }

    public Float4Matrix calc(Float4Matrix input) {
        Float4Matrix result = new Float4Matrix(input);
        for (Float4Matrix element : stack) {
            result = result.mul(element);
        }
        return result;
    }

}
