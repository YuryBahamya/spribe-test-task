package co.spribe.tests.admin;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.HttpStatus;
import co.spribe.models.enums.PlayerRole;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[Admin] Delete Player (positive scenarios)")
public class DeletePlayerByAdminPositiveTest extends TestBase {

  @Test
  @Description("Verify that Admin can delete a player with 'user' role")
  public void testAdminCanDeletePlayerWithUserRole() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    playerApi.deletePlayer(getAdminPlayer(), createdPlayerPojo.getId());

    List<PlayerPojo> playerPojoList = playerApi.getAllPlayers();

    Assert.assertTrue(
        playerPojoList.stream()
            .noneMatch(player -> player.getId().equals(createdPlayerPojo.getId())),
        "Deleted player should not be present in the list of all players.");
  }

  @Test
  @Description("Verify that Admin can delete himself")
  public void testAdminCanDeleteHimself() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomPlayerWithRole(PlayerRole.ADMIN));

    playerApi.deletePlayer(createdPlayerPojo.getLogin(), createdPlayerPojo.getId());

    List<PlayerPojo> playerPojoList = playerApi.getAllPlayers();

    Assert.assertTrue(
        playerPojoList.stream()
            .noneMatch(player -> player.getId().equals(createdPlayerPojo.getId())),
        "Deleted player should not be present in the list of all players.");
  }
}
