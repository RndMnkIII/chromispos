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
package uk.chromis.pos.config;

import java.awt.Component;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.skin.SkinInfo;
import uk.chromis.data.user.DirtyManager;
import uk.chromis.pos.forms.AppConfig;
import uk.chromis.pos.forms.AppLocal;
import uk.chromis.pos.util.DirectoryEvent;

/**
 *
 *
 */
public class JPanelConfigGeneral extends javax.swing.JPanel implements PanelConfig {

    private final DirtyManager dirty = new DirtyManager();

    /**
     * Creates new form JPanelConfigGeneral
     */
    public JPanelConfigGeneral() {

        initComponents();

        jbtnLogoText.addActionListener(new DirectoryEvent(jtxtStartupText));
        jbtnLogoName.addActionListener(new DirectoryEvent(jtxtStartupLogo));
        jtxtMachineHostname.getDocument().addDocumentListener(dirty);
        jcboLAF.addActionListener(dirty);
        jcboMachineScreenmode.addActionListener(dirty);
        jcboTicketsBag.addActionListener(dirty);
        jchkHideInfo.addActionListener(dirty);
        jtxtStartupText.getDocument().addDocumentListener(dirty);
        jRed.addActionListener(dirty);
        jBlue.addActionListener(dirty);
        jGreen.addActionListener(dirty);
        jBlack.addActionListener(dirty);
        jOrange.addActionListener(dirty);
        jRoyalBlue.addActionListener(dirty);

        // Installed skins
        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo laf : lafs) {
            jcboLAF.addItem(new LAFInfo(laf.getName(), laf.getClassName()));
        }

        // Substance skins
        // new SubstanceLookAndFeel()
        Map<String, SkinInfo> skins = SubstanceLookAndFeel.getAllSkins();

        for (SkinInfo skin : skins.values()) {
            jcboLAF.addItem(new LAFInfo(skin.getDisplayName(), skin.getClassName()));
        }

        jcboLAF.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeLAF();
            }
        });

        jcboMachineScreenmode.addItem("window");
        jcboMachineScreenmode.addItem("fullscreen");

        jcboTicketsBag.addItem("simple");
        jcboTicketsBag.addItem("standard");
        jcboTicketsBag.addItem("restaurant");

    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasChanged() {
        return dirty.isDirty();
    }

    /**
     *
     * @return
     */
    @Override
    public Component getConfigComponent() {
        return this;
    }

    /**
     *
     * @param config
     */
    @Override
    public void loadProperties() {

        jtxtMachineHostname.setText(AppConfig.getInstance().getProperty("machine.hostname"));

        String lafclass = AppConfig.getInstance().getProperty("swing.defaultlaf");
        jcboLAF.setSelectedItem(null);
        for (int i = 0; i < jcboLAF.getItemCount(); i++) {
            LAFInfo lafinfo = (LAFInfo) jcboLAF.getItemAt(i);
            if (lafinfo.getClassName().equals(lafclass)) {
                jcboLAF.setSelectedIndex(i);
                break;
            }
        }
        // jcboLAF.setSelectedItem(new LookAndFeelInfo());

        jcboMachineScreenmode.setSelectedItem(AppConfig.getInstance().getProperty("machine.screenmode"));
        jcboTicketsBag.setSelectedItem(AppConfig.getInstance().getProperty("machine.ticketsbag"));
        jchkHideInfo.setSelected(AppConfig.getInstance().getBoolean("till.hideinfo"));
        jtxtStartupLogo.setText(AppConfig.getInstance().getProperty("start.logo"));
        jtxtStartupText.setText(AppConfig.getInstance().getProperty("start.text"));

        setIconColour();
        dirty.setDirty(false);
    }

    /**
     *
     * @param config
     */
    @Override
    public void saveProperties() {

        AppConfig.getInstance().setProperty("machine.hostname", jtxtMachineHostname.getText());

        LAFInfo laf = (LAFInfo) jcboLAF.getSelectedItem();
        AppConfig.getInstance().setProperty("swing.defaultlaf", laf == null
                ? System.getProperty("swing.defaultlaf", "javax.swing.plaf.metal.MetalLookAndFeel")
                : laf.getClassName());

        AppConfig.getInstance().setProperty("machine.screenmode", comboValue(jcboMachineScreenmode.getSelectedItem()));
        AppConfig.getInstance().setProperty("machine.ticketsbag", comboValue(jcboTicketsBag.getSelectedItem()));
        AppConfig.getInstance().setBoolean("till.hideinfo", jchkHideInfo.isSelected());
        AppConfig.getInstance().setProperty("start.logo", jtxtStartupLogo.getText());
        AppConfig.getInstance().setProperty("start.text", jtxtStartupText.getText());

        dirty.setDirty(false);
    }

    private String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }

    private void setIconColour() {
        if (AppConfig.getInstance().getProperty("icon.colour") != null) {
            switch (AppConfig.getInstance().getProperty("icon.colour")) {
                case "red":
                    jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/reduser.png")));
                    jRed.setSelected(true);
                    break;
                case "blue":
                    jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/blueuser.png")));
                    jBlue.setSelected(true);
                    break;
                case "green":
                    jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/greenuser.png")));
                    jGreen.setSelected(true);
                    break;
                case "orange":
                    jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/orangeuser.png")));
                    jOrange.setSelected(true);
                    break;
                case "black":
                    jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/blackuser.png")));
                    jBlack.setSelected(true);
                    break;
                case "royalblue":
                    jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/rblueuser.png")));
                    jRoyalBlue.setSelected(true);
                    break;
            }
        }
    }

    private void changeLAF() {

        final LAFInfo laf = (LAFInfo) jcboLAF.getSelectedItem();
        if (laf != null && !laf.getClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
            // The selected look and feel is different from the current look and feel.
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        String lafname = laf.getClassName();
                        Object laf = Class.forName(lafname).newInstance();

                        if (laf instanceof LookAndFeel) {
                            UIManager.setLookAndFeel((LookAndFeel) laf);
                        } else if (laf instanceof SubstanceSkin) {
                            SubstanceLookAndFeel.setSkin((SubstanceSkin) laf);
                        }

                        SwingUtilities.updateComponentTreeUI(JPanelConfigGeneral.this.getTopLevelAncestor());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    }
                }
            });
        }
    }

    private static class LAFInfo {

        private final String name;
        private final String classname;

        public LAFInfo(String name, String classname) {
            this.name = name;
            this.classname = classname;
        }

        public String getName() {
            return name;
        }

        public String getClassName() {
            return classname;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jColourGroup = new javax.swing.ButtonGroup();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jtxtMachineHostname = new javax.swing.JTextField();
        jcboLAF = new javax.swing.JComboBox();
        jcboMachineScreenmode = new javax.swing.JComboBox();
        jcboTicketsBag = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jtxtStartupLogo = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jtxtStartupText = new javax.swing.JTextField();
        jbtnLogoName = new javax.swing.JButton();
        jbtnLogoText = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jchkHideInfo = new eu.hansolo.custom.SteelCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jRed = new javax.swing.JRadioButton();
        jGreen = new javax.swing.JRadioButton();
        jBlack = new javax.swing.JRadioButton();
        jBlue = new javax.swing.JRadioButton();
        jOrange = new javax.swing.JRadioButton();
        jRoyalBlue = new javax.swing.JRadioButton();
        jIconColour = new javax.swing.JLabel();

        setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        setPreferredSize(new java.awt.Dimension(650, 450));

        jPanel11.setPreferredSize(new java.awt.Dimension(650, 450));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText(AppLocal.getIntString("Label.MachineName")); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText(AppLocal.getIntString("label.looknfeel")); // NOI18N
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(AppLocal.getIntString("Label.MachineScreen")); // NOI18N
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText(AppLocal.getIntString("Label.Ticketsbag")); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(100, 30));

        jtxtMachineHostname.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtxtMachineHostname.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtxtMachineHostname.setMinimumSize(new java.awt.Dimension(130, 25));
        jtxtMachineHostname.setPreferredSize(new java.awt.Dimension(200, 30));

        jcboLAF.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jcboLAF.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jcboLAF.setPreferredSize(new java.awt.Dimension(200, 30));

        jcboMachineScreenmode.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jcboMachineScreenmode.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jcboMachineScreenmode.setPreferredSize(new java.awt.Dimension(200, 30));

        jcboTicketsBag.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jcboTicketsBag.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jcboTicketsBag.setPreferredSize(new java.awt.Dimension(200, 30));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), bundle.getString("label.startuppanel"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        jPanel1.setLayout(null);

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setText(bundle.getString("label.startuplogo")); // NOI18N
        jLabel18.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel18.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel18.setPreferredSize(new java.awt.Dimension(0, 30));
        jPanel1.add(jLabel18);
        jLabel18.setBounds(10, 20, 90, 30);

        jtxtStartupLogo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtxtStartupLogo.setMaximumSize(new java.awt.Dimension(0, 25));
        jtxtStartupLogo.setMinimumSize(new java.awt.Dimension(0, 0));
        jtxtStartupLogo.setPreferredSize(new java.awt.Dimension(350, 30));
        jPanel1.add(jtxtStartupLogo);
        jtxtStartupLogo.setBounds(110, 20, 350, 30);

        jLabel19.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel19.setText(AppLocal.getIntString("label.startuptext")); // NOI18N
        jLabel19.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel19.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel19.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel1.add(jLabel19);
        jLabel19.setBounds(10, 60, 70, 25);

        jtxtStartupText.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jtxtStartupText.setMaximumSize(new java.awt.Dimension(0, 25));
        jtxtStartupText.setMinimumSize(new java.awt.Dimension(0, 0));
        jtxtStartupText.setPreferredSize(new java.awt.Dimension(350, 30));
        jPanel1.add(jtxtStartupText);
        jtxtStartupText.setBounds(110, 60, 350, 30);

        jbtnLogoName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/images/fileopen.png"))); // NOI18N
        jbtnLogoName.setMaximumSize(new java.awt.Dimension(64, 32));
        jbtnLogoName.setMinimumSize(new java.awt.Dimension(64, 32));
        jbtnLogoName.setPreferredSize(new java.awt.Dimension(64, 32));
        jPanel1.add(jbtnLogoName);
        jbtnLogoName.setBounds(480, 20, 64, 32);

        jbtnLogoText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/images/fileopen.png"))); // NOI18N
        jbtnLogoText.setMaximumSize(new java.awt.Dimension(64, 32));
        jbtnLogoText.setMinimumSize(new java.awt.Dimension(64, 32));
        jbtnLogoText.setPreferredSize(new java.awt.Dimension(64, 32));
        jPanel1.add(jbtnLogoText);
        jbtnLogoText.setBounds(480, 60, 64, 32);

        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 0, 153));
        jButton1.setText("X");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(60, 63, 40, 23);

        jchkHideInfo.setText(bundle.getString("label.Infopanel")); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("label.iconcolour"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12), new java.awt.Color(102, 102, 102))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jColourGroup.add(jRed);
        jRed.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jRed.setText("Red");
        jRed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jColourActionPerformed(evt);
            }
        });
        jPanel2.add(jRed, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 60, -1));

        jColourGroup.add(jGreen);
        jGreen.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jGreen.setText("Green");
        jGreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jColourActionPerformed(evt);
            }
        });
        jPanel2.add(jGreen, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        jColourGroup.add(jBlack);
        jBlack.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jBlack.setText("Black");
        jBlack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jColourActionPerformed(evt);
            }
        });
        jPanel2.add(jBlack, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        jColourGroup.add(jBlue);
        jBlue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jBlue.setText("Blue");
        jBlue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jColourActionPerformed(evt);
            }
        });
        jPanel2.add(jBlue, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        jColourGroup.add(jOrange);
        jOrange.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jOrange.setText("Orange");
        jOrange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jColourActionPerformed(evt);
            }
        });
        jPanel2.add(jOrange, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, -1, -1));

        jColourGroup.add(jRoyalBlue);
        jRoyalBlue.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jRoyalBlue.setText("Royal Blue");
        jRoyalBlue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jColourActionPerformed(evt);
            }
        });
        jPanel2.add(jRoyalBlue, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, -1, -1));
        jPanel2.add(jIconColour, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, 60));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcboMachineScreenmode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jcboLAF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtMachineHostname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jchkHideInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jcboTicketsBag, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(84, 128, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtMachineHostname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboLAF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachineScreenmode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboTicketsBag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jchkHideInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(130, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jtxtStartupText.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jColourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jColourActionPerformed
        // TODO add your handling code here:
        String colour = evt.getActionCommand();
        switch (colour) {
            case "Red":
                jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/reduser.png")));
                AppConfig.getInstance().setProperty("icon.colour", "red");
                break;
            case "Blue":
                jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/blueuser.png")));
                AppConfig.getInstance().setProperty("icon.colour", "blue");
                break;
            case "Green":
                jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/greenuser.png")));
                AppConfig.getInstance().setProperty("icon.colour", "green");
                break;
            case "Orange":
                jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/orangeuser.png")));
                AppConfig.getInstance().setProperty("icon.colour", "orange");
                break;
            case "Black":
                jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/blackuser.png")));
                AppConfig.getInstance().setProperty("icon.colour", "black");
                break;
            case "Royal Blue":
                jIconColour.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uk/chromis/fixedimages/rblueuser.png")));
                AppConfig.getInstance().setProperty("icon.colour", "royalblue");
                break;
        }
    }//GEN-LAST:event_jColourActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton jBlack;
    private javax.swing.JRadioButton jBlue;
    private javax.swing.JButton jButton1;
    private javax.swing.ButtonGroup jColourGroup;
    private javax.swing.JRadioButton jGreen;
    private javax.swing.JLabel jIconColour;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton jOrange;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRed;
    private javax.swing.JRadioButton jRoyalBlue;
    private javax.swing.JButton jbtnLogoName;
    private javax.swing.JButton jbtnLogoText;
    private javax.swing.JComboBox jcboLAF;
    private javax.swing.JComboBox jcboMachineScreenmode;
    private javax.swing.JComboBox jcboTicketsBag;
    private eu.hansolo.custom.SteelCheckBox jchkHideInfo;
    private javax.swing.JTextField jtxtMachineHostname;
    private javax.swing.JTextField jtxtStartupLogo;
    private javax.swing.JTextField jtxtStartupText;
    // End of variables declaration//GEN-END:variables
}
