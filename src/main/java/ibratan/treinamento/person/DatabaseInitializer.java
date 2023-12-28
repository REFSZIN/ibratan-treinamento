package ibratan.treinamento.person;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@ApplicationScoped
public class DatabaseInitializer {

    @Inject
    private Jdbi jdbi;

    @Startup
    void init() throws URISyntaxException, IOException {
        var fileDir = Objects.requireNonNull(getClass().getClassLoader().getResource("init.sql")).toURI();
        var commandSql = Files.readString(Path.of(fileDir));
        try (Handle handle = jdbi.open()) {
            handle.createScript(commandSql).execute();
        }
    }
}