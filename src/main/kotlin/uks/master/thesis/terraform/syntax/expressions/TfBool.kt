package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfBool(
    private val bool: Boolean
): Expression {
    override fun toString(): String {
        return bool.toString()
    }
}
