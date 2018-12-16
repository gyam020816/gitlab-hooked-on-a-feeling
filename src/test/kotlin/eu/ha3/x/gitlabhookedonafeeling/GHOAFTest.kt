package eu.ha3.x.gitlabhookedonafeeling

import com.nhaarman.mockito_kotlin.*
import eu.ha3.x.gitlabhookedonafeeling.service.Command
import eu.ha3.x.gitlabhookedonafeeling.service.Hook
import eu.ha3.x.gitlabhookedonafeeling.service.Project
import eu.ha3.x.gitlabhookedonafeeling.system.IGitLabFeelingApi
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * (Default template)
 * Created on 2018-12-16
 *
 * @author Ha3
 */
internal class GHOAFTest {
    private val feelingApi = mock<IGitLabFeelingApi>()
    private val SUT = GHOAF(feelingApi, "https://jenkins.example.com/")

    @Test
    internal fun `it should create hooks for all projects`() {
        feelingApi.stub {
            on { getAllProjects() }.thenReturn(listOf(
                    Project(1, "alpha", "ssh://git@example.com:1234/group/alpha.git"),
                    Project(2, "beta", "ssh://git@example.com:1234/group/beta.git")
            ))
            on { getHooks(any()) }.thenReturn(emptyList())
        }

        // Exercice
        SUT.execute()

        // Verify
        verify(feelingApi, times(1)).getHooks(1)
        verify(feelingApi, times(1)).createHook(Command.CreateHook(1, "https://jenkins.example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git"))
        verify(feelingApi, times(1)).getHooks(2)
        verify(feelingApi, times(1)).createHook(Command.CreateHook(2, "https://jenkins.example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Fbeta.git"))
    }

    @Test
    internal fun `it should not create a hook for projects that already have an up to date hook`() {
        feelingApi.stub {
            on { getAllProjects() }.thenReturn(listOf(
                    Project(1, "alpha", "ssh://git@example.com:1234/group/alpha.git"),
                    Project(2, "beta", "ssh://git@example.com:1234/group/beta.git")
            ))
            on { getHooks(1) }.thenReturn(listOf(Hook(101, "https://other.example.com/api/buildNow")))
            on { getHooks(2) }.thenReturn(listOf(Hook(102, "https://jenkins.example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Fbeta.git")))
        }

        // Exercice
        SUT.execute()

        // Verify
        verify(feelingApi, times(1)).getHooks(1)
        verify(feelingApi, times(1)).createHook(Command.CreateHook(1, "https://jenkins.example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git"))
        verify(feelingApi, times(1)).createHook(any())
        verify(feelingApi, times(1)).getHooks(2)
    }

    @Test
    internal fun `it should delete hooks that target jenkins but with an obsolete url`() {
        feelingApi.stub {
            on { getAllProjects() }.thenReturn(listOf(
                    Project(1, "alpha", "ssh://git@example.com:1234/group/alpha.git")
            ))
            on { getHooks(1) }.thenReturn(listOf(
                    Hook(101, "https://jenkins.example.com//git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git"),
                    Hook(102, "https://jenkins.example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fgroup%2Falpha.git"),
                    Hook(103, "https://jenkins.example.com/git/notifyCommit?url=ssh%3A%2F%2Fgit%40example.com%3A1234%2Fsomeothergroup%2Falpha.git")
            ))
        }

        // Exercice
        SUT.execute()

        // Verify
        verify(feelingApi, times(1)).getHooks(1)
        verify(feelingApi, times(0)).createHook(any())
        verify(feelingApi, times(1)).deleteHook(Command.DeleteHook(1, 101))
        verify(feelingApi, times(1)).deleteHook(Command.DeleteHook(1, 103))
        verify(feelingApi, times(2)).deleteHook(any())
    }
}

internal class GHOAFTest_NoSetup {
    @Test
    internal fun `it should not create instances when url does not end with slash`() {
        // Exercise and Verify
        assertThrows<IllegalArgumentException> { GHOAF(mock(), "https://bad-jenkins.example.com") }
    }
}