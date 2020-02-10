def pullRequestComment(pullRequestId, message) {
  def apiURL = "https://api.github.com/repos/eclipse/capella/issues/${pullRequestId}/comments"
  
  withCredentials([string(credentialsId: '0dea5761-867c-44db-92fa-9304c81a8653', variable: 'password')]) {
  	sh """
    	curl ${apiURL}
      	-d '{"body":"${message}"}' 
      	-u "eclipse-capella-bot:${password}" 
				-X POST 
				-H "Content-Type: application/json"
  	"""
  }
}

def buildStartedComment() {
  def message = ":rocket: Build [${ENV.CHANGE_ID}-${ENV.BUILD_ID}](${ENV.BUILD_URL}) started!"
  
  pullRequestComment(ENV.CHANGE_ID, message)
}

def buildSuccessfull() {
  def message = ":thumbsup: Build [${ENV.CHANGE_ID}-${ENV.BUILD_ID}](${ENV.BUILD_URL}) is successfull! The product is available [here](http://download.eclipse.org/capella/core/products/nightly/${BUILD_KEY})."
  
  pullRequestComment(ENV.CHANGE_ID, message)
}

def buildFailed() {
  def message = ":disappointed: Build [${ENV.CHANGE_ID}-${ENV.BUILD_ID}](${ENV.BUILD_URL}) failed!"
  
  pullRequestComment(ENV.CHANGE_ID, message)
}

def buildUnstable(String pullRequestId) {
  def message = ":worried: Build [${ENV.CHANGE_ID}-${ENV.BUILD_ID}](${ENV.BUILD_URL}) is unstable! The product is available [here](http://download.eclipse.org/capella/core/products/nightly/${BUILD_KEY})."
  
  pullRequestComment(ENV.CHANGE_ID, message)
}
