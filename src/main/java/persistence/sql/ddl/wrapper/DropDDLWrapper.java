package persistence.sql.ddl.wrapper;

import persistence.entitiy.attribute.GeneralAttribute;
import persistence.entitiy.attribute.id.IdAttribute;
import persistence.sql.ddl.converter.SqlConverter;

import java.util.List;

public class DropDDLWrapper implements DDLWrapper {

    private final SqlConverter sqlConverter;

    public DropDDLWrapper(SqlConverter sqlConverter) {
        this.sqlConverter = sqlConverter;
    }

    @Override
    public String wrap(String tableName, IdAttribute idAttribute, List<GeneralAttribute> generalAttributes) {
        return String.format("DROP TABLE %s;", tableName);
    }
}
