package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element

class OneLineComment(
    private val symbol: OneLineSymbol,
    private val text: String
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

class MultiLineComment(
    private val elements: List<Element>
): Element, Child {
    private companion object {
        private const val START: String = "/*"
        private const val END: String = "*/"
    }

    class Builder {
        private var _elements: List<Element> = mutableListOf()

        fun addElement(block: Block) = apply { _elements = _elements + block }
        fun addElement(argument: Argument) = apply { _elements = _elements + argument }
        fun addElement(symbol: OneLineSymbol, text: String) = apply { _elements = _elements + OneLineComment(symbol, text) }
        fun build() = MultiLineComment(_elements)
    }

    override fun toString(): String {
        var bodyStr = ""
        elements.forEach { element: Element -> bodyStr += element }
        return START + System.lineSeparator() + bodyStr + END + System.lineSeparator()
    }
}
