package uks.master.thesis.terraform.syntax.expressions

import uks.master.thesis.terraform.syntax.Expression

class TfString(
    string: String
): Expression {
    var string: String

    init {
        require(!string.contains("\"")) {"Found \"$string\". TfString should not contain \"."}
        this.string = string
    }

    override fun toString(): String = "\"$string\""
}
