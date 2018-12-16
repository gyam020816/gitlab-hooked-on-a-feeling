package eu.ha3.x.gitlabhookedonafeeling.system.retrofit

import eu.ha3.x.gitlabhookedonafeeling.service.Command
import eu.ha3.x.gitlabhookedonafeeling.service.Hook
import eu.ha3.x.gitlabhookedonafeeling.system.IGitLabFeelingApi
import eu.ha3.x.gitlabhookedonafeeling.service.Project
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


/**
 * (Default template)
 * Created on 2018-12-15
 *
 * @author Ha3
 */
class RetrofitGitLabFeelingApi(
        apiUrl: HttpUrl,
        apiToken: String,
        retrofitModifierFn: (Retrofit) -> Retrofit = { o -> o }
) : IGitLabFeelingApi {
    private val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                                .header("Private-Token", apiToken)
                                .header("Accept", "application/json")
                                .build()
                        chain.proceed(newRequest)
                    }
                    .build())
            .addConverterFactory(JacksonConverterFactory.create(KObjectMapper.newInstance()))
            .build()
            .let(retrofitModifierFn)
    private val gitlab = retrofit.create(Projects::class.java)

    override fun getAllProjects(): List<Project> {
        val call = gitlab.projects()

        return call.execute().body()?.map {
            Project(
                    projectId = it.id,
                    name = it.name,
                    sshUrl = it.ssh_url_to_repo
            )
        } ?: throw IllegalStateException("body is null")
    }

    override fun getHooks(projectId: Int): List<Hook> {
        val call = gitlab.getHooks(projectId)

        return call.execute().body()?.map {
            Hook(
                    hookId = it.id,
                    url = it.url
            )
        } ?: throw IllegalStateException("body is null")
    }

    override fun createHook(command: Command.CreateHook) {
        val call = gitlab.createHook(command.projectId, Projects.CreateHookRequestBody(
                url = command.url,
                enable_ssl_verification = true
        ))

        call.execute()
    }
    override fun deleteHook(command: Command.DeleteHook) {
        val call = gitlab.deleteHook(command.projectId, command.hookId)

        call.execute()
    }

}