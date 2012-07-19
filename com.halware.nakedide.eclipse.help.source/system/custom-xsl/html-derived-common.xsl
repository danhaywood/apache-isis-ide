<?xml version="1.0" encoding="UTF-8"?>

<!-- This file is part of Dopus                                              -->

<!-- Dopus is free software; you can redistribute it and/or modify           -->
<!-- it under the terms of the GNU General Public License as published by    -->
<!-- the Free Software Foundation; either version 2 of the License, or       -->
<!-- (at your option) any later version.                                     -->

<!-- Dopus is distributed in the hope that it will be useful,                -->
<!-- but WITHOUT ANY WARRANTY; without even the implied warranty of          -->
<!-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           -->
<!-- GNU General Public License for more details.                            -->

<!-- You should have received a copy of the GNU General Public License       -->
<!-- along with Dopus; if not, write to the Free Software                    -->
<!-- Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
                xmlns:exsl="http://exslt.org/common"
                xmlns:set="http://exslt.org/sets"
		version="1.0"
                exclude-result-prefixes="doc exsl set">

  <xsl:import href="common-custom.xsl"/>

  <xsl:param name="html.stylesheet" select="'style.css'"/>

  <!-- Turn off text label, we'll add an image with css instead (not from xsl). -->
  <xsl:param name="admon.textlabel" select="1"></xsl:param>
  
  <!-- Again, if 1 above, what is the filename extension for admon
       graphics? -->
  <xsl:param name="admon.graphics.extension" select="'.png'"/>  
  
  <xsl:param name="table.borders.with.css" select="1" />
  
  <!--
  <xsl:param name="table.frame.border.thickness" select="thin" />
  <xsl:param name="table.cell.border.thickness" select="thin" />
  <xsl:param name="table.frame.border.color" select="Orange" />
  <xsl:param name="table.cell.border.color" select="Orange" />

  <xsl:param name="html.cellspacing" select="0" />
  <xsl:param name="html.cellpadding" select="10" />
  <xsl:param name="html.cleanup" select="1" />
  -->

  <!-- fast creation of html chunks, available with Saxon -->
  <xsl:param name="chunk.fast" select="1" />
  <xsl:param name="saxon.character.representation" select="'native;decimal'" />

  <!-- This PI gives you the ability to set a line break anywhere in your text
       bla <? lb ?> text ... -->
  <xsl:template match="processing-instruction('lb')">
    <br/>
  </xsl:template>

</xsl:stylesheet>
