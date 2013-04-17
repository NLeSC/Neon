package nl.esciencecenter.esight.examples;

import nl.esciencecenter.esight.input.InputHandler;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class ESightExampleInputHandler extends InputHandler implements
        MouseListener, KeyListener {

    private static class SingletonHolder {
        public static final ESightExampleInputHandler instance = new ESightExampleInputHandler();
    }

    public static ESightExampleInputHandler getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        // Do something that is different than the standard
    }
}
