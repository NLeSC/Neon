package nl.esciencecenter.esight.math;

import static org.junit.Assert.fail;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.Vector;
import nl.esciencecenter.esight.math.VectorList;

import org.junit.Test;

public class VectorListTest {

    @Test
    public final void testVectorList() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testGetVectorSize() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testGetType() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testAddVector() {
        VectorList points3List = new VectorList(3, Vector.Type.FLOAT);

        for (int i = 0; i < 100; i++) {
            points3List.add(new VecF3(1f, 1f, 1f));
        }

    }

    @Test
    public final void testToBuffer() {
        VectorList points3List = new VectorList(3, Vector.Type.FLOAT);

        for (int i = 0; i < 100; i++) {
            points3List.add(new VecF3(1f, 1f, 1f));
        }

        points3List.toBuffer();
    }

}
