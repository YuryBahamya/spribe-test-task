package co.spribe.tests.supervisor;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.models.enums.HttpStatus;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player Controller")
@Feature("[Supervisor] Delete Player (negative scenarios)")
public class DeletePlayerBySupervisorNegativeTest extends TestBase {

  @Test
  @Description("Verify that Supervisor cannot delete already deleted player")
  public void testSupervisorCanNotDeleteAlreadyDeleted() {
    PlayerPojo createdPlayerPojo =
        createPlayerAndMarkForCleanup(getSupervisorPlayer(), PlayerPojoBuilder.randomUserPlayer());

    playerApi.deletePlayer(getSupervisorPlayer(), createdPlayerPojo.getId());
    Response response =
        playerApi.deletePlayerNative(getSupervisorPlayer(), createdPlayerPojo.getId());

    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus._404.getCode(),
        "Status code mismatch when Supervisor tries to delete a not existing player.");
  }
}
