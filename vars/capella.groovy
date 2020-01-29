def getDownloadLink(branch = "master", platform = "win"){
  def url = "https://download.eclipse.org/capella/core/products/nightly/${branch}/"
  def fileName = "capella.html"
  
  sh "curl -o ${fileName} -k ${url}"
  def html = readFile "${WORKSPACE}/${fileName}"
  
  def regex = ""

  switch(platform){
      case ~/mac/:
          regex = /(capella-.*macosx-cocoa-x86.*zip)'/
          break
      
      case ~/linux/:
          regex = /(capella-.*linux-gtk-x86.*zip)'/
          break
          
      case ~/win/:
           regex = /(capella-.*win32-win32-x86.*zip)'/
           break
      
      default:
          regex = /(capella-.*win32-win32-x86.*zip)'/
          break
  }
  
  def tokens = html =~ regex
  def zipName = tokens[0][1]

  return url + zipName
}