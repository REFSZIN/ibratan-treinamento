package ibratan.treinamento.person.team;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;

@ApplicationScoped
public class TeamProducer {
    @Inject
    private Jdbi jdbi;

    @ApplicationScoped
    public TeamQuery personQuery(Jdbi jdbi) {
        return jdbi.onDemand(TeamQuery.class);
    }
}
