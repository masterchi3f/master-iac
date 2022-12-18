package uks.master.thesis.terraform

object TfVars {
    const val FILE_NAME = "secrets.tfvars"
    var tfVars: Map<String, String> = mutableMapOf()
        private set

    fun add(key: String, value: String) = apply {
        tfVars = tfVars + Pair(key, value)
    }

    fun addEnv(key: String) = apply {
        tfVars = tfVars + Pair(key, System.getenv(key))
    }

    fun addEnv(envKey: String, tfVarsKey: String) = apply {
        tfVars = tfVars + Pair(tfVarsKey, System.getenv(envKey))
    }
}
