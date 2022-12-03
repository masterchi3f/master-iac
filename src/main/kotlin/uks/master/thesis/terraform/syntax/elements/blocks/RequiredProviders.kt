package uks.master.thesis.terraform.syntax.elements.blocks

import uks.master.thesis.terraform.syntax.Element
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfRef

class RequiredProviders private constructor(
    val block: Block
): Element {
    private companion object {
        const val REQUIRED_PROVIDERS: String = "required_providers"
        const val SOURCE: String = "source"
        const val VERSION: String = "version"
        const val CONFIGURATION_ALIASES: String = "configuration_aliases"
    }

    class Builder {
        private val blockBuilder: Block.Builder = Block.Builder()
        private val mapBuilder: TfMap.Builder = TfMap.Builder()
        private val listBuilder: TfList.Builder = TfList.Builder()

        /**
         * @param aliases: A map of providers with aliases and boolean values.
         * If the value is true the alias is inherited from the upper module. The dot (.) is switched with a dash (-).
         * See uks.master.thesis.terraform.syntax.elements.blocks.TfModule for more.
         * An example for an alternate config looks like this:
         * aws = {
         *   source  = "hashicorp/aws"
         *   version = ">= 2.7.0"
         *   configuration_aliases = [ aws-src, aws-dst ]
         * }
         */
        fun addProvider(provider: Provider, source: String, version: String, aliases: Map<Provider, Boolean>? = null) = apply {
            mapBuilder.put(SOURCE, source).put(VERSION, version)
            aliases?.let { al: Map<Provider, Boolean> ->
                al.forEach { prov: Map.Entry<Provider, Boolean> ->
                    // Not sure if alternate with - works because it is done with . in the docs:
                    // https://developer.hashicorp.com/terraform/language/modules/develop/providers
                    prov.key.alias?.let { listBuilder.add(TfRef("${prov.key.name()}${if (prov.value) "-" else "."}$it")) }
                        ?: throw IllegalArgumentException("alias from ${prov.key.name()} was null!")
                }
                mapBuilder.put(CONFIGURATION_ALIASES, listBuilder.build())
            }
            blockBuilder.addElement(
                Argument.Builder().name(provider.name()).value(mapBuilder.build()).build()
            )
        }
        fun build() = RequiredProviders(blockBuilder.type(REQUIRED_PROVIDERS).build())
    }

    override fun toString(): String {
        return block.toString()
    }
}
