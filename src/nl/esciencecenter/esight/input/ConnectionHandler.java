package nl.esciencecenter.esight.input;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/* Copyright [2013] [Netherlands eScience Center]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
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
        // InputStream.read(buf, offs, len)
        InputStream is;

        byte[] length_buffer = new byte[4];
        byte[] message_buffer = new byte[2048];
        int message_length;

        int num_touches;

        ByteBufferView view = new ByteBufferView(length_buffer);

        double timestamp;

        TouchPoint[] points;

        // XXX nicely hardcoded :)
        points = new TouchPoint[MAX_TOUCH_EVENTS];
        for (int i = 0; i < MAX_TOUCH_EVENTS; i++)
            points[i] = new TouchPoint();

        try {
            System.out.println("Touch thread started");

            is = socket.getInputStream();

            // readFully(is, header_buffer, 6);

            while (true) {
                if (!readFully(is, length_buffer, INITIAL_MESSAGE_LENGTH))
                    return;

                view.initialize(length_buffer);
                message_length = view.getInt();
                // System.out.println("message_length: "+message_length);

                if (!readFully(is, message_buffer, message_length))
                    return;

                view.initialize(message_buffer);
                timestamp = view.getDouble();
                // System.out.println("timestamp "+timestamp);
                num_touches = view.getInt();
                // System.out.println("num touches "+num_touches);

                for (int i = 0; i < num_touches; i++) {
                    points[i].id = view.getInt();
                    points[i].state = view.getInt();
                    points[i].tx = view.getFloat();
                    points[i].ty = view.getFloat();
                }

                if (handler != null)
                    handler.OnTouchPoints(timestamp, points, num_touches);
            }

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return;
        }
    }

    private boolean readFully(InputStream is, byte[] buffer, int n)
            throws IOException {
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
