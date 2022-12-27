package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfRef
import uks.master.thesis.terraform.utils.Utils.index

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

            fun reference(index: Int? = null): String = "$RULE[${index(index)}]"
            fun direction(index: Int? = null): String = "$RULE[${index(index)}].$DIRECTION"
            fun protocol(index: Int? = null): String = "$RULE[${index(index)}].$PROTOCOL"
            fun port(index: Int? = null): String = "$RULE[${index(index)}].$PORT"
            fun sourceIps(index: Int? = null): String = "$RULE[${index(index)}].$SOURCE_IPS"
            fun directionIps(index: Int? = null): String = "$RULE[${index(index)}].$DESTINATION_IPS"
            fun description(index: Int? = null): String = "$RULE[${index(index)}].$DESCRIPTION"
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder()
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
            fun portRef(ref: TfRef) = apply { portBuilder.raw(ref.toString()) }
            fun ips(ips: TfList) = apply { ipsBuilder.value(ips) }
            fun ipsRef(ref: TfRef) = apply { ipsBuilder.raw(ref.toString()) }
            fun description(description: String) = apply { blockBuilder.addElement(descriptionBuilder.value(description).build()) }
            fun descriptionRef(ref: TfRef) = apply { blockBuilder.addElement(descriptionBuilder.raw(ref.toString()).build()) }
            fun build(): Rule {
                val builder: Block.Builder = blockBuilder
                    .type(RULE)
                    .addElement(directionBuilder.build())
                    .addElement(protocolBuilder.value(_protocol.toString()).build())
                    .addElement(ipsBuilder.build())
                if (_protocol == Protocol.TCP || _protocol == Protocol.UDP) {
                    builder.addElement(portBuilder.build())
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

            fun reference(index: Int? = null): String = "$APPLY_TO[${index(index)}]"
            fun labelSelector(index: Int? = null): String = "$APPLY_TO[${index(index)}].$LABEL_SELECTOR"
            fun server(index: Int? = null): String = "$APPLY_TO[${index(index)}].$SERVER"
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder()
            private val argumentBuilder: Argument.Builder = Argument.Builder()

            fun labelSelector(labelSelector: String) = apply { blockBuilder.addElement(argumentBuilder.name(LABEL_SELECTOR).value(labelSelector).build()) }
            fun labelSelectorRef(ref: TfRef) = apply { blockBuilder.addElement(argumentBuilder.name(LABEL_SELECTOR).raw(ref.toString()).build()) }
            fun server(server: Int) = apply { blockBuilder.addElement(argumentBuilder.name(SERVER).value(server.toString()).build()) }
            fun serverRef(ref: TfRef) = apply { blockBuilder.addElement(argumentBuilder.name(SERVER).raw(ref.toString()).build()) }
            fun build() = ApplyTo(blockBuilder.type(APPLY_TO).build())
        }

        override fun toString(): String = block.toString()
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef(reference(ID))
        val name get() = TfRef(reference(NAME))
        val labels get() = TfRef(reference(LABELS))
        val applyTo get() = TfRef(reference(ApplyTo.reference(0)))
        val applyToLabelSelector get() = TfRef(reference(ApplyTo.labelSelector(0)))
        val applyToServer get() = TfRef(reference(ApplyTo.server(0)))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private var hasApplyTo = false
            init { resourceType(HCLOUD_FIREWALL) }

            fun name(name: String) = apply { blockBuilder.addElement(nameBuilder.value(name).build()) }
            fun nameRef(ref: TfRef) = apply { blockBuilder.addElement(nameBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { blockBuilder.addElement(labelsBuilder.value(labels).build()) }
            fun labelsRef(ref: TfRef) = apply { blockBuilder.addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun applyTo(applyTo: ApplyTo) = apply { preventDupApplyTo(); blockBuilder.addElement(applyTo.block) }
            fun rule(rule: Rule) = apply { blockBuilder.addElement(rule.block) }
            override fun build() = Resource(buildBlock(), buildSelf())

            private fun preventDupApplyTo() {
                if (hasApplyTo) throw IllegalArgumentException("apply_to was already built") else hasApplyTo = true
            }
        }

        fun rule(index: Int? = null): TfRef = TfRef(reference(Rule.reference(index)))
        fun ruleDirection(index: Int? = null) = TfRef(reference(Rule.direction(index)))
        fun ruleProtocol(index: Int? = null) = TfRef(reference(Rule.protocol(index)))
        fun rulePort(index: Int? = null) = TfRef(reference(Rule.port(index)))
        fun ruleSourceIps(index: Int? = null) = TfRef(reference(Rule.sourceIps(index)))
        fun ruleDirectionIps(index: Int? = null) = TfRef(reference(Rule.directionIps(index)))
        fun ruleDescription(index: Int? = null) = TfRef(reference(Rule.description(index)))
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef(reference(ID))
        val name get() = TfRef(reference(NAME))
        val labels get() = TfRef(reference(LABELS))
        val applyTo get() = TfRef(reference(ApplyTo.reference(0)))
        val applyToLabelSelector get() = TfRef(reference(ApplyTo.labelSelector(0)))
        val applyToServer get() = TfRef(reference(ApplyTo.server(0)))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val mostRecentBuilder: Argument.Builder = Argument.Builder().name(MOST_RECENT)
            init { dataSource(HCLOUD_FIREWALL) }

            fun id(id: String) = apply { idOrNameBuilder.name(ID).value(id) }
            fun idRef(ref: TfRef) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun nameRef(ref: TfRef) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { blockBuilder.addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelectorRef(ref: TfRef) = apply { blockBuilder.addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun mostRecent(mostRecent: Boolean) = apply { blockBuilder.addElement(mostRecentBuilder.value(mostRecent).build()) }
            fun mostRecentRef(ref: TfRef) = apply { blockBuilder.addElement(mostRecentBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                blockBuilder.addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }

        fun rule(index: Int? = null): TfRef = TfRef(reference(Rule.reference(index)))
        fun ruleDirection(index: Int? = null) = TfRef(reference(Rule.direction(index)))
        fun ruleProtocol(index: Int? = null) = TfRef(reference(Rule.protocol(index)))
        fun rulePort(index: Int? = null) = TfRef(reference(Rule.port(index)))
        fun ruleSourceIps(index: Int? = null) = TfRef(reference(Rule.sourceIps(index)))
        fun ruleDirectionIps(index: Int? = null) = TfRef(reference(Rule.directionIps(index)))
        fun ruleDescription(index: Int? = null) = TfRef(reference(Rule.description(index)))
    }
}
