package persistence.sql.ddl;

import entity.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistence.fixture.TestEntityFixture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Nested
@DisplayName("CreateDDLQueryBuilder 클래스의")
public class CreateDDLQueryBuilderTest {

    @Nested
    @DisplayName("of 메소드는")
    class of {
        @Nested
        @DisplayName("유효한 엔티티 정보가 주어지면")
        class withValidEntity {
            @Test
            @DisplayName("CREATE DDL을 리턴한다.")
            void testOf() {
                String ddl = CreateDDLQueryBuilder.of(Person.class);
                assertThat(ddl).isEqualTo("CREATE TABLE users ( id BIGINT GENERATED BY DEFAULT AS IDENTITY," +
                        " nick_name VARCHAR(255), old INTEGER NOT NULL, email VARCHAR(255) NOT NULL );");
            }
        }

        @Nested
        @DisplayName("@Id 어노테이션이 2개인 엔티티 정보가 주어지면")
        class withMultiIdAnnotatedEntity {
            @Test
            @DisplayName("예외를 반환한다.")
            void testOf() {
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> CreateDDLQueryBuilder.of(TestEntityFixture.EntityWithMultiIdAnnotation.class));

                assertThat(e.getMessage()).contains("@Id 어노테이션이 정확히 1개 존재해야합니다.");
            }
        }

        @Nested
        @DisplayName("@Entity 어노테이션이 없는 엔티티 정보가 주어지면")
        class withOutEntityAnnotatedEntity {
            @Test
            @DisplayName("예외를 반환한다.")
            void testOf() {
                IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                        () -> CreateDDLQueryBuilder.of(TestEntityFixture.EntityWithOutEntityAnnotation.class));

                assertThat(e.getMessage()).contains("엔티티 어노테이션이 없습니다.");
            }
        }
    }
}
