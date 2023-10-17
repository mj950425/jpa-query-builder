package persistence.sql.ddl.builder;

import entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistence.fixture.TestEntityFixture;
import persistence.sql.ddl.DatabaseTest;
import persistence.sql.ddl.PersonRowMapper;
import persistence.sql.ddl.model.DDLType;
import persistence.sql.ddl.model.DatabaseType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static persistence.study.TestUtils.assertDoesNotThrowException;

@Nested
@DisplayName("CreateDDLQueryBuilder 클래스의")
public class CreateDDLQueryBuilderTest extends DatabaseTest {

    @Nested
    @DisplayName("prepareStatement 메소드는")
    class prepareStatement {
        @Nested
        @DisplayName("유효한 엔티티 정보가 주어지면")
        class withValidEntity {
            @Test
            @DisplayName("CREATE DDL을 리턴한다.")
            void returnDDL() {
                String ddl = DDLQueryBuilder.build()
                        .ddlType(DDLType.CREATE)
                        .database(DatabaseType.H2)
                        .build()
                        .prepareStatement(Person.class);

                assertDoesNotThrowException(() -> {
                    jdbcTemplate.execute(ddl);
                    insertPerson(new Person(1L, "민준", 29, "minjoon1995@naver.com"));
                });

                Person person = findPersonById(1L);

                Assertions.assertAll(
                        () -> assertThat(person.getEmail()).isEqualTo("minjoon1995@naver.com"),
                        () -> assertThat(person.getAge()).isEqualTo(29),
                        () -> assertThat(person.getName()).isEqualTo("민준"),
                        () -> assertThat(person.getId()).isEqualTo(1),
                        () -> assertThat(ddl).isEqualTo("CREATE TABLE users ( id BIGINT GENERATED BY DEFAULT AS IDENTITY," +
                                " nick_name VARCHAR(255), old INTEGER NOT NULL, email VARCHAR(255) NOT NULL );")
                );
            }

            private Person findPersonById(Long id) {
                return jdbcTemplate.queryForObject(
                        String.format("SELECT * FROM users WHERE id = %s", id),
                        new PersonRowMapper()
                );
            }

            private void insertPerson(Person person) {
                String sql = String.format("insert into users (id, nick_name, old, email) VALUES (%s, '%s', %s, '%s')",
                        person.getId(),
                        person.getName(),
                        person.getAge(),
                        person.getEmail()
                );
                jdbcTemplate.execute(sql);
            }
        }

        @Nested
        @DisplayName("@Id 어노테이션이 2개인 엔티티 정보가 주어지면")
        class withMultiIdAnnotatedEntity {
            @Test
            @DisplayName("예외를 반환한다.")
            void throwException() {
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> DDLQueryBuilder.build()
                                .ddlType(DDLType.CREATE)
                                .database(DatabaseType.H2)
                                .build()
                                .prepareStatement(TestEntityFixture.EntityWithMultiIdAnnotation.class));

                assertThat(e.getMessage()).contains("@Id 어노테이션이 정확히 1개 존재해야합니다.");
            }
        }

        @Nested
        @DisplayName("@Entity 어노테이션이 없는 엔티티 정보가 주어지면")
        class withOutEntityAnnotatedEntity {
            @Test
            @DisplayName("예외를 반환한다.")
            void throwException() {
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> DDLQueryBuilder.build()
                                .ddlType(DDLType.CREATE)
                                .database(DatabaseType.H2)
                                .build()
                                .prepareStatement(TestEntityFixture.EntityWithOutEntityAnnotation.class));

                assertThat(e.getMessage()).contains("엔티티 어노테이션이 없습니다.");
            }
        }
    }
}
