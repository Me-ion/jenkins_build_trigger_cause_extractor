import hudson.model.Cause.UpstreamCause
import hudson.model.Cause.UserIdCause
import hudson.model.User
import hudson.tasks.Mailer.UserProperty
import hudson.triggers.SCMTrigger.SCMTriggerCause
import hudson.triggers.TimerTrigger.TimerTriggerCause

def currentBuild = Thread.currentThread().executable
def jobName = currentBuild.environment.JOB_NAME
def buildNumber = currentBuild.environment.BUILD_NUMBER
def triggeredBy = getBuildTriggeredBy(currentBuild)

def getBuildTriggeredBy(build){
	def buildCause = build.getCause(UserIdCause)
	if(buildCause != null){
		def userId = buildCause.getUserId()
		return User.get(userId).getProperty(UserProperty).getAddress()
	}

	buildCause = build.getCause(TimerTriggerCause)
	if(buildCause != null){
		return 'JenkinsTimer'
	}

	buildCause = build.getCause(SCMTriggerCause)
	if (buildCause != null) {
		return 'SourceControlChange'
	}

	def upstreamCause = build.getCause(UpstreamCause)
	if(upstreamCause != null){
		def upstreamRun = upstreamCause.getUpstreamRun()
		if(upstreamRun != null){
			getBuildTriggeredBy(upstreamRun)
		}
	}
}

println "jobName = ${jobName}"
println "buildNumber = ${buildNumber}"
println "triggeredBy = ${triggeredBy}"

// Do something with the the data, e.g. send an email
