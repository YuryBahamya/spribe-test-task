package co.spribe.tests.supervisor;

import static co.spribe.constants.GlobalConstants.PASSWORD_MAX_LENGTH;
import static co.spribe.constants.GlobalConstants.PASSWORD_MIN_LENGTH;

import co.spribe.constants.GlobalConstants;
import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.HttpStatus;
import co.spribe.models.enums.PlayerRole;
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
@Feature("[Supervisor] Create Player (negative scenarios)")
public class CreatePlayerBySupervisorNegativeTest extends TestBase {

  @Test
  @Description("Verify that Supervisor cannot create a player with 'supervisor' role")
  public void testSupervisorCanNotCreatePlayerWithSupervisorRole() {
    Response response =
        playerApi.createPlayerNative(
            getSupervisorPlayer(), PlayerPojoBuilder.randomPlayerWithRole(PlayerRole.SUPERVISOR));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._403.getCode(),
        "Status code mismatch when Supervisor tries to create a player with 'supervisor' role");
  }

  @Test(dataProvider = "invalidPlayers")
  @Description("Verify that Supervisor cannot create a player with missing required field")
  public void testSupervisorCanNotCreatePlayerWithMissingRequiredField(
      String testName, PlayerPojo invalidPlayers) {

    Response response = playerApi.createPlayerNative(getSupervisorPlayer(), invalidPlayers);
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with missing required field: "
            + testName);
  }

  @Test
  @Description("Verify that Supervisor cannot create a player with the same data twice")
  public void testSupervisorCanNotCreatePlayerWithTheSameData() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomUserPlayer();
    createPlayerAndMarkForCleanup(playerPojo);

    Response response = playerApi.createPlayerNative(getSupervisorPlayer(), playerPojo);
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with the same data twice");
  }

  @Test(dataProvider = "notAllowedAge")
  @Description("Verify that Supervisor cannot create player with restricted age")
  public void testSupervisorCanNotCreatePlayerWithAge(Integer age) {
    Response response =
        playerApi.createPlayerNative(
            getSupervisorPlayer(), PlayerPojoBuilder.randomUserPlayer().setAge(age));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with age: " + age);
  }

  @Test
  @Description("Verify that Supervisor cannot create a player with existing 'login' field")
  public void testSupervisorCanNotCreatePlayerWithExistingLogin() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomUserPlayer();
    createPlayerAndMarkForCleanup(playerPojo);

    Response response =
        playerApi.createPlayerNative(
            getSupervisorPlayer(),
            PlayerPojoBuilder.randomUserPlayer().setLogin(playerPojo.getLogin()));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with the same data twice");
  }

  @Test
  @Description("Verify that Supervisor cannot create a player with existing 'screenName' field")
  public void testSupervisorCanNotCreatePlayerWithExistingScreenName() {
    PlayerPojo playerPojo = PlayerPojoBuilder.randomUserPlayer();
    createPlayerAndMarkForCleanup(playerPojo);

    Response response =
        playerApi.createPlayerNative(
            getSupervisorPlayer(),
            PlayerPojoBuilder.randomUserPlayer().setScreenName(playerPojo.getScreenName()));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with the same data twice");
  }

  @Test(dataProvider = "invalidPasswords")
  @Description("Verify that Supervisor cannot create a player with invalid password")
  public void testSupervisorCanNotCreatePlayerWithInvalidPassword(
      String testName, String invalidPassword) {
    Response response =
        playerApi.createPlayerNative(
            getSupervisorPlayer(),
            PlayerPojoBuilder.randomUserPlayer().setPassword(invalidPassword));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with invalid password: "
            + testName);
  }

  @Test
  @Description("Verify that Supervisor cannot create a player with invalid gender")
  public void testSupervisorCanNotCreatePlayerWithInvalidGender() {
    String invalidGender = RandomStringUtils.secure().nextAlphanumeric(10);
    Response response =
        playerApi.createPlayerNative(
            getSupervisorPlayer(), PlayerPojoBuilder.randomUserPlayer().setGender(invalidGender));
    markForCleanupPlayerIfWasCreated(response);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._400.getCode(),
        "Status code mismatch when Supervisor tries to create a player with invalid gender: "
            + invalidGender);
  }

  @DataProvider(name = "notAllowedRoles")
  public Object[][] notAllowedRoles() {
    return new Object[][] {{PlayerRole.ADMIN}, {PlayerRole.SUPERVISOR}};
  }

  @DataProvider(name = "notAllowedAge")
  public Object[][] notAllowedAge() {
    return new Object[][] {{GlobalConstants.MIN_AGE - 1}, {GlobalConstants.MAX_AGE + 1}};
  }

  @DataProvider(name = "invalidPasswords")
  public Object[][] invalidPassword() {
    return new Object[][] {
      {
        "length less then " + PASSWORD_MIN_LENGTH,
        "p0" + RandomStringUtils.secure().nextAlphanumeric(PASSWORD_MIN_LENGTH - 3)
      },
      {
        "length more then " + PASSWORD_MAX_LENGTH,
        "p0" + RandomStringUtils.secure().nextAlphanumeric(PASSWORD_MAX_LENGTH - 1)
      },
      {"only letters", RandomStringUtils.secure().nextAlphabetic(PASSWORD_MIN_LENGTH)},
      {"only digits", RandomStringUtils.secure().nextNumeric(PASSWORD_MAX_LENGTH)},
      {
        "missing special character",
        "p0" + RandomStringUtils.secure().next(PASSWORD_MIN_LENGTH - 2, "!@#$%^&*()_+-=[]{}")
      },
      {
        "non-ASCII characters",
        "p0" + RandomStringUtils.secure().next(PASSWORD_MIN_LENGTH - 2, "жфяüß")
      },
      {"empty", ""}
    };
  }

  @DataProvider(name = "invalidPlayers")
  public Object[][] invalidPlayers() {
    return new Object[][] {
      {"no age", PlayerPojoBuilder.randomUserPlayer().setAge(null)},
      {"no gender", PlayerPojoBuilder.randomUserPlayer().setGender(null)},
      {"no login", PlayerPojoBuilder.randomUserPlayer().setLogin(null)},
      {"no password", PlayerPojoBuilder.randomUserPlayer().setPassword(null)},
      {"no role", PlayerPojoBuilder.randomUserPlayer().setRole(null)},
      {"no screenName", PlayerPojoBuilder.randomUserPlayer().setScreenName(null)}
    };
  }
}
