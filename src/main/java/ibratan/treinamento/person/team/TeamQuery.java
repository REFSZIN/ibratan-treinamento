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
    @SqlUpdate("INSERT INTO Team (name, email, leader_id, lastUpdated) " +
            "VALUES (:name, :email, :leader, :now)")
    long create(@BindBean Team team, @Bind("now") LocalDateTime now);

    @SqlUpdate("UPDATE Team SET name = :name, email = :email, leader_id = :leader, " +
            "lastUpdated = :now WHERE id = :id")
    void update(@BindBean Team team, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate("DELETE FROM Team WHERE id = :id")
    void delete(@BindBean Team team, LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate("INSERT INTO TeamParticipants (team_id, participant_id) VALUES (:teamId, :personId)")
    void addParticipant(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlUpdate("DELETE FROM TeamParticipants WHERE team_id = :teamId AND participant_id = :personId")
    void removeParticipant(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlQuery("SELECT * FROM Team WHERE id = :id")
    @RegisterBeanMapper(Team.class)
    Team findById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM Team WHERE leader_id = :leader")
    @RegisterBeanMapper(Team.class)
    List<Team> findByLeader(@Bind("leaderId") Long leader);

    @SqlQuery("SELECT EXISTS (SELECT 1 FROM TeamParticipants WHERE team_id = :teamId AND participant_id = :personId)")
    boolean isPersonInTeam(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlQuery("SELECT t.* FROM Team t JOIN TeamParticipants tp ON t.id = tp.team_id")
    @RegisterBeanMapper(Team.class)
    List<Team> list();

}