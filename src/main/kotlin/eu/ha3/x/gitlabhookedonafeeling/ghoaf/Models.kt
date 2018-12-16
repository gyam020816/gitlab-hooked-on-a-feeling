package eu.ha3.x.gitlabhookedonafeeling.ghoaf

/**
 * (Default template)
 * Created on 2018-12-15
 *
 * @author Ha3
 */
data class Project(
        val projectId: Int,
        val name: String,
        val sshUrl: String
)

data class Hook(
        val hookId: Int,
        val url: String
)

class Command {
    data class CreateHook(
            val projectId: Int,
            val url: String
    )
    data class DeleteHook(
            val projectId: Int,
            val hookId: Int
    )
}
