package ibratan.treinamento.person;

import ibratan.treinamento.person.person.Person;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@QuarkusTest
class PersonResourceTest {

    @Test
    void ensureCreatePersonValid() {
        var input = getPerson();
        given()
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON )
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().post("/api/person")
                .then()
                .statusCode(201);
    }

//TODO MOKITO STATUS EXEPTIONERROR
    @Test
    void ensureCreatePersonInvalid() {
        var input = getPerson();
        input.setEmail(null);
        given()
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON )
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().post("/api/person")
                .then()
                .statusCode(400);
    }

    @Test
    void ensureCreateInvalidResponseServer() {
        given()
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON )
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body("input")
                .when().post("/api/person")
                .then()
                .statusCode(500);
    }

    @Test
    void ensureListPeople() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get("/api/person/list")
                .then()
                .statusCode(200);
    }

    @Test
    void ensureFindPersonById() {
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get("/api/person/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void ensureEditPerson() {
        var input = getPerson();
        given()
                .pathParam("id", 1)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().put("/api/person/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void ensureDeletePerson() {
        var input = getPerson();
        given()
                .pathParam("id", 10)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().delete("/api/person/{id}")
                .then()
                .statusCode(204);
    }

    private Person getPerson() {
        var input = new Person();
        input.setId(1L);
        input.setAge(18);
        input.setName("João da Silva");
        input.setEmail("joaozinho@gmail.com");
        input.setBirthDate(LocalDate.of(1990, 1, 1));
        return input;
    }
}