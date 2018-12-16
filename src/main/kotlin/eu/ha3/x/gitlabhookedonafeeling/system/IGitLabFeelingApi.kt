package eu.ha3.x.gitlabhookedonafeeling.system

import eu.ha3.x.gitlabhookedonafeeling.service.Command
import eu.ha3.x.gitlabhookedonafeeling.service.Hook
import eu.ha3.x.gitlabhookedonafeeling.service.Project

/**
 * (Default template)
 * Created on 2018-12-16
 *
 * @author Ha3
 */
interface IGitLabFeelingApi {
    fun getAllProjects(): List<Project>
    fun getHooks(projectId: Int): List<Hook>
    fun createHook(command: Command.CreateHook)
    fun deleteHook(command: Command.DeleteHook)
}