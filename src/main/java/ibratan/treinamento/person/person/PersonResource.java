package ibratan.treinamento.person.person;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Tag(name = "Person API", description = "Endpoints para gerenciamento de pessoas")
@Path("/api/person")
public class PersonResource {

    @Inject
    PersonQuery personQuery;

    @Operation(summary = "Listar cadastros", description = "Faz a listagem de todas as pessoas cadastradas")
    @APIResponses(value = {
            @APIResponse(responseCode = "100", description = "Continue"),
            @APIResponse(responseCode = "101", description = "Switching Protocols"),
            @APIResponse(responseCode = "102", description = "Processing"),
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "201", description = "Pessoa cadastrada com sucesso", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "202", description = "Informações inválidas enviadas", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "206", description = "Partial Content"),
            @APIResponse(responseCode = "300", description = "Multiple Choices"),
            @APIResponse(responseCode = "301", description = "Moved Permanently"),
            @APIResponse(responseCode = "302", description = "Found"),
            @APIResponse(responseCode = "304", description = "Not Modified"),
            @APIResponse(responseCode = "307", description = "Temporary Redirect"),
            @APIResponse(responseCode = "308", description = "Permanent Redirect"),
            @APIResponse(responseCode = "400", description = "Informações inválidas enviadas"),
            @APIResponse(responseCode = "401", description = "Não autorizado"),
            @APIResponse(responseCode = "402", description = "Pagamento necessário"),
            @APIResponse(responseCode = "403", description = "Forbidden"),
            @APIResponse(responseCode = "404", description = "Not Found"),
            @APIResponse(responseCode = "409", description = "Conflict"),
            @APIResponse(responseCode = "500", description = "Erro de processo do cadastro"),
            @APIResponse(responseCode = "501", description = "Not Implemented"),
            @APIResponse(responseCode = "502", description = "Bad Gateway"),
            @APIResponse(responseCode = "503", description = "Service Unavailable"),
            @APIResponse(responseCode = "504", description = "Gateway Timeout")
    })
    @GET
    @Path("/list")
    public List<Person> list() {
        // Your implementation
        return personQuery.list();
    }

    @Operation(summary = "Listar unico cadastro", description = "Faz a listagem de uma unica pessoa cadastrada")
    @GET
    @Path("{id}")
    @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @APIResponse(responseCode = "404", description = "Pessoa não encontrada")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public Person findById(@PathParam("id") Long id) {
        return personQuery.findById(id);
    }

    @Operation(summary = "Cadastro de pessoa", description = "Faz um novo cadastro de pessoa")
    @POST
    @Consumes("application/json")
    @APIResponse(responseCode = "201", description = "Pessoa criada com sucesso", content = @Content(mediaType = "application/json"))
    @APIResponse(responseCode = "400", description = "Informações inválidas enviadas")
    @APIResponse(responseCode = "500", description = "Erro de processor")
    public Response create(Person person) {
        String errorMessage = getValidationError(person);
        if(errorMessage != null)
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        personQuery.create(person, LocalDateTime.now());
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(summary = "Atualização de cadastro", description = "Faz a alteração dos dados já cadastrados de uma pessoa")
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @APIResponse(responseCode = "404", description = "Pessoa não encontrada")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public Person editById(Person person, @PathParam("id") Long id) {
        // Your implementation
        personQuery.update(person, LocalDateTime.now(), id);
        return personQuery.findById(id);
    }

    @Operation(summary = "Deletar cadastro", description = "Deleta um cadastro de pessoa")
    @DELETE
    @Path("{id}")
    @APIResponse(responseCode = "204", description = "No Content")
    @APIResponse(responseCode = "404", description = "Pessoa não encontrada")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public void delete( Person person , @PathParam("id") Long id) {
        personQuery.delete(person, LocalDateTime.now(), id);
    }

    private String getValidationError(Person person) {
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);
            for(ConstraintViolation<Person> violation : constraintViolations)
                return violation.getMessage();
        }
        return null;
    }
}