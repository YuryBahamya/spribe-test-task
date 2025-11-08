package co.spribe.tests.admin;

import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.HttpStatus;
import co.spribe.models.enums.PlayerRole;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("[Admin] Create Player (negative scenarios)")
public class CreatePlayerByAdminNegativeTest extends TestBase {

  @Test(dataProvider = "notAllowedRoles")
  @Description(
      "Verify that Admin cannot create players with 'supervisor' and 'admin' roles")
  public void testAdminCanNotCreatePlayerWithRole(PlayerRole notAllowedRole) {
    Response response =
        playerApi.createPlayerNative(
            getAdminPlayer(), PlayerPojoBuilder.randomPlayerWithRole(notAllowedRole));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "Status code mismatch when Admin tries to create a player with role: "
            + notAllowedRole.getRoleName());
  }

  @DataProvider(name = "notAllowedRoles")
  public Object[][] notAllowedRoles() {
    return new Object[][] {{PlayerRole.SUPERVISOR}, {PlayerRole.ADMIN}};
  }
}
