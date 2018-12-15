package eu.ha3.x.gitlabhookedonafeeling.ghoaf

/**
 * (Default template)
 * Created on 2018-12-15
 *
 * @author Ha3
 */
data class Project(
        val id: Int,
        val name: String,
        val sshUrl: String
)

data class Hook(
        val id: Int,
        val url: String
)
