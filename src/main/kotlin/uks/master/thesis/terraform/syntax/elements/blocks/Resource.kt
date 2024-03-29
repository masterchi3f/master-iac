package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.SubModule
import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.DependsOn
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.Import
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.elements.MultiLineComment
import uks.master.thesis.terraform.syntax.elements.OneLineSymbol
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.Reference

open class Resource protected constructor(
    private val block: Block,
    private val self: String
): Element, Child {
    private companion object {
        const val RESOURCE: String = "resource"
        const val PROVIDER: String = "provider"
    }

    val reference get() = Reference<Raw>(self)

    @Suppress("UNCHECKED_CAST")
    open class GBuilder<T>: DependsOn() {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val providerBuilder: Argument.Builder = Argument.Builder().name(PROVIDER)
        private var dependencies: List<Reference<Raw>> = mutableListOf()
        private lateinit var _type: Identifier
        private lateinit var _name: Identifier

        fun resourceType(type: String): T = apply { preventDupType(); _type = Identifier(type) } as T
        fun resourceName(name: String): T = apply { preventDupName(); _name = Identifier(name) } as T
        fun addElement(block: Block): T = apply { blockBuilder.addElement(block) } as T
        fun addElement(argument: Argument): T = apply { blockBuilder.addElement(argument) } as T
        fun addElement(symbol: OneLineSymbol, text: String): T = apply { blockBuilder.addElement(symbol, text) } as T
        fun addElement(multiLineComment: MultiLineComment): T = apply { blockBuilder.addElement(multiLineComment) } as T
        fun provider(provider: Provider, alternate: Boolean = false): T =
            apply { blockBuilder.addElement(providerBuilder.value(provider, alternate).build()) } as T
        fun addDependency(resource: Resource): T = apply { dependencies = dependencies + Reference(resource.referenceString()) } as T
        fun addDependency(inputVariable: InputVariable<Raw>): T = apply { dependencies = dependencies + inputVariable.reference } as T
        fun addDependency(subModule: SubModule): T = apply { dependencies = dependencies + Reference(subModule.name) } as T
        open fun build() = Resource(buildBlock(), buildSelf())
        protected fun buildBlock(): Block {
            blockBuilder.type(RESOURCE).addLabel(_type.toString()).addLabel(_name.toString())
            buildDependencies(blockBuilder, dependencies)
            return blockBuilder.build()
        }
        protected fun buildSelf(): String = "$_type.$_name"

        private fun preventDupType() {
            require(!::_type.isInitialized) {"type was already set to $_type!"}
        }

        private fun preventDupName() {
            require(!::_name.isInitialized) {"name was already set to $_name!"}
        }
    }

    class Builder: GBuilder<Builder>()

    fun referenceString(attribute: String? = null): String = attribute?.let { "$self.$it" } ?: self
    fun <S: Expression>reference(value: String): Reference<S> = Reference("$self.$value")

    /**
     *  @return Import object with address "((module.<module_name>.)*)<resource_type>.<resource_name>"
     *  (regular expression is given, real type will be String)
     */
    fun import(id: String, modules: List<TfModule>?): Import =
        Import(modules?.let { m -> "${m.joinToString("") { "${it.reference}." }}$self" } ?: self, id)

    override fun toString(): String = block.toString()
}
