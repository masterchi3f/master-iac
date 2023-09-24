package uks.master.thesis.colloquium

import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.SubModule
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloud
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudNetwork
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudNetworkSubnet
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudServer
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudServerNetwork
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudSshKey
import uks.master.thesis.terraform.syntax.elements.blocks.InputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.Terraform
import uks.master.thesis.terraform.syntax.elements.blocks.TfModule
import uks.master.thesis.terraform.syntax.expressions.Reference
import uks.master.thesis.terraform.syntax.expressions.TfFile
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfNumber
import uks.master.thesis.terraform.utils.Utils

class LongExample {
    // SSH Key used for ALL servers
    private val hcloudSshKey: HCloudSshKey.Resource = HCloudSshKey.Resource.Builder()
        .resourceName("ssh_key_server_long_example")
        .name("Test SSH Pub Key Long Example")
        .publicKey(TfFile("~/.ssh/id_rsa.pub"))
        .build()
    // Network for ALL servers
    private val hcloudNetwork: HCloudNetwork.Resource = HCloudNetwork.Resource.Builder()
        .resourceName("network_long_example")
        .name("Test-Network-Long-Example")
        .ipRange("172.16.0.0/12")
        .build()
    // Subnet of network
    private val hcloudNetworkSubnet: HCloudNetworkSubnet.Resource = HCloudNetworkSubnet.Resource.Builder()
        .resourceName("network_subnet_long_example")
        .networkId(hcloudNetwork.id)
        .type(HCloudNetworkSubnet.Type.CLOUD)
        .networkZone("eu-central")
        .ipRange("172.16.0.0/24")
        .build()

    // Some Input variables for above SSH key and subnet USED in submodule
    private val hcloudSshKeyId: InputVariable<TfNumber> = InputVariable.Builder()
        .name(Utils.convertToIdentifier(hcloudSshKey.id.toString()))
        .build(TfNumber::class.java)
    private val hcloudNetworkSubnetId: InputVariable<TfNumber> = InputVariable.Builder()
        .name(Utils.convertToIdentifier(hcloudNetworkSubnet.id.toString()))
        .build(TfNumber::class.java)

    @Test
    fun generateTfFiles() {
        // Terraform configuration
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(HCloud.requiredProviders("1.39.0"))
            .build()

        // Add one submodule BLOCK
        val tfModule: TfModule = TfModule.Local.Builder()
            .name("sub_module_long_example")
            // Two global input variables
            .addInputVariable(hcloudSshKey, hcloudSshKey.id)
            .addInputVariable(hcloudNetworkSubnet, hcloudNetworkSubnet.id)
            .build()

        // Add submodule which is CHILD element to root-module
        val subModule = SubModule(tfModule)
            .setConfiguration(terraform)
            // Two global input variables
            .addChild(hcloudSshKeyId)
            .addChild(hcloudNetworkSubnetId)

        // Server and server networks
        for (i in 1..5) {
            val server: HCloudServer.Resource = buildServer(i)
            subModule
                .addChild(server)
                .addChild(buildServerNetwork(i, server.id))
        }

        // Add Hetzner Cloud API token to TfVars and provider block to RootModule
        HCloud
            .addTokenToTfVarsFromEnv()
            .addProviderToRootModule()

        // Add configuration, children and start generation
        RootModule
            .setConfiguration(terraform)
            .addChild(HCloud.token)
            // Three global resources
            .addChild(hcloudSshKey)
            .addChild(hcloudNetwork)
            .addChild(hcloudNetworkSubnet)
            // Submodule
            .addChild(subModule)
            .generateFiles(true)
    }

    private fun buildServer(index: Int): HCloudServer.Resource =
        HCloudServer.Resource.Builder()
            .resourceName("server_long_example_$index")
            .name("Test-Server-Long-Example-$index")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .sshKeys(
                TfList.Builder().add(
                    hcloudSshKeyId.reference
                ).build()
            ).publicNet(ipv4Enabled = true, ipv6Enabled = true)
            .build()

    private fun buildServerNetwork(
        index: Int,
        serverId: Reference<TfNumber>
    ): HCloudServerNetwork.Resource =
        HCloudServerNetwork.Resource.Builder()
            .resourceName("server_network_long_example_$index")
            .ip("172.16.0.${index + 10}")
            .serverId(serverId)
            .subnetId(hcloudNetworkSubnetId.reference)
            .build()

    @Test
    fun start() {
        Executor.downloadTerraformBinary(
            "https://releases.hashicorp.com/terraform/1.3.3/terraform_1.3.3_windows_386.zip",
            true
        )
        Executor.init()
        Executor.plan()
        Executor.apply()
    }

    @Test
    fun apply() {
        generateTfFiles()
        Executor.apply()
    }

    @Test
    fun plan() {
        generateTfFiles()
        Executor.plan()
    }

    @Test
    fun destroy() {
        Executor.destroy()
    }
}
