package nl.esciencecenter.esight;

import nl.esciencecenter.esight.examples.graphs.GraphsExample;

import org.junit.Test;

public class HelloWorldExampleTest {

    @Test
    public final void testESightExample() {
        GraphsExample test = new GraphsExample();
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
