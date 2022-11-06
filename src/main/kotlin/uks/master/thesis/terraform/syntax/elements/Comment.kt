package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Element

data class OneLineComment(
    val symbol: OneLineSymbol,
    val text: String
): Element {
    override fun toString(): String {
        return "$symbol $text"
    }
}

enum class OneLineSymbol(val symbol: String) {
    HASHTAG("#"),
    DOUBLE_SLASH("//");

    override fun toString(): String {
        return symbol
    }
}

data class MultiLineComment(
    val body: List<Element>
): Element {
    companion object {
        private const val START: String = "/*"
        private const val END: String = "*/"
    }

    override fun toString(): String {
        var bodyStr = ""
        body.forEach { element: Element -> bodyStr += element }
        return START + System.lineSeparator() + bodyStr + END + System.lineSeparator()
    }
}
