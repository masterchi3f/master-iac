package uks.master.thesis.interview

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloud
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudServer
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudSshKey
import uks.master.thesis.terraform.syntax.elements.blocks.Terraform
import uks.master.thesis.terraform.syntax.expressions.TfFile
import uks.master.thesis.terraform.syntax.expressions.TfList

/**
 * Expert interview starting example
 */
class AMiniExample {
    private val binaryName = "terraform" +
        if(System.getProperty("os.name").contains("windows", true)) ".exe" else ""

    @Test
    fun destroy() {
        Executor.destroy()
    }

    @Test
    fun deploy() {

        /**
         * Download binary
         */
        Executor.downloadTerraformBinary(
            "https://releases.hashicorp.com/terraform/1.3.3/terraform_1.3.3_windows_386.zip",
            true
        )
        assertEquals(binaryName, Executor.terraformCommand)

        /**
         * Define terraform-block
         */
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(HCloud.requiredProviders("1.39.0"))
            .build()

        /**
         * Define SSH-key resource-block
         * with local public-SSH-key at "~/.ssh/id_rsa.pub"
         * to be used to access server later on
         */
        val hCloudSshKey: HCloudSshKey.Resource = HCloudSshKey.Resource.Builder()
            .resourceName("default")
            .name("hcloud_ssh_key")
            .publicKey(TfFile("~/.ssh/id_rsa.pub"))
            .build()

        /**
         * Define server resource-block
         */
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("resNameTest")
            .name("test")
            .image("debian-11")
            .serverType("cx11")
            .location(HCloudServer.Location.NBG1)
            .publicNet(ipv4Enabled = true, ipv6Enabled = true)
            .sshKeys(
                TfList.Builder()
                    .add(hCloudSshKey.id)
                    .build()
            ).build()

        /**
         * Add Hetzner Cloud token to secrets.tfvars
         * Add Hetzner Cloud provider-block to main.tf
         */
        HCloud
            .addTokenToTfVarsFromEnv()
            .addProviderToRootModule()

        /**
         * In root module:
         * Add terraform-block
         * Add Hetzner Cloud token as variable-block
         * Add SSH-key as resource-block
         * Add server as resource-block
         * Generate files in out-folder
         */
        RootModule
            .setConfiguration(terraform)
            .addChild(HCloud.token)
            .addChild(hCloudSshKey)
            .addChild(hCloudServer)
            .generateFiles(true)

        /**
         * Execute CLI-commands and deploy to Hetzner Cloud
         */
        Executor.init()
        Executor.plan()
        Executor.apply()
    }
}
