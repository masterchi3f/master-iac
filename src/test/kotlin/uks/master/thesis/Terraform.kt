package uks.master.thesis

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloud
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloudSshKey
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.elements.blocks.Resource
import uks.master.thesis.terraform.syntax.elements.blocks.Terraform
import uks.master.thesis.terraform.syntax.expressions.TfList
import uks.master.thesis.terraform.syntax.expressions.TfRef

class Terraform {
    private val binaryName = "terraform" +
        if(System.getProperty("os.name").contains("windows", true)) ".exe" else ""

    @Test
    fun deleteAllFiles() {
        Config.deleteOutDir()
    }

    @Test
    fun init() {
        Executor.init()
    }

    @Test
    fun validate() {
        Executor.validate()
    }

    @Test
    fun plan() {
        Executor.plan()
    }

    @Test
    fun apply() {
        Executor.apply()
    }

    @Test
    fun destroy() {
        Executor.destroy()
    }

    @Test
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
    fun copyBinaryFromPath() {
        Executor.copyBinaryFromPath()
        assertEquals(binaryName, Executor.terraformCommand)
    }

    @Test
    fun generateTfFiles() {
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(HCloud.requiredProviders)
            .build()
        val hCloudSshKey: HCloudSshKey._Resource = HCloudSshKey._Resource.Builder()
            .resName("default")
            .name("hcloud_ssh_key")
            .publicKeyRef("file(\"~/.ssh/id_rsa.pub\")")
            .build()
        val hCloudServer: Resource = Resource.Builder()
            .type("hcloud_server")
            .name("test")
            .addElement(
                Argument.Builder()
                    .name("name")
                    .value("test")
                    .build()
            ).addElement(
                Argument.Builder()
                    .name("image")
                    .value("debian-11")
                    .build()
            ).addElement(
                Argument.Builder()
                    .name("server_type")
                    .value("cx11")
                    .build()
            ).addElement(
                Argument.Builder()
                    .name("location")
                    .value("nbg1")
                    .build()
            ).addElement(
                Argument.Builder()
                    .name("ssh_keys")
                    .value(
                        TfList.Builder()
                            .add(TfRef(hCloudSshKey.id))
                            .build()
                    ).build()
            ).addElement(
                Block.Builder()
                    .type("public_net")
                    .addElement(
                        Argument.Builder()
                            .name("ipv4_enabled")
                            .value(true)
                            .build()
                    ).addElement(
                        Argument.Builder()
                            .name("ipv6_enabled")
                            .value(true)
                            .build()
                    ).build()
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
