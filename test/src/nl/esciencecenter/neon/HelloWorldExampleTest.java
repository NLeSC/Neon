package nl.esciencecenter.neon;

import nl.esciencecenter.neon.examples.graphs.GraphsExample;

import org.junit.Test;

public class HelloWorldExampleTest {

    @Test
    public final void testNeonExample() {
        GraphsExample test = new GraphsExample();
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
