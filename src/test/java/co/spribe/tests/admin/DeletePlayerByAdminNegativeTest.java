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
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[Admin] Delete Player (positive scenarios)")
public class DeletePlayerByAdminNegativeTest extends TestBase {

  @Test
  @Description("Verify that Admin cannot delete another player with 'admin' role")
  public void testAdminCanNotDeletePlayerWithAdminRole() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomPlayerWithRole(PlayerRole.ADMIN));

    Response response = playerApi.deletePlayerNative(getAdminPlayer(), createdPlayerPojo.getId());
    List<PlayerPojo> playerPojoList = playerApi.getAllPlayers();

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "Admin should not be able to delete an admin player.");
    softly.assertTrue(
        playerPojoList.stream()
            .anyMatch(player -> player.getId().equals(createdPlayerPojo.getId())),
        "Admin player should still be present in the list of all players.");
    softly.assertAll();
  }
}
