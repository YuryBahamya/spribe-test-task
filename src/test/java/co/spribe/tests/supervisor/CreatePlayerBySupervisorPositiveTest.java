package co.spribe.tests.supervisor;

import co.spribe.constants.GlobalConstants;
import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.PlayerRole;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[Supervisor] Create Player (positive scenarios)")
public class CreatePlayerBySupervisorPositiveTest extends TestBase {

  @Test(dataProvider = "adminAndUserRoles")
  @Description("Verify that Supervisor can create player with 'admin' and 'user' roles")
  public void testSupervisorCanCreatePlayerWithRole(PlayerRole allowedRole) {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomPlayerWithRole(allowedRole);
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(playerPojo);

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        createdPlayerPojo, playerPojo, "Created player does not match the expected player");
    softly.assertNotNull(createdPlayerPojo.getId(), "Created player ID should not be null");
    softly.assertAll();
  }

  @Test(dataProvider = "minMaxAges")
  @Description("Verify that Supervisor can create player with min and max age")
  public void testSupervisorCanCreatePlayerWithAge(Integer age) {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer().setAge(age));

    PlayerPojo getPlayerPojo = playerApi.getPlayer(createdPlayerPojo.getId());

    Assert.assertEquals(getPlayerPojo.getAge(), age, "Player age does not match the expected age");
  }

  @DataProvider(name = "minMaxAges")
  public Object[][] minMaxAges() {
    return new Object[][] {{GlobalConstants.MIN_AGE}, {GlobalConstants.MAX_AGE}};
  }
}
