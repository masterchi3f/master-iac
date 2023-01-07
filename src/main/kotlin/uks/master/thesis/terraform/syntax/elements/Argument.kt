package uks.master.thesis.terraform.syntax.elements

import uks.master.thesis.terraform.SubModule
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.blocks.DataSource
import uks.master.thesis.terraform.syntax.elements.blocks.InputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.OutputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.Provider
import uks.master.thesis.terraform.syntax.elements.blocks.Resource
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNull
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString

class Argument private constructor(
    private val name: Identifier,
    private val value: Expression,
    private val comment: OneLineComment?
): Element {
    private var additionalSpaces: Int = 0

    class Builder {
        private lateinit var _name: Identifier
        private lateinit var _value: Expression
        private var _comment: OneLineComment? = null

        fun name(name: String) = apply { preventDupName(); _name = Identifier(name) }
        fun nullValue() = apply { preventDupValue(); _value = TfNull() }
        fun value(bool: Boolean) = apply { preventDupValue(); _value = TfBool(bool); }
        fun value(number: Double) = apply { preventDupValue(); _value = TfNumber(number) }
        fun value(string: String) = apply { preventDupValue(); _value = TfString(string) }
        fun value(list: TfList) = apply { preventDupValue(); _value = list }
        fun value(map: TfMap) = apply { preventDupValue(); _value = map }
        fun value(inputVariable: InputVariable<out Expression>) = apply { preventDupValue(); _value = inputVariable.reference }
        fun <S: Expression>value(resource: Resource, attribute: String? = null) = apply { preventDupValue(); _value = attribute?.let { resource.reference<S>(attribute) } ?: resource.reference() }
        fun <S: Expression>value(dataSource: DataSource, attribute: String? = null) = apply { preventDupValue(); _value = attribute?.let { dataSource.reference<S>(attribute) } ?: dataSource.reference() }
        fun value(subModule: SubModule, outputVariable: OutputVariable<out Expression>) = apply { preventDupValue(); _value = subModule.output(outputVariable) }
        /**
         * @param alternate: If true the argument is used in a submodule which has multiple provider from same type.
         * The dot (.) is switched with a dash (-).
         * See uks.master.thesis.terraform.syntax.elements.blocks.TfModule for more.
         * See uks.master.thesis.terraform.syntax.elements.blocks.RequiredProviders for more.
         */
        fun value(provider: Provider, alternate: Boolean) = apply {
            preventDupValue(); provider.alias?.let {
            // Not sure if alternate with - works because it is done with . in the docs:
            // https://developer.hashicorp.com/terraform/language/modules/develop/providers
                _value = TfRef<Expression>("${provider.name}${if (alternate) "-" else "."}$it")
            } ?: throw IllegalArgumentException("alias from ${provider.name} was null!")
        }
        fun raw(raw: String) = apply { preventDupValue(); _value = Raw(raw) }
        fun comment(symbol: OneLineSymbol, text: String) = apply { preventDupComment(); _comment = OneLineComment(symbol, text) }
        fun build() = Argument(_name, _value, _comment)

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }

        private fun preventDupValue() {
            if (::_value.isInitialized) throw IllegalArgumentException("value was already set to $_value!")
        }

        private fun preventDupComment() {
            _comment?.let { throw IllegalArgumentException("comment was already set to $it!") }
        }
    }

    fun nameSize(): Int = name.toString().length

    fun setAdditionalSpaces(spaces: Int) = apply { additionalSpaces = spaces }

    override fun toString(): String = "$name${" ".repeat(additionalSpaces)} = $value${comment?.let { " $it" } ?: ""}${System.lineSeparator()}"
}
