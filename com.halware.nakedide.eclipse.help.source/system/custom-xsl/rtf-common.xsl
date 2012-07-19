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

  <xsl:import href="../docbook-xsl/fo/docbook.xsl"/>
  <xsl:import href="common-custom.xsl"/>

  <!-- Avoids FOP freeze.

       Per default, draft.mode is 'maybe', which causes the creation of both
       the draft and final pagemasters in the fo file.  Every draft
       pagemaster is going to wait for the draft image - hence FOP appears to
       freeze.

       The default of 'maybe' allows to define draft-mode on a per-element
       base, using the standard docbook attribute 'status="draft"' to
       indicate draft mode.
  -->
  <xsl:param name="draft.mode" select="'no'"/>


</xsl:stylesheet>