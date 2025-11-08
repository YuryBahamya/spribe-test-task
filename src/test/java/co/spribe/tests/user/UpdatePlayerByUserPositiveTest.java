package co.spribe.tests.user;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.PlayerRole;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[User] Update Player (positive scenarios)")
public class UpdatePlayerByUserPositiveTest extends TestBase {

  @Test
  @Description("Verify that a User can update his own data")
  public void testUserCanUpdateHimself() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomUserPlayer();
    String userLogin = playerPojo.getLogin();
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(playerPojo);

    // Prepare updated player data
    playerPojo = PlayerPojoBuilder.randomUserPlayer();
    PlayerPojo updatedPlayerPojo =
        playerApi.updatePlayer(userLogin, createdPlayerPojo.getId(), playerPojo);

    // Fetch the player again to verify the update of password field
    PlayerPojo getPlayerPojo = playerApi.getPlayer(createdPlayerPojo.getId());
    playerPojo.setRole(PlayerRole.USER.getRoleName()); // role should remain 'user'

    // Clone playerPojo and set password to null(is not returned by update response) for comparison
    PlayerPojo expectedPlayerPojoAfterUpdate = playerPojo.clone().setPassword(null);

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        updatedPlayerPojo,
        expectedPlayerPojoAfterUpdate,
        "Created player does not match the expected player");
    softly.assertEquals(
        updatedPlayerPojo.getId(),
        createdPlayerPojo.getId(),
        "Updated player ID should match the created player ID");
    softly.assertEquals(
        getPlayerPojo,
        playerPojo,
        "Retrieved player does not match the expected player after update");
    softly.assertAll();
  }
}
