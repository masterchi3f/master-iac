package uks.master.thesis.terraform.syntax

class Identifier(name: String) {
    private var name: String

    init {
        require("^[a-zA-Z_][a-zA-Z0-9_-]*\$".toRegex().matches(name)) {
            "Found \"$name\". Identifier should only contains dash, underscore, letters and digits." +
                " No digit or dash at position 0 allowed."
        }
        this.name = name
    }

    override fun toString(): String = name
}
