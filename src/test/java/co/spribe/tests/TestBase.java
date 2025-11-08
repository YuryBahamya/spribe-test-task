package co.spribe.tests;

import co.spribe.api.PlayerApi;
import co.spribe.config.PredefinedPlayersConfig;
import co.spribe.config.PropertyController;
import co.spribe.listeners.LoggingTestListener;
import co.spribe.models.PlayerPojo;
import co.spribe.models.enums.HttpStatus;
import co.spribe.models.enums.PlayerRole;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners({LoggingTestListener.class})
public class TestBase {
  private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

  protected static final ThreadLocal<List<Long>> CREATED_PLAYER_IDS =
      ThreadLocal.withInitial(ArrayList::new);
  protected final PredefinedPlayersConfig predefinedPlayers =
      PropertyController.getPredefinedPlayersConfig();
  protected final PlayerApi playerApi = PlayerApi.getInstance();

  @AfterMethod(alwaysRun = true)
  public void cleanupPlayers() {
    List<Long> ids = CREATED_PLAYER_IDS.get();

    if (ids.isEmpty()) {
      return;
    }

    for (Long id : new ArrayList<>(ids)) { // protect against concurrent modification
      try {
        Response response = playerApi.deletePlayerNative(getSupervisorPlayer(), id);
        int statusCode = response.getStatusCode();

        if (statusCode == HttpStatus._200.getCode() || statusCode == HttpStatus._204.getCode()) {
          LOGGER.info("[Player Cleanup] Player with id={} deleted successfully", id);
        } else if (statusCode == HttpStatus._404.getCode()) {
          LOGGER.info("[Player Cleanup] Player with id={} already deleted (404)", id);
        } else {
          LOGGER.warn(
              "[Player Cleanup] Unexpected status {} when deleting player with id={}",
              statusCode,
              id);
        }
      } catch (Exception e) {
        LOGGER.error("[Player Cleanup] Error deleting player with id={}", id, e);
      }
    }

    // Clear the list after cleanup
    CREATED_PLAYER_IDS.remove();
  }

  protected String getAdminPlayer() {
    return predefinedPlayers.adminPlayer();
  }

  protected String getSupervisorPlayer() {
    return predefinedPlayers.supervisorPlayer();
  }

  protected void markForCleanupPlayerIfWasCreated(Response response) {
    if (response.getStatusCode() == HttpStatus._200.getCode()) {
      PlayerPojo playerPojo = response.then().extract().as(PlayerPojo.class);
      markPlayerIdForCleanup(playerPojo.getId());
    }
  }

  @Step("Create Player by '{editor}' with data: {playerPojo}")
  protected PlayerPojo createPlayerAndMarkForCleanup(String editor, PlayerPojo playerPojo) {
    PlayerPojo createdPlayer = playerApi.createPlayer(editor, playerPojo);
    markPlayerIdForCleanup(createdPlayer.getId());
    return createdPlayer;
  }

  protected PlayerPojo createPlayerAndMarkForCleanup(PlayerPojo playerPojo) {
    return createPlayerAndMarkForCleanup(getSupervisorPlayer(), playerPojo);
  }

  protected void markPlayerIdForCleanup(Long id) {
    if (id != null) {
      CREATED_PLAYER_IDS.get().add(id);
    }
  }

  @DataProvider(name = "adminAndUserRoles")
  public Object[][] adminAndUserRoles() {
    return new Object[][] {{PlayerRole.ADMIN}, {PlayerRole.USER}};
  }

  @DataProvider(name = "allPlayerRoles")
  public Object[][] allPlayerRoles() {
    return new Object[][] {{PlayerRole.SUPERVISOR}, {PlayerRole.ADMIN}, {PlayerRole.USER}};
  }
}
