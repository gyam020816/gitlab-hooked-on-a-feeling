package eu.ha3.x.gitlabhookedonafeeling

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException
import eu.ha3.x.gitlabhookedonafeeling.api.GitlabHookedOnAFeelingApi
import okhttp3.HttpUrl

/**
 * (Default template)
 * Created on 2018-12-16
 *
 * @author Ha3
 */
fun main(args: Array<String>) {
    ArgParser(args).parseInto(::Arguments).run {
        GHOAF(GitlabHookedOnAFeelingApi(HttpUrl.get(gitlabUrl), gitlabApiToken), jenkinsUrl).execute()
    }
}

class Arguments(parser: ArgParser) {
    val gitlabUrl by parser.storing(
            "--gitlab_url",
            help = "gitlab root url ending with a /")
            .addValidator { if (!value.endsWith("/")) throw InvalidArgumentException("gitlab root url must end with a /") }

    val gitlabApiToken by parser.storing(
            "--gitlab_token",
            help = "gitlab personal access token")

    val jenkinsUrl by parser.storing(
            "--jenkins_url",
            help = "jenkins root url ending with a /")
            .addValidator { if (!value.endsWith("/")) throw InvalidArgumentException("jenkins root url must end with a /") }
}