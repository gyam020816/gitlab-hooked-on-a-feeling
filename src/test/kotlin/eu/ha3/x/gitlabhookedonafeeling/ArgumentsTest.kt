package eu.ha3.x.gitlabhookedonafeeling

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException
import com.xenomachina.argparser.MissingValueException
import com.xenomachina.argparser.ShowHelpException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

/**
 * (Default template)
 * Created on 2018-12-16
 *
 * @author Ha3
 */
internal class ArgumentsTest {
    @Test
    internal fun `it should parse arguments`() {
        ArgParser(arrayOf(
                "--gitlab_url=https://gitlab.example.com/",
                "--gitlab_token=someToken",
                "--jenkins_url=https://jenkins.example.com/"
                )).parseInto(::Arguments).run {
            assertThat(gitlabUrl).isEqualTo("https://gitlab.example.com/")
            assertThat(gitlabApiToken).isEqualTo("someToken")
            assertThat(jenkinsUrl).isEqualTo("https://jenkins.example.com/")
        }
    }

    @Test
    internal fun `it should not parse arguments if url does not end with slash`() {
        assertAll(
                {
                    assertThrows<InvalidArgumentException> {
                        ArgParser(arrayOf(
                                "--gitlab_url=https://gitlab.example.com",
                                "--gitlab_token=someToken",
                                "--jenkins_url=https://jenkins.example.com/"
                        )).parseInto(::Arguments)
                    }
                },
                {
                    assertThrows<InvalidArgumentException> {
                        ArgParser(arrayOf(
                                "--gitlab_url=https://gitlab.example.com/",
                                "--gitlab_token=someToken",
                                "--jenkins_url=https://jenkins.example.com"
                        )).parseInto(::Arguments)
                    }
                }
        )
    }

    @Test
    internal fun `it should not parse arguments if any is missing`() {
        assertAll(
                {
                    assertThrows<MissingValueException> {
                        ArgParser(arrayOf(
                                "--gitlab_url=https://gitlab.example.com",
                                "--gitlab_token=someToken"
                        )).parseInto(::Arguments)
                    }
                },
                {
                    assertThrows<MissingValueException> {
                        ArgParser(arrayOf(
                                "--gitlab_url=https://gitlab.example.com",
                                "--jenkins_url=https://jenkins.example.com/"
                        )).parseInto(::Arguments)
                    }
                },
                {
                    assertThrows<MissingValueException> {
                        ArgParser(arrayOf(
                                "--gitlab_token=someToken",
                                "--jenkins_url=https://jenkins.example.com/"
                        )).parseInto(::Arguments)
                    }
                }
        )
    }

    @Test
    internal fun `it should display help`() {
        assertThrows<ShowHelpException> {
            ArgParser(arrayOf("--help")).parseInto(::Arguments)
        }
    }
}