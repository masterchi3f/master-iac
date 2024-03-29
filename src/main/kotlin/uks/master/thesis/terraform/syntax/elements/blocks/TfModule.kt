package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.SubModule
import uks.master.thesis.terraform.syntax.Child
import uks.master.thesis.terraform.syntax.DependsOn
import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.Expression
import uks.master.thesis.terraform.syntax.Identifier
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.utils.Utils.convertToIdentifier

class TfModule private constructor(
    private val block: Block,
    private val self: String,
    val type: Type
): Element, Child {
    private companion object {
        const val MODULE: String = "module"
        const val SOURCE: String = "source"
        const val VERSION: String = "version"
        const val PROVIDERS: String = "providers"
    }

    val reference get() = Reference<Raw>(self)

    @Suppress("UNCHECKED_CAST")
    abstract class Builder<T>: DependsOn() {
        private var subModuleNames: List<String> = mutableListOf()
        private var providerBuilder: TfMap.Builder? = null
        private var dependencies: List<Reference<Raw>> = mutableListOf()
        protected lateinit var _name: Identifier
        protected val blockBuilder: Block.Builder = Block.Builder()
        protected val sourceBuilder: Argument.Builder = Argument.Builder().name(SOURCE)

        open fun name(name: String): T = apply { preventDupName(); _name =
            Identifier(name)
        } as T
        open fun addDependency(resource: Resource): T = apply { dependencies = dependencies + Reference(resource.referenceString()) } as T
        open fun addDependency(inputVariable: InputVariable<Raw>): T = apply { dependencies = dependencies + inputVariable.reference } as T
        open fun addDependency(subModule: SubModule): T = apply { dependencies = dependencies + Reference(subModule.name) } as T
        open fun addInputVariable(inputVariable: InputVariable<out Expression>): T =
            apply { blockBuilder.addElement(Argument.Builder().name(inputVariable.name).value(inputVariable).build()) } as T
        open fun <S: Expression>addInputVariable(resource: Resource, attribute: String? = null): T =
            apply {
                blockBuilder.addElement(Argument.Builder()
                    .name(convertToIdentifier(resource.referenceString(attribute)))
                    .value<S>(resource, attribute).build())
            } as T
        open fun <S: Expression>addInputVariable(resource: Resource, reference: Reference<S>): T =
            apply {
                require(reference.toString().replace(Regex("[^.]+"), "").length != 1) {
                    "Wrong method when using $reference! To add a resource reference use the resource as parameter."
                }
                val attribute: String = reference.toString().removePrefix("${resource.referenceString()}.")
                addInputVariable<S>(resource, attribute)
            } as T
        open fun <S: Expression>addInputVariable(dataSource: DataSource, attribute: String? = null): T =
            apply {
                blockBuilder.addElement(Argument.Builder()
                    .name(convertToIdentifier(dataSource.referenceString(attribute)))
                    .value<S>(dataSource, attribute).build())
            } as T
        open fun <S: Expression>addInputVariable(dataSource: DataSource, reference: Reference<S>): T =
            apply {
                require(reference.toString().replace(Regex("[^.]+"), "").length != 2) {
                    "Wrong method when using $reference! To add a datasource reference use the datasource as parameter."
                }
                val attribute: String = reference.toString().removePrefix("${dataSource.referenceString()}.")
                addInputVariable<S>(dataSource, attribute)
            } as T
        open fun addInputVariable(subModule: SubModule, outputValue: OutputValue<out Expression>): T =
            apply {
                subModuleNames = subModuleNames + subModule.name
                blockBuilder.addElement(Argument.Builder()
                    .name(outputValue.name)
                    .value(subModule, outputValue).build())
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
                        Reference("${provider.name}.$it")
                    )
                } ?: throw IllegalArgumentException("alias from ${provider.name} was null!")
            } as T

        abstract fun build(): TfModule

        protected fun build(sourceBuilder: Argument.Builder, type: Type): TfModule {
            blockBuilder.type(MODULE).addLabel(_name.toString()).addElement(sourceBuilder.build())
            providerBuilder?.let {
                blockBuilder.addElement(Argument.Builder().name(PROVIDERS).value(it.build()).build())
            }
            val name = "$MODULE.$_name"
            check(!subModuleNames.contains(name)) {"An output variable from this module was defined as input variable!"}
            buildDependencies(blockBuilder, dependencies)
            return TfModule(blockBuilder.build(), name, type)
        }

        private fun preventDupName() {
            require(!::_name.isInitialized) {"name was already set to $_name!"}
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
