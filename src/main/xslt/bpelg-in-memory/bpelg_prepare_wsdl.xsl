<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
    <xsl:param name="deletePattern"/>
    <xsl:output indent="yes" method="xml"/>

    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="wsdl:operation[@name=$deletePattern]"/>

</xsl:stylesheet>