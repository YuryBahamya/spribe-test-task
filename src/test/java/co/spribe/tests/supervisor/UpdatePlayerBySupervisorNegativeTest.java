package co.spribe.tests.supervisor;

import co.spribe.constants.GlobalConstants;
import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.HttpStatus;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("[Supervisor] Update Player (negative scenarios)")
public class UpdatePlayerBySupervisorNegativeTest extends TestBase {

  @Test
  @Description("Verify that Supervisor cannot update deleted player")
  public void testSupervisorCanNotUpdateDeletedPlayer() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    playerApi.deletePlayer(getSupervisorPlayer(), createdPlayerPojo.getId());

    Response response =
        playerApi.updatePlayerNative(
            getSupervisorPlayer(), createdPlayerPojo.getId(), PlayerPojoBuilder.randomUserPlayer());

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._404.getCode(),
        "Status code mismatch when Supervisor tries to update a deleted player.");
  }

  @Test(dataProvider = "notAllowedAge")
  @Description("Verify that Supervisor cannot update a player with restricted age")
  public void testSupervisorCanNotUpdatePlayerWithAge(Integer age) {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    Response response =
        playerApi.updatePlayerNative(
            getSupervisorPlayer(), createdPlayerPojo.getId(), new PlayerPojo().setAge(age));

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to update a player with age: " + age);
  }

  @Test
  @Description("Verify that Supervisor cannot update a player with existing 'login' field")
  public void testSupervisorCanNotUpdatePlayerWithExistingLogin() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomUserPlayer();
    createPlayerAndMarkForCleanup(playerPojo);

    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    Response response =
        playerApi.updatePlayerNative(
            getSupervisorPlayer(),
            createdPlayerPojo.getId(),
            new PlayerPojo().setLogin(playerPojo.getLogin()));

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to update a player with an existing login");
  }

  @Test
  @Description("Verify that Supervisor cannot update a player with existing 'screenName' field")
  public void testSupervisorCanNotUpdatePlayerWithExistingScreenName() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomUserPlayer();
    createPlayerAndMarkForCleanup(playerPojo);

    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    Response response =
        playerApi.updatePlayerNative(
            getSupervisorPlayer(),
            createdPlayerPojo.getId(),
            new PlayerPojo().setScreenName(playerPojo.getScreenName()));

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to update a player with an existing screen name");
  }

  @Test(
      dataProvider = "invalidPasswords",
      dataProviderClass = co.spribe.tests.supervisor.CreatePlayerBySupervisorNegativeTest.class)
  @Description("Verify that Supervisor cannot update a player with invalid password")
  public void testSupervisorCanNotUpdatePlayerWithInvalidPassword(
      String testName, String invalidPassword) {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    Response response =
        playerApi.updatePlayerNative(
            getSupervisorPlayer(),
            createdPlayerPojo.getId(),
            new PlayerPojo().setPassword(invalidPassword));

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with invalid password: "
            + testName);
  }

  @Test
  @Description("Verify that Supervisor cannot update a player with invalid gender")
  public void testSupervisorCanNotCreatePlayerWithInvalidGender() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    String invalidGender = RandomStringUtils.secure().nextAlphanumeric(10);
    Response response =
        playerApi.updatePlayerNative(
            getSupervisorPlayer(),
            createdPlayerPojo.getId(),
            new PlayerPojo().setGender(invalidGender));

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with invalid gender: "
            + invalidGender);
  }

  @DataProvider(name = "notAllowedAge")
  public Object[][] notAllowedAge() {
    return new Object[][] {
      {GlobalConstants.MIN_AGE - 1},
      {GlobalConstants.MAX_AGE + 1},
      {0},
      {-1},
    };
  }
}
