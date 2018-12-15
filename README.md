gitlab-hooked-on-a-feeling
====

Auto-trigger Jenkins builds when code is pushed on a GitLab server.

When this application is executed, it scans GitLab projects and creates a hook in GitLab so that a Jenkins server is notified of new commits ([Git Plugin](https://wiki.jenkins.io/display/JENKINS/Git+Plugin#GitPlugin-Pushnotificationfromrepository) `notifyCommit`).

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
