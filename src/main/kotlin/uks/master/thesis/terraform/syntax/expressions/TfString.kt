package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

data class TfString(
    val string: String
): Expression {
    override fun toString(): String {
        return "\"$string\""
    }
}
