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

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:ximy="http://www.agynamix.de/XincludeExtension"
    exclude-result-prefixes="ximy"
>

  <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"
              doctype-public="-//OASIS//DTD DocBook XML V4.4//EN"
              doctype-system="http://www.oasis-open.org/docbook/xml/4.4/docbookx.dtd" />

  <xsl:template match="*|/">
    <xsl:choose>
      <xsl:when test="local-name()">
        <xsl:element name="{local-name()}">
          <xsl:copy-of select="@*" />
          <xsl:apply-templates/>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy>
          <xsl:copy-of select="@*" />
          <xsl:apply-templates/>
        </xsl:copy>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="ximy:includelist">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="processing-instruction()">
    <xsl:copy/>
  </xsl:template>

</xsl:stylesheet>
