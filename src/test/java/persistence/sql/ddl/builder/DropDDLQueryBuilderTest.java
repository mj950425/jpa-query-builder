package persistence.sql.ddl.builder;

import entity.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistence.sql.ddl.model.DDLType;
import persistence.sql.ddl.model.DatabaseType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Nested
@DisplayName("DropDDLQueryBuilder 클래스의")
public class DropDDLQueryBuilderTest {
    @Nested
    @DisplayName("prepareStatement 메소드는")
    class prepareStatement {
        @Nested
        @DisplayName("유효한 엔티티 정보가 주어지면")
        class withValidEntity {
            @Test
            @DisplayName("DROP DDL을 리턴한다.")
            void testOf() {
                String ddl = DDLQueryBuilder.build()
                        .ddlType(DDLType.DROP)
                        .database(DatabaseType.H2)
                        .build()
                        .prepareStatement(Person.class);
                assertThat(ddl).isEqualTo("DROP TABLE users;");
            }
        }
    }
}