package eu.ha3.x.gitlabhookedonafeeling

import retrofit2.Call
import retrofit2.http.GET

/**
 * (Default template)
 * Created on 2018-12-15
 *
 * @author Ha3
 */
interface Projects {
    @GET("projects")
    fun projects(): Call<List<Project>>
}

data class Project(val id: Int)
