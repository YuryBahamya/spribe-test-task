package co.spribe.config;

import org.aeonbits.owner.ConfigFactory;

/**
 * PropertyController is a utility class that provides access to application configuration
 * properties defined in external files. It uses the OWNER library to load configurations into
 * strongly-typed interfaces.
 */
public class PropertyController {
  private static final AppConfig appConfig =
      ConfigFactory.create(AppConfig.class, System.getProperties());
  private static final PredefinedPlayersConfig predefinedPlayersConfig =
      ConfigFactory.create(PredefinedPlayersConfig.class, System.getProperties());

  private PropertyController() {}

  public static AppConfig getAppConfig() {
    return appConfig;
  }

  public static PredefinedPlayersConfig getPredefinedPlayersConfig() {
    return predefinedPlayersConfig;
  }
}
