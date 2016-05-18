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

package uk.chromis.pos.inventory;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import uk.chromis.format.Formats;
import uk.chromis.pos.util.StringUtils;


/**
 *
 * @author adrianromero
 */
public class InventoryRecord {
    
    private final Date m_dDate;
    private final MovementReason m_reason;
    private final LocationInfo m_locationOri;   
    private final List<InventoryLine> m_invlines;
    private final String user;
    
    /** Creates a new instance of InventoryRecord
     * @param d
     * @param reason
     * @param location
     * @param invlines
     * @param currentUser */
    public InventoryRecord(Date d, MovementReason reason, LocationInfo location, String currentUser, List<InventoryLine> invlines) {
        m_dDate = d;
        m_reason = reason;
        m_locationOri = location;
        m_invlines = invlines;
        user = currentUser;
        
    }
    
    /**
     *
     * @return
     */
    public Date getDate() {
        return m_dDate;
    }   

    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @return
     */
    public MovementReason getReason() {
        return m_reason;
    }    

    /**
     *
     * @return
     */
    public LocationInfo getLocation() {
        return m_locationOri;
    }

    /**
     *
     * @return
     */
    public List<InventoryLine> getLines() {
        return m_invlines;
    }

    /**
     *
     * @return
     */
    public boolean isInput() {
        return m_reason.isInput();
    }
    
    /**
     *
     * @return
     */
    public double getSubTotal() {
        double dSuma = 0.0;
        InventoryLine oLine;            
        for (Iterator<InventoryLine> i = m_invlines.iterator(); i.hasNext();) {
            oLine = i.next();
            dSuma += oLine.getSubValue();
        }        
        return dSuma;
    }
    
    /**
     *
     * @return
     */
    public String printDate() {
        return Formats.TIMESTAMP.formatValue(m_dDate);
    }    

    /**
     *
     * @return
     */
    public String printLocation() {
//        return m_locationOri.toString();
        return StringUtils.encodeXML(m_locationOri.toString());
    }

    /**
     *
     * @return
     */
    public String printReason() {
//        return m_reason.toString();
        return StringUtils.encodeXML(m_reason.toString());
    }

    /**
     *
     * @return
     */
    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(getSubTotal());
    }    
}
