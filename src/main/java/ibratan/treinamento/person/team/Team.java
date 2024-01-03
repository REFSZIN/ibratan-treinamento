package ibratan.treinamento.person.team;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Team implements Serializable {

    @Schema(description = "Id de cadastro do time", readOnly = true)
    private Long id;

    @Schema(description = "Nome do time", example = "Ibratan Teste",  required = true )
    @NotNull( message = "O nome do time não pode ser nulo")
    @Size(min = 5, max = 128, message = "Nome precisa ter no minimo 5 caracteres e no maximo 128" )
    private String name;

    @Schema( description = "Email do time", example = "capacitoibratan@ibratan.com.br", required = true)
    @Email(message = "O e-mail do time precisa ser valido")
    private String email;

    @Schema( description = "Id da pessoa leader do time", example = "1", required = true)
    private Integer leader;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema( description = "Ultima atualização feita no time", example = "2022-12-12T12:15:50", readOnly = true)
    private LocalDateTime lastUpdated;

}
