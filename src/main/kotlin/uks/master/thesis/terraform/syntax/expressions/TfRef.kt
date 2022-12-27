package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfRef<T : Expression>(
    private val reference: String
): Expression {
    override fun toString(): String = reference
}
