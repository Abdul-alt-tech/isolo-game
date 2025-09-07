package com.isolo.util;

import javafx.scene.media.AudioClip;
import java.net.URL;

public class SoundManager {
    private static AudioClip moveSound;
    private static AudioClip captureSound;
    private static AudioClip errorSound;
    
    static {
        try {
            moveSound = new AudioClip(getResourcePath("/sounds/move.mp3"));
            captureSound = new AudioClip(getResourcePath("/sounds/capture.mp3"));
            errorSound = new AudioClip(getResourcePath("/sounds/error.mp3"));
        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }
    
    private static String getResourcePath(String resource) {
        URL url = SoundManager.class.getResource(resource);
        return (url != null) ? url.toString() : "";
    }
    
    public static void playMoveSound() {
        if (moveSound != null) {
            moveSound.play();
        }
    }
    
    public static void playCaptureSound() {
        if (captureSound != null) {
            captureSound.play();
        }
    }
    
    public static void playErrorSound() {
        if (errorSound != null) {
            errorSound.play();
        }
    }
}
