package ibratan.treinamento.person.team;
import ibratan.treinamento.person.person.Person;
import ibratan.treinamento.person.person.PersonQuery;
import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.validation.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Tag(name = "Team API", description = "Endpoints para gerenciamento de Time")
@Path("/api/team")
public class TeamResource {

    @Inject
    TeamQuery teamQuery;
    @Inject
    PersonQuery personQuery;

    @Operation(summary = "Listar pessoas cadastradas em times", description = "Faz a listagem de pessoas cadastradas nos times pelo id")
    @GET
    @Path("/list/{teamId}/persons")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "500", description = "Erro de processo")
    })
    public List<Person> listPersons(@PathParam("teamId") Long teamId) {
        List<Long> personIds = teamQuery.listPersonIdsInTeam(teamId);
        List<Person> persons = new ArrayList<>();

        for (Long personId : personIds) {
            Person person = personQuery.findById(personId);
            if (person != null) {
                persons.add(person);
            }
        }
        return persons;
    }

    @Operation(summary = "Listar times cadastrados", description = "Faz a listagem de todas as pessoas cadastradas")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "500", description = "Erro de processo")
    })
    @GET
    @Path("/list")
    public List<Team> list() {
        return teamQuery.list();
    }

    @Operation(summary = "Listar único cadastro de time", description = "Faz a listagem de uma única pessoa cadastrada")
    @GET
    @Path("{id}")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "404", description = "Time não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro de processo")
    })
    public Team findById(@NotNull @PathParam("id") Long id) {
        return teamQuery.findById(id);
    }

    @Operation(summary = "Cadastro de time", description = "Faz um novo cadastro de time")
    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Time cadastrado com sucesso", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "400", description = "Informações inválidas enviadas"),
            @APIResponse(responseCode = "500", description = "Erro de processamento")
    })
    public Response create(@Valid Team team) {
        String validationError = getValidationError(team);
        if (validationError != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro de validação: " + validationError)
                    .build();
        }

        boolean emailOrNameExistsInOtherTeams = teamQuery.isEmailOrNameInOtherTeams(team.getEmail(), team.getName(), team.getId());
        if (emailOrNameExistsInOtherTeams) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("E-mail ou nome já existem em outras equipes.")
                    .build();
        }

        teamQuery.create(team, LocalDateTime.now());
        return Response.status(Response.Status.CREATED).build();
    }

    @Operation(summary = "Atualização de cadastro", description = "Faz a alteração dos dados já cadastrados de um time")
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "404", description = "Time não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro de processo")
    })
    public Team editById(@NotNull @Valid Team team, @NotNull @PathParam("id") Long id) {
        String validationError = getValidationError(team);
        if (validationError != null) {
            throw new BadRequestException("Erro de validação: " + validationError);
        }

        teamQuery.update(team, LocalDateTime.now(), id);
        return teamQuery.findById(id);
    }

    @Operation(summary = "Deletar cadastro", description = "Deleta um cadastro de time")
    @DELETE
    @Path("{id}")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "404", description = "Time não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro de processo")
    })
    public Response delete(@PathParam("id") Long id) {
        Team team = teamQuery.findById(id);

        if (team == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Time não encontrado.")
                    .build();
        }

        int participantCount = teamQuery.countParticipantsInTeam(id);
        if (participantCount > 0) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Não é possível excluir o time, pois possui participantes.")
                    .build();
        }

        teamQuery.delete(team, LocalDateTime.now(), id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Operation(summary = "Associar pessoa a time", description = "Associa uma pessoa a um time")
    @POST
    @Path("{teamId}/add-participant/{personId}")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "404", description = "Time ou pessoa não encontrada"),
            @APIResponse(responseCode = "500", description = "Erro de processo")
    })
    public Response addParticipant(@PathParam("teamId") Long teamId, @PathParam("personId") Long personId) {
        if (teamQuery.isPersonInTeam(teamId, personId)) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A pessoa já está no time.")
                    .build();
        }

        int teamCount = teamQuery.countTeamsParticipatedByPerson(personId);
        if (teamCount >= 1) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A pessoa já está participando de outro time.")
                    .build();
        }

        teamQuery.addParticipant(teamId, personId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Operation(summary = "Remover pessoa de time", description = "Remove uma pessoa de um time")
    @DELETE
    @Path("{teamId}/remove-participant/{personId}")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "404", description = "Time ou pessoa não encontrada"),
            @APIResponse(responseCode = "500", description = "Erro de processo")
    })
    public Response removeParticipant(@PathParam("teamId") Long teamId, @PathParam("personId") Long personId) {
        if (!teamQuery.isPersonInTeam(teamId, personId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("A pessoa não está no time.")
                    .build();
        }
        teamQuery.removeParticipant(teamId, personId);
        return Response.status(Response.Status.NO_CONTENT).build();
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