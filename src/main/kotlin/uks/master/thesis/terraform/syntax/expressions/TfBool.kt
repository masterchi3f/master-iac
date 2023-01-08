package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfBool(
    val bool: Boolean
): Expression {
    override fun toString(): String = bool.toString()
}
