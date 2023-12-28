package ibratan.treinamento.person;
import ibratan.treinamento.person.person.Person;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class PersonResourceTest {

    @Test
    void ensureCreatePersonValid() {
        var input = getPerson();
        given()
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON )
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().get("/api/person")
                .then()
                .statusCode(201);
    }

    @Test
    void ensureCreatePersonInvalid() {
        var input = getPerson();
        input.setEmail(null);
        given()
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON )
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().get("/api/person")
                .then()
                .statusCode(400);
    }

    @Test
    void ensureCreateInvalidResponseServer() {
        var input = getPerson();
        input.setEmail(null);
        given()
                .header(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON )
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .body(input)
                .when().get("/api/person")
                .then()
                .statusCode(500)
                .body(is("Hello from RESTEasy Reactive"));
    }

    private Person getPerson(){
        var input = new Person();
        input.setAge(18);
        input.setName("João da Silva");
        input.setEmail("joaozinho@gmail.com");
        input.setBirthDate(LocalDate.of(1990, 1, 1 ));
        return input;
    }
}