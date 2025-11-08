package co.spribe.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:app.properties"})
public interface AppConfig extends Config {
  @Key("baseUrl")
  String baseUrl();

  @Key("threadsCount")
  @DefaultValue("3")
  int threadsCount();

  @Key("parallelMode")
  @DefaultValue("methods")
  String parallelMode();

  @Key("apiLogsEnabled")
  @DefaultValue("true")
  boolean isApiLogsEnabled();
}
