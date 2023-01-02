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
import uks.master.thesis.terraform.utils.Utils

object HCloudServer {
    private const val HCLOUD_SERVER: String = "hcloud_server"
    private const val NAME: String = "name"
    private const val SERVER_TYPE: String = "server_type"
    private const val IMAGE: String = "image"
    private const val LOCATION: String = "location"
    private const val DATACENTER: String = "datacenter"
    private const val USER_DATA: String = "user_data"
    private const val SSH_KEYS: String = "ssh_keys"
    private const val PUBLIC_NET: String = "public_net"
    private const val IPV4_ENABLED: String = "ipv4_enabled"
    private const val IPV6_ENABLED: String = "ipv6_enabled"
    private const val KEEP_DISK: String = "keep_disk"
    private const val ISO: String = "iso"
    private const val RESCUE: String = "rescue"
    private const val LABELS: String = "labels"
    private const val BACKUPS: String = "backups"
    private const val FIREWALL_IDS: String = "firewall_ids"
    private const val IGNORE_REMOTE_FIREWALL_IDS: String = "ignore_remote_firewall_ids"
    private const val NETWORK: String = "network"
    private const val PLACEMENT_GROUP_ID: String = "placement_group_id"
    private const val DELETE_PROTECTION: String = "delete_protection"
    private const val REBUILD_PROTECTION: String = "rebuild_protection"
    private const val ALLOW_DEPRECATED_IMAGES: String = "allow_deprecated_images"
    private const val ID: String = "id"
    private const val BACKUP_WINDOW: String = "backup_window"
    private const val IPV4_ADDRESS: String = "ipv4_address"
    private const val IPV6_ADDRESS: String = "ipv6_address"
    private const val IPV6_NETWORK: String = "ipv6_network"
    private const val STATUS: String = "status"
    private const val WITH_SELECTOR: String = "with_selector"
    private const val WITH_STATUS: String = "with_status"

    enum class Location(private val location: String) {
        NBG1("nbg1"),
        FSN1("fsn1"),
        HEL1("hel1"),
        ASH("ash");
        override fun toString(): String = location
    }

    enum class Rescue(private val rescue: String) {
        LINUX64("linux64"),
        LINUX32("linux32"),
        FREEBSD64("freebsd64");
        override fun toString(): String = rescue
    }

    enum class Status(private val status: String) {
        INITIALIZING("initializing"),
        STARTING("starting"),
        RUNNING("running"),
        STOPPING("stopping"),
        OFF("off"),
        DELETING("deleting"),
        REBUILDING("rebuilding"),
        MIGRATING("migrating"),
        UNKNOWN("unknown");
        override fun toString(): String = status
    }

    class Network private constructor(val block: Block) {
        companion object {
            private const val NETWORK: String = "network"
            private const val NETWORK_ID: String = "network_id"
            private const val IP: String = "ip"
            private const val ALIAS_IPS: String = "alias_ips"
            private const val MAC_ADDRESS: String = "mac_address"

            fun reference(index: Int? = null): String = Utils.splatExp(NETWORK, index)
            fun networkId(index: Int? = null): String = Utils.splatExp(NETWORK, index, NETWORK_ID)
            fun ip(index: Int? = null): String = Utils.splatExp(NETWORK, index, IP)
            fun aliasIps(index: Int? = null): String = Utils.splatExp(NETWORK, index, ALIAS_IPS)
            fun macAddress(index: Int? = null): String = Utils.splatExp(NETWORK, index, MAC_ADDRESS)
        }

        class Builder {
            private val blockBuilder: Block.Builder = Block.Builder().type(NETWORK)
            private val networkIdBuilder: Argument.Builder = Argument.Builder().name(NETWORK_ID)
            private val ipBuilder: Argument.Builder = Argument.Builder().name(IP)
            private val aliasIpsBuilder: Argument.Builder = Argument.Builder().name(ALIAS_IPS)

            fun networkId(networkId: Int) = apply { networkIdBuilder.value(networkId.toDouble()) }
            fun networkId(ref: TfRef<TfNumber>) = apply { networkIdBuilder.raw(ref.toString()) }
            fun ip(ip: String) = apply { blockBuilder.addElement(ipBuilder.value(ip).build()) }
            fun ip(ref: TfRef<TfString>) = apply { blockBuilder.addElement(ipBuilder.raw(ref.toString()).build()) }
            fun aliasIps(aliasIps: TfList) = apply { blockBuilder.addElement(aliasIpsBuilder.value(aliasIps).build()) }
            fun aliasIps(ref: TfRef<TfList>) = apply { blockBuilder.addElement(aliasIpsBuilder.raw(ref.toString()).build()) }
            fun build() = Network(blockBuilder.addElement(networkIdBuilder.build()).build())
        }

        override fun toString(): String = block.toString()
    }

    class Resource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.Resource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val serverType get() = TfRef<TfString>(reference(SERVER_TYPE))
        val image get() = TfRef<TfString>(reference(IMAGE))
        val location get() = TfRef<TfString>(reference(LOCATION))
        val datacenter get() = TfRef<TfString>(reference(DATACENTER))
        val backupWindow get() = TfRef<TfString>(reference(BACKUP_WINDOW))
        val backups get() = TfRef<TfBool>(reference(BACKUPS))
        val iso get() = TfRef<TfString>(reference(ISO))
        val ipv4Address get() = TfRef<TfString>(reference(IPV4_ADDRESS))
        val ipv6Address get() = TfRef<TfString>(reference(IPV6_ADDRESS))
        val ipv6Network get() = TfRef<TfString>(reference(IPV6_NETWORK))
        val status get() = TfRef<TfString>(reference(STATUS))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val firewallIds get() = TfRef<TfList>(reference(FIREWALL_IDS))
        val placementGroupId get() = TfRef<TfNumber>(reference(PLACEMENT_GROUP_ID))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))
        val rebuildProtection get() = TfRef<TfBool>(reference(REBUILD_PROTECTION))
        val network get() = TfRef<TfMap>(reference(NETWORK))
        val networks get() = TfRef<TfList>(reference(Network.reference()))
        val networksNetworkIds get() = TfRef<TfList>(reference(Network.networkId()))
        val networksIps get() = TfRef<TfList>(reference(Network.ip()))
        val networksAliasIpsList get() = TfRef<TfList>(reference(Network.aliasIps()))
        val networksMacAddressList get() = TfRef<TfList>(reference(Network.macAddress()))

        class Builder: GBuilder<Builder>() {
            private val nameBuilder: Argument.Builder = Argument.Builder().name(NAME)
            private val serverTypeBuilder: Argument.Builder = Argument.Builder().name(SERVER_TYPE)
            private val imageBuilder: Argument.Builder = Argument.Builder().name(IMAGE)
            private val locationBuilder: Argument.Builder = Argument.Builder().name(LOCATION)
            private val datacenterBuilder: Argument.Builder = Argument.Builder().name(DATACENTER)
            private val userDataBuilder: Argument.Builder = Argument.Builder().name(USER_DATA)
            private val sshKeysBuilder: Argument.Builder = Argument.Builder().name(SSH_KEYS)
            private val keepDiskBuilder: Argument.Builder = Argument.Builder().name(KEEP_DISK)
            private val isoBuilder: Argument.Builder = Argument.Builder().name(ISO)
            private val rescueBuilder: Argument.Builder = Argument.Builder().name(RESCUE)
            private val labelsBuilder: Argument.Builder = Argument.Builder().name(LABELS)
            private val backupsBuilder: Argument.Builder = Argument.Builder().name(BACKUPS)
            private val firewallIdsBuilder: Argument.Builder = Argument.Builder().name(FIREWALL_IDS)
            private val ignoreRemoteFirewallIdsBuilder: Argument.Builder = Argument.Builder().name(IGNORE_REMOTE_FIREWALL_IDS)
            private val networkBuilder: Argument.Builder = Argument.Builder().name(NETWORK)
            private val placementGroupIdBuilder: Argument.Builder = Argument.Builder().name(PLACEMENT_GROUP_ID)
            private val deleteProtectionBuilder: Argument.Builder = Argument.Builder().name(DELETE_PROTECTION)
            private val rebuildProtectionBuilder: Argument.Builder = Argument.Builder().name(REBUILD_PROTECTION)
            private val allowDeprecatedImagesBuilder: Argument.Builder = Argument.Builder().name(ALLOW_DEPRECATED_IMAGES)
            private var hasPublicNet: Boolean = false
            init { resourceType(HCLOUD_SERVER) }

            fun name(name: String) = apply { nameBuilder.value(name) }
            fun name(ref: TfRef<TfString>) = apply { nameBuilder.raw(ref.toString()) }
            fun serverType(serverType: String) = apply { serverTypeBuilder.value(serverType) }
            fun serverType(ref: TfRef<TfString>) = apply { serverTypeBuilder.raw(ref.toString()) }
            fun image(image: String) = apply { imageBuilder.value(image) }
            fun image(ref: TfRef<TfString>) = apply { imageBuilder.raw(ref.toString()) }
            fun location(location: Location) = apply { addElement(locationBuilder.value(location.toString()).build()) }
            fun datacenter(datacenter: String) = apply { addElement(datacenterBuilder.value(datacenter).build()) }
            fun datacenter(ref: TfRef<TfString>) = apply { addElement(datacenterBuilder.raw(ref.toString()).build()) }
            fun userData(userData: String) = apply { addElement(userDataBuilder.value(userData).build()) }
            fun userData(ref: TfRef<TfString>) = apply { addElement(userDataBuilder.raw(ref.toString()).build()) }
            fun sshKeys(sshKeys: TfList) = apply { addElement(sshKeysBuilder.value(sshKeys).build()) }
            fun sshKeys(ref: TfRef<TfList>) = apply { addElement(sshKeysBuilder.raw(ref.toString()).build()) }
            fun publicNet(ipv4Enabled: Boolean, ipv6Enabled: Boolean) = apply {
                preventDupPublicNet()
                addElement(Block.Builder().type(PUBLIC_NET).addElement(
                    Argument.Builder().name(IPV4_ENABLED).value(ipv4Enabled).build()
                ).addElement(
                    Argument.Builder().name(IPV6_ENABLED).value(ipv6Enabled).build()
                ).build())
            }
            fun publicNet(ipv4Ref: TfRef<TfBool>, ipv6Ref: TfRef<TfBool>) = apply {
                preventDupPublicNet()
                addElement(Block.Builder().type(PUBLIC_NET).addElement(
                    Argument.Builder().name(IPV4_ENABLED).raw(ipv4Ref.toString()).build()
                ).addElement(
                    Argument.Builder().name(IPV6_ENABLED).raw(ipv6Ref.toString()).build()
                ).build())
            }
            fun keepDisk(keepDisk: Boolean) = apply { addElement(keepDiskBuilder.value(keepDisk).build()) }
            fun keepDisk(ref: TfRef<TfBool>) = apply { addElement(keepDiskBuilder.raw(ref.toString()).build()) }
            fun iso(iso: String) = apply { addElement(isoBuilder.value(iso).build()) }
            fun iso(ref: TfRef<TfString>) = apply { addElement(isoBuilder.raw(ref.toString()).build()) }
            fun rescue(rescue: Rescue) = apply { addElement(rescueBuilder.value(rescue.toString()).build()) }
            fun labels(labels: TfMap) = apply { addElement(labelsBuilder.value(labels).build()) }
            fun labels(ref: TfRef<TfMap>) = apply { addElement(labelsBuilder.raw(ref.toString()).build()) }
            fun backups(backups: Boolean) = apply { addElement(backupsBuilder.value(backups).build()) }
            fun backups(ref: TfRef<TfBool>) = apply { addElement(backupsBuilder.raw(ref.toString()).build()) }
            fun firewallIds(firewallIds: TfList) = apply { addElement(firewallIdsBuilder.value(firewallIds).build()) }
            fun firewallIds(ref: TfRef<TfList>) = apply { addElement(firewallIdsBuilder.raw(ref.toString()).build()) }
            fun ignoreRemoteFirewallIds(ignoreRemoteFirewallIds: Boolean) = apply { addElement(ignoreRemoteFirewallIdsBuilder.value(ignoreRemoteFirewallIds).build()) }
            fun ignoreRemoteFirewallIds(ref: TfRef<TfBool>) = apply { addElement(ignoreRemoteFirewallIdsBuilder.raw(ref.toString()).build()) }
            fun network(network: Network) = apply { addElement(network.block) }
            fun placementGroupId(placementGroupId: Int) = apply { addElement(placementGroupIdBuilder.value(placementGroupId.toDouble()).build()) }
            fun placementGroupId(ref: TfRef<TfNumber>) = apply { addElement(placementGroupIdBuilder.raw(ref.toString()).build()) }
            fun deleteProtection(deleteProtection: Boolean) = apply { addElement(deleteProtectionBuilder.value(deleteProtection).build()) }
            fun deleteProtection(ref: TfRef<TfBool>) = apply { addElement(deleteProtectionBuilder.raw(ref.toString()).build()) }
            fun rebuildProtection(rebuildProtection: Boolean) = apply { addElement(rebuildProtectionBuilder.value(rebuildProtection).build()) }
            fun rebuildProtection(ref: TfRef<TfBool>) = apply { addElement(rebuildProtectionBuilder.raw(ref.toString()).build()) }
            fun allowDeprecatedImages(allowDeprecatedImages: Boolean) = apply { addElement(allowDeprecatedImagesBuilder.value(allowDeprecatedImages).build()) }
            fun allowDeprecatedImages(ref: TfRef<TfBool>) = apply { addElement(allowDeprecatedImagesBuilder.raw(ref.toString()).build()) }
            override fun build(): Resource {
                addElement(nameBuilder.build())
                addElement(serverTypeBuilder.build())
                addElement(imageBuilder.build())
                return Resource(buildBlock(), buildSelf())
            }

            private fun preventDupPublicNet() {
                if (hasPublicNet) throw IllegalArgumentException("publicNet was already set") else hasPublicNet = true
            }
        }

        fun network(index: Int) = TfRef<Raw>(reference(Network.reference(index)))
        fun networkNetworkId(index: Int) = TfRef<TfNumber>(reference(Network.networkId(index)))
        fun networkIp(index: Int) = TfRef<TfString>(reference(Network.ip(index)))
        fun networkAliasIps(index: Int) = TfRef<TfList>(reference(Network.aliasIps(index)))
        fun networkMacAddress(index: Int) = TfRef<TfString>(reference(Network.macAddress(index)))
    }

    class DataSource private constructor(block: Block, self: String):
        uks.master.thesis.terraform.syntax.elements.blocks.DataSource(block, self) {
        val id get() = TfRef<TfNumber>(reference(ID))
        val name get() = TfRef<TfString>(reference(NAME))
        val serverType get() = TfRef<TfString>(reference(SERVER_TYPE))
        val image get() = TfRef<TfString>(reference(IMAGE))
        val location get() = TfRef<TfString>(reference(LOCATION))
        val datacenter get() = TfRef<TfString>(reference(DATACENTER))
        val backupWindow get() = TfRef<TfString>(reference(BACKUP_WINDOW))
        val backups get() = TfRef<TfBool>(reference(BACKUPS))
        val iso get() = TfRef<TfString>(reference(ISO))
        val ipv4Address get() = TfRef<TfString>(reference(IPV4_ADDRESS))
        val ipv6Address get() = TfRef<TfString>(reference(IPV6_ADDRESS))
        val ipv6Network get() = TfRef<TfString>(reference(IPV6_NETWORK))
        val status get() = TfRef<TfString>(reference(STATUS))
        val labels get() = TfRef<TfMap>(reference(LABELS))
        val firewallIds get() = TfRef<TfList>(reference(FIREWALL_IDS))
        val placementGroupId get() = TfRef<TfNumber>(reference(PLACEMENT_GROUP_ID))
        val deleteProtection get() = TfRef<TfBool>(reference(DELETE_PROTECTION))
        val rebuildProtection get() = TfRef<TfBool>(reference(REBUILD_PROTECTION))

        class Builder: GBuilder<Builder>() {
            private val idOrNameBuilder: Argument.Builder = Argument.Builder()
            private val withSelectorBuilder: Argument.Builder = Argument.Builder().name(WITH_SELECTOR)
            private val withStatusBuilder: Argument.Builder = Argument.Builder().name(WITH_STATUS)
            init { dataSource(HCLOUD_SERVER) }

            fun id(id: Int) = apply { idOrNameBuilder.name(ID).value(id.toDouble()) }
            fun id(ref: TfRef<TfNumber>) = apply { idOrNameBuilder.name(ID).raw(ref.toString()) }
            fun name(name: String) = apply { idOrNameBuilder.name(NAME).value(name) }
            fun name(ref: TfRef<TfString>) = apply { idOrNameBuilder.name(NAME).raw(ref.toString()) }
            fun withSelector(selector: String) = apply { addElement(withSelectorBuilder.value(selector).build()) }
            fun withSelector(ref: TfRef<TfString>) = apply { addElement(withSelectorBuilder.raw(ref.toString()).build()) }
            fun withStatus(withStatus: List<Status>) = apply {
                val set: Set<Status> = withStatus.toSet()
                val tfListBuilder: TfList.Builder = TfList.Builder()
                set.forEach { tfListBuilder.add(it.toString()) }
                addElement(withStatusBuilder.value(tfListBuilder.build()).build())
            }
            override fun build(): DataSource {
                addElement(idOrNameBuilder.build())
                return DataSource(buildBlock(), buildSelf())
            }
        }
    }
}
