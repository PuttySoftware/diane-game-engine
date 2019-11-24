/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.loaders;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.puttysoftware.errorlogger.ErrorLogger;

public class SoundLoader {
  private SoundLoader() {
    // Do nothing
  }

  private static final int BUFFER_SIZE = 4096; // 4Kb

  public static void play(final URL soundURL, final ErrorLogger errorHandler) {
    new Thread() {
      @Override
      public void run() {
        try (AudioInputStream audioInputStream = AudioSystem
            .getAudioInputStream(soundURL)) {
          final AudioFormat format = audioInputStream.getFormat();
          final DataLine.Info info = new DataLine.Info(SourceDataLine.class,
              format);
          try (SourceDataLine auline = (SourceDataLine) AudioSystem
              .getLine(info)) {
            auline.open(format);
            auline.start();
            int nBytesRead = 0;
            final byte[] abData = new byte[SoundLoader.BUFFER_SIZE];
            try {
              while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                  auline.write(abData, 0, nBytesRead);
                }
              }
            } catch (final IOException e) {
              errorHandler.logError(e);
            } finally {
              auline.drain();
            }
          } catch (final LineUnavailableException e) {
            errorHandler.logError(e);
          }
        } catch (final UnsupportedAudioFileException e) {
          errorHandler.logError(e);
        } catch (final IOException e) {
          errorHandler.logError(e);
        }
      }
    }.start();
  }
}