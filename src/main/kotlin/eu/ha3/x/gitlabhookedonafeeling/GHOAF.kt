package eu.ha3.x.gitlabhookedonafeeling

import eu.ha3.x.gitlabhookedonafeeling.service.Command
import eu.ha3.x.gitlabhookedonafeeling.service.Hook
import eu.ha3.x.gitlabhookedonafeeling.service.Project
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
        api.getAllProjects().forEach(::update)
    }

    private fun update(project: Project) {
        val encodedSshUrl = URLEncoder.encode(project.sshUrl, StandardCharsets.UTF_8.name())
        val finalUrl = "${jenkinsUrl}git/notifyCommit?url=$encodedSshUrl"

        val jenkinsHooks = getAllJenkinsHooks(project)

        removeObsoleteHooks(jenkinsHooks, finalUrl, project)
        createHookIfNotExists(jenkinsHooks, finalUrl, project)
    }

    private fun getAllJenkinsHooks(project: Project) =
            api.getHooks(project.projectId).filter { it.url.startsWith(jenkinsUrl) }

    private fun createHookIfNotExists(jenkinsHooks: List<Hook>, finalUrl: String, project: Project) {
        val hookAlreadyExists = jenkinsHooks.any { it.url == finalUrl }
        if (!hookAlreadyExists) {
            api.createHook(Command.CreateHook(project.projectId, finalUrl))
        }
    }

    private fun removeObsoleteHooks(jenkinsHooks: List<Hook>, finalUrl: String, project: Project) {
        jenkinsHooks
                .filter { it.url != finalUrl }
                .forEach { api.deleteHook(Command.DeleteHook(project.projectId, it.hookId)) }
    }
}