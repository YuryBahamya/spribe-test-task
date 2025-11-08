package co.spribe.tests.user;

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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[User] Delete Player (negative scenarios)")
public class DeletePlayerByUserNegativeTest extends TestBase {

  private PlayerPojo userPlayer;

  @BeforeMethod
  public void setUp() {
    userPlayer = createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());
  }

  @Test(dataProvider = "allPlayerRoles")
  @Description("Verify that User cannot delete players with any role")
  public void testUserCanNotDeletePlayerWithRole(PlayerRole role) {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomPlayerWithRole(role));

    Response response =
        playerApi.deletePlayerNative(userPlayer.getLogin(), createdPlayerPojo.getId());
    List<PlayerPojo> playerPojoList = playerApi.getAllPlayers();

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "User should not be able to delete a player with role: " + role.getRoleName());
    softly.assertTrue(
        playerPojoList.stream()
            .anyMatch(player -> player.getId().equals(createdPlayerPojo.getId())),
        "Player with role '%s' and id='%d' should still be present in the list of all players."
            .formatted(role.getRoleName(), createdPlayerPojo.getId()));
    softly.assertAll();
  }

  @Test
  @Description("Verify that User cannot delete himself")
  public void testUserCanNotDeleteHimself() {
    Response response = playerApi.deletePlayerNative(userPlayer.getLogin(), userPlayer.getId());
    List<PlayerPojo> playerPojoList = playerApi.getAllPlayers();

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "User should not be able to delete himself.");
    softly.assertTrue(
        playerPojoList.stream().anyMatch(player -> player.getId().equals(userPlayer.getId())),
        "User should still be present in the list of all players");
    softly.assertAll();
  }

  @DataProvider(name = "notAllowedRoles")
  public Object[][] notAllowedRoles() {
    return new Object[][] {{PlayerRole.ADMIN}, {PlayerRole.SUPERVISOR}, {PlayerRole.USER}};
  }
}
