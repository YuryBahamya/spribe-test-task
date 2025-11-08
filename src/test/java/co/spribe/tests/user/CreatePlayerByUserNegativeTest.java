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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("[User] Create Player (negative scenarios)")
public class CreatePlayerByUserNegativeTest extends TestBase {
  private PlayerPojo userPlayer;

  @BeforeMethod
  public void setUp() {
    userPlayer = createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());
  }

  @Test(dataProvider = "allPlayerRoles")
  @Description("Verify that User cannot create players")
  public void testUserCannotCreatePlayerWithRole(PlayerRole role) {
    Response response =
        playerApi.createPlayerNative(
            userPlayer.getLogin(), PlayerPojoBuilder.randomPlayerWithRole(role));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "Status code mismatch when User tries to create a player with role: " + role.getRoleName());
  }
}
