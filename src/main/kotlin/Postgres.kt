import org.jetbrains.exposed.sql.statements.api.ExposedConnection
import org.postgresql.jdbc.PgConnection
import org.postgresql.util.PGobject
import java.io.InputStream

fun ExposedConnection<*>.copyInto(tableName: String, from: InputStream) {
    val copyApi = (this.connection as PgConnection).copyAPI
    copyApi.copyIn("COPY $tableName FROM STDIN CSV HEADER", from)
}

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}
