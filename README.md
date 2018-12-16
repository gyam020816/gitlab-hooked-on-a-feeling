gitlab-hooked-on-a-feeling
====


When this application is executed, it creates a hook in every GitLab project so that Jenkins is notified of new commits.

If the Jenkins is configured according to [Git Plugin](https://wiki.jenkins.io/display/JENKINS/Git+Plugin#GitPlugin-Pushnotificationfromrepository) `notifyCommit`, this should in turn trigger builds.

> This will scan all the jobs that:
> 
> Have Build Triggers > Poll SCM enabled.  No polling Schedule is required.
> Are configured to build the repository at the specified URL
> Are configured to build the optionally specified branches or commit ID
>
> For jobs that meet these conditions, polling will be immediately triggered.  If polling finds a change worthy of a build, a build will in turn be triggered.

[![Hooked on a Feeling](https://img.youtube.com/vi/PJQVlVHsFF8/102.jpg)](https://www.youtube.com/watch?v=PJQVlVHsFF8 "David Hasselhoff - Hooked on a Feeling")

It requires:
- a GitLab URL
- a GitLab personal access token
- a Jenkins server URL

#### Why use Git plugin and not other GitLab plugins?

GitLab Hook Plugin (for Jenkins)
- ✔️ has a dedicated single hook URL to receive events and scan for Jenkins jobs that match,
- 🛑 but it is deprecated.
  
GitLab Plugin (for Jenkins)
- 🛑 requires configuring one hook per matching Jenkins job which contains the URL of the Jenkins job.
