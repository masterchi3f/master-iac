package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfString(
    private val string: String
): Expression {
    override fun toString(): String {
        return "\"$string\""
    }
}
