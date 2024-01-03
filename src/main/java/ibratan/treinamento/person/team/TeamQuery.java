package ibratan.treinamento.person.team;
import ibratan.treinamento.person.person.Person;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.time.LocalDateTime;
import java.util.List;

public interface TeamQuery {

    String INSERT_SQL = "INSERT INTO Team (name, email, leader_id, lastUpdated) VALUES (:name, :email, :leader, :now)";
    String UPDATE_SQL = "UPDATE Team SET name = :name, email = :email, leader_id = :leader, lastUpdated = :now WHERE id = :id";
    String DELETE_SQL = "DELETE FROM Team WHERE id = :id";
    String ADD_PARTICIPANT_SQL = "INSERT INTO TeamParticipants (team_id, participant_id) VALUES (:teamId, :personId)";
    String REMOVE_PARTICIPANT_SQL = "DELETE FROM TeamParticipants WHERE team_id = :teamId AND participant_id = :personId";
    String FIND_BY_ID_SQL = "SELECT * FROM Team WHERE id = :id";
    String FIND_BY_LEADER_SQL = "SELECT * FROM Team WHERE leader_id = :leader";
    String IS_PERSON_IN_TEAM_SQL = "SELECT EXISTS (SELECT 1 FROM TeamParticipants WHERE team_id = :teamId AND participant_id = :personId)";
    String LIST_TEAMS_SQL = "SELECT * FROM Team";
    String LIST_PERSON_IDS_IN_TEAM_SQL = "SELECT participant_id FROM TeamParticipants WHERE team_id = :teamId";
    String COUNT_PARTICIPANTS_IN_TEAM_SQL = "SELECT COUNT(*) FROM TeamParticipants WHERE team_id = :teamId";
    String COUNT_TEAMS_PARTICIPATED_BY_PERSON_SQL = "SELECT COUNT(DISTINCT team_id) FROM TeamParticipants WHERE participant_id = :personId";
    String IS_EMAIL_OR_NAME_IN_OTHER_TEAMS_SQL = "SELECT EXISTS (SELECT 1 FROM Team WHERE (email = :email OR name = :name) AND id != :id)";

    @GetGeneratedKeys
    @SqlUpdate(INSERT_SQL)
    long create(@BindBean Team team, @Bind("now") LocalDateTime now);

    @SqlUpdate(UPDATE_SQL)
    void update(@BindBean Team team, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate(DELETE_SQL)
    void delete(@BindBean Team team, LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate(ADD_PARTICIPANT_SQL)
    void addParticipant(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlUpdate(REMOVE_PARTICIPANT_SQL)
    void removeParticipant(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlQuery(FIND_BY_ID_SQL)
    @RegisterBeanMapper(Team.class)
    Team findById(@Bind("id") Long id);

    @SqlQuery(FIND_BY_LEADER_SQL)
    @RegisterBeanMapper(Team.class)
    List<Team> findByLeader(@Bind("leaderId") Long leader);

    @SqlQuery(IS_PERSON_IN_TEAM_SQL)
    boolean isPersonInTeam(@Bind("teamId") Long teamId, @Bind("personId") Long personId);

    @SqlQuery(LIST_TEAMS_SQL)
    @RegisterBeanMapper(Team.class)
    List<Team> list();

    @SqlQuery(LIST_PERSON_IDS_IN_TEAM_SQL)
    List<Long> listPersonIdsInTeam(@Bind("teamId") Long teamId);

    @SqlQuery("SELECT * FROM Person WHERE id IN (<personIds>)")
    @RegisterBeanMapper(Person.class)
    List<Person> listPersonsByIds(@BindList("personIds") List<Long> personIds);

    @SqlQuery(COUNT_PARTICIPANTS_IN_TEAM_SQL)
    int countParticipantsInTeam(@Bind("teamId") Long teamId);

    @SqlQuery(COUNT_TEAMS_PARTICIPATED_BY_PERSON_SQL)
    int countTeamsParticipatedByPerson(@Bind("personId") Long personId);

    @SqlQuery(IS_EMAIL_OR_NAME_IN_OTHER_TEAMS_SQL)
    boolean isEmailOrNameInOtherTeams(@Bind("email") String email, @Bind("name") String name, @Bind("id") Long id);
}