package Skybreaker;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class Sounds {
    static void makeSoundsGreatAgain() {
        makeBlockSoundsGreatAgain();
        makeBackgroundSoundsGreatAgain();
        makeWallSoundsGreatAgain();
        makeStarSoundsGreatAgain();
        makeThunderSoundsGreatAgain();
    }
    //------------------------------------------------------------------------------------------------------------------
    static String thunderPath = "Sounds/Thunder.wav";
    static AudioClip thunderAudioClip = new AudioClip(new File(thunderPath).toURI().toString());
    static void makeThunderSoundsGreatAgain() {
        thunderAudioClip.setVolume(0.5);
    }
    static public void thunderSound() {
        thunderAudioClip.play();
    }
    //------------------------------------------------------------------------------------------------------------------
    static String blockPath = "Sounds/drop_001.wav";
    static AudioClip blockAudioClip = new AudioClip(new File(blockPath).toURI().toString());
    static void makeBlockSoundsGreatAgain() {
        blockAudioClip.setVolume(0.5);
    }
    static public void blockSound() {
        blockAudioClip.play();
    }
    //------------------------------------------------------------------------------------------------------------------
    static String wallPath = "Sounds/drop_004.wav";
    static AudioClip wallAudioClip = new AudioClip(new File(wallPath).toURI().toString());
    static void makeWallSoundsGreatAgain() {
        wallAudioClip.setVolume(0.5);
    }
    static public void wallSound() {
        wallAudioClip.play();
    }
    //------------------------------------------------------------------------------------------------------------------
    static String starPath = "Sounds/starsound.wav";
    static AudioClip starAudioClip = new AudioClip(new File(starPath).toURI().toString());
    static void makeStarSoundsGreatAgain() {
        starAudioClip.setVolume(0.5);
    }
    static public void starSound() {
        starAudioClip.play();
    }
    //------------------------------------------------------------------------------------------------------------------
    static String path = "Sounds/airtone_-_tokyoStreet.wav";
    static Media media = new Media(new File(path).toURI().toString());
    static void makeBackgroundSoundsGreatAgain() {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
    }
}
