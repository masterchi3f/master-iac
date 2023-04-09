package uks.master.thesis.terraform.lib.hcloud.v1_36_2

import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.expressions.Raw
import uks.master.thesis.terraform.syntax.expressions.TfBool
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfMap
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.syntax.expressions.Reference
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
            fun port(ref: Reference<TfString>) = apply { portBuilder.raw(ref.toString()) }
            fun ips(ips: TfList) = apply { ipsBuilder.value(ips) }
            fun ips(ref: Reference<TfList>) = apply { ipsBuilder.raw(ref.toString()) }
            fun description(description: String) = apply { blockBuilder.addElement(descriptionBuilder.value(description).build()) }
            fun description(ref: Reference<TfString>) = apply { blockBuilder.addElement(descriptionBuilder.raw(ref.toString()).build()) }
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
                require(!::_protocol.isInitialized) {"protocol was already set to $_protocol!"}
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
            fun labelSelector(ref: Reference<TfString>) = apply { blockBuilder.addElement(argumentBuilder.name(LABEL_SELECTOR).raw(ref.toString()).build()) }
            fun server(server: Int) = apply { blockBuilder.addElement(argumentBuilder.name(SERVER).value(server.toDouble()).build()) }
            fun server(ref: Reference<TfNumber>) = apply { blockBuilder.addElement(argumentBuilder.name(SERVER).raw(ref.toString()).build()) }
            fun build() = ApplyTo(blockBuilder.build())
        }

        override fun toString(): String = block.toString()
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val rules get() = Reference<TfList>(referenceString(Rule.reference()))
        val rulesDirections get() = Reference<TfList>(referenceString(Rule.direction()))
        val rulesProtocols get() = Reference<TfList>(referenceString(Rule.protocol()))
        val rulesPorts get() = Reference<TfList>(referenceString(Rule.port()))
        val rulesSourceIpLists get() = Reference<TfList>(referenceString(Rule.sourceIps()))
        val rulesDirectionIpLists get() = Reference<TfList>(referenceString(Rule.directionIps()))
        val rulesDescriptions get() = Reference<TfList>(referenceString(Rule.description()))
        val applyTo get() = Reference<Raw>(referenceString(ApplyTo.reference(0)))
        val applyToLabelSelector get() = Reference<TfString>(referenceString(ApplyTo.labelSelector(0)))
        val applyToServer get() = Reference<TfNumber>(referenceString(ApplyTo.server(0)))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private var hasApplyTo = false
            init { resourceType(HCLOUD_FIREWALL) }

            fun name(name: String) = apply { addElement(nameBuilder.value(name).build()) }
            fun name(ref: Reference<TfString>) = apply { addElement(nameBuilder.raw(ref.toString()).build()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: Reference<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun applyTo(applyTo: ApplyTo) = apply { preventDupApplyTo(); addElement(applyTo.block) }
            fun rule(rule: Rule) = apply { addElement(rule.block) }
            override fun build() = Resource(buildBlock(), buildSelf())

            private fun preventDupApplyTo() {
                require(!hasApplyTo) {"applyTo was already built"}
                hasApplyTo = true
            }
        }

        fun rule(index: Int) = Reference<Raw>(referenceString(Rule.reference(index)))
        fun ruleDirection(index: Int) = Reference<TfString>(referenceString(Rule.direction(index)))
        fun ruleProtocol(index: Int) = Reference<TfString>(referenceString(Rule.protocol(index)))
        fun rulePort(index: Int) = Reference<TfString>(referenceString(Rule.port(index)))
        fun ruleSourceIps(index: Int) = Reference<TfList>(referenceString(Rule.sourceIps(index)))
        fun ruleDirectionIps(index: Int) = Reference<TfList>(referenceString(Rule.directionIps(index)))
        fun ruleDescription(index: Int) = Reference<TfString>(referenceString(Rule.description(index)))
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = Reference<TfNumber>(referenceString(ID))
        val name get() = Reference<TfString>(referenceString(NAME))
        val labels get() = Reference<TfMap>(referenceString(LABELS))
        val rules get() = Reference<TfList>(referenceString(Rule.reference()))
        val ruleDirections get() = Reference<TfList>(referenceString(Rule.direction()))
        val ruleProtocols get() = Reference<TfList>(referenceString(Rule.protocol()))
        val rulePorts get() = Reference<TfList>(referenceString(Rule.port()))
        val ruleSourceIpLists get() = Reference<TfList>(referenceString(Rule.sourceIps()))
        val ruleDirectionIpLists get() = Reference<TfList>(referenceString(Rule.directionIps()))
        val ruleDescriptions get() = Reference<TfList>(referenceString(Rule.description()))
        val applyTo get() = Reference<Raw>(referenceString(ApplyTo.reference(0)))
        val applyToLabelSelector get() = Reference<TfString>(referenceString(ApplyTo.labelSelector(0)))
        val applyToServer get() = Reference<TfNumber>(referenceString(ApplyTo.server(0)))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val mostRecentBuilder: Argument.Builder = Argument.Builder().name(MOST_RECENT)
            init { dataSource(HCLOUD_FIREWALL) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: Reference<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: Reference<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: Reference<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun mostRecent(mostRecent: Boolean) = apply { addElement(mostRecentBuilder.value(mostRecent).build()) }
            fun mostRecent(ref: Reference<TfBool>) = apply { addElement(mostRecentBuilder.raw(ref.toString()).build()) }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }

        fun rule(index: Int) = Reference<Raw>(referenceString(Rule.reference(index)))
        fun ruleDirection(index: Int) = Reference<TfString>(referenceString(Rule.direction(index)))
        fun ruleProtocol(index: Int) = Reference<TfString>(referenceString(Rule.protocol(index)))
        fun rulePort(index: Int) = Reference<TfString>(referenceString(Rule.port(index)))
        fun ruleSourceIps(index: Int) = Reference<TfList>(referenceString(Rule.sourceIps(index)))
        fun ruleDirectionIps(index: Int) = Reference<TfList>(referenceString(Rule.directionIps(index)))
        fun ruleDescription(index: Int) = Reference<TfString>(referenceString(Rule.description(index)))
    }
}
