package eu.ha3.x.gitlabhookedonafeeling.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.ZonedDateTime

/**
 * (Default template)
 * Created on 2018-12-15
 *
 * @author Ha3
 */
interface Projects {
    @GET("/api/v4/projects")
    fun projects(): Call<List<Project>>

    @GET("/api/v4/projects/{id}/hooks")
    fun getHooks(@Path(value = "id") id: Int): Call<List<Hook>>

    @POST("/api/v4/projects/{id}/hooks")
    fun createHook(@Path(value = "id") id: Int, @Body body: CreateHookRequestBody): Call<Void>

    data class Project(
            val id: Int,
            val name: String,
            val ssh_url_to_repo: String
    )

    data class Hook(
            val id: Int,
            val url: String,
            val created_at: ZonedDateTime?,
            val push_events: Boolean?,
            val tag_push_events: Boolean?,
            val merge_requests_events: Boolean?,
            val repository_update_events: Boolean?,
            val enable_ssl_verification: Boolean?,
            val issues_events: Boolean?,
            val confidential_issues_events: Boolean?,
            val note_events: Boolean?,
            val pipeline_events: Boolean?,
            val wiki_page_events: Boolean?,
            val job_events: Boolean?
    )

    data class CreateHookRequestBody(
            val url: String,
            val enable_ssl_verification: Boolean
    )
}