package cat.app.tiledom.GUI;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class ImplementedAudioManager implements AudioManager {
       private Clip currentClip;

    @Override
    public void startMusic(String track) {
        stopMusic(); 

        try {
            String path = "/assets/audio/" + track + ".wav";
            URL soundURL = getClass().getResource(path);

            if (soundURL == null) {
                System.err.println("No s'ha trobat l'arxiu d'àudio: " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            //currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            currentClip.start();

            System.out.println("Reproduint: " + track);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Format d'àudio no suportat: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Dispositiu d'àudio no disponible: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de lectura d'àudio: " + e.getMessage());
        }
    }

    @Override
    public void stopMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
            currentClip.close();
            currentClip = null;
            System.out.println("Música aturada");
        }
    }
}
