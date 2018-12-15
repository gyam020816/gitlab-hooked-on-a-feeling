package eu.ha3.x.gitlabhookedonafeeling.ghoaf

/**
 * (Default template)
 * Created on 2018-12-16
 *
 * @author Ha3
 */
interface IFeelingApi {
    fun getAllProjects(): List<Project>
    fun getHooks(projectId: Int): List<Hook>
}