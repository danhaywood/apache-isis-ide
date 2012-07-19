boolean hasCopyright(f){
    def COPYRIGHT_FRAGMENT="""
/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/"""

    return f.text.contains(COPYRIGHT_FRAGMENT)
}




String[] stripLast(f, numLines) {

	String[] lines = f.readLines()
	linesList = lines.toList()
	linesList.removeRange(lines.size()-numLines,lines.size())
	return linesList.toArray()
}


void replace(f, lines) {
	f.delete()
    write(f, lines)
}


void addCopyright(f){
    def COPYRIGHT_TEXT="""
/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
"""

    write(f, COPYRIGHT_TEXT.split("\n"))
}


void write(f, lines) {
    lines.each {
	    f << it << "\n"
	}
}


void addOrReplaceCopyright(f) {
    println(f.getCanonicalPath())
    if (hasProprietary(f)) {
      replace(f, stripLast(f,9)) 
      addCopyright(f)
    }
    //if (hasCopyright(f)) {
    //  replace(f, stripLast(f,17)) 
    //}
}



///////////////////////////////////////////

def ant=new AntBuilder()

scanner = ant.fileScanner {
           fileset(dir:".") {
              include(name:"**/*.java")
          }
}
	        
scanner.each {
	 addOrReplaceCopyright(it)   
}
