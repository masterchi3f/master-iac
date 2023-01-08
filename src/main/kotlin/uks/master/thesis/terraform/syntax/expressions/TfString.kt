package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfString(
    string: String
): Expression {
    var string: String

    init {
        if (!string.contains("\"")) {
            this.string = string
        } else {
            throw IllegalArgumentException(
                "Found \"$string\". TfString should not contain \"."
            )
        }
    }

    override fun toString(): String = "\"$string\""
}
