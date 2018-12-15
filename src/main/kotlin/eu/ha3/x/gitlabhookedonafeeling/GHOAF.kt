package eu.ha3.x.gitlabhookedonafeeling

import eu.ha3.x.gitlabhookedonafeeling.ghoaf.IFeelingApi

class GHOAF(private val api: IFeelingApi) {
    fun execute() {
        for (project in api.getAllProjects()) {
            api.getHooks(project.id)
        }
    }
}