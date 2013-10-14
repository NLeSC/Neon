package nl.esciencecenter.neon.util;

import java.util.ArrayList;
import java.util.List;

import nl.esciencecenter.neon.math.MatF4;

public class ModelViewStack {
    private final List<MatF4> stack;

    public ModelViewStack() {
        stack = new ArrayList<MatF4>();
    }

    public ModelViewStack(ModelViewStack old) {
        stack = new ArrayList<MatF4>();
        for (MatF4 oMV : old.stack) {
            stack.add(oMV);
        }
    }

    public void putTop(MatF4 mv) {
        stack.add(mv);
    }

    public void putBottom(MatF4 mv) {
        stack.add(0, mv);
    }

    public MatF4 calc(MatF4 input) {
        MatF4 result = new MatF4(input);
        for (MatF4 element : stack) {
            result = result.mul(element);
        }
        return result;
    }

}
