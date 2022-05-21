package Game;

import Log.SLF4J;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.HashMap;

public class SoundUtility {

    private static HashMap<String, Clip> allSoundMap = new HashMap<>();

    private static SoundUtility soundUtility;

    private SoundUtility() {

        loadClipToMap("startStageClip", "sound/stage_start.wav");
        loadClipToMap("gameOverClip", "sound/game_over.wav");
        loadClipToMap("pauseClip", "sound/pause.wav");
        loadClipToMap("tankStopped", "sound/tankStopped.wav");
        loadClipToMap("tankMoving", "sound/tankMoving.wav");
        loadClipToMap("fire", "sound/bullet_shot.wav");
        loadClipToMap("bulletBrickClip", "sound/bullet_hit_2.wav");
        loadClipToMap("powerUpPickClip", "sound/powerup_appear.wav");
        loadClipToMap("explosion1Clip", "sound/explosion_1.wav");
        loadClipToMap("explosion2Clip", "sound/explosion_2.wav");
        loadClipToMap("statisticsClip", "sound/statistics_1.wav");

    }

    public static HashMap<String, Clip> getAllSoundMap() {
        return allSoundMap;
    }

    public static void loadClipToMap(String key, String pathname) {
        allSoundMap.put(key, loadClip(pathname));
    }

    public static void stopAllSound() {
        allSoundMap.forEach( (k, v) -> v.stop());
    }

    public static Clip getClipFromMap(String key) {
        return allSoundMap.get(key);
    }

    private static Clip loadClip(String pathname) {

        Clip clip = null;

        try {

           File srcFile = new File(pathname);
           clip = AudioSystem.getClip();
           AudioInputStream ais = AudioSystem.getAudioInputStream(srcFile);
           clip.open(ais);
           clip.setFramePosition(clip.getFrameLength());

       } catch (Exception e) {
           SLF4J.getLogger().error("loadClip {} error {}", pathname, e.getMessage());
       }

       return clip;
    }

    public static void SoundUtilityInit() {
        if (soundUtility == null) {
            soundUtility = new SoundUtility();
        };
    }

    public static void startStage() {
        SoundUtilityInit();
        stopAllSound();
        getClipFromMap("startStageClip").loop(1);
    }

    public static void gameOver() {
        SoundUtilityInit();
        stopAllSound();
        getClipFromMap("gameOverClip").loop(1);

    }

    public static void pause() {
        SoundUtilityInit();
        stopAllSound();
        getClipFromMap("pauseClip").loop(1);
    }

    public static void tankStopped() {
        SoundUtilityInit();
        getClipFromMap("tankMoving").stop();
        //tankStopped.loop(1);
    }
    public static void tankMoving() {
        SoundUtilityInit();
        //getClipFromMap("tankStopped").stop();
        getClipFromMap("tankMoving").loop(1);
    }

    public static void fire() {
        SoundUtilityInit();
        getClipFromMap("fire").loop(1);
    }

    public static void bulletHitBrick() {
        SoundUtilityInit();
        getClipFromMap("bulletBrickClip").loop(1);
    }

    public static void powerUpPick() {
        SoundUtilityInit();
        getClipFromMap("powerUpPickClip").loop(1);
    }

    public static void explosion1() {
        SoundUtilityInit();
        getClipFromMap("explosion1Clip").loop(1);

    }

    public static void explosion2() {
        SoundUtilityInit();
        getClipFromMap("explosion2Clip").loop(1);

    }

    public static void statistics() {
        SoundUtilityInit();
        getClipFromMap("statisticsClip").loop(1);

    }
}
