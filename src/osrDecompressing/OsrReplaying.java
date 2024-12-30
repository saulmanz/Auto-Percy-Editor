package osrDecompressing;

import osuDecompressor.LongNote;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OsrReplaying {

  private List<HeldKey> changeHitsToHeldKeys(List<HitAction> hits) {
    int currentTime = hits.getFirst().timeSinceLastHit;
    // change for higher keymodes
    List<String> currentKeys = new ArrayList<>(hits.getFirst().keysPressed);
    List<HeldKey> notesHeld = new ArrayList<>();

    for (int hitIndex = 1; hitIndex < hits.size(); hitIndex++) {
      currentTime += hits.get(hitIndex).timeSinceLastHit;

      if (currentKeys != hits.get(hitIndex).keysPressed) {
        for (String currentKey : currentKeys) {
          if (!hits.get(hitIndex).keysPressed.contains(currentKey)) {
            notesHeld.add(new HeldKey(currentKey, currentTime));
          }
        }
      }

      currentKeys = hits.get(hitIndex).keysPressed;
    }

    return notesHeld;
  }

  /**
   * Finds the average time the user releases incorrectly.
   * @param hits the hits in the maps
   * @param lns the long notes present in the map
   * @return a double of the average off the user is
   */
  public double getMillisecondsOff(List<HitAction> hits, List<LongNote> lns) {
    int lnIndex = 0;
    List<Integer> millisecondsOff = new ArrayList<>();
    List<HeldKey> notesHeld = changeHitsToHeldKeys(hits);

    for (HeldKey heldKey : notesHeld) {
      LongNote currentLn = lns.get(lnIndex);
      if (heldKey.releaseTime() > currentLn.endTime() - 151
              && heldKey.releaseTime() < currentLn.endTime() + 151
              && Objects.equals(heldKey.key(), currentLn.key())
      ) {
        millisecondsOff.add(heldKey.releaseTime() - currentLn.endTime());
        lnIndex++;
      }
      if (heldKey.releaseTime() >= currentLn.endTime() + 151) {
        lnIndex++;
      }
    }

    return millisecondsOff.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
  }
}
