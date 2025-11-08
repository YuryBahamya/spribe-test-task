package co.spribe.utils;

import ch.qos.logback.core.PropertyDefinerBase;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** A Logback property definer that generates a log file name with a timestamp. */
public class TimestampLogFileName extends PropertyDefinerBase {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss.SSS");

  @Override
  public String getPropertyValue() {
    return "application_" + LocalDateTime.now().format(FORMATTER) + ".log";
  }
}
