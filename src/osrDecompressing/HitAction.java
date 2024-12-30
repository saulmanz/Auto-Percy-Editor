package osrDecompressing;

import java.util.List;

public class HitAction {
  public int timeSinceLastHit;
  public List<String> keysPressed;

  public HitAction(String textOfHit) {
    String[] splitText = textOfHit.split("\\|");

    timeSinceLastHit = Integer.parseInt(splitText[0]);
    keysPressed = findKeysPressed(Integer.parseInt(splitText[1]));
  }

  private List<String> findKeysPressed(int keyValue) {
    return switch (keyValue) {
      case 1 -> List.of("key1");
      case 2 -> List.of("key2");
      case 3 -> List.of("key1", "key2");
      case 4 -> List.of("key3");
      case 5 -> List.of("key1", "key3");
      case 6 -> List.of("key2", "key3");
      case 7 -> List.of("key1", "key2", "key3");
      case 8 -> List.of("key4");
      case 9 -> List.of("key1", "key4");
      case 10 -> List.of("key2", "key4");
      case 11 -> List.of("key1", "key2", "key4");
      case 12 -> List.of("key3", "key4");
      case 13 -> List.of("key1", "key3", "key4");
      case 14 -> List.of("key2", "key3", "key4");
      case 15 -> List.of("key1", "key2", "key3", "key4");
      default -> List.of();
    };
  }

  @Override
  public String toString() {
    return "hitAction{" +
            "keysPressed=" + keysPressed +
            ", timeSinceLastHit=" + timeSinceLastHit +
            '}';
  }
}
