package eu.ha3.x.gitlabhookedonafeeling

import eu.ha3.x.gitlabhookedonafeeling.service.Command
import eu.ha3.x.gitlabhookedonafeeling.system.IGitLabFeelingApi
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class GHOAF(private val api: IGitLabFeelingApi, private val jenkinsUrl: String) {
    init {
        if (!jenkinsUrl.endsWith("/")) {
            throw IllegalArgumentException("jenkinsUrl must end with /")
        }
    }

    fun execute() {
        for (project in api.getAllProjects()) {
            val encodedSshUrl = URLEncoder.encode(project.sshUrl, StandardCharsets.UTF_8.name())
            val finalUrl = "${jenkinsUrl}git/notifyCommit?url=$encodedSshUrl"

            val hooks = api.getHooks(project.projectId)
            for (hook in hooks) {
                if (hook.url.startsWith(jenkinsUrl) && hook.url != finalUrl) {
                    api.deleteHook(Command.DeleteHook(project.projectId, hook.hookId))
                }
            }

            val hookAlreadyExists = hooks
                    .any { it.url == finalUrl }

            if (!hookAlreadyExists) {
                api.createHook(Command.CreateHook(project.projectId, finalUrl))
            }
        }
    }
}