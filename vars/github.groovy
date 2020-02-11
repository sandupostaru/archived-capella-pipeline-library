def pullRequestComment(pullRequestId, message) {
  def apiURL = "https://api.github.com/repos/eclipse/capella/issues/${pullRequestId}/comments"
  
  withCredentials([string(credentialsId: '0dea5761-867c-44db-92fa-9304c81a8653', variable: 'password')]) {
  	sh """curl ${apiURL} -d '{"body":"${message}"}' -u 'eclipse-capella-bot:${password}' -X POST -H 'Content-Type: application/json'"""
  }
}

def buildStartedComment() {
  def message = ":rocket: Build [${CHANGE_ID}-${BUILD_ID}](${BUILD_URL}) started!"
  
  pullRequestComment(CHANGE_ID, message)
}

def buildSuccessfullComment() {
  def message = ":thumbsup: Build [${CHANGE_ID}-${BUILD_ID}](${BUILD_URL}) is successfull! The product is available [here](http://download.eclipse.org/capella/core/products/nightly/${BUILD_KEY})."
  
  pullRequestComment(CHANGE_ID, message)
}

def buildFailedComment() {
  def message = ":disappointed: Build [${CHANGE_ID}-${BUILD_ID}](${BUILD_URL}) failed!"
  
  pullRequestComment(CHANGE_ID, message)
}

def buildUnstableComment(String pullRequestId) {
  def message = ":worried: Build [${CHANGE_ID}-${BUILD_ID}](${BUILD_URL}) is unstable! The product is available [here](http://download.eclipse.org/capella/core/products/nightly/${BUILD_KEY})."
  
  pullRequestComment(CHANGE_ID, message)
}

def buildAbortedComment(String pullRequestId) {
  def message = ":neutral_face: Build [${CHANGE_ID}-${BUILD_ID}](${BUILD_URL}) has been aborted!"
  
  pullRequestComment(CHANGE_ID, message)
}
