package persistence.sql.ddl.builder;

import entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistence.sql.ddl.DatabaseTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static persistence.sql.ddl.model.DDLType.DROP;
import static persistence.sql.ddl.model.DatabaseType.H2;

@Nested
@DisplayName("DropDDLQueryBuilder 클래스의")
public class DropDDLQueryBuilderTest extends DatabaseTest {
    @Nested
    @DisplayName("prepareStatement 메소드는")
    class prepareStatement {
        @Nested
        @DisplayName("유효한 엔티티 정보가 주어지면")
        class withValidEntity {
            @Test
            @DisplayName("DROP DDL을 리턴한다.")
            void returnDDL() {
                String drop = DDLQueryBuilderFactory.createQueryBuilder(DROP, H2)
                        .prepareStatement(Person.class);

                String message = Assertions.assertThrows(RuntimeException.class, () -> jdbcTemplate.execute(drop)).getMessage();
                assertThat(message).contains("Table \"USERS\" not found; SQL statement");
                assertThat(drop).isEqualTo("DROP TABLE users;");
            }
        }
    }
}
