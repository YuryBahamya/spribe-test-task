package co.spribe.tests.publicapi;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.HttpStatus;
import co.spribe.models.enums.PlayerRole;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/** Test class to verify getting player by ID for different scenarios. */
public class GetPlayerPositiveTest extends TestBase {

  @Test(dataProvider = "adminAndUserRoles")
  @Description("Verify that getting player by ID works for different player roles")
  public void testGetPlayerWithRole(PlayerRole role) {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomPlayerWithRole(role);
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(playerPojo);

    PlayerPojo getPlayerPojo = playerApi.getPlayer(createdPlayerPojo.getId());

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        getPlayerPojo, playerPojo, "Retrieved player does not match the expected player");
    softly.assertEquals(
        getPlayerPojo.getId(),
        createdPlayerPojo.getId(),
        "Retrieved player ID should match the created player ID");
    softly.assertAll();
  }
}
