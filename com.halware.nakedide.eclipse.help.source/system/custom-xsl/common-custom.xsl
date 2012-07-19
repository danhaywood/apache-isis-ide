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
                version='1.0'>

    <xsl:param name="alignment"            select="'left'"/>

    <xsl:param name="use.extension"        select="1"/>

    <xsl:param name="header.rule"          select="1"/>
    <xsl:param name="footer.rule"          select="1"/>

    <xsl:param name="ulink.show"           select="1"/>

    <xsl:param name="ignore.image.scaling" select="1"/>

    <!-- Use section numbering for 1 depth level in the document tree -->
    <xsl:param name="chapter.autolabel" select="1"></xsl:param>
    <xsl:param name="section.autolabel" select="1"></xsl:param>
    <xsl:param name="section.label.includes.component.label" select="1"></xsl:param>
    <xsl:param name="toc.section.depth" select="2" />

    <!-- Causes variablelists to display like html dt, dl -->
    <xsl:param name="variablelist.as.blocks" select="1"/>

    <!-- This allows tables and other block objects to wrap across multiple pages -->
    <xsl:attribute-set name="formal.object.properties">
        <xsl:attribute name="keep-together.within-column">auto</xsl:attribute>
    </xsl:attribute-set>

    <!-- Add support for revision history in its own appendix -->
    <!--
    <xsl:template match="appendix[@id='revhistory']/para">
        <xsl:apply-templates select="//revhistory" mode="titlepage.mode"/>
    </xsl:template>
    -->

  <!-- Should graphics be used for admonitions (notes, warnings)? 0 or 1 -->
  <xsl:param name="admon.graphics" select="1"/>

  <!-- If 1 above, what is path to graphics? Make sure it ends in "/" and
       make sure that it is all on one line. -->
  <!-- <xsl:param name="admon.graphics.path">images/admon/</xsl:param> -->
  <xsl:param name="admon.graphics.path">../../../../system/docbook-xsl/images/</xsl:param>

  <!-- Again, if 1 above, what is the filename extension for admon
       graphics? -->
  <xsl:param name="admon.graphics.extension" select="'.svg'"/>

<xsl:param name="generate.toc">
appendix  toc,title
article/appendix  nop
article   toc,title
book      toc,title,figure,table,example,equation
/chapter   toc,title
part      toc,title
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

</xsl:stylesheet>
