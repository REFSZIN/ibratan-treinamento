package ibratan.treinamento.person;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@ApplicationScoped
public class PersistenceConfig {
    @Inject AgroalDataSource dataSource;

    @ApplicationScoped
    public Jdbi jdbi(){
        return Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin());
     }
}
