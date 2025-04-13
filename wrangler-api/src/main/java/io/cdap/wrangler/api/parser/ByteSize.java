package io.cdap.wrangler.api.parser;

/**
 * Represents a parsed Byte Size value (e.g., "1MB", "512KB")
 * and provides the value in bytes.
 */
public class ByteSize {
  private final long bytes;

  /**
   * Constructs a ByteSize object and parses the input string into bytes.
   *
   * @param value the raw string (e.g., "2MB", "1024KB")
   */
  public ByteSize(String value) {
    this.bytes = parseByteSize(value);
  }

  public long getBytes() {
    return bytes;
  }

  /**
   * Parses the byte size string into a long value in bytes.
   */
  private long parseByteSize(String value) {
    value = value.trim().toUpperCase();

    if (value.endsWith("KB")) {
      return Long.parseLong(value.replace("KB", "")) * 1024;
    } else if (value.endsWith("MB")) {
      return Long.parseLong(value.replace("MB", "")) * 1024 * 1024;
    } else if (value.endsWith("GB")) {
      return Long.parseLong(value.replace("GB", "")) * 1024 * 1024 * 1024;
    } else if (value.endsWith("TB")) {
      return Long.parseLong(value.replace("TB", "")) * 1024L * 1024L * 1024L * 1024L;
    } else if (value.endsWith("B")) {
      return Long.parseLong(value.replace("B", ""));
    } else {
      throw new IllegalArgumentException("Invalid byte size format: " + value);
    }
  }
}
