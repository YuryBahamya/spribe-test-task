package co.spribe.tests.supervisor;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.PlayerRole;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("[Supervisor] Delete Player (positive scenarios)")
public class DeletePlayerBySupervisorPositiveTest extends TestBase {

  @Test(dataProvider = "adminAndUserRoles")
  @Description("Verify that Supervisor can delete players with 'admin' and 'user' roles")
  public void testSupervisorCanDeletePlayerWithRole(PlayerRole role) {
    PlayerPojo createdPlayerPojo =
        playerApi.createPlayer(getSupervisorPlayer(), PlayerPojoBuilder.randomPlayerWithRole(role));

    playerApi.deletePlayer(getSupervisorPlayer(), createdPlayerPojo.getId());

    List<PlayerPojo> playerPojoList = playerApi.getAllPlayers();

    Assert.assertTrue(
        playerPojoList.stream()
            .noneMatch(player -> player.getId().equals(createdPlayerPojo.getId())),
        "Deleted player should not be present in the list of all players.");
  }
}
