def buildUpdateSiteFromProduct(equinoxJarPath, sourceProductPath, targetUpdateSitePath) {
  sh "java -jar ${equinoxJarPath} -source ${sourceProductPath} -metadataRepository ${targetUpdateSitePath} -artifactRepository ${targetUpdateSitePath} -application org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher -compress -publishArtifacts"
}


def installFeature(String targetProductPath, String featureRepository, String featureName) {
	sh "${targetProductPath} -repository ${featureRepository} -installIU ${featureName} -application org.eclipse.equinox.p2.director -noSplash"  
}