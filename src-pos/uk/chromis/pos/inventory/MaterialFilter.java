//    Opentpv is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Open Sistemas de Información Internet, S.L.
//    Copyright (C) 2007-2008 
//    http://www.opensistemas.com
//    e-mail: imasd@opensistemas.com
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package uk.chromis.pos.inventory;

import uk.chromis.basic.BasicException;
import uk.chromis.data.gui.ComboBoxValModel;
import uk.chromis.data.gui.ListQBFModelNumber;
import uk.chromis.data.loader.QBFCompareEnum;
import uk.chromis.data.loader.SentenceList;
import uk.chromis.data.user.EditorCreator;
import uk.chromis.format.Formats;
import uk.chromis.pos.forms.AppLocal;
import uk.chromis.pos.forms.DataLogicSales;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class MaterialFilter extends javax.swing.JPanel implements EditorCreator {
    
    private SentenceList m_sentprods;
    private ComboBoxValModel m_ProdsModel;

    /** Creates new form JQBFProduct
     * @param dlSales */
    public MaterialFilter(DataLogicSales dlSales) {
        initComponents();
        
        //El modelo de productos
        m_sentprods = dlSales.getProductList();
        m_ProdsModel = new ComboBoxValModel();
         
        m_jCboName.setModel(new ListQBFModelNumber());
        m_jCboPriceBuy.setModel(new ListQBFModelNumber());
    }
    
    /**
     *
     * @throws BasicException
     */
    public void activate() throws BasicException {
        java.util.List prodlist = m_sentprods.list();
        prodlist.add(0, null);
        m_ProdsModel.refresh(prodlist);
        m_jCboProduct.setModel(m_ProdsModel);
    }
   
    /**
     *
     * @return
     * @throws BasicException
     */
    @Override
    public Object createValue() throws BasicException {
        return new Object[] {
            m_jCboName.getSelectedItem(), m_jName.getText(),
            m_jCboPriceBuy.getSelectedItem(), Formats.CURRENCY.parseValue(m_jPriceBuy.getText()),
            m_ProdsModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, m_ProdsModel.getSelectedKey()
        };
    } 
 
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        m_jCboName = new javax.swing.JComboBox();
        m_jName = new javax.swing.JTextField();
        m_jPriceBuy = new javax.swing.JTextField();
        m_jCboPriceBuy = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        m_jCboProduct = new javax.swing.JComboBox();

        setMaximumSize(new java.awt.Dimension(32767, 160));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(500, 160));
        setRequestFocusEnabled(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.byform"))); // NOI18N
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 140));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 140));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText(AppLocal.getIntString("label.prodpricebuy")); // NOI18N
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 50, 130, 25);

        m_jCboName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jCboName);
        m_jCboName.setBounds(150, 20, 150, 25);

        m_jName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.add(m_jName);
        m_jName.setBounds(310, 20, 180, 25);

        m_jPriceBuy.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.add(m_jPriceBuy);
        m_jPriceBuy.setBounds(310, 50, 60, 25);

        m_jCboPriceBuy.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jCboPriceBuy);
        m_jCboPriceBuy.setBounds(150, 50, 150, 25);

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 20, 130, 25);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("label.stockproduct")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 80, 130, 25);

        m_jCboProduct.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.add(m_jCboProduct);
        m_jCboProduct.setBounds(150, 80, 220, 25);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents
   
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox m_jCboName;
    private javax.swing.JComboBox m_jCboPriceBuy;
    private javax.swing.JComboBox m_jCboProduct;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextField m_jPriceBuy;
    // End of variables declaration//GEN-END:variables
    
}
