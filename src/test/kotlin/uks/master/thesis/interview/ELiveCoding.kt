package uks.master.thesis.interview

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import uks.master.thesis.terraform.Executor
import uks.master.thesis.terraform.RootModule
import uks.master.thesis.terraform.lib.hcloud.v1_36_2.HCloud

// Also show:
// 1. Open Hetzner Cloud-Tf-provider-directory und compare to
// https://registry.terraform.io/providers/hetznercloud/hcloud/latest/docs
// 2. Show toString generation
// 3. count und foreach not implemented
// https://developer.hashicorp.com/terraform/language/meta-arguments/count
// https://developer.hashicorp.com/terraform/language/meta-arguments/for_each
// 4. Show .gitignore + out-directory-structure by convention
// https://developer.hashicorp.com/terraform/language/modules/develop/structure
// 5. Show class diagram
// 6. Executor-class -> Show CLI-Commands
// 7. Show GitHub repositories (Open-Source) + JitPack deployment
// https://github.com/masterchi3f/master-iac
// https://github.com/masterchi3f/master-iac-example
// https://jitpack.io/#masterchi3f/master-iac
// 8. Open Hetzner Cloud website and do some live demonstration

class ELiveCoding {

    @Disabled("To allow deployment")
    @Test
    fun destroy() {
        Executor.destroy()
    }

    @Disabled("To allow deployment")
    @Test
    fun liveCoding() {
        /**
         * Download binary
         */
        Executor.downloadTerraformBinary(
            "https://releases.hashicorp.com/terraform/1.3.3/terraform_1.3.3_windows_386.zip",
            true
        )

        // val terraform: Terraform = ... TODO
        // TODO implement resources

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
         * Add children
         * Generate files in out-folder
         */
        RootModule
            // .setConfiguration(terraform) TODO
            .addChild(HCloud.token)
            // .addChild(...) TODO
            .generateFiles(true)
        /**
         * Execute CLI-commands and deploy to Hetzner Cloud
         */
        Executor.init()
        Executor.plan()
        // Executor.apply() // TODO comment in to deploy?
    }
}
