package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.syntax.expressions.TfString
import uks.master.thesis.terraform.utils.Utils.splatExp

object HCloudFirewall {
    private const val HCLOUD_FIREWALL: String = "hcloud_firewall"
    private const val ID: String = "id"
    private const val NAME: String = "name"
    private const val LABELS: String = "labels"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val MOST_RECENT: String = "most_recent"

    enum class Direction(private val direction: String) {
        IN("in"),
        OUT("out");
        override fun toString(): String = direction
    }

    enum class Protocol(private val protocol: String) {
        TCP("tcp"),
        ICMP("icmp"),
        UDP("udp"),
        GRE("gre"),
        ESP("esp");
        override fun toString(): String = protocol
    }

    class Rule private constructor(val block: Block) {
        companion object {
            private const val RULE: String = "rule"
            private const val DIRECTION: String = "direction"
            private const val PROTOCOL: String = "protocol"
            private const val PORT: String = "port"
            private const val SOURCE_IPS: String = "source_ips"
            private const val DESTINATION_IPS: String = "destination_ips"
            private const val DESCRIPTION: String = "description"

            fun reference(index: Int? = null): String = splatExp(RULE, index)
            fun direction(index: Int? = null): String = splatExp(RULE, index, DIRECTION)
            fun protocol(index: Int? = null): String = splatExp(RULE, index, PROTOCOL)
            fun port(index: Int? = null): String = splatExp(RULE, index, PORT)
            fun sourceIps(index: Int? = null): String = splatExp(RULE, index, SOURCE_IPS)
            fun directionIps(index: Int? = null): String = splatExp(RULE, index, DESTINATION_IPS)
            fun description(index: Int? = null): String = splatExp(RULE, index, DESCRIPTION)
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder().type(RULE)
            private val directionBuilder: Argument.Builder = Argument.Builder().name(DIRECTION)
            private val protocolBuilder: Argument.Builder = Argument.Builder().name(PROTOCOL)
            private val portBuilder: Argument.Builder = Argument.Builder().name(PORT)
            private val ipsBuilder: Argument.Builder = Argument.Builder()
            private val descriptionBuilder: Argument.Builder = Argument.Builder().name(DESCRIPTION)
            private lateinit var _protocol: Protocol

            fun direction(direction: Direction) = apply {
                directionBuilder.value(direction.toString())
                ipsBuilder.name(if (direction == Direction.IN) SOURCE_IPS else DESTINATION_IPS)
            }
            fun protocol(protocol: Protocol) = apply { preventDupProtocol(); _protocol = protocol }
            fun port(port: String) = apply { portBuilder.value(port) }
            fun port(ref: TfRef<TfString>) = apply { portBuilder.raw(ref.toString()) }
            fun ips(ips: TfList) = apply { ipsBuilder.value(ips) }
            fun ips(ref: TfRef<TfList>) = apply { ipsBuilder.raw(ref.toString()) }
            fun description(description: String) = apply { blockBuilder.addElement(descriptionBuilder.value(description).build()) }
            fun description(ref: TfRef<TfString>) = apply { blockBuilder.addElement(descriptionBuilder.raw(ref.toString()).build()) }
            fun build(): Rule {
                blockBuilder.addElement(directionBuilder.build())
                blockBuilder.addElement(protocolBuilder.value(_protocol.toString()).build())
                blockBuilder.addElement(ipsBuilder.build())
                if (_protocol == Protocol.TCP || _protocol == Protocol.UDP) {
                    blockBuilder.addElement(portBuilder.build())
                }
                return Rule(blockBuilder.build())
            }

            private fun preventDupProtocol() {
                if (::_protocol.isInitialized) throw IllegalArgumentException("protocol was already set to $_protocol!")
            }
        }

        override fun toString(): String = block.toString()
    }

    class ApplyTo private constructor(val block: Block) {
        companion object {
            private const val APPLY_TO: String = "apply_to"
            private const val LABEL_SELECTOR: String = "label_selector"
            private const val SERVER: String = "server"

            fun reference(index: Int? = null): String = splatExp(APPLY_TO, index)
            fun labelSelector(index: Int? = null): String = splatExp(APPLY_TO, index, LABEL_SELECTOR)
            fun server(index: Int? = null): String = splatExp(APPLY_TO, index, SERVER)
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder().type(APPLY_TO)
            private val argumentBuilder: Argument.Builder = Argument.Builder()

            fun labelSelector(labelSelector: String) = apply { blockBuilder.addElement(argumentBuilder.name(LABEL_SELECTOR).value(labelSelector).build()) }
            fun labelSelector(ref: TfRef<TfString>) = apply { blockBuilder.addElement(argumentBuilder.name(LABEL_SELECTOR).raw(ref.toString()).build()) }
            fun server(server: Int) = apply { blockBuilder.addElement(argumentBuilder.name(SERVER).value(server.toDouble()).build()) }
            fun server(ref: TfRef<TfNumber>) = apply { blockBuilder.addElement(argumentBuilder.name(SERVER).raw(ref.toString()).build()) }
            fun build() = ApplyTo(blockBuilder.build())
        }

        override fun toString(): String = block.toString()
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val rules get() = TfRef<TfList>(reference(Rule.reference()))
        val rulesDirections get() = TfRef<TfList>(reference(Rule.direction()))
        val rulesProtocols get() = TfRef<TfList>(reference(Rule.protocol()))
        val rulesPorts get() = TfRef<TfList>(reference(Rule.port()))
        val rulesSourceIpLists get() = TfRef<TfList>(reference(Rule.sourceIps()))
        val rulesDirectionIpLists get() = TfRef<TfList>(reference(Rule.directionIps()))
        val rulesDescriptions get() = TfRef<TfList>(reference(Rule.description()))
        val applyTo get() = TfRef<Raw>(reference(ApplyTo.reference(0)))
        val applyToLabelSelector get() = TfRef<TfString>(reference(ApplyTo.labelSelector(0)))
        val applyToServer get() = TfRef<TfNumber>(reference(ApplyTo.server(0)))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private var hasApplyTo = false
            init { resourceType(HCLOUD_FIREWALL) }

            fun name(name: String) = apply { addElement(nameBuilder.value(name).build()) }
            fun name(ref: TfRef<TfString>) = apply { addElement(nameBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun applyTo(applyTo: ApplyTo) = apply { preventDupApplyTo(); addElement(applyTo.block) }
            fun rule(rule: Rule) = apply { addElement(rule.block) }
            override fun build() = Resource(buildBlock(), buildSelf())

            private fun preventDupApplyTo() {
                if (hasApplyTo) throw IllegalArgumentException("applyTo was already built") else hasApplyTo = true
            }
        }

        fun rule(index: Int) = TfRef<Raw>(reference(Rule.reference(index)))
        fun ruleDirection(index: Int) = TfRef<TfString>(reference(Rule.direction(index)))
        fun ruleProtocol(index: Int) = TfRef<TfString>(reference(Rule.protocol(index)))
        fun rulePort(index: Int) = TfRef<TfString>(reference(Rule.port(index)))
        fun ruleSourceIps(index: Int) = TfRef<TfList>(reference(Rule.sourceIps(index)))
        fun ruleDirectionIps(index: Int) = TfRef<TfList>(reference(Rule.directionIps(index)))
        fun ruleDescription(index: Int) = TfRef<TfString>(reference(Rule.description(index)))
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val rules get() = TfRef<TfList>(reference(Rule.reference()))
        val ruleDirections get() = TfRef<TfList>(reference(Rule.direction()))
        val ruleProtocols get() = TfRef<TfList>(reference(Rule.protocol()))
        val rulePorts get() = TfRef<TfList>(reference(Rule.port()))
        val ruleSourceIpLists get() = TfRef<TfList>(reference(Rule.sourceIps()))
        val ruleDirectionIpLists get() = TfRef<TfList>(reference(Rule.directionIps()))
        val ruleDescriptions get() = TfRef<TfList>(reference(Rule.description()))
        val applyTo get() = TfRef<Raw>(reference(ApplyTo.reference(0)))
        val applyToLabelSelector get() = TfRef<TfString>(reference(ApplyTo.labelSelector(0)))
        val applyToServer get() = TfRef<TfNumber>(reference(ApplyTo.server(0)))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val mostRecentBuilder: Argument.Builder = Argument.Builder().name(MOST_RECENT)
            init { dataSource(HCLOUD_FIREWALL) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun mostRecent(mostRecent: Boolean) = apply { addElement(mostRecentBuilder.value(mostRecent).build()) }
            fun mostRecent(ref: TfRef<TfBool>) = apply { addElement(mostRecentBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }

        fun rule(index: Int) = TfRef<Raw>(reference(Rule.reference(index)))
        fun ruleDirection(index: Int) = TfRef<TfString>(reference(Rule.direction(index)))
        fun ruleProtocol(index: Int) = TfRef<TfString>(reference(Rule.protocol(index)))
        fun rulePort(index: Int) = TfRef<TfString>(reference(Rule.port(index)))
        fun ruleSourceIps(index: Int) = TfRef<TfList>(reference(Rule.sourceIps(index)))
        fun ruleDirectionIps(index: Int) = TfRef<TfList>(reference(Rule.directionIps(index)))
        fun ruleDescription(index: Int) = TfRef<TfString>(reference(Rule.description(index)))
    }
}
