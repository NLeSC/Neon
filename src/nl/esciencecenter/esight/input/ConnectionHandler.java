package nl.esciencecenter.esight.input;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connection handling class. Used for Touch events for collaboratorium.
 * 
 * @author Paul Melis, SurfSARA
 */
class ConnectionHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);

    private static final int INITIAL_MESSAGE_LENGTH = 4;
    private static final int MAX_TOUCH_EVENTS = 6;
    private final Socket socket;
    private final TouchEventHandler handler;

    public ConnectionHandler(TouchEventHandler handler, Socket socket) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        InputStream is;

        byte[] lengthBuffer = new byte[4];
        byte[] messageBuffer = new byte[2048];
        int messageLength;

        int numTouches;

        ByteBufferView view = new ByteBufferView(lengthBuffer);

        double timestamp;

        TouchPoint[] points;

        points = new TouchPoint[MAX_TOUCH_EVENTS];
        for (int i = 0; i < MAX_TOUCH_EVENTS; i++) {
            points[i] = new TouchPoint();
        }

        try {
            logger.debug("Touch thread started");

            is = socket.getInputStream();

            while (true) {
                if (!readFully(is, lengthBuffer, INITIAL_MESSAGE_LENGTH)) {
                    return;
                }

                view.initialize(lengthBuffer);
                messageLength = view.getInt();

                if (!readFully(is, messageBuffer, messageLength)) {
                    return;
                }

                view.initialize(messageBuffer);
                timestamp = view.getDouble();

                numTouches = view.getInt();

                for (int i = 0; i < numTouches; i++) {
                    points[i].setId(view.getInt());
                    points[i].setState(view.getInt());
                    points[i].setTx(view.getFloat());
                    points[i].setTy(view.getFloat());
                }

                if (handler != null) {
                    handler.onTouchPoints(timestamp, points, numTouches);
                }
            }

        } catch (Exception e) {
            logger.error("Exception: " + e);
            return;
        }
    }

    private boolean readFully(InputStream is, byte[] buffer, int n) throws IOException {
        int read;
        int offset = 0;
        int tmp = n;

        while (tmp > 0) {
            read = is.read(buffer, offset, tmp);
            if (read == -1) {
                logger.error("readFully(): read = -1");
                return false;
            }
            tmp -= read;
            offset += read;
        }

        return true;
    }
}
