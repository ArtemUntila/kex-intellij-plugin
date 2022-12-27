package org.vorpal.research.kex.plugin.util

class BindList : PrefixList() {

    override val prefix = "-v"

    fun addBind(hostPath: String, containerPath: String) {
        val bind = "$hostPath:$containerPath"
        addPrefix()
        add(bind)
    }
}