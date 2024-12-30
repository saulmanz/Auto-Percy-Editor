package ImageAdjuster;

import osrDecompressing.HitAction;
import osrDecompressing.OsrParser;
import osrDecompressing.OsrReplaying;
import osuDecompressor.OsuParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;

public class AutoPercyAdjuster {
  private OsrParser osr;
  private OsuParser osu;
  private BufferedImage originalImage;
  private String originalImageFileName;

  private double getAverageMillisecondsOff() throws IOException {
    String keyEvents = osr.decompressToText(osr.replayData);
    String[] keyEventArray = keyEvents.split(",");

    List<HitAction> actions = new ArrayList<>();

    for (String keyEvent : keyEventArray) {
      actions.add(new HitAction(keyEvent));
    }

    OsrReplaying replay = new OsrReplaying();

    return replay.getMillisecondsOff(actions, osu.getListOfLns());
  }

  /**
   * Removes or adds pixels to the image given by the user based on how
   * early or late the long note are being released.
   * @throws IOException when longNotes folder cannot be found
   */
  public void changeImage() throws IOException {
    double average = floor(getAverageMillisecondsOff() / .25);
    ImageEditor editor = new ImageEditor(originalImage);
    if (average > 0.0) {
      editor.removeColumn((int) average * -1);
    } else if (average < 0.0) {
      editor.addColumn((int) average);
    } else {
      System.out.println("You are inhuman");
    }

    try {
      String OUTPUT_PATH = "longNotes/" + originalImageFileName;
      File newFile = new File(OUTPUT_PATH);
      ImageIO.write(editor.image, "png", newFile);
      System.out.println("Altered image stored to " + OUTPUT_PATH);
    } catch (IOException e) {
      System.out.println("Unable to save file.");
    }
  }

  /**
   * @param osr which will be set for the class.
   */
  public void setOsr(OsrParser osr) {
    this.osr = osr;
  }

  /**
   * @param osu which will be set for the class.
   */
  public void setOsu(OsuParser osu) {
    this.osu = osu;
  }

  /**
   * @param originalImage which will be set for the class.
   * @throws IOException when the file cannot be open
   */
  public void setOriginalImage(File originalImage) throws IOException {
    try {
      String[] list = originalImage.getAbsolutePath().split("\\\\");
      this.originalImageFileName = list[list.length - 1];

      this.originalImage = ImageIO.read(originalImage);
    } catch (IOException e) {
      System.out.println("Unable to open " + originalImage);
    }
  }

  /**
   * @return name of the file path.
   */
  public String getOriginalImageFileName() {
    return originalImageFileName;
  }
}
