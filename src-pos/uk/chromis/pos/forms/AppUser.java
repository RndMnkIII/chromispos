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

package uk.chromis.pos.forms;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import uk.chromis.data.loader.LocalRes;
import uk.chromis.pos.ticket.UserInfo;
import uk.chromis.pos.util.Hashcypher;

/**
 *
 * @author adrianromero
 */
public class AppUser {

    private static final Logger logger = Logger.getLogger("uk.chromis.pos.forms.AppUser");

    private static SAXParser m_sp = null;
    private static HashMap<String, String> m_oldclasses; // This is for backwards compatibility purposes
    
    private final String m_sId;
    private final String m_sName;
    private final String m_sCard;
    private String m_sPassword;
    private final String m_sRole;
    private final Icon m_Icon;
    
    private Set<String> m_apermissions;
    
    static {        
        initOldClasses();
    }
    
    /** Creates a new instance of AppUser
     * @param id
     * @param name
     * @param card
     * @param password
     * @param icon
     * @param role */
    public AppUser(String id, String name, String password, String card, String role, Icon icon) {
        m_sId = id;
        m_sName = name;
        m_sPassword = password;
        m_sCard = card;
        m_sRole = role;
        m_Icon = icon;
        m_apermissions = null;
    }
    
    /**
     *
     * @return
     */
    public Icon getIcon() {
        return m_Icon;
    }
    
    /**
     *
     * @return
     */
    public String getId() {
        return m_sId;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return m_sName;
    }
    
    /**
     *
     * @param sValue
     */
    public void setPassword(String sValue) {
        m_sPassword = sValue;
    }
    
    /**
     *
     * @return
     */
    public String getPassword() {
        return m_sPassword;
    }
    
    /**
     *
     * @return
     */
    public String getRole() {
        return m_sRole;
    }
    
    /**
     *
     * @return
     */
    public String getCard() {
        return m_sCard;
    }
    
    /**
     *
     * @return
     */
    public boolean authenticate() {
        return m_sPassword == null || m_sPassword.equals("") || m_sPassword.startsWith("empty:");
    }

    /**
     *
     * @param sPwd
     * @return
     */
    public boolean authenticate(String sPwd) {
        return Hashcypher.authenticate(sPwd, m_sPassword);
    }
    
    /**
     *
     * @param dlSystem
     */
    public void fillPermissions(DataLogicSystem dlSystem) {
        
        m_apermissions = new HashSet<>();
        // Y lo que todos tienen permisos
        m_apermissions.add("uk.chromis.pos.forms.JPanelMenu");
        m_apermissions.add("Menu.Exit");        
        
        String sRolePermisions = dlSystem.findRolePermissions(m_sRole);
       
        if (sRolePermisions != null) {
            try {
                if (m_sp == null) {
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    m_sp = spf.newSAXParser();
                }
                m_sp.parse(new InputSource(new StringReader(sRolePermisions)), new ConfigurationHandler());

            } catch (ParserConfigurationException ePC) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.parserconfig"), ePC);
            } catch (SAXException eSAX) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.xmlfile"), eSAX);
            } catch (IOException eIO) {
                logger.log(Level.WARNING, LocalRes.getIntString("exception.iofile"), eIO);
            }
        }

    }
    
    /**
     *
     * @param classname
     * @return
     */
    public boolean hasPermission(String classname) {

        return (m_apermissions == null) ? false : m_apermissions.contains(classname);        
    }

    /**
     *
     * @return
     */
    public UserInfo getUserInfo() {
        return new UserInfo(m_sId, m_sName);
    }
    
    private static String mapNewClass(String classname) {
        String newclass = m_oldclasses.get(classname);
        return newclass == null 
                ? classname 
                : newclass;
    }
    
    private static void initOldClasses() {
        m_oldclasses = new HashMap<>();
        
        // update permissions from 0.0.24 to 2.20    
        m_oldclasses.put("net.adrianromero.tpv.panelsales.JPanelTicketSales", "uk.chromis.pos.sales.JPanelTicketSales");
        m_oldclasses.put("net.adrianromero.tpv.panelsales.JPanelTicketEdits", "uk.chromis.pos.sales.JPanelTicketEdits");
        m_oldclasses.put("net.adrianromero.tpv.panels.JPanelPayments", "uk.chromis.pos.panels.JPanelPayments");
        m_oldclasses.put("net.adrianromero.tpv.panels.JPanelCloseMoney", "uk.chromis.pos.panels.JPanelCloseMoney");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportClosedPos", "/uk/chromis/reports/closedpos.bs");

//        m_oldclasses.put("payment.cash", "");
//        m_oldclasses.put("payment.cheque", "");
//        m_oldclasses.put("payment.paper", "");
//        m_oldclasses.put("payment.tichet", "");
//        m_oldclasses.put("payment.magcard", "");
//        m_oldclasses.put("payment.free", "");
//        m_oldclasses.put("refund.cash", "");
//        m_oldclasses.put("refund.cheque", "");
//        m_oldclasses.put("refund.paper", "");
//        m_oldclasses.put("refund.magcard", "");

        m_oldclasses.put("Menu.StockManagement", "uk.chromis.pos.forms.MenuStockManagement");
        m_oldclasses.put("net.adrianromero.tpv.inventory.ProductsPanel", "uk.chromis.pos.inventory.ProductsPanel");
        m_oldclasses.put("net.adrianromero.tpv.inventory.ProductsWarehousePanel", "uk.chromis.pos.inventory.ProductsWarehousePanel");
        m_oldclasses.put("net.adrianromero.tpv.inventory.CategoriesPanel", "uk.chromis.pos.inventory.CategoriesPanel");
        m_oldclasses.put("net.adrianromero.tpv.panels.JPanelTax", "uk.chromis.pos.inventory.TaxPanel");
        m_oldclasses.put("net.adrianromero.tpv.inventory.StockDiaryPanel", "uk.chromis.pos.inventory.StockDiaryPanel");
        m_oldclasses.put("net.adrianromero.tpv.inventory.StockManagement", "uk.chromis.pos.inventory.StockManagement");        
 
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportProducts", "/uk/chromis/reports/products.bs");      
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportCatalog", "/uk/chromis/reports/productscatalog.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportInventory", "/uk/chromis/reports/inventory.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportInventory2", "/uk/chromis/reports/inventoryb.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportInventoryBroken", "/uk/chromis/reports/inventorybroken.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportInventoryDiff", "/uk/chromis/reports/inventorydiff.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportInventoryReOrder", "/uk/chromis/reports/InventoryReOrder.bs");

        m_oldclasses.put("Menu.SalesManagement", "uk.chromis.pos.forms.MenuSalesManagement");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportUserSales", "/uk/chromis/reports/usersales.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportClosedProducts", "/uk/chromis/reports/closedproducts.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JReportTaxes", "/uk/chromis/reports/taxes.bs");
        m_oldclasses.put("net.adrianromero.tpv.reports.JChartSales", "/uk/chromis/reports/chartsales.bs");

        m_oldclasses.put("Menu.Maintenance", "uk.chromis.pos.forms.MenuMaintenance");
        m_oldclasses.put("net.adrianromero.tpv.admin.PeoplePanel", "uk.chromis.pos.admin.PeoplePanel");
        m_oldclasses.put("net.adrianromero.tpv.admin.RolesPanel", "uk.chromis.pos.admin.RolesPanel");
        m_oldclasses.put("net.adrianromero.tpv.admin.ResourcesPanel", "uk.chromis.pos.admin.ResourcesPanel");
        m_oldclasses.put("net.adrianromero.tpv.inventory.LocationsPanel", "uk.chromis.pos.inventory.LocationsPanel");
        m_oldclasses.put("net.adrianromero.tpv.mant.JPanelFloors", "uk.chromis.pos.mant.JPanelFloors");
        m_oldclasses.put("net.adrianromero.tpv.mant.JPanelPlaces", "uk.chromis.pos.mant.JPanelPlaces");
        m_oldclasses.put("uk.chromis.possync.ProductsSync", "uk.chromis.possync.ProductsSyncCreate");
        m_oldclasses.put("uk.chromis.possync.OrdersSync", "uk.chromis.possync.OrdersSyncCreate");

        m_oldclasses.put("Menu.ChangePassword", "Menu.ChangePassword");
        m_oldclasses.put("net.adrianromero.tpv.panels.JPanelPrinter", "uk.chromis.pos.panels.JPanelPrinter");
        m_oldclasses.put("net.adrianromero.tpv.config.JPanelConfiguration", "uk.chromis.pos.config.JPanelConfiguration");
        
//        m_oldclasses.put("button.print", "");
//        m_oldclasses.put("button.opendrawer", "");
        
        // update permissions from 2.00 to 2.20       
        m_oldclasses.put("uk.chromis.pos.reports.JReportCustomers", "/uk/chromis/reports/customers.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportCustomersB", "/uk/chromis/reports/customersb.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportClosedPos", "/uk/chromis/reports/closedpos.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportClosedProducts", "/uk/chromis/reports/closedproducts.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JChartSales", "/uk/chromis/reports/chartsales.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportInventory", "/uk/chromis/reports/inventory.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportInventory2", "/uk/chromis/reports/inventoryb.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportInventoryBroken", "/uk/chromis/reports/inventorybroken.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportInventoryDiff", "/uk/chromis/reports/inventorydiff.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportPeople", "/uk/chromis/reports/people.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportTaxes", "/uk/chromis/reports/taxes.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportUserSales", "/uk/chromis/reports/usersales.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportProducts", "/uk/chromis/reports/products.bs");
        m_oldclasses.put("uk.chromis.pos.reports.JReportCatalog", "/uk/chromis/reports/productscatalog.bs");
        
        // update permissions from 2.10 to 2.20
        m_oldclasses.put("uk.chromis.pos.panels.JPanelTax", "uk.chromis.pos.inventory.TaxPanel");
        
    }
    
    private class ConfigurationHandler extends DefaultHandler {       
        @Override
        public void startDocument() throws SAXException {}
        @Override
        public void endDocument() throws SAXException {}    
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            if ("class".equals(qName)){
                m_apermissions.add(mapNewClass(attributes.getValue("name")));
            }
        }      
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {}
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {}
    }     
    
    
}
