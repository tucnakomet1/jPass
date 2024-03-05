package org.jpass.utils;

import java.awt.*;
import java.awt.datatransfer.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Copy text to clipboard */
public class CopyToClipboard {

    /**
     * Copy text to clipboard (seconds are loaded from config)
     * @param text text to copy
     */
    public static void copy(String text) {
        int seconds = ConfigGetter.getSeconds();
        copy(text, seconds);
    }

    /**
     * Copy text to clipboard
     * @param text text to copy
     * @param seconds time to remove clipboard
     */
    public static void copy(String text, int seconds) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        if (seconds != 0) removeClipboardAfter(seconds);
    }

    /**
     * Remove clipboard after x seconds - using thread
     * @param seconds time to remove clipboard
     */
    public static void removeClipboardAfter(int seconds) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(() -> {
            StringSelection selection = new StringSelection("");
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            executorService.shutdown();
        }, seconds, TimeUnit.SECONDS);
    }
}
