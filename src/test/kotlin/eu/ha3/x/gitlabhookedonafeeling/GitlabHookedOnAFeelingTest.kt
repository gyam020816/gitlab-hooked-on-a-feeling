package eu.ha3.x.gitlabhookedonafeeling

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test



/**
 * (Default template)
 * Created on 2018-12-15
 *
 * @author Ha3
 */
internal class GitlabHookedOnAFeelingTest {
    companion object {
        val BASE_URL = "/api/v4/"
        val TOKEN = "sometoken"
    }

    val mockServer = MockWebServer()
    val SUT = GitlabHookedOnAFeeling(mockServer.url(BASE_URL), TOKEN)

    @AfterEach
    internal fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    internal fun `it should get all projects regardless of extra properties in the json object`() {
        mockServer.enqueue(MockResponse()
                .setBody("""[
  {
    "id": 45,
    "anyRandomProperty": "",
    "ssh_url_to_repo": "ssh://git@example.com:1234/group/project45.git"
  },
  {
    "id": 44,
    "anyRandomProperty": "",
    "ssh_url_to_repo": "ssh://git@example.com:1234/group/project44.git"
  }
]"""))

        // Execute
        val result = SUT.getAllProjects()

        // Verify
        mockServer.takeRequest().let {
            assertThat(it.path).isEqualTo("${BASE_URL}projects")
            assertThat(it.getHeader("Private-Token")).isEqualTo(TOKEN)
            assertThat(it.getHeader("Accept")).isEqualTo("application/json")
        }
        assertThat(result).isEqualTo(listOf(
                Project(45, "ssh://git@example.com:1234/group/project45.git"),
                Project(44, "ssh://git@example.com:1234/group/project44.git")
        ))
    }

}