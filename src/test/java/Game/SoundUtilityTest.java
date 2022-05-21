package Game;

import Game.SoundUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoundUtilityTest {

    private static SoundUtility soundUtility;

    @BeforeAll
    static void beforeAll() {
        soundUtility.SoundUtilityInit();
    }

    @Test
    void checkSoundFiles() {
        soundUtility.getAllSoundMap().forEach((k, v) -> {
            if (v.getFrameLength() == 0) {
                assertEquals(true, false);
                System.out.println("waw file not found " + k);
            }
        });

    }
}