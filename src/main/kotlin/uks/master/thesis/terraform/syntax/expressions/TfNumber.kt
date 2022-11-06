package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfNumber(
    private val number: Double
): Expression {
    override fun toString(): String {
        return number.toString()
    }
}
