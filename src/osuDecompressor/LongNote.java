package osuDecompressor;

public record LongNote(String key, int endTime) {

  @Override
  public String toString() {
    return "key=" + key + ", endTime=" + endTime +"\n";
  }
}
