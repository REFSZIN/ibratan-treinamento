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

    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO Person (name, age, email, birthDate , lastUpdated) VALUES (:name, :age, :email, :birthDate, :now)")
    long create(@BindBean Person person, @Bind("now") LocalDateTime now);

    @SqlUpdate("UPDATE Person set name = :name, email = :email, age = :age, birthDate = :birthDate, lastUpdated = :now WHERE id = :id")
    void update(@BindBean Person person, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlUpdate("DELETE FROM Person WHERE id = :id")
    void delete(@BindBean Person person, @Bind("now") LocalDateTime now, @Bind("id") Long id);

    @SqlQuery("SELECT * FROM Person WHERE id = :id")
    @RegisterBeanMapper(Person.class)
    Person findById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM Person")
    @RegisterBeanMapper(Person.class)
    List<Person> list();

}