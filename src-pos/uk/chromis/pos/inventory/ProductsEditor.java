//    Chromis POS  - The New Face of Open Source POS
//    Copyright (c) (c) 2015-2016
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
//
package uk.chromis.pos.inventory;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang.StringUtils;
import uk.chromis.basic.BasicException;
import uk.chromis.data.gui.ComboBoxValModel;
import uk.chromis.data.loader.SentenceList;
import uk.chromis.data.user.DirtyManager;
import uk.chromis.data.user.EditorRecord;
import uk.chromis.format.Formats;
import uk.chromis.pos.forms.AppLocal;
import uk.chromis.pos.forms.DataLogicSales;
import uk.chromis.pos.sales.TaxesLogic;
import uk.chromis.pos.util.BarcodeValidator;
import uk.chromis.pos.ticket.ProductInfoExt;
import uk.chromis.pos.util.AutoCompleteComboBox;

/**
 *
 * @author adrianromero
 */
public final class ProductsEditor extends JPanel implements EditorRecord {

    private final SentenceList m_sentcat;
    private ComboBoxValModel m_CategoryModel;
    private final SentenceList m_sentpromotion;
    private ComboBoxValModel m_PromotionModel;
    private final SentenceList taxcatsent;
    private ComboBoxValModel taxcatmodel;
    private final SentenceList attsent;
    private ComboBoxValModel attmodel;
    private final SentenceList taxsent;
    private TaxesLogic taxeslogic;
    private final ComboBoxValModel m_CodetypeModel;
    private final SentenceList packproductsent;
    private ComboBoxValModel packproductmodel;
    private Object m_id;
    private Object pricesell;
    private boolean priceselllock = false;
    private boolean reportlock = false;
    private BarcodeValidator validate;
    private DataLogicSales m_dlSales;

    /**
     * Creates new form JEditProduct
     *
     * @param dlSales
     * @param dirty
     */
    public ProductsEditor(DataLogicSales dlSales, DirtyManager dirty) {
        initComponents();

        m_dlSales = dlSales;

        // Taxes sentence
        taxsent = dlSales.getTaxList();

        // Categories model
        m_sentcat = dlSales.getCategoriesList();

        m_CategoryModel = new ComboBoxValModel();

        // Promotions model
        m_sentpromotion = dlSales.getPromotionsList();
        m_PromotionModel = new ComboBoxValModel();

        // Taxes model
        taxcatsent = dlSales.getTaxCategoriesList();
        taxcatmodel = new ComboBoxValModel();

        // Attributes model
        attsent = dlSales.getAttributeSetList();
        attmodel = new ComboBoxValModel();

        m_CodetypeModel = new ComboBoxValModel();
        m_CodetypeModel.add(null);
        m_CodetypeModel.add(CodeType.EAN13);
        m_CodetypeModel.add(CodeType.CODE128);
        m_jCodetype.setModel(m_CodetypeModel);
        m_jCodetype.setVisible(true);//RndMnkIII

        // Pack Product model
        packproductsent = dlSales.getPackProductList();
        packproductmodel = new ComboBoxValModel();

        m_jRef.getDocument().addDocumentListener(dirty);
        //((JTextField)m_jCodetype.getEditor().getEditorComponent()).getDocument().addDocumentListener(dirty);
        m_jCode.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jComment.addActionListener(dirty);
        m_jScale.addActionListener(dirty);
        m_jCategory.addActionListener(dirty);
        jComboBoxPromotion.addActionListener(dirty);
        m_jTax.addActionListener(dirty);
        m_jAtt.addActionListener(dirty);
        m_jPriceBuy.getDocument().addDocumentListener(dirty);
        m_jPriceSell.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        m_jstockcost.getDocument().addDocumentListener(dirty);
        m_jstockvolume.getDocument().addDocumentListener(dirty);
        m_jInCatalog.addActionListener(dirty);
        m_jCatalogOrder.getDocument().addDocumentListener(dirty);
        txtAttributes.getDocument().addDocumentListener(dirty);
        m_jKitchen.addActionListener(dirty);
        m_jService.addActionListener(dirty);
        m_jVprice.addActionListener(dirty);
        m_jVerpatrib.addActionListener(dirty);
        m_jTextTip.getDocument().addDocumentListener(dirty);
        m_jDisplay.getDocument().addDocumentListener(dirty);
        m_jStockUnits.getDocument().putProperty(dlSales, 24);

        FieldsManager fm = new FieldsManager();
        m_jPriceBuy.getDocument().addDocumentListener(fm);
        m_jPriceSell.getDocument().addDocumentListener(new PriceSellManager());
        m_jTax.addActionListener(fm);

        m_jIsPack.addActionListener(dirty);
        m_jPackQuantity.getDocument().addDocumentListener(dirty);
        m_jPackProduct.addActionListener(dirty);
        m_jPackProduct.addActionListener(fm);
        m_jPriceSellTax.getDocument().addDocumentListener(new PriceTaxManager());
        m_jmargin.getDocument().addDocumentListener(new MarginManager());
        m_jCheckWarrantyReceipt.addActionListener(dirty);
        m_jGrossProfit.getDocument().addDocumentListener(new MarginManager());
        m_jAlias.getDocument().addDocumentListener(dirty);
        m_jAlwaysAvailable.addActionListener(dirty);
        m_jDiscounted.addActionListener(dirty);

        writeValueEOF();
    }

    /**
     *
     * @throws BasicException
     */
    public void activate() throws BasicException {

        // Load the taxes logic
        taxeslogic = new TaxesLogic(taxsent.list());

        m_CategoryModel = new ComboBoxValModel(m_sentcat.list());
        m_jCategory.setModel(m_CategoryModel);

        m_PromotionModel = new ComboBoxValModel(m_sentpromotion.list());
        jComboBoxPromotion.setModel(m_PromotionModel);

        taxcatmodel = new ComboBoxValModel(taxcatsent.list());
        m_jTax.setModel(taxcatmodel);

        attmodel = new ComboBoxValModel(attsent.list());
        attmodel.add(0, null);
        m_jAtt.setModel(attmodel);

        packproductmodel = new ComboBoxValModel(packproductsent.list());
        m_jPackProduct.setModel(packproductmodel);

        AutoCompleteComboBox.enable(m_jPackProduct);
    }
    
/**
 * Cálculo del código de control
 */
private int controlCodeCalculator(String firstTwelveDigits)
{
     char[] charDigits = firstTwelveDigits.toCharArray();
     int[] ean13 =
     {
        1, 3
     };
     int sum = 0;
     for(int i = 0; i < charDigits.length; i++)
     {
         sum += Character.getNumericValue(charDigits[i]) * ean13[i % 2];
     }
     int checksum = 10 - sum % 10;

     if(checksum == 10)
         checksum = 0;

     return checksum;
}    

// Set the product to be edited.  
    public void setProduct(String productID, String barcode) {
        
        try {
            if (productID != null) {
                ProductInfoExt info = m_dlSales.getProductInfo(productID);

                Object[] myprod = new Object[DataLogicSales.FIELD_COUNT];
                
                m_jCodetype.setSelectedIndex(0);
                m_jCodetype.getSelectedItem();
                if(info.getCodetype().equals("EAN13")){
                    m_jCodetype.setSelectedItem(CodeType.EAN13);
                    m_jCodetype.getSelectedItem();
                }
                if(info.getCodetype().equals("CODE128")){
                    m_jCodetype.setSelectedItem(CodeType.CODE128);
                    m_jCodetype.getSelectedItem();
                }

                m_id = productID;
                m_jRef.setText(info.getReference());
                m_jCode.setText(info.getCode());            
                m_jName.setText(info.getName());
                m_jComment.setSelected(info.isCom());
                m_jScale.setSelected(info.isScale());
                m_jPriceBuy.setText(Formats.CURRENCY.formatValue(info.getPriceBuy()));
                m_jPriceSell.setText(Formats.CURRENCY.formatValue(info.getPriceSell()));
                setPriceSell(info.getPriceSell());
                m_CategoryModel.setSelectedKey(info.getCategoryID());
                m_PromotionModel.setSelectedKey(info.getPromotionID());
                taxcatmodel.setSelectedKey(info.getTaxCategoryID());
                attmodel.setSelectedKey(info.getAttributeSetID());
                m_jImage.setImage(info.getImage());
                m_jstockcost.setText(Formats.CURRENCY.formatValue(info.getStockCost()));
                m_jstockvolume.setText(Formats.DOUBLE.formatValue(info.getStockVolume()));
                m_jInCatalog.setSelected(info.getInCatalog());
                m_jCatalogOrder.setText(Formats.INT.formatValue(info.getCatOrder()));
                m_jKitchen.setSelected(info.isKitchen());
                m_jService.setSelected(info.isService());
                m_jDisplay.setText(info.getDisplay());
                m_jVprice.setSelected(info.isVprice());
                m_jVerpatrib.setSelected(info.isVerpatrib());
                m_jTextTip.setText(info.getTextTip());
                m_jCheckWarrantyReceipt.setSelected(info.getWarranty());
                m_jStockUnits.setText(Formats.DOUBLE.formatValue(info.getStockUnits()));
                m_jAlias.setText(info.getAlias());
                m_jAlwaysAvailable.setSelected(info.getAlwaysAvailable());
                m_jDiscounted.setSelected(info.getCanDiscount());
                m_jIsPack.setSelected(info.getIsPack());
                m_jPackQuantity.setText(Formats.DOUBLE.formatValue(info.getPackQuantity()));
                packproductmodel.setSelectedKey(info.getPromotionID());
            } else if (barcode != null) {
                m_jRef.setText(barcode);
                m_jCode.setText(barcode);
            }
        } catch (BasicException ex) {
            Logger.getLogger(ProductsEditor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getID() {
        return (String) m_id;
    }

    // Save the currently edited product.  
    public void saveProduct() {
    }

    /**
     *
     */
    @Override
    public void refresh() {
    }

    /**
     *
     */
    @Override
    public void writeValueEOF() {

        reportlock = true;

        m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
        m_id = null;
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jCodetype.setSelectedItem(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        m_PromotionModel.setSelectedKey(null);
        jCheckBoxPromotion.setSelected(false);
        taxcatmodel.setSelectedKey(null);
        attmodel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        setPriceSell(null);
        m_jImage.setImage(null);
        m_jstockcost.setText(null);
        m_jstockvolume.setText(null);
        m_jInCatalog.setSelected(false);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        m_jKitchen.setSelected(false);
        m_jService.setSelected(false);
        m_jDisplay.setText(null);
        m_jVprice.setSelected(false);
        m_jVerpatrib.setSelected(false);
        m_jTextTip.setText(null);
        m_jCheckWarrantyReceipt.setSelected(false);
        m_jStockUnits.setVisible(false);
        m_jAlias.setText(null);
        m_jAlwaysAvailable.setSelected(false);
        m_jDiscounted.setSelected(false);

        reportlock = false;

        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jCodetype.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        jComboBoxPromotion.setEnabled(false);
        jCheckBoxPromotion.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jAtt.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
        m_jKitchen.setEnabled(false);
        m_jService.setEnabled(false);
        m_jDisplay.setEnabled(false);
        m_jVprice.setEnabled(false);
        m_jVerpatrib.setEnabled(false);
        m_jTextTip.setEnabled(false);
        m_jCheckWarrantyReceipt.setEnabled(false);
        m_jStockUnits.setVisible(false);
        m_jAlias.setEnabled(false);
        m_jAlwaysAvailable.setEnabled(false);
        m_jIsPack.setEnabled(false);
        m_jPackQuantity.setEnabled(false);
        packproductmodel.setSelectedKey(null);
        m_jPackProduct.setEnabled(false);
        jLabelPackQuantity.setEnabled(false);
        jLabelPackProduct.setEnabled(false);

        m_jDiscounted.setEnabled(false);

        calculateMargin();
        calculatePriceSellTax();
        calculateGP();
    }

    /**
     *
     */
    @Override
    public void writeValueInsert() {
        reportlock = true;

        m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
        m_id = UUID.randomUUID().toString();
        m_jRef.setText(null);
        m_jCode.setText(null);
        m_jCodetype.setSelectedItem(null);
        m_jName.setText(null);
        m_jComment.setSelected(false);
        m_jScale.setSelected(false);
        m_CategoryModel.setSelectedKey(null);
        m_PromotionModel.setSelectedKey(null);
        jCheckBoxPromotion.setSelected(false);
        taxcatmodel.setSelectedKey(null);
        attmodel.setSelectedKey(null);
        m_jPriceBuy.setText(null);
        setPriceSell(null);
        m_jImage.setImage(null);
        m_jstockcost.setText("0.00");
        m_jstockvolume.setText("0.00");
        m_jInCatalog.setSelected(true);
        m_jCatalogOrder.setText(null);
        txtAttributes.setText(null);
        m_jKitchen.setSelected(false);
        m_jService.setSelected(false);
        m_jDisplay.setText(null);
        m_jVprice.setSelected(false);
        m_jVerpatrib.setSelected(false);
        m_jTextTip.setText(null);
        m_jCheckWarrantyReceipt.setSelected(false);
        m_jStockUnits.setVisible(false);
        m_jAlias.setText(null);
        m_jAlwaysAvailable.setSelected(false);
        m_jDiscounted.setSelected(true);

        reportlock = false;

        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jCodetype.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        jCheckBoxPromotion.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jAtt.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true);
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(true);
        m_jKitchen.setEnabled(true);
        m_jService.setEnabled(true);
        m_jDisplay.setEnabled(true);
        m_jVprice.setEnabled(true);
        m_jVerpatrib.setEnabled(true);
        m_jTextTip.setEnabled(true);
        m_jCheckWarrantyReceipt.setEnabled(true);
        m_jStockUnits.setVisible(false);
        m_jAlias.setEnabled(true);
        m_jAlwaysAvailable.setEnabled(true);
        m_jIsPack.setEnabled(true);
        m_jPackQuantity.setEnabled(false);
        m_jPackProduct.setEnabled(false);
        jLabelPackQuantity.setEnabled(false);
        jLabelPackProduct.setEnabled(false);

        m_jDiscounted.setEnabled(true);

        m_jIsPack.setSelected(false);
        m_jPackQuantity.setText(null);
        packproductmodel.setSelectedKey(null);

        calculateMargin();
        calculatePriceSellTax();
        calculateGP();

    }

    private void extractValues(Object[] myprod) {
        m_jTitle.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_NAME]));
        m_id = myprod[DataLogicSales.INDEX_ID];
        m_jRef.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_REFERENCE]));
        m_jCode.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_CODE]));
        
        m_jCodetype.setSelectedIndex(0);
        m_jCodetype.getSelectedItem();
        if(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_CODETYPE]).equals("EAN13")){
            m_jCodetype.setSelectedItem(CodeType.EAN13);
            m_jCodetype.getSelectedItem();
        }
        if(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_CODETYPE]).equals("CODE128")){
            m_jCodetype.setSelectedItem(CodeType.CODE128);
            m_jCodetype.getSelectedItem();
        }       
        
        m_jName.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_NAME]));
        m_jComment.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISCOM]));
        m_jScale.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISSCALE]));
        m_jPriceBuy.setText(Formats.CURRENCY.formatValue(myprod[DataLogicSales.INDEX_PRICEBUY]));
        setPriceSell(myprod[DataLogicSales.INDEX_PRICESELL]);
        m_CategoryModel.setSelectedKey(myprod[DataLogicSales.INDEX_CATEGORY]);

        Object prom = myprod[DataLogicSales.INDEX_PROMOTIONID];
        if (prom == null) {
            jComboBoxPromotion.setEnabled(false);
            jCheckBoxPromotion.setSelected(false);
        } else {
            jComboBoxPromotion.setEnabled(true);
            jCheckBoxPromotion.setSelected(true);
        }
        m_PromotionModel.setSelectedKey(prom);

        taxcatmodel.setSelectedKey(myprod[DataLogicSales.INDEX_TAXCAT]);
        attmodel.setSelectedKey(myprod[DataLogicSales.INDEX_ATTRIBUTESET_ID]);
        m_jImage.setImage((BufferedImage) myprod[DataLogicSales.INDEX_IMAGE]);
        m_jstockcost.setText(Formats.CURRENCY.formatValue(myprod[DataLogicSales.INDEX_STOCKCOST]));
        m_jstockvolume.setText(Formats.DOUBLE.formatValue(myprod[DataLogicSales.INDEX_STOCKVOLUME]));
        m_jInCatalog.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISCATALOG]));
        m_jCatalogOrder.setText(Formats.INT.formatValue(myprod[DataLogicSales.INDEX_CATORDER]));
        txtAttributes.setText(Formats.BYTEA.formatValue(myprod[DataLogicSales.INDEX_ATTRIBUTES]));
        
        		if(!txtAttributes.getText().equals("")){
            String cadena=txtAttributes.getText();
            InputStream stream = new ByteArrayInputStream(cadena.getBytes(StandardCharsets.UTF_8));
            Properties prop = new Properties();
            try {
                prop.loadFromXML(stream);
                Enumeration enuKeys = prop.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = prop.getProperty(key);
				System.out.println(key + ": " + value);
			}
            } catch (IOException ex){
                Logger.getLogger(ProductsEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
            prop.list(System.out);
            //System.out.println("* Valor1: "+prop.getProperty("valor1"));
            //System.out.println("* Valor2: "+prop.getProperty("valor2"));
        }
                        
        m_jKitchen.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISKITCHEN]));
        m_jService.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISSERVICE]));
        m_jDisplay.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_DISPLAY]));
        m_jVprice.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISVPRICE]));
        m_jVerpatrib.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISVERPATRIB]));
        m_jTextTip.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_TEXTTIP]));
        m_jCheckWarrantyReceipt.setSelected(((Boolean) myprod[DataLogicSales.INDEX_WARRANTY]));
        m_jStockUnits.setText(Formats.DOUBLE.formatValue(myprod[DataLogicSales.INDEX_STOCKUNITS]));
        m_jAlias.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_ALIAS]));
        m_jAlwaysAvailable.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ALWAYSAVAILABLE]));
        m_jDiscounted.setSelected(((Boolean) myprod[DataLogicSales.INDEX_CANDISCOUNT]));
        m_jIsPack.setSelected(((Boolean) myprod[DataLogicSales.INDEX_ISPACK]));
        m_jPackQuantity.setText(Formats.DOUBLE.formatValue(myprod[DataLogicSales.INDEX_PACKQUANTITY]));
        packproductmodel.setSelectedKey(myprod[DataLogicSales.INDEX_PACKPRODUCT]);

    }

    /**
     *
     * @param value
     */
    @Override
    public void writeValueDelete(Object value) {

        reportlock = true;
        Object[] myprod = (Object[]) value;
        extractValues(myprod);
        m_jTitle.setText(Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_REFERENCE]) + " - "
                + Formats.STRING.formatValue(myprod[DataLogicSales.INDEX_NAME]) + " "
                + AppLocal.getIntString("label.recorddeleted"));
        txtAttributes.setCaretPosition(0);

        reportlock = false;

        // Los habilitados
        m_jRef.setEnabled(false);
        m_jCode.setEnabled(false);
        m_jCodetype.setEnabled(false);
        m_jName.setEnabled(false);
        m_jComment.setEnabled(false);
        m_jScale.setEnabled(false);
        m_jCategory.setEnabled(false);
        jComboBoxPromotion.setEnabled(false);
        jCheckBoxPromotion.setEnabled(false);
        m_jTax.setEnabled(false);
        m_jAtt.setEnabled(false);
        m_jPriceBuy.setEnabled(false);
        m_jPriceSell.setEnabled(false);
        m_jPriceSellTax.setEnabled(false);
        m_jmargin.setEnabled(false);
        m_jImage.setEnabled(false);
        m_jstockcost.setEnabled(false);
        m_jstockvolume.setEnabled(false);
        m_jInCatalog.setEnabled(false);
        m_jCatalogOrder.setEnabled(false);
        txtAttributes.setEnabled(false);
        m_jKitchen.setEnabled(false);
        m_jService.setEnabled(true);
        m_jDisplay.setEnabled(false);
        m_jVprice.setEnabled(false);
        m_jVerpatrib.setEnabled(false);
        m_jTextTip.setEnabled(false);
        m_jCheckWarrantyReceipt.setEnabled(false);
        m_jStockUnits.setVisible(false);
        m_jAlias.setEnabled(false);
        m_jAlwaysAvailable.setEnabled(false);
        m_jDiscounted.setEnabled(false);
        m_jIsPack.setEnabled(true);

        m_jPackQuantity.setEnabled(m_jIsPack.isSelected());
        m_jPackProduct.setEnabled(m_jIsPack.isSelected());
        jLabelPackQuantity.setEnabled(m_jIsPack.isSelected());
        jLabelPackProduct.setEnabled(m_jIsPack.isSelected());

        calculateMargin();
        calculatePriceSellTax();
        calculateGP();
    }

    /**
     *
     * @param value
     */
    @Override
    public void writeValueEdit(Object value) {

        reportlock = true;
        Object[] myprod = (Object[]) value;
        extractValues(myprod);

        txtAttributes.setCaretPosition(0);
        reportlock = false;

        // Los habilitados
        m_jRef.setEnabled(true);
        m_jCode.setEnabled(true);
        m_jCodetype.setEnabled(true);
        m_jName.setEnabled(true);
        m_jComment.setEnabled(true);
        m_jScale.setEnabled(true);
        m_jCategory.setEnabled(true);
        jCheckBoxPromotion.setEnabled(true);
        m_jTax.setEnabled(true);
        m_jAtt.setEnabled(true);
        m_jPriceBuy.setEnabled(true);
        m_jPriceSell.setEnabled(true);
        m_jPriceSellTax.setEnabled(true);
        m_jmargin.setEnabled(true);
        m_jImage.setEnabled(true);
        m_jstockcost.setEnabled(true);
        m_jstockvolume.setEnabled(true);
        m_jInCatalog.setEnabled(true);
        m_jCatalogOrder.setEnabled(m_jInCatalog.isSelected());
        txtAttributes.setEnabled(true);
        m_jKitchen.setEnabled(true);
        m_jService.setEnabled(true);
        m_jDisplay.setEnabled(true);
        setButtonHTML();
        m_jVprice.setEnabled(true);
        m_jVerpatrib.setEnabled(true);
        m_jTextTip.setEnabled(true);
        m_jCheckWarrantyReceipt.setEnabled(true);
        m_jStockUnits.setVisible(false);
        m_jAlias.setEnabled(true);
        m_jAlwaysAvailable.setEnabled(true);

        m_jDiscounted.setEnabled(true);

        m_jPackQuantity.setEnabled(m_jIsPack.isSelected());
        m_jPackProduct.setEnabled(m_jIsPack.isSelected());
        jLabelPackQuantity.setEnabled(m_jIsPack.isSelected());
        jLabelPackProduct.setEnabled(m_jIsPack.isSelected());

        m_jDiscounted.setEnabled(true);
        calculateMargin();
        calculatePriceSellTax();
        calculateGP();
    }

    /**
     *
     * @return myprod
     * @throws BasicException
     */
    @Override
    public Object createValue() throws BasicException {
        Object[] myprod = new Object[DataLogicSales.FIELD_COUNT];
        myprod[DataLogicSales.INDEX_ID] = m_id;
        myprod[DataLogicSales.INDEX_REFERENCE] = m_jRef.getText();

        String code = m_jCode.getText();
        if (code.startsWith("977")) {
            // This is an ISSN barcode (news and magazines) 
            // the first 3 digits correspond to the 977 prefix assigned to serial publications, 
            // the next 7 digits correspond to the ISSN of the publication 
            // Anything after that is publisher dependant - we strip everything after  
            // the 10th character 
            code = code.substring(0, 10);
        }
        myprod[DataLogicSales.INDEX_CODE] = code;
        //myprod[DataLogicSales.INDEX_CODETYPE] = BarcodeValidator.BarcodeValidate(m_jCode.getText());
        myprod[DataLogicSales.INDEX_CODETYPE] = m_jCodetype.getSelectedItem().toString();
        myprod[DataLogicSales.INDEX_NAME] = m_jName.getText();
        myprod[DataLogicSales.INDEX_ISCOM] = m_jComment.isSelected();
        myprod[DataLogicSales.INDEX_ISSCALE] = m_jScale.isSelected();
        myprod[DataLogicSales.INDEX_PRICEBUY] = Formats.CURRENCY.parseValue(m_jPriceBuy.getText());
        myprod[DataLogicSales.INDEX_PRICESELL] = pricesell;
        myprod[DataLogicSales.INDEX_CATEGORY] = m_CategoryModel.getSelectedKey();
        myprod[DataLogicSales.INDEX_PROMOTIONID] = m_PromotionModel.getSelectedKey();
        myprod[DataLogicSales.INDEX_TAXCAT] = taxcatmodel.getSelectedKey();
        myprod[DataLogicSales.INDEX_ATTRIBUTESET_ID] = attmodel.getSelectedKey();
        myprod[DataLogicSales.INDEX_IMAGE] = m_jImage.getImage();
        myprod[DataLogicSales.INDEX_STOCKCOST] = Formats.CURRENCY.parseValue(m_jstockcost.getText());
        myprod[DataLogicSales.INDEX_STOCKVOLUME] = Formats.DOUBLE.parseValue(m_jstockvolume.getText());
        myprod[DataLogicSales.INDEX_ISCATALOG] = m_jInCatalog.isSelected();
        myprod[DataLogicSales.INDEX_CATORDER] = Formats.INT.parseValue(m_jCatalogOrder.getText());
        myprod[DataLogicSales.INDEX_ATTRIBUTES] = Formats.BYTEA.parseValue(txtAttributes.getText());
        myprod[DataLogicSales.INDEX_ISKITCHEN] = m_jKitchen.isSelected();
        myprod[DataLogicSales.INDEX_ISSERVICE] = m_jService.isSelected();
        myprod[DataLogicSales.INDEX_DISPLAY] = m_jDisplay.getText();
        myprod[DataLogicSales.INDEX_ISVPRICE] = m_jVprice.isSelected();
        myprod[DataLogicSales.INDEX_ISVERPATRIB] = m_jVerpatrib.isSelected();
        myprod[DataLogicSales.INDEX_TEXTTIP] = m_jTextTip.getText();
        myprod[DataLogicSales.INDEX_WARRANTY] = m_jCheckWarrantyReceipt.isSelected();
        myprod[DataLogicSales.INDEX_STOCKUNITS] = Formats.DOUBLE.parseValue(m_jStockUnits.getText());
        myprod[DataLogicSales.INDEX_ALIAS] = m_jAlias.getText();
        myprod[DataLogicSales.INDEX_ALWAYSAVAILABLE] = m_jAlwaysAvailable.isSelected();
        myprod[DataLogicSales.INDEX_DISCOUNTED] = "no";
        myprod[DataLogicSales.INDEX_CANDISCOUNT] = m_jDiscounted.isSelected();
        myprod[DataLogicSales.INDEX_ISPACK] = m_jIsPack.isSelected();
        myprod[DataLogicSales.INDEX_PACKQUANTITY] = Formats.DOUBLE.parseValue(m_jPackQuantity.getText());
        myprod[DataLogicSales.INDEX_PACKPRODUCT] = packproductmodel.getSelectedKey();

        return myprod;

    }

    /**
     *
     * @return this
     */
    @Override
    public Component getComponent() {
        return this;
    }

    /**
     * Aug 2014 - temporary only! ADD Product now requires a CurrentStock entry
     * record This is experimental whilst developing connex to external hosted
     * DB as need to get online product from its DB. So for now just consume a
     * new DB session. Expensive... (I know!)
     */
    private void setCurrentStock() {

        // connect to the database
        String url = AppLocal.getIntString("db.URL");
        String user = AppLocal.getIntString("db.user");
        String password = AppLocal.getIntString("db.password");

        {
            try {
                // create our java jdbc statement
                try (Connection conn = DriverManager.getConnection(url, "user", "password")) {
                    // create our java jdbc statement
                    Statement statement = conn.createStatement();
                    statement.executeUpdate("INSERT INTO STOCKCURRENT " + "VALUES (1001, 'Simpson', 'Mr.', 'Springfield', 2001)");
                }
            } catch (SQLException e) {
                System.err.println("Got an exception! ");
                System.err.println(e.getMessage());
            }
        }
    }

    private void setCode() {

        Long lDateTime = new Date().getTime(); // USED FOR RANDOM CODE DETAILS

        if (!reportlock) {
            reportlock = true;

            if (m_jRef == null) {
                //m_jCode.setText("0123456789012");
                m_jCode.setText(Long.toString(lDateTime));
            } else if (m_jCode.getText() == null || "".equals(m_jCode.getText())) {
                m_jCode.setText(m_jRef.getText());
            }
            reportlock = false;
        }
    }

    private void setDisplay() {

        String str = (m_jName.getText());
        int length = str.length();

        if (!reportlock) {
            reportlock = true;

            if (length == 0) {
//                m_jDisplay.setText("<html><font size=-3>" + "Need Button Text");
                m_jDisplay.setText(m_jName.getText());
            } else if (m_jDisplay.getText() == null || "".equals(m_jDisplay.getText())) {
                m_jDisplay.setText("<html>" + m_jName.getText());
            }
            reportlock = false;
        }
    }
    
        private void setDisplay2() {

        if (!reportlock) {
            reportlock = true;
            m_jDisplay.setText("<html><font size=-3>" + m_jName.getText()+ "</font><br><font size=-2>" + m_jAlias.getText() + "</font>");
            
            reportlock = false;
        }
    }

    private void setButtonHTML() {

        String str = (m_jDisplay.getText());
        int length = str.length();

        if (!reportlock) {
            reportlock = true;

            if (length == 0) {
                jButtonHTML.setText("Click Me");
            } else {
                jButtonHTML.setText(m_jDisplay.getText());
            }
            reportlock = false;
        }
    }

    private void setTextHTML() {
// TODO - expand m_jDisplay HTML functionality        
    }

    private void calculateMargin() {

        if (!reportlock) {
            reportlock = true;

            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dPriceSell = (Double) pricesell;

            if (dPriceBuy == null || dPriceSell == null) {
                m_jmargin.setText(null);
            } else {
                m_jmargin.setText(Formats.PERCENT.formatValue(dPriceSell / dPriceBuy - 1.0));
            }
            reportlock = false;
        }
    }

    private void calculatePriceSellTax() {

        if (!reportlock) {
            reportlock = true;

            Double dPriceSell = (Double) pricesell;

            if (dPriceSell == null) {
                m_jPriceSellTax.setText(null);
            } else {
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem());
                //*** Inicio modificaciones RndMnkIII
                BigDecimal dTaxRateBD=new BigDecimal(String.valueOf(dTaxRate)).setScale(8,RoundingMode.HALF_UP);
                BigDecimal dPriceSellBD=new BigDecimal(String.valueOf(dPriceSell)).setScale(8,RoundingMode.HALF_UP);
                BigDecimal priceSellTaxBD=dPriceSellBD.multiply(BigDecimal.ONE.add(dTaxRateBD)).setScale(8,RoundingMode.HALF_UP);
                //m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(dPriceSell * (1.0 + dTaxRate)));
                m_jPriceSellTax.setText(Formats.CURRENCY.formatValue(priceSellTaxBD.doubleValue()));
                //*** Fin modificaciones RndMnkIII
            }
            reportlock = false;
        }
    }
    


    private void calculateGP() {

        if (!reportlock) {
            reportlock = true;

            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dPriceSell = (Double) pricesell;

            if (dPriceBuy == null || dPriceSell == null) {
                m_jGrossProfit.setText(null);
            } else {
                m_jGrossProfit.setText(Formats.PERCENT.formatValue((dPriceSell - dPriceBuy) / dPriceSell));
            }
            reportlock = false;
        }
    }

    private void calculatePriceSellfromMargin() {

        if (!reportlock) {
            reportlock = true;

            Double dPriceBuy = readCurrency(m_jPriceBuy.getText());
            Double dMargin = readPercent(m_jmargin.getText());

            if (dMargin == null || dPriceBuy == null) {
                setPriceSell(null);
            } else {
                setPriceSell(dPriceBuy * (1.0 + dMargin));
            }

            reportlock = false;
        }

    }

    private void calculatePriceSellfromPST() {

        if (!reportlock) {
            reportlock = true;

            Double dPriceSellTax = readCurrency(m_jPriceSellTax.getText());

            if (dPriceSellTax == null) {
                setPriceSell(null);
            } else {
                double dTaxRate = taxeslogic.getTaxRate((TaxCategoryInfo) taxcatmodel.getSelectedItem());
                //*** Inicio modificaciones RndMnkIII
                BigDecimal dPriceSellTaxBD=new BigDecimal(String.valueOf(dPriceSellTax)).setScale(8,RoundingMode.HALF_UP);
                BigDecimal dTaxRateBD=new BigDecimal(String.valueOf(dTaxRate)).setScale(8,RoundingMode.HALF_UP);
                BigDecimal priceSellBD=dPriceSellTaxBD.divide(BigDecimal.ONE.add(dTaxRateBD),8,RoundingMode.HALF_UP);
                //setPriceSell(dPriceSellTax / (1.0 + dTaxRate));
                setPriceSell(priceSellBD.doubleValue());
                //*** Fin modificaciones RndMnkIII
            }

            reportlock = false;
        }
    }

    private void setPriceSell(Object value) {

        if (!priceselllock) {
            priceselllock = true;
            pricesell = value;
            m_jPriceSell.setText(Formats.CURRENCY.formatValue(pricesell));
            priceselllock = false;
        }
    }

    private class PriceSellManager implements DocumentListener {

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();
            calculateGP();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();
            calculateGP();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (!priceselllock) {
                priceselllock = true;
                pricesell = readCurrency(m_jPriceSell.getText());
                priceselllock = false;
            }
            calculateMargin();
            calculatePriceSellTax();
            calculateGP();
        }
    }

    private class FieldsManager implements DocumentListener, ActionListener {

        @Override
        public void changedUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
            calculateGP();

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
            calculateGP();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            calculateMargin();
            calculatePriceSellTax();
            calculateGP();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            calculateMargin();
            calculatePriceSellTax();
            calculateGP();
        }
    }

    private class PriceTaxManager implements DocumentListener {

        @Override
        public void changedUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
            calculateGP();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
            calculateGP();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            calculatePriceSellfromPST();
            calculateMargin();
            calculateGP();
        }
    }

    private class MarginManager implements DocumentListener {

        @Override
        public void changedUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
            calculateGP();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
            calculateGP();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            calculatePriceSellfromMargin();
            calculatePriceSellTax();
            calculateGP();
        }
    }

    private static Double readCurrency(String sValue) {
        try {
            return (Double) Formats.CURRENCY.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

    private static Double readPercent(String sValue) {
        try {
            return (Double) Formats.PERCENT.parseValue(sValue);
        } catch (BasicException e) {
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel24 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        m_jTitle = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_jRef = new javax.swing.JTextField();
        m_jCode = new javax.swing.JTextField();
        m_jCodetype = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        m_jAtt = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        m_jTax = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        m_jPriceSellTax = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        m_jPriceSell = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        m_jmargin = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jPriceBuy = new javax.swing.JTextField();
        m_jTextTip = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        m_jGrossProfit = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        m_jAlias = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        m_jVerpatrib = new eu.hansolo.custom.SteelCheckBox();
        m_jCheckWarrantyReceipt = new eu.hansolo.custom.SteelCheckBox();
        jLabel36 = new javax.swing.JLabel();
        jComboBoxPromotion = new javax.swing.JComboBox();
        jCheckBoxPromotion = new eu.hansolo.custom.SteelCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        m_jstockcost = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        m_jstockvolume = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        m_jCatalogOrder = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        m_jStockUnits = new javax.swing.JTextField();
        m_jPackQuantity = new javax.swing.JTextField();
        m_jPackProduct = new javax.swing.JComboBox();
        jLabelPackQuantity = new javax.swing.JLabel();
        jLabelPackProduct = new javax.swing.JLabel();
        m_jInCatalog = new eu.hansolo.custom.SteelCheckBox();
        m_jKitchen = new eu.hansolo.custom.SteelCheckBox();
        m_jIsPack = new eu.hansolo.custom.SteelCheckBox();
        m_jAlwaysAvailable = new eu.hansolo.custom.SteelCheckBox();
        m_jScale = new eu.hansolo.custom.SteelCheckBox();
        m_jDiscounted = new eu.hansolo.custom.SteelCheckBox();
        m_jVprice = new eu.hansolo.custom.SteelCheckBox();
        m_jService = new eu.hansolo.custom.SteelCheckBox();
        m_jComment = new eu.hansolo.custom.SteelCheckBox();
        m_jImage = new uk.chromis.data.gui.JImageEditor();
        jPanel4 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jDisplay = new javax.swing.JTextPane();
        jButtonHTML = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAttributes = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();

        jLabel24.setText("jLabel24");

        jLabel27.setText("jLabel27");

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        m_jTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        m_jTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        add(m_jTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 30));

        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("label.prodref")); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 10, 65, 25);

        m_jRef.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jRef.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jRefFocusLost(evt);
            }
        });
        jPanel1.add(m_jRef);
        m_jRef.setBounds(130, 10, 450, 25);

        m_jCode.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jCodeFocusLost(evt);
            }
        });
        m_jCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCodeActionPerformed(evt);
            }
        });
        m_jCode.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                m_jCodePropertyChange(evt);
            }
        });
        jPanel1.add(m_jCode);
        m_jCode.setBounds(390, 40, 190, 25);

        m_jCodetype.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        m_jCodetype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jCodetypeActionPerformed(evt);
            }
        });
        jPanel1.add(m_jCodetype);
        m_jCodetype.setBounds(130, 40, 130, 25);

        jLabel34.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel34.setText(AppLocal.getIntString("Label.Alias")); // NOI18N
        jPanel1.add(jLabel34);
        jLabel34.setBounds(10, 100, 100, 25);

        m_jName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jNameFocusLost(evt);
            }
        });
        jPanel1.add(m_jName);
        m_jName.setBounds(130, 70, 450, 25);

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText(AppLocal.getIntString("label.prodcategory")); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(10, 130, 110, 25);

        m_jCategory.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jCategory);
        m_jCategory.setBounds(130, 130, 450, 25);

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setText(AppLocal.getIntString("label.attributes")); // NOI18N
        jPanel1.add(jLabel13);
        jLabel13.setBounds(10, 160, 110, 25);

        m_jAtt.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jAtt);
        m_jAtt.setBounds(130, 160, 170, 25);

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText(AppLocal.getIntString("label.taxcategory")); // NOI18N
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 190, 110, 25);

        m_jTax.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jTax);
        m_jTax.setBounds(130, 190, 170, 25);

        jLabel16.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel16.setText(AppLocal.getIntString("label.prodpriceselltax")); // NOI18N
        jPanel1.add(jLabel16);
        jLabel16.setBounds(10, 220, 90, 25);

        m_jPriceSellTax.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jPriceSellTax.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceSellTax);
        m_jPriceSellTax.setBounds(130, 220, 80, 25);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(AppLocal.getIntString("label.prodpricesell")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(210, 220, 100, 25);

        m_jPriceSell.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jPriceSell.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jPriceSell.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jPriceSellFocusLost(evt);
            }
        });
        jPanel1.add(m_jPriceSell);
        m_jPriceSell.setBounds(310, 220, 70, 25);

        jLabel19.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
        jLabel19.setText(bundle.getString("label.margin")); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(48, 15));
        jPanel1.add(jLabel19);
        jLabel19.setBounds(390, 220, 70, 25);

        m_jmargin.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jmargin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jmargin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        m_jmargin.setEnabled(false);
        jPanel1.add(m_jmargin);
        m_jmargin.setBounds(470, 220, 110, 25);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(10, 250, 80, 25);

        m_jPriceBuy.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jPriceBuy.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(m_jPriceBuy);
        m_jPriceBuy.setBounds(130, 250, 80, 25);

        m_jTextTip.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.add(m_jTextTip);
        m_jTextTip.setBounds(130, 320, 450, 25);

        jLabel21.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel21.setText(bundle.getString("label.texttip")); // NOI18N
        jPanel1.add(jLabel21);
        jLabel21.setBounds(10, 320, 100, 25);

        m_jGrossProfit.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jGrossProfit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jGrossProfit.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        m_jGrossProfit.setEnabled(false);
        jPanel1.add(m_jGrossProfit);
        m_jGrossProfit.setBounds(470, 250, 110, 25);

        jLabel22.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText(bundle.getString("label.grossprofit")); // NOI18N
        jPanel1.add(jLabel22);
        jLabel22.setBounds(370, 250, 90, 20);

        m_jAlias.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jAlias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jAliasFocusLost(evt);
            }
        });
        jPanel1.add(m_jAlias);
        m_jAlias.setBounds(130, 100, 170, 25);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 70, 100, 25);

        m_jVerpatrib.setText(bundle.getString("label.mandatory")); // NOI18N
        jPanel1.add(m_jVerpatrib);
        m_jVerpatrib.setBounds(320, 160, 180, 30);

        m_jCheckWarrantyReceipt.setText(bundle.getString("label.productreceipt")); // NOI18N
        jPanel1.add(m_jCheckWarrantyReceipt);
        m_jCheckWarrantyReceipt.setBounds(10, 350, 260, 30);

        jLabel36.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel36.setText("Promotion");
        jPanel1.add(jLabel36);
        jLabel36.setBounds(10, 280, 90, 30);

        jComboBoxPromotion.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(jComboBoxPromotion);
        jComboBoxPromotion.setBounds(160, 280, 420, 30);

        jCheckBoxPromotion.setText(" ");
        jCheckBoxPromotion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxPromotionActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBoxPromotion);
        jCheckBoxPromotion.setBounds(130, 280, 30, 30);

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText(AppLocal.getIntString("label.prodbarcode")); // NOI18N
        jPanel1.add(jLabel8);
        jLabel8.setBounds(270, 40, 110, 25);

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setText("Tipo Cod. Barras");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(10, 40, 110, 20);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodgeneral"), jPanel1); // NOI18N

        jPanel2.setLayout(null);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel9.setText(AppLocal.getIntString("label.prodstockcost")); // NOI18N
        jPanel2.add(jLabel9);
        jLabel9.setBounds(250, 60, 120, 25);

        m_jstockcost.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        m_jstockcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jstockcost.setText("0.00");
        jPanel2.add(m_jstockcost);
        m_jstockcost.setBounds(370, 60, 80, 25);

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText(AppLocal.getIntString("label.prodstockvol")); // NOI18N
        jPanel2.add(jLabel10);
        jLabel10.setBounds(250, 100, 120, 25);

        m_jstockvolume.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        m_jstockvolume.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jstockvolume.setText("0.00");
        jPanel2.add(m_jstockvolume);
        m_jstockvolume.setBounds(370, 100, 80, 25);

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setText(AppLocal.getIntString("label.prodorder")); // NOI18N
        jLabel18.setToolTipText("");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(250, 140, 120, 25);

        m_jCatalogOrder.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        m_jCatalogOrder.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(m_jCatalogOrder);
        m_jCatalogOrder.setBounds(370, 140, 80, 25);

        jLabel23.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText(bundle.getString("label.prodminmax")); // NOI18N
        jLabel23.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel2.add(jLabel23);
        jLabel23.setBounds(250, 180, 270, 60);

        m_jStockUnits.setEditable(false);
        m_jStockUnits.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        m_jStockUnits.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jStockUnits.setText("0");
        m_jStockUnits.setBorder(null);
        jPanel2.add(m_jStockUnits);
        m_jStockUnits.setBounds(370, 210, 80, 25);

        m_jPackQuantity.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        m_jPackQuantity.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        m_jPackQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                m_jPackQuantityFocusLost(evt);
            }
        });
        jPanel2.add(m_jPackQuantity);
        m_jPackQuantity.setBounds(350, 270, 80, 25);

        m_jPackProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel2.add(m_jPackProduct);
        m_jPackProduct.setBounds(350, 300, 220, 25);

        jLabelPackQuantity.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelPackQuantity.setText("Pack Quantity");
        jPanel2.add(jLabelPackQuantity);
        jLabelPackQuantity.setBounds(260, 270, 90, 20);

        jLabelPackProduct.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabelPackProduct.setText("of Product");
        jPanel2.add(jLabelPackProduct);
        jLabelPackProduct.setBounds(260, 290, 80, 30);

        m_jInCatalog.setSelected(true);
        m_jInCatalog.setText(bundle.getString("label.prodincatalog")); // NOI18N
        m_jInCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jInCatalogActionPerformed(evt);
            }
        });
        jPanel2.add(m_jInCatalog);
        m_jInCatalog.setBounds(20, 50, 200, 30);

        m_jKitchen.setText("Print to Remote Printer");
        jPanel2.add(m_jKitchen);
        m_jKitchen.setBounds(20, 170, 210, 30);

        m_jIsPack.setText("Multi Pack");
        m_jIsPack.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        m_jIsPack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jIsPackActionPerformed(evt);
            }
        });
        jPanel2.add(m_jIsPack);
        m_jIsPack.setBounds(250, 240, 110, 30);

        m_jAlwaysAvailable.setText(bundle.getString("Label.AlwaysAvailable")); // NOI18N
        m_jAlwaysAvailable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jAlwaysAvailableActionPerformed(evt);
            }
        });
        jPanel2.add(m_jAlwaysAvailable);
        m_jAlwaysAvailable.setBounds(20, 230, 210, 30);

        m_jScale.setText(bundle.getString("label.prodscale")); // NOI18N
        jPanel2.add(m_jScale);
        m_jScale.setBounds(20, 140, 200, 30);

        m_jDiscounted.setText(bundle.getString("label.discounted")); // NOI18N
        jPanel2.add(m_jDiscounted);
        m_jDiscounted.setBounds(20, 260, 200, 30);

        m_jVprice.setText(bundle.getString("label.variableprice")); // NOI18N
        jPanel2.add(m_jVprice);
        m_jVprice.setBounds(20, 200, 200, 30);

        m_jService.setText("Service Item");
        jPanel2.add(m_jService);
        m_jService.setBounds(20, 80, 210, 30);

        m_jComment.setText(bundle.getString("label.prodaux")); // NOI18N
        jPanel2.add(m_jComment);
        m_jComment.setBounds(20, 110, 200, 30);

        jTabbedPane1.addTab(AppLocal.getIntString("label.prodstock"), jPanel2); // NOI18N
        jTabbedPane1.addTab("Image", m_jImage);

        jPanel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel4.setLayout(null);

        jLabel28.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel28.setText(bundle.getString("label.prodbuttonhtml")); // NOI18N
        jPanel4.add(jLabel28);
        jLabel28.setBounds(10, 10, 270, 20);

        m_jDisplay.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane2.setViewportView(m_jDisplay);

        jPanel4.add(jScrollPane2);
        jScrollPane2.setBounds(10, 40, 480, 40);

        jButtonHTML.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButtonHTML.setText(bundle.getString("button.htmltest")); // NOI18N
        jButtonHTML.setMargin(new java.awt.Insets(1, 1, 1, 1));
        jButtonHTML.setMaximumSize(new java.awt.Dimension(96, 72));
        jButtonHTML.setMinimumSize(new java.awt.Dimension(96, 72));
        jButtonHTML.setPreferredSize(new java.awt.Dimension(96, 72));
        jButtonHTML.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonHTMLMouseClicked(evt);
            }
        });
        jPanel4.add(jButtonHTML);
        jButtonHTML.setBounds(205, 90, 110, 70);

        jLabel17.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText(bundle.getString("label.producthtmlguide")); // NOI18N
        jLabel17.setToolTipText("");
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel4.add(jLabel17);
        jLabel17.setBounds(10, 200, 330, 100);
        jPanel4.add(jSeparator1);
        jSeparator1.setBounds(150, 300, 0, 2);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel32.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel32.setText(bundle.getString("label.fontexample")); // NOI18N
        jLabel32.setToolTipText(bundle.getString("tooltip.fontexample")); // NOI18N

        jLabel25.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel25.setText(bundle.getString("label.fontcolour")); // NOI18N
        jLabel25.setToolTipText(bundle.getString("tooltip.fontcolour")); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(160, 30));

        jLabel29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel29.setText(bundle.getString("label.fontsizelarge")); // NOI18N
        jLabel29.setToolTipText(bundle.getString("tooltip.fontsizelarge")); // NOI18N
        jLabel29.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel29.setPreferredSize(new java.awt.Dimension(160, 30));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setText(bundle.getString("label.fontsize")); // NOI18N
        jLabel26.setToolTipText(bundle.getString("tooltip.fontsizesmall")); // NOI18N
        jLabel26.setPreferredSize(new java.awt.Dimension(160, 30));

        jLabel31.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel31.setText(bundle.getString("label.fontitalic")); // NOI18N
        jLabel31.setToolTipText(bundle.getString("tooltip.fontitalic")); // NOI18N
        jLabel31.setPreferredSize(new java.awt.Dimension(160, 30));

        jLabel30.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel30.setText(bundle.getString("label.fontweight")); // NOI18N
        jLabel30.setToolTipText(bundle.getString("tooltip.fontbold")); // NOI18N
        jLabel30.setPreferredSize(new java.awt.Dimension(160, 30));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel5);
        jPanel5.setBounds(360, 110, 180, 220);

        jTabbedPane1.addTab("Button", jPanel4);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtAttributes.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(txtAttributes);

        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(AppLocal.getIntString("label.properties"), jPanel3); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 615, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 391, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Properties from XML", jPanel6);

        add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 620, 420));
    }// </editor-fold>//GEN-END:initComponents

    private void m_jRefFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jRefFocusLost
        setCode();
    }//GEN-LAST:event_m_jRefFocusLost

    private void m_jNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jNameFocusLost
        //setDisplay();
        setDisplay2();
    }//GEN-LAST:event_m_jNameFocusLost

    private void jButtonHTMLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonHTMLMouseClicked
        setButtonHTML();
    }//GEN-LAST:event_jButtonHTMLMouseClicked


    private void m_jAlwaysAvailableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jAlwaysAvailableActionPerformed
        if (m_jAlwaysAvailable.isSelected()) {
            m_jInCatalog.setSelected(false);
        }
    }//GEN-LAST:event_m_jAlwaysAvailableActionPerformed

    private void m_jInCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jInCatalogActionPerformed
        if (m_jInCatalog.isSelected()) {
            m_jCatalogOrder.setEnabled(true);
        } else {
            m_jCatalogOrder.setEnabled(false);
            m_jCatalogOrder.setText(null);
        }

        if (m_jInCatalog.isSelected()) {
            m_jAlwaysAvailable.setSelected(false);
        }
    }//GEN-LAST:event_m_jInCatalogActionPerformed

    private void m_jIsPackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jIsPackActionPerformed
        m_jPackQuantity.setEnabled(m_jIsPack.isSelected());
        m_jPackProduct.setEnabled(m_jIsPack.isSelected());
        jLabelPackQuantity.setEnabled(m_jIsPack.isSelected());
        jLabelPackProduct.setEnabled(m_jIsPack.isSelected());
    }//GEN-LAST:event_m_jIsPackActionPerformed

    private void jCheckBoxPromotionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxPromotionActionPerformed
        if (jCheckBoxPromotion.isSelected()) {
            jComboBoxPromotion.setEnabled(true);
        } else {
            jComboBoxPromotion.setEnabled(false);
            jComboBoxPromotion.getModel().setSelectedItem(null);
        }
    }//GEN-LAST:event_jCheckBoxPromotionActionPerformed

    private void m_jPackQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jPackQuantityFocusLost
       Object selectItem =  m_jPackProduct.getSelectedItem();
       Object selectIndex = m_jPackProduct.getSelectedItem();
        try {
            packproductmodel = new ComboBoxValModel(packproductsent.list());
        } catch (BasicException ex) {
            Logger.getLogger(ProductsEditor.class.getName()).log(Level.SEVERE, null, ex);
        }       
        m_jPackProduct.setModel(packproductmodel);
        if (selectItem != null){
            m_jPackProduct.setSelectedItem(selectItem);
            m_jPackProduct.setSelectedItem(selectIndex);            
        }
    }//GEN-LAST:event_m_jPackQuantityFocusLost

    private void m_jCodetypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCodetypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m_jCodetypeActionPerformed

    private void m_jCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jCodeActionPerformed

    }//GEN-LAST:event_m_jCodeActionPerformed

    private void m_jCodePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_m_jCodePropertyChange
         //RndMnkIII
                Integer checkDigit;
                if(m_jCode.getText().length() == 12 && StringUtils.isNumeric(m_jCode.getText()) && m_jCodetype.getSelectedItem().toString().equals("EAN13")){
                    checkDigit=controlCodeCalculator(m_jCode.getText());
                    m_jCode.setText(m_jCode.getText()+Integer.toString(checkDigit));
                }
    }//GEN-LAST:event_m_jCodePropertyChange

    private void m_jCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jCodeFocusLost
        //RndMnkIII
        System.out.println("*** m_jCodeFocusLost ***");
        Integer checkDigit;
        //Si introduces codigo de barras EAN13 sin digito de control lo calcula y añade,
        //seleccionar previamente el tipo de codigo de barras (EAN13) antes de introducirlo
        if(m_jCode.getText().length() == 12 && m_jCode.getText().matches("[0-9]+")){
            if(m_jCodetype.getSelectedItem().toString().equals("EAN13")){
                checkDigit=controlCodeCalculator(m_jCode.getText());
                m_jCode.setText(m_jCode.getText()+Integer.toString(checkDigit));
            }
        }
        if(m_jCode.getText().length() == 13 && m_jCodetype.getSelectedItem().toString().equals("EAN13") && !m_jCode.getText().matches("[0-9]+")){
            System.out.println("*** no numerico ***");
            JFrame frame = new JFrame("JOptionPane showMessageDialog example");
            JOptionPane.showMessageDialog(frame,
            "Introdujo caracteres no numéricos",           
            "Error Código de Barras",
            JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(m_jCode.getText().length() == 13 && m_jCodetype.getSelectedItem().toString().equals("EAN13")){
            if(BarcodeValidator.BarcodeValidate(m_jCode.getText()).equals("null")){   
                if(m_jCode.getText().matches("[0-9]+")){
                    JFrame frame = new JFrame("JOptionPane showMessageDialog example");
                    JOptionPane.showMessageDialog(frame,
                    "Dígito de control incorrecto\nDebería ser: "+controlCodeCalculator(m_jCode.getText().substring(0,12)),           
                    "Error Código de Barras",
                    JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_m_jCodeFocusLost

    private void m_jAliasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jAliasFocusLost
        setDisplay2();
    }//GEN-LAST:event_m_jAliasFocusLost

    private void m_jPriceSellFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_jPriceSellFocusLost
        setPriceBuy();
    }//GEN-LAST:event_m_jPriceSellFocusLost

    private void setPriceBuy(){
        //Always sets 60% of pricesell
        Double pricebuy = ((Double)pricesell) * 0.6;
        if (!priceselllock) {
            priceselllock = true;
            m_jPriceBuy.setText(Formats.CURRENCY.formatValue(pricebuy));
            priceselllock = false;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonHTML;
    private eu.hansolo.custom.SteelCheckBox jCheckBoxPromotion;
    private javax.swing.JComboBox jComboBoxPromotion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelPackProduct;
    private javax.swing.JLabel jLabelPackQuantity;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField m_jAlias;
    private eu.hansolo.custom.SteelCheckBox m_jAlwaysAvailable;
    private javax.swing.JComboBox m_jAtt;
    private javax.swing.JTextField m_jCatalogOrder;
    private javax.swing.JComboBox m_jCategory;
    private eu.hansolo.custom.SteelCheckBox m_jCheckWarrantyReceipt;
    private javax.swing.JTextField m_jCode;
    private javax.swing.JComboBox m_jCodetype;
    private eu.hansolo.custom.SteelCheckBox m_jComment;
    private eu.hansolo.custom.SteelCheckBox m_jDiscounted;
    private javax.swing.JTextPane m_jDisplay;
    private javax.swing.JTextField m_jGrossProfit;
    private uk.chromis.data.gui.JImageEditor m_jImage;
    private eu.hansolo.custom.SteelCheckBox m_jInCatalog;
    private eu.hansolo.custom.SteelCheckBox m_jIsPack;
    private eu.hansolo.custom.SteelCheckBox m_jKitchen;
    private javax.swing.JTextField m_jName;
    private javax.swing.JComboBox m_jPackProduct;
    private javax.swing.JTextField m_jPackQuantity;
    private javax.swing.JTextField m_jPriceBuy;
    private javax.swing.JTextField m_jPriceSell;
    private javax.swing.JTextField m_jPriceSellTax;
    private javax.swing.JTextField m_jRef;
    private eu.hansolo.custom.SteelCheckBox m_jScale;
    private eu.hansolo.custom.SteelCheckBox m_jService;
    private javax.swing.JTextField m_jStockUnits;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JTextField m_jTextTip;
    private javax.swing.JLabel m_jTitle;
    private eu.hansolo.custom.SteelCheckBox m_jVerpatrib;
    private eu.hansolo.custom.SteelCheckBox m_jVprice;
    private javax.swing.JTextField m_jmargin;
    private javax.swing.JTextField m_jstockcost;
    private javax.swing.JTextField m_jstockvolume;
    private javax.swing.JTextArea txtAttributes;
    // End of variables declaration//GEN-END:variables

}
