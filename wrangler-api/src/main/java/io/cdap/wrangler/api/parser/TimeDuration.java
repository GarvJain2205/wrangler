package io.cdap.wrangler.api.parser;

/**
 * Represents a time duration (e.g., "5s", "100ms").
 */
public class TimeDuration {
  private final long milliseconds;

  /**
   * Constructs a TimeDuration object and parses the input string into milliseconds.
   *
   * @param value the raw string (e.g., "2s", "100ms")
   */
  public TimeDuration(String value) {
    this.milliseconds = parseDuration(value);
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  /**
   * Parses the time string into milliseconds.
   */
  private long parseDuration(String value) {
    value = value.trim().toLowerCase();

    if (value.endsWith("ms")) {
      return Long.parseLong(value.replace("ms", ""));
    } else if (value.endsWith("s")) {
      return Long.parseLong(value.replace("s", "")) * 1000;
    } else if (value.endsWith("m")) {
      return Long.parseLong(value.replace("m", "")) * 60 * 1000;
    } else if (value.endsWith("h")) {
      return Long.parseLong(value.replace("h", "")) * 60 * 60 * 1000;
    } else if (value.endsWith("d")) {
      return Long.parseLong(value.replace("d", "")) * 24 * 60 * 60 * 1000;
    } else {
      throw new IllegalArgumentException("Invalid time duration format: " + value);
    }
  }
}
