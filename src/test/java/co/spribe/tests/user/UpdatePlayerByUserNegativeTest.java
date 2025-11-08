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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[User] Update Player (negative scenarios)")
public class UpdatePlayerByUserNegativeTest extends TestBase {

  private PlayerPojo userPlayer;

  @BeforeMethod
  public void setUp() {
    userPlayer = createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());
  }

  @Test(dataProvider = "adminAndUserRoles")
  @Description("Verify that a User cannot update players with any role")
  public void testUserCanNotUpdatePlayerWithRole(PlayerRole role) {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomPlayerWithRole(role);
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(playerPojo);

    Response response =
        playerApi.updatePlayerNative(
            userPlayer.getLogin(),
            createdPlayerPojo.getId(),
            PlayerPojoBuilder.randomPlayerWithRole(null));
    PlayerPojo getPlayerPojo = playerApi.getPlayer(createdPlayerPojo.getId());

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "User should not be able to update a player with role: " + role.getRoleName());
    softly.assertEquals(
        getPlayerPojo,
        playerPojo,
        "Player with role '%s' and id='%d' should not be updated by User."
            .formatted(role.getRoleName(), createdPlayerPojo.getId()));
    softly.assertEquals(
        getPlayerPojo.getId(), createdPlayerPojo.getId(), "Player ID should remain unchanged.");
    softly.assertAll();
  }
}
