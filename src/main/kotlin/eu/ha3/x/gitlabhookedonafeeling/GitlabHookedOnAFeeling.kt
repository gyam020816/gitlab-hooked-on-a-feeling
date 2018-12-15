package eu.ha3.x.gitlabhookedonafeeling

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
class GitlabHookedOnAFeeling(
        apiUrl: HttpUrl,
        apiToken: String,
        retrofitModifierFn: (Retrofit) -> Retrofit = { o -> o }
) {
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

    fun getAllProjects(): List<Project> {
        val gitlab = retrofit.create(Projects::class.java)

        val call = gitlab.projects()

        return call.execute().body() ?: throw IllegalStateException("body is null")
    }
}