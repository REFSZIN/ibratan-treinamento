package ibratan.treinamento.team;

import ibratan.treinamento.person.person.Person;
import ibratan.treinamento.person.team.Team;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.constraints.Past;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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
    void ensureCreateTeamInvalid() {
        var input = getTeam();
        input.setEmail(null);
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().post("/api/team")
                .then()
                .statusCode(400);
    }

    @Test
    void ensureCreateInvalidResponseServer() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body("input")
                .when().post("/api/team")
                .then()
                .statusCode(500)
                .body(is("Hello from RESTEasy Reactive"));
    }

    @Test
    void ensureListTeams() {
        given()
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
                .when().get("/api/team/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void ensureEditTeam() {
        var input = getTeam();
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
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().delete("/api/team/{id}")
                .then()
                .statusCode(204);
    }

    private @Past List<Person> getPerson() {
        var input = new Person();
        input.setAge(18);
        input.setName("João da Silva");
        input.setEmail("joaozinho@gmail.com");
        input.setBirthDate(LocalDate.of(1990, 1, 1));
        input.setId(input.getId());
        return (List<Person>) input;
    }

    private Team getTeam() {
        var input = new Team();
        input.setLeader_id(1);
        input.setName("BIANCA");
        input.setEmail("capacita@live.com");
        input.setLastUpdated(LocalDate.now().atStartOfDay());
        input.setParticipants(getPerson());
        return input;
    }
}