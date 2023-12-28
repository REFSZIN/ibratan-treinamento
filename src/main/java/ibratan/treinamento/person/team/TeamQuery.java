package ibratan.treinamento.person.team;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;
import java.util.List;

public interface TeamQuery {

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO Team (name, email, leader_id, participants, lastUpdated) " +
            "VALUES (:name, :email, :leader, :participants, :now)")
    long create(@BindBean Team team, @Bind("now") LocalDateTime now);

    @SqlUpdate("UPDATE Team SET name = :name, email = :email, leader_id = :leader, " +
            "participants = :participants, lastUpdated = :now WHERE id = :id")
    void update(@BindBean Team team, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate("DELETE FROM Team WHERE id = :id AND participants IS NULL")
    void delete(@BindBean Team team, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate("UPDATE Team SET participants = array_append(participants, :personId) WHERE id = :teamId")
    void addParticipant(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlUpdate("UPDATE Team SET participants = array_remove(participants, :personId) WHERE id = :teamId")
    void removeParticipant(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlQuery("SELECT * FROM Team WHERE id = :id")
    @RegisterBeanMapper(Team.class)
    Team findById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM Team WHERE leader_id = :leaderId")
    @RegisterBeanMapper(Team.class)
    List<Team> findByLeader(@Bind("leaderId") Long leaderId);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM Team WHERE id = :teamId AND :personId = ANY(participants))")
    boolean isPersonInTeam(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlQuery("SELECT * FROM Team")
    @RegisterBeanMapper(Team.class)
    List<Team> list();

}