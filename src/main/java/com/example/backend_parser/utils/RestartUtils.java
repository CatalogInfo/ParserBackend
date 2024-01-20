package com.example.backend_parser.utils;

import java.awt.*;
import java.awt.event.KeyEvent;

public class RestartUtils {
    public static void restartApp() {
        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        r.keyPress(KeyEvent.VK_SHIFT);
        r.keyPress(KeyEvent.VK_F10);
        r.keyRelease(KeyEvent.VK_SHIFT);
        r.keyRelease(KeyEvent.VK_F10);
    }
}
