package com.interview

import org.postgresql.jdbc.PgConnection
import org.postgresql.util.PGobject
import org.postgresql.util.PSQLException
import java.io.InputStream

fun PgConnection.copyInto(tableName: String, from: InputStream) {
    try {
        copyAPI.copyIn("COPY $tableName FROM STDIN CSV HEADER", from)
    } catch (e: PSQLException) {
        // there seems to be a bug in Exposed that swallows raw PSQLException in this case
        throw RuntimeException(e)
    }
}

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}
