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

  <xsl:import href="../docbook-xsl/javahelp/javahelp.xsl"/>
  <xsl:import href="html-derived-common.xsl"/>

  <xsl:param name="chunk.first.sections" select="1" />
  <xsl:param name="chunk.section.depth" select="3" />
  <!--<xsl:param name="base.dir" select="'contents/'" />-->
  <xsl:param name="use.id.as.filename" select="1" />
  
  <xsl:param name="table.borders.with.css" select="0" />
  <xsl:param name="table.frame.border.thickness" select="thin" />
  <xsl:param name="table.cell.border.thickness" select="thin" />
  <xsl:param name="table.frame.border.color" select="Orange" />
  <xsl:param name="table.cell.border.color" select="Orange" />

  <xsl:param name="html.cellspacing" select="0" />
  <xsl:param name="html.cellpadding" select="10" />
  <xsl:param name="html.cleanup" select="1" />

  <!--  These are repaired stylesheets for removing special characters from the
        tocitem entries  -->

  <xsl:template match="set" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id">
        <xsl:with-param name="object" select="."/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates select="book" mode="jhtoc"/>
    </tocitem>
  </xsl:template>
  
  <xsl:template match="book" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates select="part|reference|preface|chapter|appendix|article|colophon"
                           mode="jhtoc"/>
    </tocitem>
  </xsl:template>

  <xsl:template match="part|reference|preface|chapter|appendix|article"
                mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates
        select="article|preface|chapter|appendix|refentry|section|sect1"
        mode="jhtoc"/>
    </tocitem>
  </xsl:template>

  <xsl:template match="section" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates select="section" mode="jhtoc"/>
    </tocitem>
  </xsl:template>
  
  <xsl:template match="sect1" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates select="sect2" mode="jhtoc"/>
    </tocitem>
  </xsl:template>
  
  <xsl:template match="sect2" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates select="sect3" mode="jhtoc"/>
    </tocitem>
  </xsl:template>
  
  <xsl:template match="sect3" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates select="sect4" mode="jhtoc"/>
    </tocitem>
  </xsl:template>
  
  <xsl:template match="sect4" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
      <xsl:apply-templates select="sect5" mode="jhtoc"/>
    </tocitem>
  </xsl:template>
  
  <xsl:template match="sect5|colophon|refentry" mode="jhtoc">
    <xsl:variable name="id">
      <xsl:call-template name="object.id"/>
    </xsl:variable>
    <xsl:variable name="title">
      <xsl:apply-templates select="." mode="title.markup"/>
    </xsl:variable>
  
    <tocitem target="{$id}">
      <xsl:attribute name="text">
        <xsl:value-of select="normalize-space($title)"/>
      </xsl:attribute>
    </tocitem>
  </xsl:template>
  

</xsl:stylesheet>