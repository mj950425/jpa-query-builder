package persistence.sql.ddl.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import persistence.entitiy.attribute.GeneralAttribute;
import persistence.entitiy.attribute.id.IdAttribute;
import persistence.fixture.TestEntityFixture;
import persistence.sql.infra.H2SqlConverter;
import persistence.sql.parser.AttributeParser;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Nested
@DisplayName("AttributeParser 클래스의")
public class AttributeParserTest {
    @Nested
    @DisplayName("parseIdAttribute 메소드는")
    class pareIdAttribute {
        @Nested
        @DisplayName("클래스 정보가 주어지면")
        class withClass {
            @Test
            @DisplayName("IdAttribute 를 반환한다.")
            void returnIdAttribute() {
                AttributeParser attributeParser = new AttributeParser();
                IdAttribute idAttribute = attributeParser
                        .parseIdAttribute(TestEntityFixture.EntityWithValidAnnotation.class);

                assertThat(idAttribute.prepareDDL(new H2SqlConverter())).isEqualTo("id BIGINT GENERATED BY DEFAULT AS IDENTITY");
            }
        }
    }

    @Nested
    @DisplayName("parseGeneralAttributes 메소드는")
    class parseGeneralAttributes {
        @Nested
        @DisplayName("클래스 정보가 주어지면")
        class withClass {
            @Test
            @DisplayName("GeneralAttribute 리스트를 반환한다.")
            void returnGeneralAttributes() {
                AttributeParser attributeParser = new AttributeParser();
                List<GeneralAttribute> generalAttributeList = attributeParser
                        .parseGeneralAttributes(TestEntityFixture.EntityWithValidAnnotation.class);

                Assertions.assertAll(
                        () -> assertThat(generalAttributeList.size()).isEqualTo(2),
                        () -> assertThat(generalAttributeList.get(0).getColumnName())
                                .isEqualTo("name"),
                        () -> assertThat(generalAttributeList.get(0).getFieldName())
                                .isEqualTo("name"),
                        () -> assertThat(generalAttributeList.get(1).getColumnName())
                                .isEqualTo("old"),
                        () -> assertThat(generalAttributeList.get(1).getFieldName())
                                .isEqualTo("age")
                );
            }
        }
    }
}
