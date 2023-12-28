package ibratan.treinamento.person.person;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;

@ApplicationScoped
public class PersonProducer {
    @Inject
    private Jdbi jdbi;

    @ApplicationScoped
    public PersonQuery personQuery(Jdbi jdbi) {
        return jdbi.onDemand(PersonQuery.class);
    }
}
