package ibratan.treinamento.person;
import ibratan.treinamento.person.person.Person;
import ibratan.treinamento.person.person.PersonQuery;
import ibratan.treinamento.person.person.PersonResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@QuarkusTest
class PersonResourceTest {
    @InjectMock
    PersonQuery personQuery;

    @Inject
    PersonResource personResource;

    @BeforeEach
    void setUp() {
        QuarkusMock.installMockForType(Mockito.mock(PersonQuery.class), PersonQuery.class);
    }

    @Test
    void ensureCreatePersonValid() {
        var input = getPerson();
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().post("/api/person")
                .then()
                .statusCode(201);
    }

    @Test
    void ensureCreatePersonInvalid() {
        var input = getPerson();
        input.setEmail(null);
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().post("/api/person")
                .then()
                .statusCode(400);
    }

    @Test
    void ensureCreateInvalidResponseServer() {
        given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
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
    void ensureEditPerson() {
        var input = getPerson();
        Long id = 1L;
        given()
                .pathParam("id", id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().put("/api/person/{id}")
                .then()
                .statusCode(200);
    }

    //TODO

    //@Test
    void ensureDeletePerson() {
        var input = getPerson();
        given()
                .pathParam("id", 2)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().delete("/api/person/{id}")
                .then()
                .statusCode(204);
    }

    //@Test
    void ensureFindPersonById() {
        Long id = 2L;
        Person mockPerson = getPerson();
        when(Mockito.spy(PersonQuery.class).findById(id)).thenReturn(mockPerson);
        given()
                .pathParam("id", id)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get("/api/person/{id}")
                .then()
                .statusCode(200);
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