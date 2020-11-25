# jenkins_build_trigger_cause_extractor
A script to extract the `Started By` regardless of the tree depth. This script would recursively search all the way to get the "original" started by, where
other examples of this solution would only go one or two levels deep. Examle scenario:
```bash
Started by upstream project "some-job-3" build number 15423
13:09:02 originally caused by:
13:09:02  Started by upstream project "some-job-2" build number 3493
13:09:02  originally caused by:
13:09:02   Started by upstream project "some-job-1" build number 2899
13:09:02   originally caused by:
13:09:02    Started by upstream project "some-job-0" build number 8788
13:09:02    originally caused by:
13:09:02     Started by an SCM change
```
would corectly get `SCM change`

```bash
21:45:24 Started by upstream project "some-job-1" build number 3
21:45:24 originally caused by:
21:45:24  Started by upstream project "some-job-0" build number 5989
21:45:24  originally caused by:
21:45:24   Started by user Me-ion
```
would corectly get the user's email, but could also get the username

```bash
Started by upstream project "some-job-2" build number 23
originally caused by:
 Started by upstream project "some-job-1" build number 2012
 originally caused by:
  Started by upstream project "some-job-0" build number 1956
  originally caused by:
   Started by timer
```
would return `JenkinsTimer`

## Usage:
- Add the file to a location on the Jenkins instance aceesible to the service user
- On the Jenkins job configuration page (might need a plugin)
* `Add build step` -> `Execute system Groovy script` -> Select `Groovy script file` and paste the path to the script

## Note: First run might cause an `In-Process ScriptApproval` and you'll need an admin to approve the "quarantined" script
