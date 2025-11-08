package co.spribe.tests.admin;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[Admin] Create Player (positive scenarios)")
public class CreatePlayerByAdminPositiveTest extends TestBase {

  @Test
  @Description("Verify that Admin can create players with 'user' role")
  public void testAdminCanCreatePlayerWithUserRole() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomUserPlayer();
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(getAdminPlayer(), playerPojo);

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        createdPlayerPojo, playerPojo, "Created player does not match the expected player");
    softly.assertNotNull(createdPlayerPojo.getId(), "Created player ID should not be null");
    softly.assertAll();
  }
}
