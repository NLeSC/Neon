package nl.esciencecenter.esight.input;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Connection handling class. Used for Touch events for collaboratorium.
 * 
 * @author Paul Melis, SurfSARA
 */
class ConnectionHandler implements Runnable {
    private static final int INITIAL_MESSAGE_LENGTH = 4;
    private static final int MAX_TOUCH_EVENTS = 6;
    final protected Socket socket;
    final protected TouchEventHandler handler;

    public ConnectionHandler(TouchEventHandler handler, Socket socket) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        InputStream is;

        byte[] length_buffer = new byte[4];
        byte[] message_buffer = new byte[2048];
        int message_length;

        int num_touches;

        ByteBufferView view = new ByteBufferView(length_buffer);

        double timestamp;

        TouchPoint[] points;

        points = new TouchPoint[MAX_TOUCH_EVENTS];
        for (int i = 0; i < MAX_TOUCH_EVENTS; i++)
            points[i] = new TouchPoint();

        try {
            System.out.println("Touch thread started");

            is = socket.getInputStream();

            while (true) {
                if (!readFully(is, length_buffer, INITIAL_MESSAGE_LENGTH))
                    return;

                view.initialize(length_buffer);
                message_length = view.getInt();

                if (!readFully(is, message_buffer, message_length))
                    return;

                view.initialize(message_buffer);
                timestamp = view.getDouble();

                num_touches = view.getInt();

                for (int i = 0; i < num_touches; i++) {
                    points[i].setId(view.getInt());
                    points[i].setState(view.getInt());
                    points[i].setTx(view.getFloat());
                    points[i].setTy(view.getFloat());
                }

                if (handler != null)
                    handler.OnTouchPoints(timestamp, points, num_touches);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return;
        }
    }

    private boolean readFully(InputStream is, byte[] buffer, int n) throws IOException {
        int read;
        int offset = 0;

        while (n > 0) {
            read = is.read(buffer, offset, n);
            if (read == -1) {
                System.out.println("readFully(): read = -1");
                return false;
            }
            n -= read;
            offset += read;
        }

        return true;
    }
}
