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

package uk.chromis.pos.mant;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.UUID;
import javax.swing.JPanel;
import uk.chromis.basic.BasicException;
import uk.chromis.data.user.DirtyManager;
import uk.chromis.data.user.EditorRecord;
import uk.chromis.format.Formats;
import uk.chromis.pos.forms.AppLocal;


/**
 *
 * @author adrianromero
 */
public final class FloorsEditor extends JPanel implements EditorRecord {
    
//    private DirtyManager m_Dirty = new DirtyManager();    
    private String m_sID;
    
    /** Creates new form FloorsEditor
     * @param dirty */
    public FloorsEditor(DirtyManager dirty) {
        initComponents();
         
        m_jName.getDocument().addDocumentListener(dirty);
        m_jImage.addPropertyChangeListener("image", dirty);
        
        writeValueEOF();
    }

    /**
     *
     */
    @Override
    public void writeValueEOF() {
        
        m_sID = null;
        m_jName.setText(null);
        m_jImage.setImage(null);

        m_jName.setEnabled(false);
        m_jImage.setEnabled(false);
    }  

    /**
     *
     */
    @Override
    public void writeValueInsert() {
        
        m_sID = UUID.randomUUID().toString(); 
        m_jName.setText(null);
        m_jImage.setImage(null);

        m_jName.setEnabled(true);
        m_jImage.setEnabled(true);
    }

    /**
     *
     * @param value
     */
    @Override
    public void writeValueDelete(Object value) {
        
        Object[] floor = (Object[]) value;
        m_sID = Formats.STRING.formatValue(floor[0]);
        m_jName.setText(Formats.STRING.formatValue(floor[1]));
        m_jImage.setImage((BufferedImage) floor[2]);

        m_jName.setEnabled(false);
        m_jImage.setEnabled(false);
    }    

    /**
     *
     * @param value
     */
    @Override
    public void writeValueEdit(Object value) {
        
        Object[] floor = (Object[]) value;
        m_sID = Formats.STRING.formatValue(floor[0]);
        m_jName.setText(Formats.STRING.formatValue(floor[1]));
        m_jImage.setImage((BufferedImage) floor[2]);

        m_jName.setEnabled(true);
        m_jImage.setEnabled(true);
    }

    /**
     *
     * @return
     * @throws BasicException
     */
    @Override
    public Object createValue() throws BasicException {
        
        Object[] floor = new Object[3];

        floor[0] = m_sID;
        floor[1] = m_jName.getText();
        floor[2] = m_jImage.getImage();
        return floor;
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }
    
    /**
     *
     */
    @Override
    public void refresh() {
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        m_jImage = new uk.chromis.data.gui.JImageEditor();

        setMinimumSize(new java.awt.Dimension(91, 125));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setPreferredSize(new java.awt.Dimension(150, 100));
        jPanel1.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("Label.Name")); // NOI18N
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 20, 90, 25);

        m_jName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel1.add(m_jName);
        m_jName.setBounds(110, 20, 180, 25);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jPanel3.setLayout(new java.awt.BorderLayout());

        m_jImage.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jPanel3.add(m_jImage, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private uk.chromis.data.gui.JImageEditor m_jImage;
    private javax.swing.JTextField m_jName;
    // End of variables declaration//GEN-END:variables
    
}
