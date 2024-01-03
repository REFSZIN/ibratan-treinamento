package ibratan.treinamento.person.person;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.time.LocalDateTime;
import java.util.List;

public interface PersonQuery {

    String INSERT_SQL = "INSERT INTO Person (name, age, email, birthDate, lastUpdated) VALUES (:name, :age, :email, :birthDate, :now)";
    String UPDATE_SQL = "UPDATE Person SET name = :name, email = :email, age = :age, birthDate = :birthDate, lastUpdated = :now WHERE id = :id";
    String DELETE_SQL = "DELETE FROM Person WHERE id = :id";
    String SELECT_BY_ID_SQL = "SELECT * FROM Person WHERE id = :id";
    String SELECT_ALL_SQL = "SELECT * FROM Person";
    String SELECT_BY_EMAIL_SQL = "SELECT * FROM Person WHERE email = :email";

    @GetGeneratedKeys
    @SqlUpdate(INSERT_SQL)
    long create(@BindBean Person person, @Bind("now") LocalDateTime now);

    @SqlUpdate(UPDATE_SQL)
    void update(@BindBean Person person, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate(DELETE_SQL)
    void delete(@BindBean Person person, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlQuery(SELECT_BY_ID_SQL)
    @RegisterBeanMapper(Person.class)
    Person findById(@Bind("id") Long id);

    @SqlQuery(SELECT_ALL_SQL)
    @RegisterBeanMapper(Person.class)
    List<Person> list();

    @SqlQuery(SELECT_BY_EMAIL_SQL)
    @RegisterBeanMapper(Person.class)
    List<Person> findByEmail(@Bind("email") String email);
}