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
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("[Admin] Update Player (negative scenarios)")
public class UpdatePlayerByAdminNegativeTest extends TestBase {

  @Test
  @Description("Verify that Admin cannot update another player with 'admin' role")
  public void testAdminCanNotUpdateAnotherPlayerWithAdminRole() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomPlayerWithRole(PlayerRole.ADMIN);
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(playerPojo);

    playerPojo = PlayerPojoBuilder.randomPlayerWithRole(null);
    Response response =
        playerApi.updatePlayerNative(getAdminPlayer(), createdPlayerPojo.getId(), playerPojo);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "Status code mismatch when Admin tries to update another player with 'admin' role.");
  }
}
