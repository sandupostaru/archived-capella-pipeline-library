def call(body) {

    // Jenkinsfile:
    // capellaAddon {
    //   url = "git repo url"  // where the git clone comes from
    //   name = "addon-name"   // name will be used for:
    //                              * download folder name: download.eclipse.org/capella/addons/<name>/
    //                              * dropins and site zip name
    //
    //   (optional) targetPlatform = <path-to-tp-pom>. Starts with capella-releng-parent/ or <name>/
    // }.

    def pipelineParams= [targetPlatform: 'capella-releng-parent/tp/capella-default-addon-target/pom.xml']

    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    pipeline {

	// dont checkout into the root folder, we use 2 subfolders, one for the addon, one for the capella-releng-parent
	options { skipDefaultCheckout() }

	agent {
	    label "migration"
	}

	tools {
	    maven "apache-maven-latest"
	    jdk "oracle-jdk8-latest"
	}

	stages {

	    // TODO: For PR branches, the releng branch must be the branch
	    // that is targeted by the PR, e.g. a PR for 1.3 should use the 1.3 releng branch
	    stage ('Fail for pull requests'){
		when { changeRequest() }
		steps {
		    error("Cannot build pull requests (yet)")
		}
	    }

	    stage ('Checkout capella-releng-parent & addon code') {
		steps {

		    checkout([$class: 'GitSCM',
			      branches: [[name: "*/${env.BRANCH_NAME}"]],
			      doGenerateSubmoduleConfigurations: false,
			      extensions: [[$class: 'RelativeTargetDirectory',
					    relativeTargetDir: pipelineParams.name]],
			      submoduleCfg: [], userRemoteConfigs: [[url: pipelineParams.url]]])


		    checkout([$class: 'GitSCM',
			      branches: [[name: "*/${env.BRANCH_NAME}"]],
			      doGenerateSubmoduleConfigurations: false,
			      extensions: [[$class: 'RelativeTargetDirectory',
					    relativeTargetDir: 'capella-releng-parent']],
			      submoduleCfg: [],
			      userRemoteConfigs: [[url: 'https://github.com/eclipse/capella-releng-parent.git']]])
		}
	    }

	    stage ('Generate Targetplatform'){
		steps {
		    sh "mvn --batch-mode --activate-profiles generate-target -f \"${pipelineParams.targetPlatform}\" clean install"
		}
	    }
	    stage ('Build') {
		steps {
		    sh "mvn --batch-mode -DpackagedSiteName=\"${pipelineParams.name}\" -f \"${pipelineParams.name}/pom.xml\" clean verify"
		}
	    }
	}
    }
}
