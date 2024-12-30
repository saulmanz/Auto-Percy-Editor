package gui;

import ImageAdjuster.AutoPercyAdjuster;
import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import osrDecompressing.OsrParser;
import osuDecompressor.OsuParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

  @FXML
  private Text messageText;
  @FXML
  private Text osrText;
  @FXML
  private Text osuText;
  @FXML
  private Text lnTailText;

  private final AutoPercyAdjuster autoAdjuster = new AutoPercyAdjuster();
  private Stage stage;

  @FXML
  private void handleDragOver(DragEvent event) {
    if (event.getDragboard().hasFiles()) {
      event.acceptTransferModes(TransferMode.ANY);
    }
  }

  private void changeText(Text textBox, File fileToChangeTo) {
    String[] list = fileToChangeTo.getAbsolutePath().split("\\\\");
    String visualPath = list[list.length - 1];
    if (visualPath.length() > 40) {
      visualPath = visualPath.substring(0, 40) + "...";
    }
    textBox.setText(visualPath);
  }

  /**
   * Handles any files dropped on top of either the osrButton or the osrText.
   * @param event anything that is dragged onto the file
   */
  public void handleOsrButtonDrop(DragEvent event) {
    List<File> files = event.getDragboard().getFiles();

    try {
      messageText.setText("");
      System.out.println("OSR Inserted");

      OsrParser data = new OsrParser();
      File filePath = files.getFirst();

      if (!filePath.getAbsolutePath().contains(".osr")) {
        throw new IllegalArgumentException();
      }

      String result = data.parseOsrFile(filePath);
      autoAdjuster.setOsr(data);
      changeText(osrText, filePath);
    } catch (IOException | NullPointerException | IllegalArgumentException e) {
      messageText.setText(".osr file was incorrect");
    }
  }

  /**
   * Asks user for a file and changes the osr being used. Makes
   * sure that the file is a .osr file.
   */
  public void osrButton() {
    try {
      messageText.setText("");
      System.out.println("OSR button clicked");
      FileChooser fileChooser = new FileChooser();

      OsrParser data = new OsrParser();
      File filePath = fileChooser.showOpenDialog(stage);

      if (!filePath.getAbsolutePath().contains(".osr")) {
        throw new IllegalArgumentException();
      }

      String result = data.parseOsrFile(filePath);
      autoAdjuster.setOsr(data);
      changeText(osrText, filePath);
    } catch (IOException | NullPointerException | IllegalArgumentException e) {
      messageText.setText(".osr file was incorrect");
    }
  }

  /**
   * Handles any files dropped on top of either the osuButton or the osuText.
   * @param event anything that is dragged onto the file
   */
  public void handleOsuButtonDrop(DragEvent event) {
    List<File> files = event.getDragboard().getFiles();

    try {
      messageText.setText("");
      System.out.println("OSU inserted");

      File filePath = files.getFirst();

      if (!filePath.getAbsolutePath().contains(".osu")) {
        throw new IllegalArgumentException();
      }

      autoAdjuster.setOsu(new OsuParser(filePath));
      changeText(osuText, filePath);
    } catch (RuntimeException e) {
      messageText.setText(".osu file was incorrect");
    }
  }

  /**
   * Asks user for a file and changes the osu being used. Makes
   * sure that the file is a .osu file.
   */
  public void osuButton() {
    try {
      messageText.setText("");
      System.out.println("OSU button clicked");
      FileChooser fileChooser = new FileChooser();

      File filePath = fileChooser.showOpenDialog(stage);

      if (!filePath.getAbsolutePath().contains(".osu")) {
        throw new IllegalArgumentException();
      }

      autoAdjuster.setOsu(new OsuParser(filePath));
      changeText(osuText, filePath);
    } catch (RuntimeException e) {
      messageText.setText(".osu file was incorrect");
    }
  }

  /**
   * Handles any files dropped on top of either the lnTailButton or the lnTailText.
   * @param event anything that is dragged onto the file
   */
  public void handleLNTailButtonDrop(DragEvent event) {
    List<File> files = event.getDragboard().getFiles();

    try {
      messageText.setText("");
      System.out.println("LN Tail button clicked");

      File filePath = files.getFirst();

      if (!filePath.getAbsolutePath().contains(".png") && !filePath.getAbsolutePath().contains(".jpg")) {
        throw new IllegalArgumentException();
      }

      autoAdjuster.setOriginalImage(filePath);
      changeText(lnTailText, filePath);
    } catch (IOException | IllegalArgumentException | NullPointerException e) {
      messageText.setText("ln tail file was incorrect");
    }
  }

  /**
   * Asks user for a file and changes the ln tail being used. Makes
   * sure that the file is a .png or .jpg.
   */
  public void lnTailButton() {
    try {
      messageText.setText("");
      System.out.println("LN Tail button clicked");
      FileChooser fileChooser = new FileChooser();

      File filePath = fileChooser.showOpenDialog(stage);

      if (!filePath.getAbsolutePath().contains(".png") && !filePath.getAbsolutePath().contains(".jpg")) {
        throw new IllegalArgumentException();
      }

      autoAdjuster.setOriginalImage(filePath);
      changeText(lnTailText, filePath);
    } catch (IOException | IllegalArgumentException | NullPointerException e) {
      messageText.setText("ln tail file was incorrect");
    }
  }

  /**
   * Generates the new long note tail for the user to longNotes folder.
   */
  public void generateButton() {
    try {
      messageText.setText("");
      System.out.println("Generate LN Tail button clicked");
      autoAdjuster.changeImage();
      messageText.setText("New ln tail has been saved to longNotes/" + autoAdjuster.getOriginalImageFileName());
    } catch (IOException | NullPointerException e) {
      messageText.setText("You are missing files");
    } catch (IndexOutOfBoundsException e) {
      messageText.setText("Your replay does not line up with your .osu ");
    }
  }
}
