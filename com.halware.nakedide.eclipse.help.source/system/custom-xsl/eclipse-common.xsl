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

  <xsl:import href="../docbook-xsl/eclipse/eclipse.xsl"/>
  <xsl:import href="html-derived-common.xsl"/>

  <xsl:param name="html.stylesheet" >../style.css</xsl:param>
  <xsl:param name="chunk.first.sections" select="1" />
  <xsl:param name="chunk.section.depth" select="3" />
  <xsl:param name="base.dir" select="'contents/'" />
  <xsl:param name="use.id.as.filename" select="0" />
  <xsl:param name="suppress.navigation" select="1" />
  <xsl:param name="chapter.autolabel" select="0" />
  <xsl:param name="generate.section.toc.level" select="0" />
  <xsl:param name="table.borders.with.css" select="0" />
  <xsl:param name="table.cell.border.thickness" select="1" />
  <xsl:param name="html.cellspacing" select="0" />
  <xsl:param name="html.cellpadding" select="10" />
  <xsl:param name="html.cleanup" select="1" />
  
    <!-- If 1 above, what is path to graphics? Make sure it ends in "/" and
       make sure that it is all on one line. -->
  <xsl:param name="admon.graphics.path">../images/admon/</xsl:param>

  <xsl:param name="generate.toc">
   appendix  toc,title
   article/appendix  nop
   article   toc,title
   book      nop
   chapter   nop
   part      nop
   preface   toc,title
   qandadiv  toc
   qandaset  toc
   reference toc,title
   sect1     toc
   sect2     toc
   sect3     toc
   sect4     toc
   sect5     toc
   section   toc
   set       toc,title
  </xsl:param>
  
<xsl:template name="plugin.xml">
  <xsl:call-template name="write.chunk">
    <xsl:with-param name="filename">
      <xsl:if test="$manifest.in.base.dir != 0">
        <xsl:value-of select="$base.dir"/>
      </xsl:if>
      <xsl:value-of select="'plugin.xml'"/>
    </xsl:with-param>
    <xsl:with-param name="method" select="'xml'"/>
    <xsl:with-param name="encoding" select="'utf-8'"/>
    <xsl:with-param name="indent" select="'yes'"/>
    <xsl:with-param name="content">
      <plugin>

        <extension point="org.eclipse.help.toc">
          <toc file="toc.xml" primary="true"/>
        </extension>

      </plugin>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>


</xsl:stylesheet>
