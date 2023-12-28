package ibratan.treinamento.person.person;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Person implements Serializable {
    @Schema(description = "Id de cadastro", readOnly = true)
    private Long id;

    @Schema(description = "Nome da pessoa", readOnly = true)
    @NotNull( message = "O nome não pode ser nulo")
    @Size(min = 5, max = 128, message = "Nome precisa ter no minimo 5 caracteres e no maximo 128" )
    private String name;

    @Schema( description = "Email da pessoa", example = "ibratan@ibratan.com.br", required = true)
    @Email(message = "O e-mail precisa ser valido")
    private String email;

    @Schema( description = "Idade da pessoa", example = "18", required = false)
    @Min(18)
    @Max(199)
    private Integer age;

    @Past
    @Schema( description = "Data de nascimento da pessoa", example = "1990-12-12", required = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema( description = "Ultima atualização", example = "2022-12-12T12:15:50", readOnly = true)
    private LocalDateTime lastUpdated;
}
