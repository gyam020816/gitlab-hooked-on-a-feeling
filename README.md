gitlab-hooked-on-a-feeling
====


When this application is executed, it creates a hook in every GitLab project so that Jenkins is notified of new commits.

[![Hooked on a Feeling](https://img.youtube.com/vi/PJQVlVHsFF8/102.jpg)](https://www.youtube.com/watch?v=PJQVlVHsFF8 "David Hasselhoff - Hooked on a Feeling")


If the Jenkins is configured according to [Git Plugin](https://wiki.jenkins.io/display/JENKINS/Git+Plugin#GitPlugin-Pushnotificationfromrepository) `notifyCommit`, this should in turn trigger builds:

> This will scan all the jobs that:
> 
> - Have Build Triggers > Poll SCM enabled.
> - No polling Schedule is required.
> - Are configured to build the repository at the specified URL
> - Are configured to build the optionally specified branches or commit ID
>
> For jobs that meet these conditions, polling will be immediately triggered.  If polling finds a change worthy of a build, a build will in turn be triggered.

It also works *Multibranch Pipeline* jobs without needing to configure anything related to *Poll SCM*, but I could not find it documented within Git Plugin.

It requires:
- a GitLab URL
- a GitLab personal access token
- a Jenkins URL

Hooks are not re-created on a project if it already has a hook that starts with the Jenkins URL.
- 🚩 Updating a hook is not currently supported (i.e. if the project group or name changes)
- 🚩 Deleting all hooks is not currently supported (i.e. if the Jenkins URL changes)

#### Build
```
mvn clean assembly:assembly
```

#### Run
```
# GITLAB_URL must end with a /
# GITLAB_TOKEN is the personal access token of the user with `api` rights
# JENKINS_URL must end with a /

java -jar target/gitlabhookedonafeeling.jar --gitlab_url=$GITLAB_URL --gitlab_token=$GITLAB_TOKEN --jenkins_url=$JENKINS_URL
```

Sample usage:
```
java -jar target/gitlabhookedonafeeling.jar --gitlab_url=https://gitlab.example.com/ --gitlab_token=0123456789abcdefgHIJ --jenkins_url=https://jenkins.example.com/
```

#### Why use Git plugin and not other GitLab plugins?

GitLab Hook Plugin (for Jenkins)
- ✔️ has a dedicated single hook URL to receive events and scan for Jenkins jobs that match,
- 🛑 but it is deprecated.
  
GitLab Plugin (for Jenkins)
- 🛑 requires configuring one hook per matching Jenkins job which contains the URL of the Jenkins job.
