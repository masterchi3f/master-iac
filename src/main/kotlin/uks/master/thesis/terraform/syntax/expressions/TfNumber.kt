package uks.master.thesis.terraform.syntax.expressions

import kotlin.math.floor
import uks.master.thesis.terraform.syntax.Expression

class TfNumber(
    private val number: Double
): Expression {
    override fun toString(): String = String.format(if (floor(number) == number) "%.0f" else "%s", number)
}
