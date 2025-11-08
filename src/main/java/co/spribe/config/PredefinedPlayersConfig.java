package co.spribe.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:players.properties"})
public interface PredefinedPlayersConfig extends Config {
  @Key("adminPlayer")
  @DefaultValue("admin")
  String adminPlayer();

  @Key("supervisorPlayer")
  @DefaultValue("supervisor")
  String supervisorPlayer();
}
