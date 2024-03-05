package org.jpass.utils;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import org.jpass.database.IterateHistory;

/** Class to listen for key presses using library Jnativehook*/
public class KeyListenerUtil implements NativeKeyListener {
    boolean[] keys = new boolean[]{false, false, false, false}; // <Ctrl>, B, P, D

    /** Constructor */
    public KeyListenerUtil() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Unable to get regional keyboard layout: " + ex.getMessage());
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    /**
     * Check for key presses - &lt;Ctrl&gt; + B, &lt;Ctrl&gt; + P, &lt;Ctrl&gt; + D, Up, Down
     * @param e NativeKeyEvent
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == 29) {
            keys[0] = true;
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("B")) {
            keys[1] = true;
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("P")) {
            keys[2] = true;
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("D")) {
            keys[3] = true;
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Up")) {
            IterateHistory.updateHistory();
            IterateHistory.up();
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {
            IterateHistory.updateHistory();
            IterateHistory.down();
        }

        // Copy username: <Ctrl> + B
        if (keys[0] && keys[1]) {
            String credToCopy = ConfigGetter.getLastRead()[0];
            CopyToClipboard.copy(credToCopy);
        }

        // Copy password: <Ctrl> + P
        if (keys[0] && keys[2]) {
            String credToCopy = ConfigGetter.getLastRead()[1];
            CopyToClipboard.copy(credToCopy);
        }

        // Exit program: <Ctrl> + D
        if (keys[0] && keys[3]) {
            ExitProgram.exit(true);
        }
    }


    /**
     * Check for key releases - &lt;Ctrl&gt;, B, P, D
     * @param e NativeKeyEvent
     */
    @Override
    public void nativeKeyReleased(NativeKeyEvent e){
        if (e.getKeyCode() == 29) {
            keys[0] = false;
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("B")) {
            keys[1] = false;
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("P")) {
            keys[2] = false;
        } if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("D")) {
            keys[3] = false;
        }
    }
}
