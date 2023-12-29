package ibratan.treinamento.person.team;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Tag(name = "Team API", description = "Endpoints para gerenciamento de Time")
@Path("/api/team")
public class TeamResource {

    @Inject
    TeamQuery teamQuery;

    @Operation(summary = "Listar times cadastrados", description = "Faz a listagem de todas as pessoas cadastradas")
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
    public List<Team> list() {
        return teamQuery.list();
    }

    @Operation(summary = "Listar único cadastro de time", description = "Faz a listagem de uma única pessoa cadastrada")
    @GET
    @Path("{id}")
    @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @APIResponse(responseCode = "404", description = "Pessoa não encontrada")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public Team findById(@PathParam("id") Long id) {
        return teamQuery.findById(id);
    }

    @Operation(summary = "Cadastro de time", description = "Faz um novo cadastro de time")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "201", description = "Time cadastrado com sucesso", content = @Content(mediaType = "application/json"))
    @APIResponse(responseCode = "400", description = "Informações inválidas enviadas")
    @APIResponse(responseCode = "500", description = "Erro de processamento")
    public Response create(Team team) {
        teamQuery.create(team, LocalDateTime.now());
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(summary = "Atualização de cadastro", description = "Faz a alteração dos dados já cadastrados de um time")
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json"))
    @APIResponse(responseCode = "404", description = "Time não encontrado")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public Team editById(Team team, @PathParam("id") Long id) {
        teamQuery.update(team, LocalDateTime.now(), id);
        return teamQuery.findById(id);
    }

    @Operation(summary = "Deletar cadastro", description = "Deleta um cadastro de time")
    @DELETE
    @Path("{id}")
    @APIResponse(responseCode = "204", description = "No Content")
    @APIResponse(responseCode = "404", description = "Time não encontrado")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public void delete(Team team, @PathParam("id") Long id) {
        teamQuery.delete(team, LocalDateTime.now(), id);
    }

    @Operation(summary = "Associar pessoa a time", description = "Associa uma pessoa a um time")
    @POST
    @Path("{teamId}/add-participant/{personId}")
    @APIResponse(responseCode = "204", description = "No Content")
    @APIResponse(responseCode = "404", description = "Time ou pessoa não encontrada")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public void addParticipant(@PathParam("teamId") Long teamId, @PathParam("personId") Long personId) {
        if (!teamQuery.isPersonInTeam(teamId, personId)) {
            teamQuery.addParticipant(teamId, personId);
        }
    }

    @Operation(summary = "Remover pessoa de time", description = "Remove uma pessoa de um time")
    @DELETE
    @Path("{teamId}/remove-participant/{personId}")
    @APIResponse(responseCode = "204", description = "No Content")
    @APIResponse(responseCode = "404", description = "Time ou pessoa não encontrada")
    @APIResponse(responseCode = "500", description = "Erro de processo")
    public void removeParticipant(@PathParam("teamId") Long teamId, @PathParam("personId") Long personId) {
        if (teamQuery.isPersonInTeam(teamId, personId)) {
            teamQuery.removeParticipant(teamId, personId);
        }
    }

    private String getValidationError(Team team) {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Team>> constraintViolations = validator.validate(team);
            for (ConstraintViolation<Team> violation : constraintViolations)
                return violation.getMessage();
        }
        return null;
    }
}