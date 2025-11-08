package co.spribe.tests.supervisor;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.PlayerRole;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("[Supervisor] Update Player (positive scenarios)")
public class UpdatePlayerBySupervisorPositiveTest extends TestBase {

  @Test(dataProvider = "adminAndUserRoles")
  @Description(
      "Verify that Supervisor can update players with 'admin' and 'user' roles (all fields except role)")
  public void testSupervisorCanUpdatePlayerWithRole(PlayerRole role) {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomPlayerWithRole(role);
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(playerPojo);

    // Prepare updated player data
    playerPojo = PlayerPojoBuilder.randomPlayerWithRole(null);
    PlayerPojo updatedPlayerPojo =
        playerApi.updatePlayer(getSupervisorPlayer(), createdPlayerPojo.getId(), playerPojo);

    // Fetch the player again to verify the update of password field
    PlayerPojo getPlayerPojo = playerApi.getPlayer(createdPlayerPojo.getId());
    playerPojo.setRole(role.getRoleName()); // role should remain the same

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

  @Test(dataProvider = "playerRoleUpdate")
  @Description("Verify that Supervisor can update player role")
  public void testSupervisorCanUpdatePlayerRole(
      String testName, PlayerRole initialRole, PlayerRole updatedRole) {
    PlayerPojo playerPojo = new PlayerPojoBuilder().setRole(initialRole.getRoleName()).build();
    PlayerPojo createdPlayerPojo = createPlayerAndMarkForCleanup(playerPojo);

    PlayerPojo updatedPlayerPojo =
        playerApi.updatePlayer(
            getSupervisorPlayer(),
            createdPlayerPojo.getId(),
            new PlayerPojo().setRole(updatedRole.getRoleName()));

    SoftAssert softly = new SoftAssert();
    softly.assertEquals(
        updatedPlayerPojo.getRole(),
        updatedRole.getRoleName(),
        "Player role should be updated to " + updatedRole.getRoleName());
    softly.assertEquals(
        updatedPlayerPojo.getId(),
        createdPlayerPojo.getId(),
        "Updated player ID should match the created player ID");
    softly.assertAll();
  }

  @DataProvider(name = "playerRoles")
  public Object[][] playerRoles() {
    return new Object[][] {{PlayerRole.ADMIN}, {PlayerRole.USER}};
  }

  @DataProvider(name = "playerRoleUpdate")
  public Object[][] playerRoleUpdate() {
    return new Object[][] {
      {"user => admin", PlayerRole.USER, PlayerRole.ADMIN},
      {"admin => user", PlayerRole.ADMIN, PlayerRole.USER}
    };
  }
}
