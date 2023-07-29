package uks.master.thesis.interview

import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.SubModule
import uks.master.thesis.terraform.TfVars
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloud
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudNetwork
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudNetworkSubnet
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudServer
import uks.master.thesis.terraform.syntax.Import
import uks.master.thesis.terraform.syntax.elements.blocks.Provider
import uks.master.thesis.terraform.syntax.elements.blocks.RequiredProviders
import uks.master.thesis.terraform.syntax.elements.blocks.Terraform
import uks.master.thesis.terraform.syntax.elements.blocks.TfModule

/**
 * Expert interview more examples
 */
class DMoreFeatures {
    private val logger: KLogger = KotlinLogging.logger {}

    @Test
    fun root() {
        /**
         * Example terraform block
         */
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(HCloud.requiredProviders("1.39.0"))
            .build()

        /**
         * Example resource
         */
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .build()

        /**
         * Example provider
         */
        val someProvider: Provider = Provider.Builder()
            // .addConfig() -> add arguments (varies in every provider)
            .name("someProvider")
            .build()

        // Adds Hetzner Cloud provider to root module
        HCloud.addProviderToRootModule()

        RootModule
            .setConfiguration(terraform)
            // Add children: data sources, input variables, output values, resources, modules
            .addChild(hCloudServer)
            .addChild(HCloud.token)
            // Add providers
            .addProvider(someProvider)
            .generateFiles(true)
    }

    @Test
    fun tfVars() {
        /**
         * Add secrets to secrets.tfvars file
         */
        TfVars.add("key", "value")
        TfVars.addEnv("envKey", "newKey")
        TfVars.addEnv("envKey")

        // Searches for "hcloud_token" in system environment variables and adds to secrets.tfvars
        HCloud.addTokenToTfVarsFromEnv()

        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .build()

        RootModule
            .setConfiguration(terraform)
            // Terraform input variable for "hcloud_token"
            .addChild(HCloud.token)
            .generateFiles(true)
    }

    @Test
    fun subModule() {
        /**
         * Example terraform block
         */
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(HCloud.requiredProviders("1.39.0"))
            .build()

        /**
         * Example resource
         */
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .build()

        /**
         * Adding Hetzer Cloud provider and token
         */
        HCloud
            .addProviderToRootModule()
            .addTokenToTfVarsFromEnv()

        /**
         * Module 1
         */
        val module1: TfModule = TfModule.Local.Builder()
            .addInputVariable(hCloudServer, hCloudServer.image) // ALSO POSSIBLE
            .name("module1")
            .build()
        val subModule1 = SubModule(module1)

        subModule1
            .setConfiguration(terraform)
            .addChild(hCloudServer)

        /**
         * Module 2
         */
        val module2: TfModule = TfModule.Local.Builder()
            .name("module2")
            .build()
        val subModule2: SubModule = SubModule(module2)
            .setConfiguration(terraform)

        /**
         * Example root module
         */
        RootModule
            .setConfiguration(terraform)
            .addChild(hCloudServer)
            .addChild(HCloud.token)
            .addChild(subModule1) // ADDED HERE
            .addChild(subModule2) // ADDED HERE
            .generateFiles(true)
    }

    @Test
    fun dependsOn() {
        val hcloudNetwork: HCloudNetwork.Resource = HCloudNetwork.Resource.Builder()
            .resourceName("sol")
            .name("sol")
            .ipRange("10.0.0.0/16")
            .build()
        val hcloudNetworkSubnet: HCloudNetworkSubnet.Resource = HCloudNetworkSubnet.Resource.Builder()
            .resourceName("sol-system")
            .networkId(hcloudNetwork.id)
            .type(HCloudNetworkSubnet.Type.CLOUD)
            .networkZone("eu-central")
            .ipRange("10.0.0.0/24")
            .build()

        /**
         * Initialize the 2 resources before first
         */
        val hcloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .addDependency(hcloudNetworkSubnet) // THIS IS THE REASON
            .build()

        logger.info("\n" + hcloudNetwork)
        logger.info("\n" + hcloudNetworkSubnet)
        logger.info("\n" + hcloudServer)
    }

    @Test
    fun import() {
        val hcloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .build()

        val import: Import = hcloudServer.import("someIdIGotFromSomewhereFromHetzner", null)
        logger.info(import.address)
        logger.info(import.id)

        // From documentation (https://registry.terraform.io/providers/hetznercloud/hcloud/latest/docs/resources/server):
        // Servers can be imported using the server id:
        // terraform import hcloud_server.myserver id
        // In above axample: terraform import <import.address> <import.id>

        // To execute with Kotlin through Terrraform CLI:
        // Executor.import(import)
    }

    @Test
    fun providerArgument() {
        val eastProvider: Provider = Provider.Builder()
            // .addConfig() -> add arguments (varies in every provider)
            .alias("east")
            .name("aws")
            .build()

        val westProvider: Provider = Provider.Builder()
            // .addConfig() -> add arguments (varies in every provider)
            .alias("west")
            .name("aws")
            .build()

        val req: RequiredProviders = RequiredProviders.Builder()
            .addProvider(
                eastProvider,
                "hashicorp/aws",
                ">= 2.7.0",
                mapOf(
                    Pair(eastProvider, true),
                    Pair(westProvider, true)
                )
            )
            .build()

        val hcloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .provider(westProvider)
            .build()

        logger.info("\n" + req)
        logger.info("\n" + eastProvider)
        logger.info("\n" + westProvider)
        logger.info("\n" + hcloudServer)
    }

    @Disabled("To allow deployment")
    @Test
    fun errorDetection() {
        val hcloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .name("test2") // Double assigned value
            .build()
    }

    @Disabled("To allow deployment")
    @Test
    fun errorDetection2() {
        val hcloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            // Missing values like "name"
            .build()
    }
}
