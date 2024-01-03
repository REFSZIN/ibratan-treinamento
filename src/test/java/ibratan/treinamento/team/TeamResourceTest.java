package ibratan.treinamento.team;
import ibratan.treinamento.person.team.Team;
import ibratan.treinamento.person.team.TeamQuery;
import ibratan.treinamento.person.team.TeamResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import static io.restassured.RestAssured.given;

@QuarkusTest
class TeamResourceTest {

    @InjectMock
    TeamQuery teamQuery;

    @Inject
    TeamResource teamResource;

    @BeforeEach
    void setUp() {
        QuarkusMock.installMockForType(Mockito.mock(TeamQuery.class), TeamQuery.class);
    }

    @Test
    void ensureCreateTeamValid() {
        var input = getNewTeam();
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().post("/api/team")
                .then()
                .statusCode(201);
    }

    @Test
    void ensureCreateInvalidResponseServer() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body("")
                .when().post("/api/team")
                .then()
                .statusCode(500);
    }

    @Test
    void ensureListTeams() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get("/api/team/list")
                .then()
                .statusCode(200);
    }

    //TODO

    //@Test
    void ensureFindTeamById() {
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().get("/api/team/{id}")
                .then()
                .statusCode(200);
    }

    //@Test
    void ensureEditTeam() {
        var input = getTeam();
        input.setLeader(1);
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().put("/api/team/{id}")
                .then()
                .statusCode(200);
    }

    //@Test
    void ensureDeleteTeam() {
        var input = getTeamDelete();
        given()
                .pathParam("id", input.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().delete("/api/team/{id}")
                .then()
                .statusCode(204);
    }

    private Team getTeam() {
        var input = new Team();
        input.setId(999L);
        input.setLeader(1);
        input.setName("BIANCA");
        input.setEmail("capacita@live.com");
        input.setLastUpdated(LocalDateTime.now());
        return input;
    }

    private Team getTeamDelete() {
        var input = new Team();
        input.setId(9999L);
        input.setLeader(1);
        input.setName("BIANCA");
        input.setEmail("capacita@live.com");
        input.setLastUpdated(LocalDateTime.now());
        return input;
    }

    private Team getNewTeam() {
        var input = new Team();
        input.setName("BIANCA TESTE");
        input.setEmail("capacitalider@live.com");
        return input;
    }
}