package uks.master.thesis

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.TfVars
import uks.master.thesis.terraform.syntax.elements.Argument
import uks.master.thesis.terraform.syntax.elements.Block
import uks.master.thesis.terraform.syntax.elements.blocks.InputVariable
import uks.master.thesis.terraform.syntax.elements.blocks.Provider
import uks.master.thesis.terraform.syntax.elements.blocks.RequiredProviders
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
        val hCloudToken: InputVariable = InputVariable.Builder()
            .name("hcloud_token")
            .sensitive()
            .build()
        val hCloud: Provider = Provider.Builder()
            .name("hcloud")
            .addConfig(
                Argument.Builder()
                    .name("token")
                    .value(hCloudToken)
                    .build()
            ).build()
        val requiredProviders: RequiredProviders = RequiredProviders.Builder()
            .addProvider(hCloud, "hetznercloud/${hCloud.name}", "1.35.1")
            .build()
        val terraform: Terraform = Terraform.Builder()
            .requiredVersion(">= 1.2.4")
            .requiredProviders(requiredProviders)
            .build()
        val hCloudSshKey: Resource = Resource.Builder()
            .type("hcloud_ssh_key")
            .name("default")
            .addElement(
                Argument.Builder()
                    .name("name")
                    .value("hcloud_ssh_key")
                    .build()
            ).addElement(
                Argument.Builder()
                    .name("public_key")
                    .raw("file(\"~/.ssh/id_rsa.pub\")")
                    .build()
            ).build()
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
                            .add(TfRef(hCloudSshKey.reference("id")))
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
        RootModule
            .setConfiguration(terraform)
            .addChild(hCloudToken)
            .addProvider(hCloud)
            .addChild(hCloudSshKey)
            .addChild(hCloudServer)
        TfVars
            .addEnv("hcloud_token")
        RootModule.generateFiles(true)
    }
}
