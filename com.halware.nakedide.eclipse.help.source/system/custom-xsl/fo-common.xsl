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
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:rx="http://www.renderx.com/XSL/Extensions"
                version='1.0'>

    <xsl:import href="../docbook-xsl/fo/docbook.xsl"/>
    <xsl:import href="fo/component.xsl"/>
    <xsl:import href="fo/pagesetup.xsl"/>

    <!-- Customized titlepage template eliminates blank page between the verso and the TOC -->
    <xsl:include href="titlepage-spec/titlepage.nv2.xsl"/>
    <xsl:include href="common-custom.xsl"/>

    <xsl:param name="paper.type"      select="'A4'"/>
    <xsl:param name="draft.mode"      select="'no'"/>

    <xsl:param name="fop.extensions"  select="1"/>
    <!--  <xsl:param name="xep.extensions"  select="1"/> -->

    <xsl:param name="title.font.family">Georgia</xsl:param>
    <xsl:param name="body.font.family">Times</xsl:param>

    <!-- <xsl:param name="body.font.family" select="'serif'"></xsl:param> -->

    <xsl:param name="insert.xref.page.number" select="'yes'" />

    <!-- We would like to have 1 column Table Of Contents -->
    <xsl:param name="column.count.lot" select="1"></xsl:param>

    <!-- This allows tables and other block objects to wrap across multiple pages -->
    <xsl:attribute-set name="formal.object.properties">
        <xsl:attribute name="keep-together.within-column">auto</xsl:attribute>
    </xsl:attribute-set>

    <!-- Set left and right margins -->
    <xsl:param name="page.margin.inner">2.0cm</xsl:param>
    <xsl:param name="page.margin.outer">1cm</xsl:param>

    <!-- Don't indent the body text, as it wastes paper! -->
    <xsl:param name="body.start.indent">0pt</xsl:param>
    
    <!-- FOP uses this param, see http://docbook.sourceforge.net/release/xsl/current/doc/fo/body.start.indent.html -->
    <xsl:param name="title.margin.left">0pt</xsl:param>

    <!-- Display URLs as footnotes, rather than inline -->
    <xsl:param name="ulink.footnotes">1</xsl:param>
    
    <!-- Add some space above the revision history table -->
    <xsl:attribute-set name="revhistory.table.properties">
        <xsl:attribute name="space-before.minimum">1em</xsl:attribute>
        <xsl:attribute name="space-before.minimum">1em</xsl:attribute>
        <xsl:attribute name="border-top-style">solid</xsl:attribute>
        <xsl:attribute name="border-top-width">thin</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- Make xrefs and links blue -->
    <xsl:attribute-set name="xref.properties">
        <xsl:attribute name="color">blue</xsl:attribute>
    </xsl:attribute-set>

    <!-- Make revision history header entries bold -->
    <xsl:attribute-set name="revhistory.table.cell.properties">
       	<xsl:attribute name="text-align">left</xsl:attribute> 
	<xsl:attribute name="font-size">8pt</xsl:attribute>
        <xsl:attribute name="padding-right">8pt</xsl:attribute>
        <xsl:attribute name="padding-bottom">8pt</xsl:attribute>
    </xsl:attribute-set>
    
    <xsl:attribute-set name="revhistory.table.cell.block.properties">
        <xsl:attribute name="padding-left">8pt</xsl:attribute>
	<xsl:attribute name="border-left-style">solid</xsl:attribute>
        <xsl:attribute name="border-left-width">thin</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- Custom Revision History template -->
    <!-- Do not display the "Revision History" title -->
    <xsl:template match="revhistory" mode="titlepage.mode">
      <xsl:variable name="explicit.table.width">
        <xsl:call-template name="dbfo-attribute">
          <xsl:with-param name="pis"
                          select="processing-instruction('dbfo')"/>
          <xsl:with-param name="attribute" select="'table-width'"/>
        </xsl:call-template>
      </xsl:variable>
    
      <xsl:variable name="table.width">
        <xsl:choose>
          <xsl:when test="$explicit.table.width != ''">
            <xsl:value-of select="$explicit.table.width"/>
          </xsl:when>
          <xsl:when test="$default.table.width = ''">
            <xsl:text>100%</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="$default.table.width"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
   
 
      <fo:block xsl:use-attribute-sets="revhistory.title.properties">
            <xsl:call-template name="gentext">
              <xsl:with-param name="key" select="'RevHistory'"/>
            </xsl:call-template>
      </fo:block>
      
      <fo:table table-layout="fixed" width="{$table.width}" xsl:use-attribute-sets="revhistory.table.properties">
        <fo:table-column column-number="1" column-width="proportional-column-width(1)"/>
        <fo:table-column column-number="2" column-width="proportional-column-width(1)"/>
        <fo:table-column column-number="3" column-width="proportional-column-width(2)"/>
        <fo:table-column column-number="4" column-width="proportional-column-width(5)"/>
        <fo:table-body start-indent="0pt" end-indent="0pt" space-before="8pt">
          <xsl:apply-templates mode="titlepage.mode"/>
        </fo:table-body>
      </fo:table>
    </xsl:template>

    
    
    <!-- Customized Revision History / Revision template -->
    <!-- Reference the attribute set revhistory.revremark.table.cell.properties -->
    <!-- Copied from xslt/fo/titlepage.xsl -->
    <xsl:template match="revhistory/revision" mode="titlepage.mode">
        <xsl:variable name="revnumber" select="revnumber"/>
        <xsl:variable name="revdate"   select="date"/>
        <xsl:variable name="revauthor" select="authorinitials|author"/>
        <xsl:variable name="revremark" select="revremark|revdescription"/>
	<fo:table-row>
    	    <fo:table-cell xsl:use-attribute-sets="revhistory.table.cell.properties">
                <fo:block>
                    <xsl:apply-templates select="$revdate[1]" mode="titlepage.mode"/>
                </fo:block>
            </fo:table-cell>
    	    <fo:table-cell xsl:use-attribute-sets="revhistory.table.cell.properties">
                <fo:block xsl:use-attribute-sets="revhistory.table.cell.block.properties">
                    <xsl:apply-templates select="$revnumber" mode="titlepage.mode"/>
		</fo:block>
	    </fo:table-cell>
            <fo:table-cell xsl:use-attribute-sets="revhistory.table.cell.properties">
                <fo:block xsl:use-attribute-sets="revhistory.table.cell.block.properties">
                    <xsl:for-each select="$revauthor">
                        <xsl:apply-templates select="." mode="titlepage.mode"/>
                        <xsl:if test="position() != last()">
                            <xsl:text>, </xsl:text>
                        </xsl:if>
                    </xsl:for-each>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell xsl:use-attribute-sets="revhistory.table.cell.properties">
                <fo:block xsl:use-attribute-sets="revhistory.table.cell.block.properties">
                    <xsl:for-each select="$revremark">
                        <xsl:apply-templates select="." mode="titlepage.mode"/>
                        <xsl:if test="position() != last()">
                            <xsl:text>, </xsl:text>
                        </xsl:if>
                    </xsl:for-each>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>


    <!-- Add support for revision history in its own appendix -->
    <xsl:template match="appendix[@id='revhistory']/para">
        <xsl:apply-templates select="//revhistory" mode="titlepage.mode"/>
    </xsl:template>

    <!-- Add support for revision history in its own preface -->
    <xsl:template match="preface[@id='revhistory']/para">
        <xsl:apply-templates select="//revhistory" mode="titlepage.mode"/>
    </xsl:template>


    <!-- Add support for newtext class -->
    <xsl:template match="para[@role='newtext']">
        <fo:block xsl:use-attribute-sets="normal.para.spacing"
            background-color="#ffccff">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <xsl:template match="emphasis[@role='newtext']">
        <fo:inline
            background-color="#ffccff">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>

    <!-- Add support for deletedtext class -->
    <xsl:template match="para[@role='deletedtext']">
        <fo:block xsl:use-attribute-sets="normal.para.spacing"
            background-color="#ffff88"
            text-decoration="line-through">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <xsl:template match="emphasis[@role='deletedtext']">
        <fo:inline
            background-color="#ffff88"
            text-decoration="line-through">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>


    <!-- Add support for commenttext class -->
    <xsl:template match="para[@role='commenttext']">
        <fo:block xsl:use-attribute-sets="normal.para.spacing"
            background-color="#88ff88"
            font-style="italic">
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

    <xsl:template match="emphasis[@role='commenttext']">
        <fo:inline
            background-color="#88ff88"
            font-style="italic">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>
    
    <!-- add support for different font sizes in monospaced areas
      For instance: <programlisting><?db-font-size 75% ?># A long line listing
    -->
    <xsl:attribute-set name="monospace.verbatim.properties">
      <xsl:attribute name="font-size">
        <xsl:choose>
          <xsl:when test="processing-instruction('db-font-size')">
            <xsl:value-of select="processing-instruction('db-font-size')"/>
          </xsl:when>
          <xsl:otherwise>inherit</xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </xsl:attribute-set>
 
 <!-- 
    <xsl:attribute-set name="monospace.verbatim.properties">
      <xsl:attribute name="font-size">75%</xsl:attribute>
    </xsl:attribute-set>
-->

    <!-- make footnotes a little bit nicer -->
    <xsl:template name="format.footnote.mark">
      <xsl:param name="mark" select="'?'"/>
        <fo:inline baseline-shift="super"
                   font-size="{$body.font.master*0.8}pt"
                   vertical-align="super">
        <xsl:copy-of select="$mark"/>
      </fo:inline>
    </xsl:template>
    
  <xsl:attribute-set name="component.title.properties">
    <xsl:attribute name="keep-with-next.within-column">always</xsl:attribute>
    <xsl:attribute name="space-before.optimum"><xsl:value-of select="concat($body.font.master, 'pt')"></xsl:value-of></xsl:attribute>
    <xsl:attribute name="space-before.minimum"><xsl:value-of select="concat($body.font.master, 'pt * 0.8')"></xsl:value-of></xsl:attribute>
    <xsl:attribute name="space-before.maximum"><xsl:value-of select="concat($body.font.master, 'pt * 1.2')"></xsl:value-of></xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="text-align">
      <xsl:choose>
        <xsl:when test="((parent::article | parent::articleinfo | parent::info/parent::article) and not(ancestor::book) and not(self::bibliography))         or (parent::slides | parent::slidesinfo)">center</xsl:when>
        <xsl:when test="parent::chapter" >right</xsl:when>
        <xsl:otherwise>left</xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
    <xsl:attribute name="start-indent"><xsl:value-of select="$title.margin.left"></xsl:value-of></xsl:attribute>
    <xsl:attribute name="border-bottom-color">orange</xsl:attribute>
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <xsl:attribute name="border-bottom-width">4pt</xsl:attribute>
    <xsl:attribute name="space-after">
      <xsl:choose>
        <xsl:when test="parent::chapter"><xsl:value-of select="concat($body.font.master, 'pt * 3')"></xsl:value-of></xsl:when>
        <xsl:otherwise><xsl:value-of select="concat($body.font.master, 'pt')"></xsl:value-of></xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:attribute-set>

  <!--
  <xsl:attribute-set name="footer.content.properties">
    <xsl:attribute name="background-color">orange</xsl:attribute>
    <xsl:attribute name="font-family">
      <xsl:value-of select="$body.fontset"/>
    </xsl:attribute>
    <xsl:attribute name="margin-left">
      <xsl:value-of select="$title.margin.left"/>
    </xsl:attribute>
  </xsl:attribute-set>
  -->
  
  <xsl:attribute-set name="chap.label.properties">
    <xsl:attribute name="font-size">80pt</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="chap.title.properties">
  </xsl:attribute-set>

<xsl:param name="formal.title.placement">
figure after
example after
equation before
table before
procedure before
</xsl:param>

  <!-- This PI gives you the ability to set a line break anywhere in your text
       bla <? lb ?> text ... -->
  <xsl:template match="processing-instruction('lb')">
    <fo:block/>
  </xsl:template>

  <!-- Multi column printing with XEP
       XEP has the ability to layout parts of a document multi column.
       To use this I have invented this non standard tag 'multi-column'.
       The format is very limited (because it's not used at the moment),
       but I keep it here for reference. To use it, write:
       <multi-column columns="2" gap="18pt">
         <para>...</para>
       </multi-column>
       It does only work in book documents and if the <rx:flow-section> element
       is a direct child of <fo:flow> (limitation of XEP) 
   -->
  <xsl:template match="multi-column">
    <rx:flow-section>
      <xsl:attribute name="column-count">
        <xsl:value-of select="@columns" />
      </xsl:attribute>
      <xsl:attribute name="column-gap">
        <xsl:value-of select="@gap" />
      </xsl:attribute>
      <xsl:apply-templates />
    </rx:flow-section>
  </xsl:template>

</xsl:stylesheet>
