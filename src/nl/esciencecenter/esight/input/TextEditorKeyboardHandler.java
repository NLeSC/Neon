package nl.esciencecenter.esight.input;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.media.opengl.GLException;

/* Copyright 2013 Netherlands eScience Center
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
 * Keyboard input handler for a text editor interface.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class TextEditorKeyboardHandler extends InputHandler {
    private ArrayList<String> textLines = new ArrayList<String>();
    private ArrayList<String> clipBoard = new ArrayList<String>();
    private final ArrayList<ArrayList<String>> undoSave = new ArrayList<ArrayList<String>>();

    private int cursorPosition = 0;
    private int linePosition = 0;
    private int screenPosition = 0;

    private int selectionLineStartPosition = 0;
    private int selectionLineStopPosition = 0;
    private int selectionCursorStartPosition = 0;
    private int selectionCursorStopPosition = 0;

    private final int MAX_SCREEN_POSITION = 30;

    private static class SingletonHolder {
        public static final TextEditorKeyboardHandler instance = new TextEditorKeyboardHandler();
    }

    public static TextEditorKeyboardHandler getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("unchecked")
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
            if (undoSave.size() != 0) {
                textLines = undoSave.remove(undoSave.size() - 1);
            }
        } else if (e.getKeyCode() != KeyEvent.VK_CONTROL && !isMovementKey(e)) {
            undoSave.add((ArrayList<String>) textLines.clone());
        }

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (isAnythingSelected()) {
                deleteSection(selectionLineStartPosition, selectionLineStopPosition, selectionCursorStartPosition,
                        selectionCursorStopPosition);
            } else {
                if (cursorPosition == 0) {
                    if (linePosition != 0) {
                        linePosition -= 1;

                        cursorPosition = textLines.get(linePosition).length();
                        String start = textLines.get(linePosition).substring(0, cursorPosition);
                        String finish = textLines.get(linePosition + 1);

                        textLines.set(linePosition, start + finish);
                        textLines.remove(linePosition + 1);
                    }
                } else {
                    replaceCharAt(linePosition, cursorPosition - 1, "");
                    cursorPosition -= 1;
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
            if (isAnythingSelected()) {
                deleteSection(selectionLineStartPosition, selectionLineStopPosition, selectionCursorStartPosition,
                        selectionCursorStopPosition);
            } else {
                if (cursorPosition < textLines.get(linePosition).length()) {
                    replaceCharAt(linePosition, cursorPosition, "");
                } else {
                    String start = "", finish = "";
                    start = textLines.get(linePosition);
                    if (linePosition + 1 < textLines.size()) {
                        finish = textLines.get(linePosition + 1);
                        textLines.remove(linePosition + 1);
                    } else {
                        finish = "";
                    }
                    textLines.set(linePosition, start + finish);
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_TAB) {
            if (isAnythingSelected()) {
                deleteSection(selectionLineStartPosition, selectionLineStopPosition, selectionCursorStartPosition,
                        selectionCursorStopPosition);
            }

            insertStringAt(linePosition, cursorPosition, "    ");
            cursorPosition += 4;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (isAnythingSelected()) {
                deleteSection(selectionLineStartPosition, selectionLineStopPosition, selectionCursorStartPosition,
                        selectionCursorStopPosition);
            }
            String start = textLines.get(linePosition).substring(0, cursorPosition);
            String finish = textLines.get(linePosition).substring(cursorPosition);

            textLines.set(linePosition, start);
            textLines.add(linePosition + 1, finish);
            linePosition += 1;
            cursorPosition = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            saveShader("FinishedShader");
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (cursorPosition == 0) {
                linePosition--;
                cursorPosition = 100000;
            } else {
                cursorPosition--;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (cursorPosition == textLines.get(linePosition).length()) {
                linePosition++;
                cursorPosition = 0;
            } else {
                cursorPosition++;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            linePosition -= 1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            linePosition += 1;
        } else if (e.getKeyCode() == KeyEvent.VK_HOME) {
            cursorPosition = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_END) {
            cursorPosition = textLines.get(linePosition).length();
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            linePosition -= MAX_SCREEN_POSITION;
        } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            linePosition += MAX_SCREEN_POSITION;
        } else if (!e.isActionKey()) {
            if (e.getKeyCode() != KeyEvent.VK_SHIFT && e.getKeyCode() != KeyEvent.VK_CONTROL
                    && e.getKeyCode() != KeyEvent.VK_ALT && !e.isControlDown()) {
                if (isAnythingSelected()) {
                    deleteSection(selectionLineStartPosition, selectionLineStopPosition, selectionCursorStartPosition,
                            selectionCursorStopPosition);
                }

                insertStringAt(linePosition, cursorPosition, "" + e.getKeyChar());
                cursorPosition += 1;
            }
        }

        // TEST ALL BORDER CASES
        if (linePosition < 0)
            linePosition = 0;
        if (linePosition > textLines.size() - 1)
            linePosition = textLines.size() - 1;
        if (cursorPosition < 0)
            cursorPosition = 0;
        if (cursorPosition > textLines.get(linePosition).length())
            cursorPosition = textLines.get(linePosition).length();

        if (linePosition < screenPosition)
            screenPosition = linePosition;

        if (linePosition > MAX_SCREEN_POSITION) {
            if (linePosition - MAX_SCREEN_POSITION > screenPosition) {
                screenPosition = linePosition - MAX_SCREEN_POSITION;
            }
        }

        if (screenPosition < 0)
            screenPosition = 0;
        if (screenPosition > textLines.size() - 1)
            screenPosition = textLines.size() - 1;

        // SELECTION HANDLING
        handleSelection(e);
    }

    private ArrayList<String> getSelectedLines() {
        // swap order in weird selection cases
        int temp;
        int startLineIndex = selectionLineStartPosition;
        int stopLineIndex = selectionLineStopPosition;
        int startCursorIndex = selectionCursorStartPosition;
        int stopCursorIndex = selectionCursorStopPosition;

        if (stopLineIndex < startLineIndex) {
            temp = stopLineIndex;
            stopLineIndex = startLineIndex;
            startLineIndex = temp;

            temp = stopCursorIndex;
            stopCursorIndex = startCursorIndex;
            startCursorIndex = temp;
        } else if (stopLineIndex == startLineIndex) {
            if (stopCursorIndex < startCursorIndex) {
                temp = stopCursorIndex;
                stopCursorIndex = startCursorIndex;
                startCursorIndex = temp;
            }
        }

        ArrayList<String> newSelectionLines = new ArrayList<String>();

        // select first line, from startCursor to end of line, or to
        // endCursor
        String selectionFirstLine = "";
        int pos = 0;
        for (Character c : textLines.get(startLineIndex).toCharArray()) {
            if (stopLineIndex != startLineIndex) {
                if (pos >= startCursorIndex) {
                    selectionFirstLine += c;
                }
            } else {
                if (pos >= startCursorIndex && pos < stopCursorIndex) {
                    selectionFirstLine += c;
                }
            }
            pos++;
        }
        newSelectionLines.add(selectionFirstLine);

        // Add intermediate lines fully
        for (int lineNR = startLineIndex + 1; lineNR < stopLineIndex; lineNR++) {
            if (lineNR < stopLineIndex) {
                newSelectionLines.add(textLines.get(lineNR));
            }
        }

        // Add the last line until the cursor position
        if (startLineIndex != stopLineIndex) {
            String selectionLastLine = "";
            pos = 0;
            for (Character c : textLines.get(stopLineIndex).toCharArray()) {
                if (pos < stopCursorIndex) {
                    selectionLastLine += c;
                }
                pos++;
            }
            newSelectionLines.add(selectionLastLine);
        }

        return newSelectionLines;
    }

    private void handleSelection(KeyEvent e) {
        if (e.isControlDown()) {
            if (e.getKeyCode() == KeyEvent.VK_C) {
                if (isAnythingSelected()) {
                    clipBoard = getSelectedLines();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_X) {
                if (isAnythingSelected()) {
                    clipBoard = getSelectedLines();
                    deleteSection(selectionLineStartPosition, selectionLineStopPosition, selectionCursorStartPosition,
                            selectionCursorStopPosition);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_V) {
                pasteClipboard();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            if (!isAnythingSelected()) {
                selectionCursorStartPosition = cursorPosition;
                selectionLineStartPosition = linePosition;
            }
        } else if (e.isShiftDown() && isMovementKey(e)) {
            selectionCursorStopPosition = cursorPosition;
            selectionLineStopPosition = linePosition;
        } else if (e.getKeyCode() != KeyEvent.VK_CONTROL && !(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C)) {
            selectionCursorStartPosition = cursorPosition;
            selectionLineStartPosition = linePosition;
            selectionCursorStopPosition = cursorPosition;
            selectionLineStopPosition = linePosition;
        }
    }

    private void pasteClipboard() {
        if (clipBoard.size() > 0) {
            ArrayList<String> result = new ArrayList<String>();

            // First, add all of the lines prior to the line position
            for (int i = 0; i < linePosition; i++) {
                result.add(textLines.get(i));
            }

            // Add all of the characters on the current line prior to the cursor
            // position
            // And add all of the characters of the first line of the clipboard
            String startCharactersCurrentline = "", finishCharactersCurrentline = "";
            int pos = 0;
            for (Character c : textLines.get(linePosition).toCharArray()) {
                if (pos < cursorPosition) {
                    startCharactersCurrentline += c;
                } else {
                    finishCharactersCurrentline += c;
                }

                pos++;
            }
            if (clipBoard.size() == 1) {
                result.add(startCharactersCurrentline + clipBoard.get(0) + finishCharactersCurrentline);
                cursorPosition += clipBoard.get(clipBoard.size() - 1).length();
            } else {
                result.add(startCharactersCurrentline + clipBoard.get(0));

                // Add all of the intermediate lines on the clipboard
                for (int i = 1; i < clipBoard.size() - 1; i++) {
                    result.add(clipBoard.get(i));
                }

                // Add the remaining characters of the current line after the
                // cursor
                // position
                result.add(clipBoard.get(clipBoard.size() - 1) + finishCharactersCurrentline);
            }

            // Add all of the lines after the current line position
            for (int i = linePosition + 1; i < textLines.size(); i++) {
                result.add(textLines.get(i));
            }

            textLines = result;

            linePosition += clipBoard.size() - 1;
        }
    }

    private int posInScreenString(int lineIndex, int cursorIndex) {
        int result = 0;
        // First add the length of all lines before this one
        for (int i = screenPosition; i < lineIndex; i++) {
            result += textLines.get(i).length() + 1;
        }
        // then add the length of this line until the cursor position
        result += cursorIndex;

        return result;
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void setText(File textFile) throws FileNotFoundException {
        linePosition = 0;
        cursorPosition = 0;
        textLines = new ArrayList<String>();

        FileInputStream fstream = new FileInputStream(textFile);

        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;
        try {
            while ((strLine = br.readLine()) != null) {
                String trimmedLine = "";
                for (Character c : strLine.toCharArray()) {
                    if (c.compareTo('\t') == 0) {
                        trimmedLine += "    ";
                    } else {
                        trimmedLine += c;
                    }
                }
                textLines.add(trimmedLine);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getText() {
        String result = "";

        for (String line : textLines) {
            result += line + "\n";
        }

        return result;
    }

    public String getScreenText() {
        String result = "";

        int linePos = screenPosition;
        for (String line : textLines.subList(linePos, textLines.size())) {
            if (linePosition < linePos) {
                result += line;
            } else if (linePosition == linePos) {
                String start = "", finish = "";
                int pos = 0;
                for (Character c : line.toCharArray()) {
                    if (pos < cursorPosition) {
                        start += c;
                    } else {
                        finish += c;
                    }

                    pos++;
                }
                result += start + "|" + finish;
            } else {
                result += line;
            }
            result += "\n";
            linePos++;
        }

        return result;
    }

    public void saveShader(String fileName) {
        String path = System.getProperty("user.dir");
        File newDir = new File(path + "screenshots");
        if (!newDir.exists())
            newDir.mkdir();

        String bareName = path + "screenshots/" + fileName;

        File newFile = new File(bareName + ".txt");
        try {
            int attempt = 1;
            while (newFile.exists()) {
                String newName = bareName + " (" + attempt + ")";
                newName += ".txt";
                newFile = new File(newName);

                attempt++;
            }

            FileWriter outFile = new FileWriter(newFile);
            PrintWriter out = new PrintWriter(outFile);

            for (String line : textLines) {
                out.println(line);
            }

            out.close();
            outFile.close();
        } catch (GLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isAnythingSelected() {
        if (!(selectionLineStopPosition - selectionLineStartPosition == 0 && selectionCursorStopPosition
                - selectionCursorStartPosition == 0)) {
            return true;
        }
        return false;
    }

    public String getSelectedText() {
        // swap order in weird selection cases
        int temp;
        int startLineIndex = selectionLineStartPosition;
        int stopLineIndex = selectionLineStopPosition;
        int startCursorIndex = selectionCursorStartPosition;
        int stopCursorIndex = selectionCursorStopPosition;

        if (stopLineIndex < startLineIndex) {
            temp = stopLineIndex;
            stopLineIndex = startLineIndex;
            startLineIndex = temp;

            temp = stopCursorIndex;
            stopCursorIndex = startCursorIndex + 1;
            startCursorIndex = temp;
        } else if (stopLineIndex == startLineIndex) {
            if (stopCursorIndex < startCursorIndex) {
                temp = stopCursorIndex;
                stopCursorIndex = startCursorIndex + 1;
                startCursorIndex = temp;
            }
        }

        int startStrIndex = posInScreenString(startLineIndex, startCursorIndex);
        int stopStrIndex = posInScreenString(stopLineIndex, stopCursorIndex);

        return getText().substring(startStrIndex, stopStrIndex);
    }

    public int getSelectedTextIndex() {
        // swap order in weird selection cases
        int temp;
        int startLineIndex = selectionLineStartPosition;
        int stopLineIndex = selectionLineStopPosition;
        int startCursorIndex = selectionCursorStartPosition;
        int stopCursorIndex = selectionCursorStopPosition;

        if (stopLineIndex < startLineIndex) {
            temp = stopLineIndex;
            stopLineIndex = startLineIndex;
            startLineIndex = temp;

            temp = stopCursorIndex;
            stopCursorIndex = startCursorIndex + 1;
            startCursorIndex = temp;
        } else if (stopLineIndex == startLineIndex) {
            if (stopCursorIndex < startCursorIndex) {
                temp = stopCursorIndex;
                stopCursorIndex = startCursorIndex + 1;
                startCursorIndex = temp;
            }
        }

        return posInScreenString(startLineIndex, startCursorIndex);
    }

    public boolean[] getSelectedMask() {
        // swap order in weird selection cases
        int temp;
        int startLineIndex = selectionLineStartPosition;
        int stopLineIndex = selectionLineStopPosition;
        int startCursorIndex = selectionCursorStartPosition;
        int stopCursorIndex = selectionCursorStopPosition;

        if (stopLineIndex < startLineIndex) {
            temp = stopLineIndex;
            stopLineIndex = startLineIndex;
            startLineIndex = temp;

            temp = stopCursorIndex;
            stopCursorIndex = startCursorIndex + 1;
            startCursorIndex = temp;
        } else if (stopLineIndex == startLineIndex) {
            if (stopCursorIndex < startCursorIndex) {
                temp = stopCursorIndex;
                stopCursorIndex = startCursorIndex + 1;
                startCursorIndex = temp;
            }
        }

        boolean[] result = new boolean[getScreenText().length()];
        for (int i = 0; i < result.length; i++) {
            if (i >= posInScreenString(startLineIndex, startCursorIndex)
                    && i < posInScreenString(stopLineIndex, stopCursorIndex)) {
                result[i] = true;
            } else {
                result[i] = false;
            }
        }

        return result;
    }

    public boolean[] getUnSelectedMask() {
        boolean[] result = new boolean[getScreenText().length()];
        boolean[] inverse = getSelectedMask();
        for (int i = 0; i < result.length; i++) {
            result[i] = !inverse[i];
        }

        return result;
    }

    public boolean isMovementKey(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN || code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT
                || code == KeyEvent.VK_PAGE_UP || code == KeyEvent.VK_PAGE_DOWN || code == KeyEvent.VK_HOME
                || code == KeyEvent.VK_END) {
            return true;
        }
        return false;
    }

    private void deleteSection(int startLineIndex, int stopLineIndex, int startCursorindex, int stopCursorindex) {
        // swap order in weird selection cases
        int temp;
        if (stopLineIndex < startLineIndex) {
            temp = stopLineIndex;
            stopLineIndex = startLineIndex;
            startLineIndex = temp;
        } else if (stopLineIndex == startLineIndex) {
            if (stopCursorindex < startCursorindex) {
                temp = stopCursorindex;
                stopCursorindex = startCursorindex;
                startCursorindex = temp;
            }
        }

        String start = textLines.get(startLineIndex).substring(0, startCursorindex);
        String finish = textLines.get(stopLineIndex).substring(stopCursorindex);

        for (int i = startLineIndex; i < stopLineIndex; i++) {
            textLines.remove(i);
        }

        textLines.set(startLineIndex, start + finish);

        linePosition = startLineIndex;
        cursorPosition = startCursorindex;
    }

    private void replaceCharAt(int lineIndex, int cursorIndex, String newChar) {
        String start = textLines.get(lineIndex).substring(0, cursorIndex);
        String finish = textLines.get(lineIndex).substring(cursorIndex + 1);

        textLines.set(lineIndex, start + newChar + finish);
    }

    private void insertStringAt(int lineIndex, int cursorIndex, String newChar) {
        String start = textLines.get(lineIndex).substring(0, cursorIndex);
        String finish = textLines.get(lineIndex).substring(cursorIndex);

        textLines.set(lineIndex, start + newChar + finish);
    }

    public ArrayList<String> getTextlines() {
        return textLines;
    }
}
