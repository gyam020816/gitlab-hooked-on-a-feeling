package eu.ha3.x.gitlabhookedonafeeling.api

import eu.ha3.x.gitlabhookedonafeeling.ghoaf.Command
import eu.ha3.x.gitlabhookedonafeeling.ghoaf.Hook
import eu.ha3.x.gitlabhookedonafeeling.ghoaf.Project
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
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
        val TOKEN = "sometoken"
    }

    val mockServer = MockWebServer()
    val SUT = GitlabHookedOnAFeelingApi(mockServer.url("/"), TOKEN)

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
    "name": "alpha",
    "anyRandomProperty": "",
    "ssh_url_to_repo": "ssh://git@example.com:1234/group/alpha.git"
  },
  {
    "id": 44,
    "name": "beta",
    "anyRandomProperty": "",
    "ssh_url_to_repo": "ssh://git@example.com:1234/group/beta.git"
  }
]"""))

        // Exercise
        val result = SUT.getAllProjects()

        // Verify
        assertThat(mockServer.requestCount).isEqualTo(1)
        mockServer.takeRequest().let {
            assertThat(it.path).isEqualTo("/api/v4/projects")
            assertThat(it.getHeader("Private-Token")).isEqualTo(TOKEN)
            assertThat(it.getHeader("Accept")).isEqualTo("application/json")
        }
        assertThat(result).isEqualTo(listOf(
                Project(45, "alpha", "ssh://git@example.com:1234/group/alpha.git"),
                Project(44, "beta", "ssh://git@example.com:1234/group/beta.git")
        ))
    }

    @Test
    internal fun `it should get all hooks of a project`() {
        mockServer.enqueue(MockResponse()
                .setBody("""[
  {
    "id": 6,
    "url": "https://example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git",
    "created_at": "2018-06-22T23:33:12.214Z",
    "push_events": true,
    "tag_push_events": false,
    "merge_requests_events": false,
    "repository_update_events": false,
    "enable_ssl_verification": true,
    "project_id": 45,
    "issues_events": false,
    "confidential_issues_events": false,
    "note_events": false,
    "confidential_note_events": null,
    "pipeline_events": false,
    "wiki_page_events": false,
    "job_events": false,
    "push_events_branch_filter": null
  }
]"""))

        // Exercise
        val result = SUT.getHooks(45)

        // Verify
        assertThat(mockServer.requestCount).isEqualTo(1)
        mockServer.takeRequest().let {
            assertThat(it.path).isEqualTo("/api/v4/projects/45/hooks")
            assertThat(it.getHeader("Private-Token")).isEqualTo(TOKEN)
            assertThat(it.getHeader("Accept")).isEqualTo("application/json")
        }
        assertThat(result).isEqualTo(listOf(
                Hook(
                        hookId = 6,
                        url = "https://example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git"
                )
        ))
    }

    @Test
    internal fun `it should create a hook`() {
        mockServer.enqueue(MockResponse().setBody("{}"))

        // Exercise
        SUT.createHook(Command.CreateHook(45, "https://example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git"))

        // Verify
        assertThat(mockServer.requestCount).isEqualTo(1)
        mockServer.takeRequest().let {
            assertThat(it.path).isEqualTo("/api/v4/projects/45/hooks")
            assertThat(it.method).isEqualTo("POST")
            assertThat(it.getHeader("Content-Type")).startsWith("application/json")
            assertThat(it.getHeader("Private-Token")).isEqualTo(TOKEN)
            assertThatJson(it.body.readUtf8()).isEqualTo("""{
  "url": "https://example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git",
  "enable_ssl_verification": true
}""")
        }
    }
}