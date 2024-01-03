package ibratan.treinamento.person.person;
import jakarta.inject.Inject;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Tag(name = "Person API", description = "Endpoints para gerenciamento de pessoas")
@Path("/api/person")
public class PersonResource {

    private static final String EMAIL_ALREADY_IN_USE = "E-mail já está em uso :(";
    @Inject
    PersonQuery personQuery;

    @Operation(summary = "Listar cadastros", description = "Faz a listagem de todas as pessoas cadastradas")
    @APIResponses(value = {
            @APIResponse(responseCode = "100", description = "Continue"),
            @APIResponse(responseCode = "101", description = "Switching protocols"),
            @APIResponse(responseCode = "102", description = "Processing"),
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "201", description = "Pessoa cadastrada com sucesso", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "202", description = "Informações inválidas enviadas", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "204", description = "Sem conteúdo", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "206", description = "Conteúdo parcial"),
            @APIResponse(responseCode = "300", description = "Escolhas múltiplas"),
            @APIResponse(responseCode = "301", description = "Movido permanentemente"),
            @APIResponse(responseCode = "302", description = "Encontrado"),
            @APIResponse(responseCode = "304", description = "Não modificado"),
            @APIResponse(responseCode = "307", description = "Redirecionamento temporário"),
            @APIResponse(responseCode = "308", description = "Redirecionamento permanente"),
            @APIResponse(responseCode = "400", description = "Informações inválidas enviadas"),
            @APIResponse(responseCode = "401", description = "Não autorizado"),
            @APIResponse(responseCode = "402", description = "Pagamento necessário"),
            @APIResponse(responseCode = "403", description = "Proibido"),
            @APIResponse(responseCode = "404", description = "Não encontrado"),
            @APIResponse(responseCode = "409", description = "Conflito"),
            @APIResponse(responseCode = "500", description = "Erro no processo de cadastro"),
            @APIResponse(responseCode = "501", description = "Não implementado"),
            @APIResponse(responseCode = "502", description = "Gateway ruim"),
            @APIResponse(responseCode = "503", description = "Serviço indisponível"),
            @APIResponse(responseCode = "504", description = "Timeout no gateway")
    })
    @GET
    @Path("/list")
    public List<Person> list() {
        return personQuery.list();
    }

    @Operation(summary = "Listar unico cadastro", description = "Faz a listagem de uma unica pessoa cadastrada")
    @APIResponses(value = {
            @APIResponse(responseCode = "100", description = "Continue"),
            @APIResponse(responseCode = "101", description = "Switching protocols"),
            @APIResponse(responseCode = "102", description = "Processing"),
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "201", description = "Listado com sucesso", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "202", description = "Informações inválidas enviadas", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "204", description = "Sem conteúdo", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "206", description = "Conteúdo parcial"),
            @APIResponse(responseCode = "300", description = "Escolhas múltiplas"),
            @APIResponse(responseCode = "301", description = "Movido permanentemente"),
            @APIResponse(responseCode = "302", description = "Encontrado"),
            @APIResponse(responseCode = "304", description = "Não modificado"),
            @APIResponse(responseCode = "307", description = "Redirecionamento temporário"),
            @APIResponse(responseCode = "308", description = "Redirecionamento permanente"),
            @APIResponse(responseCode = "400", description = "Informações inválidas enviadas"),
            @APIResponse(responseCode = "401", description = "Não autorizado"),
            @APIResponse(responseCode = "402", description = "Pagamento necessário"),
            @APIResponse(responseCode = "403", description = "Proibido"),
            @APIResponse(responseCode = "404", description = "Não encontrado"),
            @APIResponse(responseCode = "409", description = "Conflito"),
            @APIResponse(responseCode = "500", description = "Erro no processo de listagem"),
            @APIResponse(responseCode = "501", description = "Não implementado"),
            @APIResponse(responseCode = "502", description = "Gateway ruim"),
            @APIResponse(responseCode = "503", description = "Serviço indisponível"),
            @APIResponse(responseCode = "504", description = "Timeout no gateway"),
            @APIResponse(responseCode = "505", description = "HTTP Version Not Supported")
    })
    @GET
    @Path("{id}")
    public Person findById(@PathParam("id") Long id) {
        return personQuery.findById(id);
    }

    @Operation(summary = "Cadastro de pessoa", description = "Faz um novo cadastro de pessoa")
    @POST
    @Consumes("application/json")
    @APIResponses(value = {
            @APIResponse(responseCode = "100", description = "Continue"),
            @APIResponse(responseCode = "101", description = "Switching protocols"),
            @APIResponse(responseCode = "102", description = "Processing"),
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "201", description = "Pessoa criada com sucesso", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "202", description = "Informações inválidas enviadas", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "204", description = "Sem conteúdo", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "206", description = "Conteúdo parcial"),
            @APIResponse(responseCode = "300", description = "Escolhas múltiplas"),
            @APIResponse(responseCode = "301", description = "Movido permanentemente"),
            @APIResponse(responseCode = "302", description = "Encontrado"),
            @APIResponse(responseCode = "304", description = "Não modificado"),
            @APIResponse(responseCode = "307", description = "Redirecionamento temporário"),
            @APIResponse(responseCode = "308", description = "Redirecionamento permanente"),
            @APIResponse(responseCode = "400", description = "Informações inválidas enviadas"),
            @APIResponse(responseCode = "401", description = "Não autorizado"),
            @APIResponse(responseCode = "402", description = "Pagamento necessário"),
            @APIResponse(responseCode = "403", description = "Proibido"),
            @APIResponse(responseCode = "404", description = "Não encontrado"),
            @APIResponse(responseCode = "409", description = "Conflito"),
            @APIResponse(responseCode = "500", description = "Erro no processo de listagem"),
            @APIResponse(responseCode = "501", description = "Não implementado"),
            @APIResponse(responseCode = "502", description = "Gateway ruim"),
            @APIResponse(responseCode = "503", description = "Serviço indisponível"),
            @APIResponse(responseCode = "504", description = "Timeout no gateway"),
            @APIResponse(responseCode = "505", description = "HTTP Version Not Supported")
    })
    @Path("")
    public Response create(Person person) {
        try {
            validatePerson(person);
            validateEmailUniqueness(person.getEmail());
            personQuery.create(person, LocalDateTime.now());
            return Response.status(Response.Status.CREATED).build();
        } catch (ValidationException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (EmailAlreadyTakenException e) {
            return buildBadRequestResponse(EMAIL_ALREADY_IN_USE);
        }
    }

    @Operation(summary = "Atualização de cadastro", description = "Faz a alteração dos dados já cadastrados de uma pessoa")
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    @APIResponses(value = {
            @APIResponse(responseCode = "100", description = "Continue"),
            @APIResponse(responseCode = "101", description = "Switching protocols"),
            @APIResponse(responseCode = "102", description = "Processing"),
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "201", description = "Pessoa criada com sucesso", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "202", description = "Informações inválidas enviadas", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "204", description = "Sem conteúdo", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "206", description = "Conteúdo parcial"),
            @APIResponse(responseCode = "300", description = "Escolhas múltiplas"),
            @APIResponse(responseCode = "301", description = "Movido permanentemente"),
            @APIResponse(responseCode = "302", description = "Encontrado"),
            @APIResponse(responseCode = "304", description = "Não modificado"),
            @APIResponse(responseCode = "307", description = "Redirecionamento temporário"),
            @APIResponse(responseCode = "308", description = "Redirecionamento permanente"),
            @APIResponse(responseCode = "400", description = "Informações inválidas enviadas"),
            @APIResponse(responseCode = "401", description = "Não autorizado"),
            @APIResponse(responseCode = "402", description = "Pagamento necessário"),
            @APIResponse(responseCode = "403", description = "Proibido"),
            @APIResponse(responseCode = "404", description = "Não encontrado"),
            @APIResponse(responseCode = "409", description = "Conflito"),
            @APIResponse(responseCode = "500", description = "Erro no processo de listagem"),
            @APIResponse(responseCode = "501", description = "Não implementado"),
            @APIResponse(responseCode = "502", description = "Gateway ruim"),
            @APIResponse(responseCode = "503", description = "Serviço indisponível"),
            @APIResponse(responseCode = "504", description = "Timeout no gateway"),
            @APIResponse(responseCode = "505", description = "HTTP Version Not Supported")
    })
    public Response editById(Person person, @PathParam("id") Long id) {
        try {
            validatePerson(person);
            personQuery.update(person, LocalDateTime.now(), id);
            return Response.status(Response.Status.OK).entity(personQuery.findById(id)).build();
        } catch (ValidationException e) {
            return buildBadRequestResponse(e.getMessage());
        }
    }

    @Operation(summary = "Deletar cadastro", description = "Deleta um cadastro de pessoa")
    @DELETE
    @Path("{id}")
    @APIResponses(value = {
            @APIResponse(responseCode = "100", description = "Continue"),
            @APIResponse(responseCode = "101", description = "Switching protocols"),
            @APIResponse(responseCode = "102", description = "Processing"),
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "201", description = "Pessoa criada com sucesso", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "202", description = "Informações inválidas enviadas", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "204", description = "Sem conteúdo", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "206", description = "Conteúdo parcial"),
            @APIResponse(responseCode = "300", description = "Escolhas múltiplas"),
            @APIResponse(responseCode = "301", description = "Movido permanentemente"),
            @APIResponse(responseCode = "302", description = "Encontrado"),
            @APIResponse(responseCode = "304", description = "Não modificado"),
            @APIResponse(responseCode = "307", description = "Redirecionamento temporário"),
            @APIResponse(responseCode = "308", description = "Redirecionamento permanente"),
            @APIResponse(responseCode = "400", description = "Informações inválidas enviadas"),
            @APIResponse(responseCode = "401", description = "Não autorizado"),
            @APIResponse(responseCode = "402", description = "Pagamento necessário"),
            @APIResponse(responseCode = "403", description = "Proibido"),
            @APIResponse(responseCode = "404", description = "Não encontrado"),
            @APIResponse(responseCode = "409", description = "Conflito"),
            @APIResponse(responseCode = "500", description = "Erro no processo de listagem"),
            @APIResponse(responseCode = "501", description = "Não implementado"),
            @APIResponse(responseCode = "502", description = "Gateway ruim"),
            @APIResponse(responseCode = "503", description = "Serviço indisponível"),
            @APIResponse(responseCode = "504", description = "Timeout no gateway"),
            @APIResponse(responseCode = "505", description = "HTTP Version Not Supported")
    })
    public Response delete(@PathParam("id") Long id) {
        try {
            Person person = personQuery.findById(id);
            if (person != null) {
                personQuery.delete(person, LocalDateTime.now(), id);
                return Response.status(Response.Status.NO_CONTENT).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (ValidationException e) {
            return buildBadRequestResponse(e.getMessage());
        } catch (UnableToExecuteStatementException e) {
            Throwable cause = e.getCause();
            if (cause instanceof JdbcSQLIntegrityConstraintViolationException) {
                JdbcSQLIntegrityConstraintViolationException integrityViolationException = (JdbcSQLIntegrityConstraintViolationException) cause;
                if (integrityViolationException.getErrorCode() == 23503) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity("Não é possível excluir a pessoa, pois ela é líder de uma equipe.")
                            .build();
                }
            }
            // Caso contrário, trata como erro interno do servidor
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    private void validatePerson(Person person) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);
        if (!constraintViolations.isEmpty()) {
            throw new ValidationException(constraintViolations.iterator().next().getMessage());
        }
    }

    private void validateEmailUniqueness(String email) {
        if (isEmailAlreadyTaken(email)) {
            throw new EmailAlreadyTakenException(EMAIL_ALREADY_IN_USE);
        }
    }

    private boolean isEmailAlreadyTaken(String email) {
        List<Person> existingPersons = personQuery.findByEmail(email);
        return !existingPersons.isEmpty();
    }

    private Response buildBadRequestResponse(String message) {
        return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
    }

    private static class EmailAlreadyTakenException extends RuntimeException {
        public EmailAlreadyTakenException(String message) {
            super(message);
        }
    }
}