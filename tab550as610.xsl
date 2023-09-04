<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:my="my:my"
  version="1.0"
  >
  <xsl:output method="html" encoding="utf-8" indent="yes" />
  <my:headerfields>
      <Field name="netCalc"/>
      <Field name="currency"/>
      <Field name="ozMask"/>
      <Field name="projectName1"/>
      <Field name="projectName2"/>
      <Field name="clientid"/>
      <!-- <Field name="proposalID"/> -->
      <Field name="deliveryConditions"/>
      <Field name="shippingType"/>
  </my:headerfields>
  <xsl:variable name="headerFields" select="document('')/*/my:headerfields/*" />

  <my:calcpagefields>
      <Field name="calcpageNumber"/>
      <Field name="dateCalcpageModify"/>
      <Field name="userCalcpageModify"/>
      <Field name="calcPageName"/>
      <Field name="mgFact3isSurchargeDiscount"/>
      <Field name="BOQfinalFactor"/>
      <Field name="factorAll"/>
      <Field name="discountAll"/>
      <Field name="factorMaterial"/>
      <Field name="discountMaterial"/>
      <Field name="factorWage"/>
      <Field name="discountWage"/>
      <Field name="discountSpecial"/>
      <!-- <Field name="gross"/> -->
      <Field name="clientDiscount"/>
			<Field name="mode_surcharge_discount"/>
			<Field name="factorLimitMargin"/>
			<Field name="calced_material_cost"/>
			<Field name="calced_material_revenue"/>
			<Field name="calced_wage_cost"/>
			<Field name="calced_wage_revenue"/>
			<Field name="calced_wage_hours"/>
      <!--
			<Field name="fixCostName1"/>
			<Field name="fixCost1"/>
			<Field name="calced_fixCost1_cost"/>
			<Field name="calced_fixCost1_revenue"/>
			<Field name="fixCostName2"/>
			<Field name="fixCost2"/>
			<Field name="calced_fixCost2_cost"/>
			<Field name="calced_fixCost2_revenue"/>
			<Field name="fixCostName3"/>
			<Field name="fixCost3"/>
			<Field name="calced_fixCost3_cost"/>
			<Field name="calced_fixCost3_revenue"/>
			<Field name="fixCostName4"/>
			<Field name="fixCost4"/>
			<Field name="calced_fixCost4_cost"/>
			<Field name="calced_fixCost4_revenue"/>
			<Field name="fixCostName5"/>
			<Field name="fixCost5"/>
			<Field name="calced_fixCost5_cost"/>
			<Field name="calced_fixCost5_revenue"/>
			<Field name="surchargeName1"/>
			<Field name="surchargeFactor1"/>
			<Field name="surchargeFactorProposal1"/>
			<Field name="calced_surcharge1_cost"/>
			<Field name="calced_surcharge1_revenue"/>
			<Field name="surchargeName2"/>
			<Field name="surchargeFactor2"/>
			<Field name="surchargeFactorProposal2"/>
			<Field name="calced_surcharge2_cost"/>
			<Field name="calced_surcharge2_revenue"/>
			<Field name="surchargeName3"/>
			<Field name="surchargeFactor3"/>
			<Field name="surchargeFactorProposal3"/>
			<Field name="calced_surcharge3_cost"/>
			<Field name="calced_surcharge3_revenue"/>
			<Field name="surchargeName4"/>
			<Field name="surchargeFactor4"/>
			<Field name="surchargeFactorProposal4"/>
			<Field name="calced_surcharge4_cost"/>
			<Field name="calced_surcharge4_revenue"/>
			<Field name="surchargeName5"/>
			<Field name="surchargeFactor5"/>
			<Field name="surchargeFactorProposal5"/>
			<Field name="calced_surcharge5_cost"/>
			<Field name="calced_surcharge5_revenue"/>
			<Field name="shippingAbsolute"/>
			<Field name="shippingFactor"/>
			<Field name="calced_shipping_cost"/>
			<Field name="calced_shipping_revenue"/>
			<Field name="handlingAbsolute"/>
			<Field name="handlingFactor"/>
			<Field name="calced_handling_cost"/>
			<Field name="calced_handling_revenue"/>
      -->
			<Field name="calced_discounted_cost"/>
			<Field name="calced_discounted_revenue"/>
			<Field name="calced_all_cost"/>
			<Field name="calced_all_revenue"/>
  </my:calcpagefields>
  <xsl:variable name="calcpageFields" select="document('')/*/my:calcpagefields/*" />

  <my:ogcalcpagefields>
    <Field name="ogName"/>
    <Field name="overheadCostAbsoluteOrFactor"/>
    <Field name="overheadSurchargeFactor"/>
    <Field name="overheadSharing_mark"/>
    <Field name="calced_overhead_cost"/>
    <Field name="calced_overhead_revenue"/>
  </my:ogcalcpagefields>
  <xsl:variable name="ogcalcpageFields" select="document('')/*/my:ogcalcpagefields/*" />

  <my:mgcalcpagefields>
    <Field name="mgName"/>
    <Field name="mgCalcListpriceBased"/>
      <Field name="mgOverheadCostFactor"/>
      <Field name="mgFactor1"/>
      <Field name="mgFactor2"/>
      <Field name="mgFactor3"/>
      <Field name="calced_cost"/>
      <Field name="calced_revenue"/>
      <Field name="warrantyCostCalculation"/>
      <Field name="efbMark"/>
  </my:mgcalcpagefields>
  <xsl:variable name="mgcalcpageFields" select="document('')/*/my:mgcalcpagefields/*" />
  
  <my:wgcalcpagefields>
      <Field name="wgName"/>
      <Field name="wgTimeFactor"/>
      <Field name="wgCostPerHour"/>
      <Field name="wgOverheadCostFactor"/>
      <Field name="wgFactor1"/>
      <Field name="wgFactor2"/>
      <Field name="wgPricePerHourHardCoded"/>
      <Field name="calced_cost"/>
      <Field name="calced_revenue"/>
      <Field name="calced_hours"/>
      <Field name="warrantyCostCalculation"/>
      <Field name="efbMark"/>
  </my:wgcalcpagefields>
  <xsl:variable name="wgcalcpageFields" select="document('')/*/my:wgcalcpagefields/*" />

  <my:posfields>
      <Field name="oz"/>
      <Field name="clientoz"/>
      <Field name="goodsgroup"/>
      <Field name="ordernumber"/>
      <Field name="name"/>
      <Field name="supplier"/>
      <Field name="suppliernumber"/>
      <Field name="manufacturer"/>
      <Field name="unit"/>
      <Field name="circa"/>
      <Field name="formfeed"/>
</my:posfields>
<xsl:variable name="posFields" select="document('')/*/my:posfields/*" />

<my:poscalcfields>
  <Field name="directSellingPrice"/>
      <Field name="netpos"/>
      <Field name="rounding"/>
      <Field name="markZ"/>
      <Field name="materialGroup"/>
      <Field name="materialFactor"/>
      <Field name="materialPurchasePrice"/>
      <Field name="materialPurchaseDiscount"/>
      <Field name="materialSupplierFactor"/>
      <Field name="materialSellingPrice"/>
      <Field name="surchargeFactor"/>
      <Field name="wageGroup"/>
      <Field name="wageFactor"/>
      <Field name="wagePurchasePrice"/>
      <Field name="wageSellingPrice"/>
      <Field name="wageGroup1"/>
      <Field name="wageGroup2"/>
      <Field name="wageGroup3"/>
      <Field name="wageGroup4"/>
      <Field name="wageGroup5"/>
      <Field name="wageMinutes1"/>
      <Field name="wageMinutes2"/>
      <Field name="wageMinutes3"/>
      <Field name="wageMinutes4"/>
      <Field name="wageMinutes5"/>
      <Field name="amount"/>
</my:poscalcfields>

  <xsl:variable name="poscalcFields" select="document('')/*/my:poscalcfields/*" />

  <xsl:variable name="docroot" select="/"/>

  <xsl:template match="/">
    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html&gt;</xsl:text>
    <html>
      <head/>
      <body bgcolor="#FFFFFF">

        <table border="1">
          <caption>Kopfdaten</caption>
          <thead>

            <tr><th scope="col">Name</th><th scope="col">Wert</th></tr>
          </thead>
          <tbody>
          <xsl:for-each select="$headerFields">
          <tr>
            <th scope="row"><xsl:value-of select="@name" /></th>
            <td><xsl:value-of select="$docroot/lv/HEADER/*[name() = current()/@name]" disable-output-escaping="yes" /></td>
          </tr>
        </xsl:for-each>           
      </tbody>
      </table>

      <table border="1">
        <caption>Kalkblatt</caption>
        <thead>

          <tr><th scope="col">Name</th><th scope="col">Wert</th></tr>
        </thead>
        <tbody>
        <xsl:for-each select="$calcpageFields">
        <tr>
          <th scope="row"><xsl:value-of select="@name" /></th>
          <td><xsl:value-of select="$docroot/lv/CALCPAGE/*[name() = current()/@name]" disable-output-escaping="yes" /></td>
        </tr>
      </xsl:for-each>           
    </tbody>
    </table>

    <table border="1">
      <caption>Gemeinkosten</caption>
      <thead>
        <tr>
          <th scope="col">id</th>
          <xsl:for-each select="$ogcalcpageFields">
            <th scope="col"><xsl:value-of select="@name"/></th>
          </xsl:for-each>           
        </tr>
      </thead>
      <tbody>
        <xsl:variable name="ogroot" select="/lv/CALCPAGE"/>
        <tr>
          <td>og01</td>
          <td><xsl:value-of select="$ogroot/fixCostName1" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/fixCost1" disable-output-escaping="yes" /></td>
          <td/>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_fixCost1_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_fixCost1_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og02</td>
          <td><xsl:value-of select="$ogroot/fixCostName2" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/fixCost2" disable-output-escaping="yes" /></td>
          <td/>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_fixCost2_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_fixCost2_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og03</td>
          <td><xsl:value-of select="$ogroot/fixCostName3" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/fixCost3" disable-output-escaping="yes" /></td>
          <td/>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_fixCost3_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_fixCost3_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og04</td>
          <td><xsl:value-of select="$ogroot/fixCostName4" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/fixCost4" disable-output-escaping="yes" /></td>
          <td/>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_fixCost4_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_fixCost4_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og05</td>
          <td><xsl:value-of select="$ogroot/fixCostName5" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/fixCost5" disable-output-escaping="yes" /></td>
          <td/>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_fixCost5_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_fixCost5_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og06</td>
          <td><xsl:value-of select="$ogroot/surchargeName1" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactor1" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactorProposal1" disable-output-escaping="yes" /></td>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_surcharge1_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_surcharge1_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og07</td>
          <td><xsl:value-of select="$ogroot/surchargeName2" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactor2" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactorProposal2" disable-output-escaping="yes" /></td>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_surcharge2_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_surcharge2_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og08</td>
          <td><xsl:value-of select="$ogroot/surchargeName3" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactor3" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactorProposal3" disable-output-escaping="yes" /></td>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_surcharge3_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_surcharge3_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og09</td>
          <td><xsl:value-of select="$ogroot/surchargeName4" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactor4" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactorProposal4" disable-output-escaping="yes" /></td>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_surcharge4_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_surcharge4_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og10</td>
          <td><xsl:value-of select="$ogroot/surchargeName5" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactor5" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/surchargeFactorProposal5" disable-output-escaping="yes" /></td>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_surcharge5_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_surcharge5_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og11</td>
          <xsl:choose>
            <xsl:when test="$ogroot/shippingAbsolute[normalize-space()]"> 
              <td>Fracht</td>
            </xsl:when>
            <xsl:otherwise> 
              <td/>
            </xsl:otherwise> 
          </xsl:choose>
          <td></td>
          <td><xsl:value-of select="$ogroot/shippingAbsolute" disable-output-escaping="yes" /><xsl:value-of select="$ogroot/shippingFactor" disable-output-escaping="yes" /></td>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_shipping_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_shipping_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og12</td>
          <xsl:choose>
            <xsl:when test="$ogroot/handlingAbsolute[normalize-space()]"> 
              <td>Verpackung</td>
            </xsl:when>
            <xsl:otherwise> 
              <td/>
            </xsl:otherwise> 
          </xsl:choose>
          <td></td>
          <td><xsl:value-of select="$ogroot/handlingAbsolute" disable-output-escaping="yes" /><xsl:value-of select="$ogroot/handlingFactor" disable-output-escaping="yes" /></td>
          <td/>
          <td><xsl:value-of select="$ogroot/calced_handling_cost" disable-output-escaping="yes" /></td>
          <td><xsl:value-of select="$ogroot/calced_handling_revenue" disable-output-escaping="yes" /></td>
        </tr>
        <tr>
          <td>og13</td>
          <td>AK</td>
          <td/>
          <td/>
          <td/>
          <td/>
          <td/>
        </tr>
      </tbody>
  </table>

    <table border="1">
      <caption>Kalkblatt Material</caption>
      <thead>
        <tr>
          <th scope="col">id</th>
          <xsl:for-each select="$mgcalcpageFields">
            <th scope="col"><xsl:value-of select="@name"/></th>
          </xsl:for-each>           
        </tr>
      </thead>
      <tbody>
        <xsl:for-each select="/lv/CALCPAGE/MaterialGroups/mg">
          <tr>
          <xsl:variable name="mgroot" select="current()"/>
          <td><xsl:value-of select="$mgroot/@id" disable-output-escaping="yes" /></td>
          <xsl:for-each select="$mgcalcpageFields">
              <td><xsl:value-of select="$mgroot/*[name() = current()/@name]" disable-output-escaping="yes" /></td>
            </xsl:for-each>           
          </tr>
        </xsl:for-each>           
      </tbody>
  </table>

  <table border="1">
    <caption>Kalkblatt Lohn</caption>
    <thead>
      <tr>
        <th scope="col">id</th>
        <xsl:for-each select="$wgcalcpageFields">
          <th scope="col"><xsl:value-of select="@name"/></th>
        </xsl:for-each>
      </tr>
    </thead>
    <tbody>
      <xsl:for-each select="/lv/CALCPAGE/WageGroups/wg">
        <tr>
        <xsl:variable name="wgroot" select="current()"/>
        <td><xsl:value-of select="$wgroot/@id" disable-output-escaping="yes" /></td>
        <xsl:for-each select="$wgcalcpageFields">
            <td><xsl:value-of select="$wgroot/*[name() = current()/@name]" disable-output-escaping="yes" /></td>
          </xsl:for-each>
        </tr>
      </xsl:for-each>
    </tbody>
</table>

<table border="1">
  <caption>Positionen</caption>
  <thead>
    <tr>
      <th scope="col">kind</th>
      <xsl:for-each select="$posFields">
        <th scope="col"><xsl:value-of select="@name"/></th>
      </xsl:for-each>
      <xsl:for-each select="$poscalcFields">
        <th scope="col"><xsl:value-of select="@name"/></th>
      </xsl:for-each>
    </tr>
  </thead>
  <tbody>
    <xsl:for-each select="/lv/POSITIONS/POSITION[contains('12345PUQRSAEMI', @kind)]">
      <tr>
      <xsl:variable name="posroot" select="current()"/>
      <td><xsl:value-of select="$posroot/@kind" disable-output-escaping="yes" /></td>
      <xsl:for-each select="$posFields">
          <td><xsl:value-of select="$posroot/*[name() = current()/@name]" disable-output-escaping="yes" /></td>
        </xsl:for-each>           
        <xsl:for-each select="$poscalcFields">
          <td><xsl:value-of select="$posroot/calc/*[name() = current()/@name]" disable-output-escaping="yes" /></td>
        </xsl:for-each>           
      </tr>
    </xsl:for-each>           
  </tbody>
</table>

    </body>
    </html>
  </xsl:template>


</xsl:stylesheet>
