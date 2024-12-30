package osuDecompressor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class OsuParser {
  private final List<LongNote> listOfLns;

  /**
   * A parser of the osu file which only shows the long notes of the maps.
   * @param osuFile the file for the .osu
   */
  public OsuParser(File osuFile) {
    try {
      Scanner scanner = new Scanner(osuFile);
      List<String> allNotes = new ArrayList<>();
      boolean isHitObjects = false;

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();

        if (isHitObjects) {
          allNotes.add(line);
        }

        if (line.contains("[HitObjects]")) {
          isHitObjects = true;
        }
      }

      listOfLns = removeRegularNotes(allNotes);

    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private String changePositionToKeyString(String keyPosition) {
    int positionValue = Integer.parseInt(keyPosition);
    return switch (positionValue) {
      case 64 -> "key1";
      case 192 -> "key2";
      case 320 -> "key3";
      case 448 -> "key4";
      default -> "";
    };
  }

  private List<LongNote> removeRegularNotes(List<String> list) {
    List<LongNote> result = new ArrayList<>();

    for (String s : list) {
      String[] split = s.split(",");
      if (Objects.equals(split[3], "128")) {
        String[] split2 = split[5].split(":");
        result.add(new LongNote(
                changePositionToKeyString(split[0]), Integer.parseInt(split2[0]))
        );
      }
    }

    return result;
  }

  public List<LongNote> getListOfLns() {
    return listOfLns;
  }
}
