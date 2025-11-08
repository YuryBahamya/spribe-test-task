package co.spribe.tests.publicapi;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.HttpStatus;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("Get Player (negative scenarios)")
public class GetPlayerNegativeTest extends TestBase {

  @Test
  @Description("Verify that deleted player cannot be retrieved")
  public void testCanNotGetDeletedPlayer() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    playerApi.deletePlayer(getSupervisorPlayer(), createdPlayerPojo.getId());

    Response response = playerApi.getPlayerNative(createdPlayerPojo.getId());

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._404.getCode(),
        "Status code mismatch when trying to get a deleted player");
  }

  @Test
  @Description("Verify that non-existing player cannot be retrieved")
  public void testCanNotGetNotExistingUser() {
    long biggestId =
        playerApi.getAllPlayers().stream().mapToLong(PlayerPojo::getId).max().orElse(0L);

    Long id = ThreadLocalRandom.current().nextLong(biggestId + 1, Long.MAX_VALUE);

    Response response = playerApi.getPlayerNative(id);

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._404.getCode(),
        "Status code mismatch when trying to get a player with non-existing ID: " + id);
  }

  @Test(invocationCount = 3)
  @Description("Verify that getting player response takes less than 2 seconds")
  public void testGetPlayerResponseTime() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(PlayerPojoBuilder.randomUserPlayer());

    playerApi
        .getPlayerNative(createdPlayerPojo.getId())
        .then()
        .time(Matchers.lessThan(2000L), TimeUnit.MILLISECONDS);
  }
}
