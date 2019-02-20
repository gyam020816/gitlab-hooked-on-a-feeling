package eu.ha3.x.gitlabhookedonafeeling.system.retrofit

import retrofit2.Call
import retrofit2.http.*
import java.time.ZonedDateTime

/**
 * (Default template)
 * Created on 2018-12-15
 *
 * @author Ha3
 */
interface Projects {
    @GET("/api/v4/projects?per_page=999")
    fun projects(): Call<List<Project>>

    @GET("/api/v4/projects/{id}/hooks")
    fun getHooks(@Path(value = "id") id: Int): Call<List<Hook>>

    @POST("/api/v4/projects/{id}/hooks")
    fun createHook(@Path(value = "id") id: Int, @Body body: CreateHookRequestBody): Call<Void>

    @DELETE("/api/v4/projects/{id}/hooks/{hookId}")
    fun deleteHook(@Path(value = "id") id: Int, @Path(value = "hookId") hookId: Int): Call<Void>

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