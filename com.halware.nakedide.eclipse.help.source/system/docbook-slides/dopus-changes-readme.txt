The following changes where made to adapt the stylesheets to the Dopus environment:

Replace all link that point to the docbook repository on the web (like: <xsl:import href="http://docbook.sourceforge.net/release/xsl/current/xhtml/chunk.xsl"/>) with import of the local stylesheets:

<xsl:import href="../../../docbook-xsl/xhtml/chunk.xsl"/>