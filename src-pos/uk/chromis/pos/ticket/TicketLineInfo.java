//    Chromis POS  - The New Face of Open Source POS
//    Copyright (c) (c) 2015-2016uniCenta
//    http://www.chromis.co.uk
//
//    This file is part of Chromis POS
//
//     Chromis POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Chromis POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Chromis POS.  If not, see <http://www.gnu.org/licenses/>.
package uk.chromis.pos.ticket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Properties;
//import sun.rmi.runtime.Log;
import uk.chromis.basic.BasicException;
import uk.chromis.data.loader.DataRead;
import uk.chromis.data.loader.DataWrite;
import uk.chromis.data.loader.SerializableRead;
import uk.chromis.data.loader.SerializableWrite;
import uk.chromis.format.Formats;
import uk.chromis.pos.forms.AppLocal;
import uk.chromis.pos.util.StringUtils;

public class TicketLineInfo implements SerializableWrite, SerializableRead, Serializable {

    private static final long serialVersionUID = 6608012948284450199L;
    private String m_sTicket;
    private int m_iLine;
    private double multiply;
    //*** Inicio modificaciones RndMnkIII
    //private double price;    
    private BigDecimal priceBD;
    //private BigDecimal refundQtyBD;
    //private BigDecimal orderQtyBD;
    //*** Fin modificaciones RndMnkIII
    
    private TaxInfo tax;
    private Properties attributes;
    private String productid;
    private String attsetinstid;
    private Boolean updated = false;
    private Double refundQty;
    private Double orderQty;

    public TicketLineInfo(String productid, double dMultiply, double dPrice, TaxInfo tax, Properties props) {
        init(productid, null, dMultiply, dPrice, tax, props, 0.0);
    }

    public TicketLineInfo(String productid, double dMultiply, double dPrice, TaxInfo tax) {
        init(productid, null, dMultiply, dPrice, tax, new Properties(), 0.0);
    }

    public TicketLineInfo(String productid, String productname, double dMultiply, double dPrice, String producttaxcategory, TaxInfo tax) {
        Properties props = new Properties();
        props.setProperty("product.name", productname);
        props.setProperty("product.taxcategoryid", producttaxcategory);
        init(productid, null, dMultiply, dPrice, tax, props, 0.0);
    }

    public TicketLineInfo(String productid, String productname, String producttaxcategory, double dMultiply, double dPrice, TaxInfo tax) {
        Properties props = new Properties();
        props.setProperty("product.name", productname);
        props.setProperty("product.taxcategoryid", producttaxcategory);
        init(productid, null, dMultiply, dPrice, tax, props, 0.0);
    }

    public TicketLineInfo(String productname, String producttaxcategory, double dMultiply, double dPrice, TaxInfo tax) {
        Properties props = new Properties();
        props.setProperty("product.name", productname);
        props.setProperty("product.taxcategoryid", producttaxcategory);
        init(null, null, dMultiply, dPrice, tax, props, 0.0);
    }

    public TicketLineInfo() {
        init(null, null, 0.0, 0.0, null, new Properties(), 0.0);
    }

    public TicketLineInfo(ProductInfoExt product, double dMultiply, double dPrice, TaxInfo tax, Properties attributes) {
        String pid;

        if (product == null) {
            pid = null;
        } else {
            pid = product.getID();

            attributes.setProperty("product.name", product.getName());
            attributes.setProperty("product.code", product.getCode() == null ? "" : product.getCode());
            attributes.setProperty("product.com", product.isCom() ? "true" : "false");
            attributes.setProperty("product.kitchen", product.isKitchen() ? "true" : "false");
            attributes.setProperty("product.service", product.isService() ? "true" : "false");
            attributes.setProperty("product.vprice", product.isVprice() ? "true" : "false");
            attributes.setProperty("product.verpatrib", product.isVerpatrib() ? "true" : "false");

            if (product.getTextTip() != null) {
                attributes.setProperty("product.texttip", product.getTextTip());
            }

            attributes.setProperty("product.alwaysavailable", product.getAlwaysAvailable() ? "true" : "false");

            if (product.getAlias() != null) {
                attributes.setProperty("product.alias", product.getAlias());
            }
            attributes.setProperty("product.warranty", product.getWarranty() ? "true" : "false");

            if (product.getAttributeSetID() != null) {
                attributes.setProperty("product.attsetid", product.getAttributeSetID());
            }
            attributes.setProperty("product.taxcategoryid", product.getTaxCategoryID());
            if (product.getCategoryID() != null) {
                attributes.setProperty("product.categoryid", product.getCategoryID());
            }

            attributes.setProperty("product.discounted", product.getDiscounted() == null ? "no" : product.getDiscounted());
            attributes.setProperty("product.candiscount", product.getCanDiscount() ? "true" : "false");

            attributes.setProperty("product.ispack", product.getIsPack() ? "true" : "false");

            if (product.getPackProduct() != null) {
                attributes.setProperty("product.packproduct", product.getPackProduct());
            }
            if (product.getPromotionID() != null) {
                attributes.setProperty("product.promotionid", product.getPromotionID());
            }
            attributes.setProperty("product.promotionadded", "false");
            attributes.setProperty("product.nosc", "0");

        }
        init(pid, null, dMultiply, dPrice, tax, attributes, 0.0);
    }

    public TicketLineInfo(ProductInfoExt oProduct, double dPrice, TaxInfo tax, Properties attributes) {
        this(oProduct, 1.0, dPrice, tax, attributes);
    }

    public TicketLineInfo(TicketLineInfo line) {
        //*** START modificaciones RndMnkIII
        init(line.productid, line.attsetinstid, line.multiply, line.priceBD.doubleValue(),
                line.tax, (Properties) line.attributes.clone(), line.refundQty);
        //*** END modificaciones RndMnkIII
    }

    private void init(String productid, String attsetinstid, double dMultiply,
            Double dPrice, TaxInfo tax, Properties attributes, double refund) {

        this.productid = productid;
        this.attsetinstid = attsetinstid;
        multiply = dMultiply;
        //*** Inicio modificaciones RndMnkIII
        //price = dPrice;
        priceBD=new BigDecimal(String.valueOf(dPrice)).setScale(8,RoundingMode.HALF_UP);
        //*** Fin modificaciones RndMnkIII
        this.tax = tax;
        this.attributes = attributes;
        m_sTicket = null;
        m_iLine = -1;
        refundQty = refund;
    }

    void setTicket(String ticket, int line) {
        m_sTicket = ticket;
        m_iLine = line;
    }

    public String getTicket() {
        return m_sTicket;
    }

    public void setRefundTicket(String ticket, int line) {
        m_sTicket = ticket;
        m_iLine = line;
    }

    @Override
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sTicket);
        dp.setInt(2, m_iLine);
        dp.setString(3, productid);
        dp.setString(4, attsetinstid);
        dp.setDouble(5, multiply);
        //*** Inicio modificaciones RndMnkIII
        //dp.setDouble(6, price);
        dp.setDouble(6, priceBD.doubleValue());
        //*** Fin modificaciones RndMnkIII
        dp.setString(7, tax.getId());
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            attributes.storeToXML(o, AppLocal.APP_NAME, "UTF-8");
            dp.setBytes(8, o.toByteArray());
        } catch (IOException e) {
            dp.setBytes(8, null);
        }
        dp.setDouble(9, refundQty);
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        m_sTicket = dr.getString(1);
        m_iLine = dr.getInt(2);
        productid = dr.getString(3);
        attsetinstid = dr.getString(4);
        multiply = dr.getDouble(5);
        //*** Inicio modificaciones RndMnkIII
        //price = dr.getDouble(6);
        priceBD = new BigDecimal(String.valueOf(dr.getDouble(6))).setScale(8,RoundingMode.HALF_UP);
        //*** Fin modificaciones RndMnkIII
        tax = new TaxInfo(
                dr.getString(7),
                dr.getString(8),
                dr.getString(9),
                dr.getString(10),
                dr.getString(11),
                dr.getDouble(12),
                dr.getBoolean(13),
                dr.getInt(14));
        attributes = new Properties();
        try {
            byte[] img = dr.getBytes(15);
            if (img != null) {
                attributes.loadFromXML(new ByteArrayInputStream(img));
            }
        } catch (IOException e) {
        }
        refundQty = dr.getDouble(16);
    }

    public TicketLineInfo copyTicketLine() {
        TicketLineInfo l = new TicketLineInfo();
        // l.m_sTicket = null;
        // l.m_iLine = -1;
        l.productid = productid;
        l.attsetinstid = attsetinstid;
        l.multiply = multiply;
        //*** Inicio modificaciones RndMnkIII
        //l.price = price;
        l.priceBD = priceBD;
        //*** Fin modificaciones RndMnkIII
        l.tax = tax;
        l.attributes = (Properties) attributes.clone();
        return l;
    }

    // getPromotionId is the Id of the promotion who's script is to be 
    // run if this item is added/removed or changed in a ticket. 
    // The promotion script is also called when the ticket is totaled or
    // subtotaled.
    public String getPromotionId() {
        return attributes.getProperty("product.promotionid");
    }

    // PromotionAdded is a flag indicating this item has been 
    // automatically added to a ticket by a promotion script
    // This item may subsequently be removed from the ticket, for example if it
    // is a free item from a Buy One Get One free script and the original item
    // is subsequently removed.
    public Boolean isPromotionAdded() {
        return "true".equals(attributes.getProperty("product.promotionadded"));
    }

    public void setPromotionAdded(Boolean value) {
        attributes.setProperty("product.promotionadded", value ? "true" : "false");
    }

    public int getTicketLine() {
        return m_iLine;
    }

    public void setTicketLine(int line) {
        m_iLine = line;
    }

    public Double getRefundQty() {
        return refundQty;
    }

    public void setRefundQty(double qty) {
        refundQty = qty;
    }

    public String getProductID() {
        return productid;
    }

    public Double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double qty) {
        orderQty = qty;
    }

    public void setProductID(String value) {
        productid = value;
    }

    public String getProductName() {
        return attributes.getProperty("product.name");
    }

    public String getProductAttSetId() {
        return attributes.getProperty("product.attsetid");
    }

    public String getProductAttSetInstDesc() {
        return attributes.getProperty("product.attsetdesc", "");
    }

    public void setProductAttSetInstDesc(String value) {
        if (value == null) {
            attributes.remove(value);
        } else {
            attributes.setProperty("product.attsetdesc", value);
        }
    }

    public String getProductAttSetInstId() {
        return attsetinstid;
    }

    public void setProductAttSetInstId(String value) {
        attsetinstid = value;
    }

    public boolean isProductCom() {
        return "true".equals(attributes.getProperty("product.com"));
    }

    public String getProductTaxCategoryID() {
        return (attributes.getProperty("product.taxcategoryid"));
    }

    public void setProductTaxCategoryID(String taxID) {
        attributes.setProperty("product.taxcategoryid", taxID);
    }

    public String getProductCategoryID() {
        return (attributes.getProperty("product.categoryid"));
    }

    public double getMultiply() {
        return multiply;
    }

    public void setMultiply(double dValue) {
        multiply = dValue;
    }

    public double getPrice() {
        //*** Inicio modificaciones RndMnkIII
        //return price;
        return priceBD.doubleValue();
        //*** Fin modificaciones RndMnkIII
    }

    public void setPrice(double dValue) {
        //*** Inicio modificaciones RndMnkIII
        //price = dValue;
        priceBD = new BigDecimal(String.valueOf(dValue)).setScale(8,RoundingMode.HALF_UP);
        //*** Fin modificaciones RndMnkIII
    }

    public double getPriceTax() {
        //*** Inicio modificaciones RndMnkIII
        //return price * (1.0 + getTaxRate());
        BigDecimal taxRateBD=new BigDecimal(String.valueOf(getTaxRate())).setScale(8,RoundingMode.HALF_UP);
        BigDecimal priceTaxBD = priceBD.multiply(BigDecimal.ONE.add(taxRateBD)).setScale(8,RoundingMode.HALF_UP);
        return priceTaxBD.setScale(8,RoundingMode.HALF_UP).doubleValue();
        //*** Fin modificaciones RndMnkIII
        
    }

    public void setPriceTax(double dValue) {
        //*** Inicio modificaciones RndMnkIII
        //price = dValue / (1.0 + getTaxRate());
        BigDecimal dValueBD=new BigDecimal(String.valueOf(dValue)).setScale(8,RoundingMode.HALF_UP);
        BigDecimal taxRateBD=new BigDecimal(String.valueOf(getTaxRate())).setScale(8,RoundingMode.HALF_UP);
        priceBD = dValueBD.divide(BigDecimal.ONE.add(taxRateBD),8,RoundingMode.HALF_UP); //OJO IMPORTANTE SETSCALE VALUE
        //System.out.println("Valor priceBD (BD): "+priceBD.toPlainString());
        //System.out.println("Valor priceBD (Double): "+priceBD.doubleValue());
        
        
        //*** Fin modificaciones RndMnkIII
    }

    public TaxInfo getTaxInfo() {
        return tax;
    }

    public void setTaxInfo(TaxInfo value) {
        tax = value;
    }

    public String getProperty(String key) {
        return attributes.getProperty(key);
    }

    public String getProperty(String key, String defaultvalue) {
        return attributes.getProperty(key, defaultvalue);
    }

    public void setProperty(String key, String value) {
        attributes.setProperty(key, value);
    }

    public Properties getProperties() {
        return attributes;
    }

    public double getTaxRate() {
        return tax == null ? 0.0 : tax.getRate();
    }

    public double getSubValue() {
        //*** Inicio modificaciones RndMnkIII
        //return price * multiply;
        BigDecimal multiplyBD=new BigDecimal(String.valueOf(multiply)).setScale(8,RoundingMode.HALF_UP);
        BigDecimal price2BD=priceBD.setScale(8,RoundingMode.HALF_UP);
        return price2BD.multiply(multiplyBD).setScale(8,RoundingMode.HALF_UP).doubleValue();
        //Log(Log.VERBOSE, priceBD.toPlainString());
        //*** Fin modificaciones RndMnkIII
    }

    public double getTax() {
        //*** Inicio modificaciones RndMnkIII
        //return price * multiply * getTaxRate();
        BigDecimal taxRateBD=new BigDecimal(String.valueOf(getTaxRate())).setScale(8,RoundingMode.HALF_UP);
        BigDecimal multiplyBD=new BigDecimal(String.valueOf(multiply)).setScale(8,RoundingMode.HALF_UP);
        return priceBD.multiply(multiplyBD).multiply(taxRateBD).setScale(8,RoundingMode.HALF_UP).doubleValue();
         //*** Fin modificaciones RndMnkIII
    }
    

    public double getValue() {
        //*** Inicio modificaciones RndMnkIII
        //return price * multiply * (1.0 + getTaxRate());
        BigDecimal taxRateBD=new BigDecimal(String.valueOf(getTaxRate())).setScale(8,RoundingMode.HALF_UP);
        BigDecimal multiplyBD=new BigDecimal(String.valueOf(multiply)).setScale(8,RoundingMode.HALF_UP);
        return priceBD.multiply(multiplyBD).multiply(BigDecimal.ONE.add(taxRateBD)).setScale(8,RoundingMode.HALF_UP).doubleValue();
        //*** Fin modificaciones RndMnkIII
    }

    public String printName() {
        //System.out.println("|");
        //System.out.println("+ Name: "+ StringUtils.encodeXML(attributes.getProperty("product.name")));
        return StringUtils.encodeXML(attributes.getProperty("product.name"));
    }

    public String printBarcode() {
        return StringUtils.encodeXML(attributes.getProperty("product.code"));
    }

    public String getBarcode() {
        return attributes.getProperty("product.code");
    }

    public String printMultiply() {
        //System.out.println("|");
        //System.out.println("+ Multiply: "+ multiply);
        return Formats.DOUBLE.formatValue(multiply);
    }

    public String printRefundQty() {
        return Formats.DOUBLE.formatValue(refundQty);
    }

    public String printPrice() {
        //System.out.println("|");
        //System.out.println("+ Price: "+ getPrice());
        //printPriceTax();
        return Formats.CURRENCY.formatValue(getPrice());
    }

    public String printPriceTax() {
        //System.out.println("|");
        //System.out.println("+ PriceTax: "+ getPriceTax());
        return Formats.CURRENCY.formatValue(getPriceTax());
    }

    public String printTax() {
        return Formats.CURRENCY.formatValue(getTax());
    }

    public String printTaxRate() {
        return Formats.PERCENT.formatValue(getTaxRate());
    }

    public String printSubValue() {
        //System.out.println("|");
        //System.out.println("+ SubValue: "+ getSubValue());
        return Formats.CURRENCY.formatValue(getSubValue());
    }

    public String printValue() {
        //System.out.println("|");
        //System.out.println("+ Value: "+ getValue());
        return Formats.CURRENCY.formatValue(getValue());
    }

    public boolean isProductKitchen() {
        return "true".equals(attributes.getProperty("product.kitchen"));
    }

    public boolean isProductService() {
        return "true".equals(attributes.getProperty("product.service"));
    }

    public boolean isProductVprice() {
        return "true".equals(attributes.getProperty("product.vprice"));
    }

    public boolean isProductVerpatrib() {
        return "true".equals(attributes.getProperty("product.verpatrib"));
    }

    public String printTextTip() {
        return attributes.getProperty("product.texttip");
    }

    public boolean isProductWarranty() {
        return "true".equals(attributes.getProperty("product.warranty"));
    }

    public boolean isAlwaysAvailable() {
        return "true".equals(attributes.getProperty("product.alwaysavailable"));
    }

    public boolean canDiscount() {
        return "true".equals(attributes.getProperty("product.candiscount"));
    }

    public String getDiscounted() {
        return (attributes.getProperty("product.discounted"));
    }

    public void setDiscounted(String value) {
        attributes.setProperty("product.discounted", value);
    }

    public String printAlias() {
         return StringUtils.encodeXML(attributes.getProperty("product.alias")); 
    }

    public void setUpdated(Boolean value) {
        updated = value;
    }

    public boolean getUpdated() {
        return updated;
    }

    void incMultiply() {
    }
}
