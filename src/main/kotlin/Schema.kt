import org.jetbrains.exposed.dao.id.UUIDTable

object Clients : UUIDTable("clients") {
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val email = varchar("email", 255)
    val phone = varchar("phone", 255)
    val gender = customEnumeration("gender", "Gender", { value -> Gender.valueOf(value as String) }, { PGEnum("Gender", it) })
    val banned = bool("banned")
}

enum class Gender {
    Male, Female
}
