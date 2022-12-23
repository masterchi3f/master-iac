package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.SubModule
import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.utils.Utils

@Suppress("UNCHECKED_CAST")
class TfModule private constructor(
    private val block: Block,
    val self: String,
    val type: Type
): Element, Child {
    private companion object {
        const val MODULE: String = "module"
        const val SOURCE: String = "source"
        const val VERSION: String = "version"
        const val PROVIDERS: String = "providers"
    }

    abstract class Builder<T> {
        private var subModuleNames: List<String> = mutableListOf()
        private var providerBuilder: TfMap.Builder? = null
        protected lateinit var _name: Identifier
        protected val blockBuilder: Block.Builder = Block.Builder()
        protected val sourceBuilder: Argument.Builder = Argument.Builder().name(SOURCE)

        open fun name(name: String): T = apply { preventDupName(); _name =
            Identifier(name)
        } as T
        open fun addInputVariable(inputVariable: InputVariable): T =
            apply { blockBuilder.addElement(Argument.Builder().name(inputVariable.name).value(inputVariable).build()) } as T
        open fun addInputVariable(resource: Resource, attribute: String? = null): T =
            apply {
                blockBuilder.addElement(Argument.Builder()
                    .name(attribute ?: Utils.convertToIdentifier(resource.reference()))
                    .value(resource, attribute).build())
            } as T
        open fun addInputVariable(dataSource: DataSource, attribute: String? = null): T =
            apply {
                blockBuilder.addElement(Argument.Builder()
                    .name(attribute ?: Utils.convertToIdentifier(dataSource.reference()))
                    .value(dataSource, attribute).build())
            } as T
        open fun addInputVariable(subModule: SubModule, outputVariable: OutputVariable): T =
            apply {
                subModuleNames = subModuleNames + subModule.name
                blockBuilder.addElement(Argument.Builder()
                    .name(outputVariable.name)
                    .value(subModule, outputVariable).build())
            } as T
        /**
         * @param alternate: If true there are more providers from same type in the root module.
         * A dash (-) with the alias is added to the key.
         * See uks.master.thesis.terraform.syntax.elements.blocks.RequiredProviders for more.
         * An example with multiple providers with same type looks like this:
         * providers = {
         *   aws-usw1 = aws.usw1
         *   aws-usw2 = aws.usw2
         * }
         */
        open fun addProvider(provider: Provider, alternate: Boolean = false): T =
            apply {
                provider.alias?.let {
                    providerBuilder ?: run { providerBuilder = TfMap.Builder() }
                    // Not sure if alternate with - works because it is done with . in the docs:
                    // https://developer.hashicorp.com/terraform/language/modules/develop/providers
                    providerBuilder?.put(
                        "${provider.name}${if (alternate) "-$it" else ""}",
                        TfRef("${provider.name}.$it")
                    )
                } ?: throw IllegalArgumentException("alias from ${provider.name} was null!")
            } as T

        abstract fun build(): TfModule

        protected fun build(sourceBuilder: Argument.Builder, type: Type): TfModule {
            providerBuilder?.let {
                blockBuilder.addElement(Argument.Builder().name(PROVIDERS).value(it.build()).build())
            }
            val name = "$MODULE.$_name"
            if (subModuleNames.contains(name)) {
                throw IllegalStateException("An output variable from this module was defined as input variable!")
            }
            return TfModule(blockBuilder.type(MODULE).addLabel(_name.toString()).addElement(sourceBuilder.build()).build(), name, type)
        }

        private fun preventDupName() {
            if (::_name.isInitialized) throw IllegalArgumentException("name was already set to $_name!")
        }
    }

    class Local {
        class Builder: TfModule.Builder<Builder>() {
            override fun build(): TfModule = build(sourceBuilder.value("./$_name"), Type.LOCAL)
        }
    }

    class Registry {
        class Builder: TfModule.Builder<Builder>() {
            private val versionBuilder: Argument.Builder = Argument.Builder().name(VERSION)

            fun source(source: String) = apply { sourceBuilder.value(source) }
            fun version(version: String) = apply { versionBuilder.value(version) }

            override fun build(): TfModule = kotlin.run {
                blockBuilder.addElement(versionBuilder.build())
                build(sourceBuilder, Type.REGISTRY)
            }
        }
    }

    class Other {
        class Builder: TfModule.Builder<Builder>() {
            fun source(source: String) = apply { sourceBuilder.value(source) }

            override fun build(): TfModule = build(sourceBuilder, Type.OTHER)
        }
    }

    enum class Type {
        LOCAL,
        REGISTRY,
        OTHER
    }

    override fun toString(): String = block.toString()
}
