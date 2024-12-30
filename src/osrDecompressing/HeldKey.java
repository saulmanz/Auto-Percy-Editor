package osrDecompressing;

public record HeldKey(String key, int releaseTime) {
  @Override
  public String toString() {
    return "key=" + key + ", releaseTime=" + releaseTime +"\n";
  }
}
