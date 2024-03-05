package org.jpass.database;

import org.jpass.utils.ConfigGetter;
import org.jpass.utils.CopyToClipboard;

import java.util.ArrayList;

/** Iterate through history */
public class IterateHistory {
    private static ArrayList<String> history = ConfigGetter.getHistory();
    private static int i = history.size()-1;

    /**
     * Move up in history - copy the command to clipboard
     * The index i is decremented */
    public static void up() {
        if (i > 0) {
            CopyToClipboard.copy(history.get(i--), 0);
        }
    }

    /**
     * Move down in history - copy the command to clipboard
     * The index i is incremented */
    public static void down() {
        if (i < history.size() - 1) {
            CopyToClipboard.copy(history.get(i++), 0);
        }
    }

    /** Update history - get history from config */
    public static void updateHistory() {
        history = ConfigGetter.getHistory();
    }

    /** Update index */
    public static void updateIndex() {
        i = history.size()-1;
    }
}
