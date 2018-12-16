package eu.ha3.x.gitlabhookedonafeeling

import eu.ha3.x.gitlabhookedonafeeling.ghoaf.Command
import eu.ha3.x.gitlabhookedonafeeling.ghoaf.IFeelingApi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class GHOAF(private val api: IFeelingApi, private val jenkinsUrl: String) {
    init {
        if (!jenkinsUrl.endsWith("/")) {
            throw IllegalArgumentException("jenkinsUrl must end with /")
        }
    }

    fun execute() {
        for (project in api.getAllProjects()) {
            val hookAlreadyExists = api.getHooks(project.projectId)
                    .any { it.url.startsWith(jenkinsUrl) }

            if (!hookAlreadyExists) {
                val encodedSshUrl = URLEncoder.encode(project.sshUrl, StandardCharsets.UTF_8.name())
                api.createHook(Command.CreateHook(project.projectId, "${jenkinsUrl}git/notifyCommit?url=$encodedSshUrl"))
            }
        }
    }
}