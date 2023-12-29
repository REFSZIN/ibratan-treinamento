package ibratan.treinamento.team;
import ibratan.treinamento.person.team.Team;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static io.restassured.RestAssured.given;

@QuarkusTest
class TeamResourceTest {

    @Test
    void ensureCreateTeamValid() {
        var input = getTeam();
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

    @Test
    void ensureFindTeamById() {
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when().get("/api/team/{id}")
                .then()
                .statusCode(200);
    }

    @Test
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

    @Test
    void ensureDeleteTeam() {
        var input = getTeam();
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().delete("/api/team/{id}")
                .then()
                .statusCode(204);
    }

    private Team getTeam() {
        Team team = new Team();
        team.setId(1L);
        team.setLeader(1);
        team.setName("BIANCA");
        team.setEmail("capacita@live.com");
        team.setLastUpdated(LocalDate.now().atStartOfDay());
        return team;
    }
}