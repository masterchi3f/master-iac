package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier

class Argument(
    name: String,
    private val value: Expression,
    private val comment: OneLineComment? = null
): Element {
    private var name: Identifier

    init {
        this.name = Identifier(name)
    }

    override fun toString(): String {
        return "$name = $value${comment?.let { " $it" } ?: ""}${System.lineSeparator()}"
    }
}
