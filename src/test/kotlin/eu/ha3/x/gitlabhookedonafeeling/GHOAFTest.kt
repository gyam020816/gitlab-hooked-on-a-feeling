package eu.ha3.x.gitlabhookedonafeeling

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.stub
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import eu.ha3.x.gitlabhookedonafeeling.ghoaf.IFeelingApi
import eu.ha3.x.gitlabhookedonafeeling.ghoaf.Project
import org.junit.jupiter.api.Test

/**
 * (Default template)
 * Created on 2018-12-16
 *
 * @author Ha3
 */
internal class GHOAFTest {
    private val feelingApi = mock<IFeelingApi>()
    private val SUT = GHOAF(feelingApi)

    @Test
    internal fun `it should get hooks for all projects`() {
        feelingApi.stub {
            on { getAllProjects() }.thenReturn(listOf(
                    Project(1, "alpha", "ssh://git@example.com:1234/group/alpha.git"),
                    Project(2, "beta", "ssh://git@example.com:1234/group/beta.git")
            ))
        }

        // Exercice
        SUT.execute()

        // Verify
        verify(feelingApi, times(1)).getHooks(1)
        verify(feelingApi, times(1)).getHooks(2)
    }
}