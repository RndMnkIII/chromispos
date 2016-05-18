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

package uk.chromis.pos.printer;

import javax.swing.JComponent;

/**
 *
 *   
 */
public interface DeviceDisplay {

    // INTERFAZ DESCRIPCION

    /**
     *
     * @return
     */
        public String getDisplayName();

    /**
     *
     * @return
     */
    public String getDisplayDescription();

    /**
     *
     * @return
     */
    public JComponent getDisplayComponent();
    
    // INTERFAZ VISOR

    /**
     *
     * @param animation
     * @param sLine1
     * @param sLine2
     */
        public void writeVisor(int animation, String sLine1, String sLine2);
        
     /**
     *
     * @param animation
     * @param sLine1
     * @param sLine2
     * @param sLine3
     * @param sLine4
     */
        public void writeVisor(int animation, String sLine1, String sLine2, String sLine3, String sLine4);    

    /**
     *
     * @param sLine1
     * @param sLine2
     */
    public void writeVisor(String sLine1, String sLine2);
    
       /**
     *
     * @param sLine1
     * @param sLine2
     * @param sLine3
     * @param sLine4
     */
    public void writeVisor(String sLine1, String sLine2,String sLine3, String sLine4);

    /**
     *
     */
    public void clearVisor();
}
