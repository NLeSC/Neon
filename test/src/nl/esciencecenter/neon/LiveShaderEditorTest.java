package nl.esciencecenter.neon;

import nl.esciencecenter.neon.examples.shadertest.LiveShaderEditor;

import org.junit.Test;

public class LiveShaderEditorTest {

    @Test
    public final void LiveShaderEditorTest() {
        LiveShaderEditor test = new LiveShaderEditor();
        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
