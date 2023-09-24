package uks.master.thesis.colloquium

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloud
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudServer
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudSshKey
import uks.master.thesis.terraform.syntax.elements.blocks.Terraform
import uks.master.thesis.terraform.syntax.expressions.TfFile
import uks.master.thesis.terraform.syntax.expressions.TfList

class ShortExample {

    @Test
    fun generateTfFiles() {
        // Terraform configuration
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(HCloud.requiredProviders("1.39.0"))
            .build()

        // SSH Key for Server
        val hCloudSshKey: HCloudSshKey.Resource = HCloudSshKey.Resource.Builder()
            .resourceName("ssh_key_server_short_example")
            .name("Test SSH Pub Key Short Example")
            .publicKey(TfFile("~/.ssh/id_rsa.pub"))
            .build()

        // Server
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("server_short_example")
            .name("Test-Server-Short-Example")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .publicNet(ipv4Enabled = true, ipv6Enabled = true)
            .sshKeys(
                TfList.Builder()
                    .add(hCloudSshKey.id)
                    .build()
            ).build()

        // Add Hetzner Cloud API token to TfVars and provider block to RootModule
        HCloud

            // Same as TfVars.addEnv("hcloud_token", "hcloud_token") => see token_env.png
            .addTokenToTfVarsFromEnv()

            /* Same as:
             * RootModule.addProvider(
             *    Provider.Builder().name("hcloud").addConfig(
             *        Argument.Builder().name("token").value(
             *            InputVariable.Builder().name("hcloud_token").sensitive().build(TfString::class.java)
             *        ).build()
             *    ).build()
             * )
             */
            .addProviderToRootModule()

        // Add configuration, children and start generation
        RootModule
            .setConfiguration(terraform)
            .addChild(HCloud.token)
            .addChild(hCloudSshKey)
            .addChild(hCloudServer)
            .generateFiles(true)
    }

    @Test
    @Disabled("Executor function!")
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
    @Disabled("Executor function!")
    fun destroy() {
        Executor.destroy()
    }
}
