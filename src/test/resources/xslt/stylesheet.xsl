<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="xml" />
    
    <xsl:param name="data" />
    
    <xsl:template match="/document">
        <xsl:element name="document">
            <xsl:copy-of select="document($data)/nodes/node" />
        </xsl:element>
    </xsl:template>
    
</xsl:stylesheet>