package uks.master.thesis

import kotlin.test.assertEquals
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

/**
 * Example Terraform class
 * Comment out @Disabled when using disabled methods
 */
class Terraform {
    private val binaryName = "terraform" +
        if(System.getProperty("os.name").contains("windows", true)) ".exe" else ""

    @Test
    @Disabled("Deletes everything!")
    fun deleteAllFiles() {
        Config.deleteOutDir()
    }

    @Test
    @Disabled("Executor function!")
    fun init() {
        Executor.init()
    }

    @Test
    @Disabled("Executor function!")
    fun validate() {
        Executor.validate()
    }

    @Test
    @Disabled("Executor function!")
    fun plan() {
        Executor.plan()
    }

    @Test
    @Disabled("Executor function!")
    fun apply() {
        Executor.apply()
    }

    @Test
    @Disabled("Executor function!")
    fun destroy() {
        Executor.destroy()
    }

    @Test
    @Disabled("Executor function!")
    fun help() {
        Executor.help()
    }

    @Test
    fun downloadBinary() {
        Executor.downloadTerraformBinary(
            "https://releases.hashicorp.com/terraform/1.3.3/terraform_1.3.3_windows_386.zip",
            true
        )
        assertEquals(binaryName, Executor.terraformCommand)
    }

    @Test
    @Disabled("Executor function!")
    fun copyBinaryFromPath() {
        Executor.copyBinaryFromPath()
        assertEquals(binaryName, Executor.terraformCommand)
    }

    @Test
    fun generateTfFiles() {
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(HCloud.requiredProviders("1.39.0"))
            .build()
        val hCloudSshKey: HCloudSshKey.Resource = HCloudSshKey.Resource.Builder()
            .resourceName("default")
            .name("hcloud_ssh_key")
            .publicKey(TfFile("~/.ssh/id_rsa.pub"))
            .build()
        val hCloudServer: HCloudServer.Resource = HCloudServer.Resource.Builder()
            .resourceName("test")
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
        HCloud
            .addTokenToTfVarsFromEnv()
            .addProviderToRootModule()
        RootModule
            .setConfiguration(terraform)
            .addChild(HCloud.token)
            .addChild(hCloudSshKey)
            .addChild(hCloudServer)
            .generateFiles(true)
    }
}
