package co.spribe.tests.publicapi;

import co.spribe.models.PlayerPojo;
import co.spribe.models.PlayerPojoBuilder;
import co.spribe.tests.TestBase;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Player Controller")
@Feature("Get All Players")
public class GetAllPlayersTest extends TestBase {

  @Test
  @Description("Verify that Get All Players API returns non-empty list with unique players including the created one")
  public void testGetAllPlayersShouldReturnNonEmptyListWithUniqueAndExpectedPlayers() {
    PlayerPojo createUserPlayerPojo = PlayerPojoBuilder.randomUserPlayer();
    PlayerPojo userPlayerPojo = createPlayerAndMarkForCleanup(createUserPlayerPojo);

    List<PlayerPojo> playerPojoList = playerApi.getAllPlayers();
    // Verify no duplicate IDs in the list
    List<Long> playerIds = playerPojoList.stream().map(PlayerPojo::getId).toList();
    Set<Long> uniquePlayerIds = new HashSet<>();
    List<Long> duplicateIds =
        playerIds.stream().filter(id -> !uniquePlayerIds.add(id)).distinct().toList();

    SoftAssert softly = new SoftAssert();
    softly.assertFalse(playerPojoList.isEmpty(), "The list of all players should not be empty.");
    softly.assertTrue(
        duplicateIds.isEmpty(),
        "There should be no duplicate player IDs in the list of all players. Duplicates found: "
            + duplicateIds);

    PlayerPojo getUserPlayerPojo =
        playerPojoList.stream()
            .filter(p -> userPlayerPojo.getId().equals(p.getId()))
            .findFirst()
            .orElse(null);

    softly.assertNotNull(
        getUserPlayerPojo, "The created user player should be present in the list of all players.");
    softly.assertEquals(
        getUserPlayerPojo.getAge(), createUserPlayerPojo.getAge(), "Player age mismatch.");
    softly.assertEquals(
        getUserPlayerPojo.getGender(), createUserPlayerPojo.getGender(), "Player gender mismatch.");
    softly.assertEquals(
        getUserPlayerPojo.getRole(), createUserPlayerPojo.getRole(), "Player role mismatch.");
    softly.assertEquals(
        getUserPlayerPojo.getScreenName(),
        createUserPlayerPojo.getScreenName(),
        "Player screen name mismatch.");
    softly.assertNull(getUserPlayerPojo.getPassword(), "Player password should not be exposed.");
    softly.assertNull(getUserPlayerPojo.getLogin(), "Player login should not be exposed.");
    softly.assertAll();
  }
}
