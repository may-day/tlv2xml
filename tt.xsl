<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:template match="/">
    <html>
      <body bgcolor="#FFFFFF">
        <h2><xsl:value-of select="/lv/Head/proposalName1" /></h2>
        <h2><xsl:value-of select="/lv/Head/proposalName2" /></h2>
        <table border="1">
          <tr>
            <th>Kenn</th>
            <th>OZ</th>
            <th>BNR</th>
            <th>Bezeichnung</th>
            <th>WGR</th>
            <th>Lieferant</th>
            <th>LBNR</th>
            <th>Hersteller</th>
            <th>Menge</th>
            <th>Einheit</th>
            <th>Kurztext</th>
            <th>Langtext</th>
            <th>Netto</th>
            <th>MG</th>
            <th>MATEK</th>
            <th>MATVK</th>
            <th>MATFAK</th>
            <th>MATPREIS</th>
            <th>LG</th>
            <th>LOHNEK</th>
            <th>LOHNVK</th>
            <th>LOHNFAK</th>
            <th>LOHNPREIS</th>
            <th>ZUABFAK</th>
          </tr>
          <xsl:apply-templates/>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="position">
    <tr>
      <td><xsl:value-of select="@kind" /></td>
      <td><xsl:value-of select="oz" /></td>
      <td><xsl:value-of select="ordernumber" /></td>
      <td><xsl:value-of select="name" /></td>
      <td><xsl:value-of select="goodsgroup" /></td>
      <td><xsl:value-of select="supplier" /></td>
      <td><xsl:value-of select="suppliernumber" /></td>
      <td><xsl:value-of select="manufacturer" /></td>
      <td><xsl:value-of select="calc/amount" /></td>
      <td><xsl:value-of select="unit" /></td>
      <td><xsl:value-of select="shortText" /></td>
      <td><xsl:value-of select="longText" /></td>
      <td><xsl:value-of select="calc/netpos" /></td>
      <td><xsl:value-of select="calc/materialGroup" /></td>
      <td><xsl:value-of select="calc/materialPurchasePrice" /></td>
      <td><xsl:value-of select="calc/materialSellingPrice" /></td>
      <td><xsl:value-of select="calc/materialFactor" /></td>
      <td><xsl:value-of select="calc/materialUnitprice_wo_addons" /></td>
      <td><xsl:value-of select="calc/wageGroup" /></td>
      <td><xsl:value-of select="calc/wagePurchasePrice" /></td>
      <td><xsl:value-of select="calc/wageSellingPrice" /></td>
      <td><xsl:value-of select="calc/wageFactor" /></td>
      <td><xsl:value-of select="calc/wageUnitprice" /></td>
      <td><xsl:value-of select="calc/surchargeFactor" /></td>
    </tr>
  </xsl:template>

<xsl:template match="Head|Calc|Text">
</xsl:template>


</xsl:stylesheet>
