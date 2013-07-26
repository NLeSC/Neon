package nl.esciencecenter.esight;

import nl.esciencecenter.esight.examples.HelloWorldExample;

import org.junit.Test;

public class HelloWorldExampleTest {

    @Test
    public final void testESightExample() {
        HelloWorldExample test = new HelloWorldExample();
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
