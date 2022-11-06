package uks.master.thesis.terraform.syntax

class Identifier(name: String) {
    private var name: String

    init {
        if ("^[a-zA-Z_-][a-zA-Z0-9_-]*\$".toRegex().matches(name)) {
            this.name = name
        } else {
            throw IllegalArgumentException(
                "Found \"$name\". Identifier should only contains -, _, letters and digits. No digit at position 0 allowed."
            )
        }
    }

    override fun toString(): String {
        return name
    }
}
