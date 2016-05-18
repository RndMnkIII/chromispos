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

package uk.chromis.pos.printer.escpos;

import uk.chromis.pos.printer.DeviceTicket;

/**
 *
 * @author adrianromero
 */
public class DeviceDisplaySurePOS extends DeviceDisplaySerial {
    
    private UnicodeTranslator trans;
    
    /**
     *
     * @param display
     */
    public DeviceDisplaySurePOS(PrinterWritter display) { 
         //*** START Changed by RNDMNKIII
        //trans = new UnicodeTranslatorSurePOS();
        //*** END Changed by RNDMNKIII
        init(display);                
    }
   
    /**
     *
     */
    @Override
    public void initVisor() {
        //display.write(new byte[]{0x00, 0x01}); // IBM Mode
        //display.write(new byte[]{0x02}); // Set the code page
        //display.write(trans.getCodeTable());
        //display.write(new byte[]{0x11}); // HIDE CURSOR
        //display.write(new byte[]{0x14}); // HIDE CURSOR
        //display.write(new byte[]{0x10, 0x00}); // VISOR HOME
        display.write(new byte[]{0x10}); //ARDUINO LCD VISOR CLEAR COMMAND
        display.flush();
    }

    /**
     *
     */
    @Override
    public void repaintLines() {
        //*** START Changed by RNDMNKIII
        //display.write(new byte[]{0x10, 0x00}); // VISOR HOME
        //display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine1(), 20)));
        //display.write(new byte[]{0x10, 0x14});
        //display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine2(), 20))); 
        display.write(new byte[]{0x11}); //ARDUINO LCD VISOR HOME COMMAND
        display.write(DeviceTicket.alignLeft(m_displaylines.getLine1(), 20));
        display.write(new byte[]{0x12}); //ARDUINO LCD SECOND LINE COMMAND
        display.write(DeviceTicket.alignLeft(m_displaylines.getLine2(), 20)); 
        //System.out.println("---------------------------------");
        //System.out.println(m_displaylines.getLine1());
        //System.out.println(" á é í ó ú ü ñ Á É Í Ó Ú Ü Ñ € ");
        //byte[] Bytes=m_displaylines.getLine1().getBytes();
        //byte[] Bytes=" á é í ó ú ü ñ Á É Í Ó Ú Ü Ñ € ".getBytes();
        //for(byte c: Bytes){
        //    System.out.format("%02x ", c & 0xff);            
        //}
        //System.out.println();
        //System.out.println("---------------------------------");
        
        //*** END Changed by RNDMNKIII
        display.flush();
    }
    
        /**
     *
     */
    @Override
    public void repaint4Lines() {
        //*** START Changed by RNDMNKIII
        //display.write(new byte[]{0x10, 0x00}); // VISOR HOME
        //display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine1(), 20)));
        //display.write(new byte[]{0x10, 0x14});
        //display.write(trans.transString(DeviceTicket.alignLeft(m_displaylines.getLine2(), 20))); 
        display.write(new byte[]{0x11}); //ARDUINO LCD VISOR HOME COMMAND
        display.write(DeviceTicket.alignLeft(m_displaylines.getLine1(), 20));
        display.write(new byte[]{0x12}); //ARDUINO LCD SECOND LINE COMMAND
        display.write(DeviceTicket.alignLeft(m_displaylines.getLine2(), 20));
        display.write(new byte[]{0x13}); //ARDUINO LCD THIRD LINE COMMAND
        display.write(DeviceTicket.alignLeft(m_displaylines.getLine3(), 20));
        display.write(new byte[]{0x14}); //ARDUINO LCD FOURTH LINE COMMAND
        display.write(DeviceTicket.alignLeft(m_displaylines.getLine4(), 20));
        
        //System.out.println("---------------------------------");
        //System.out.println(m_displaylines.getLine1());
        //System.out.println(" á é í ó ú ü ñ Á É Í Ó Ú Ü Ñ € ");
        //byte[] Bytes=m_displaylines.getLine1().getBytes();
        //byte[] Bytes=" á é í ó ú ü ñ Á É Í Ó Ú Ü Ñ € ".getBytes();
        //for(byte c: Bytes){
        //    System.out.format("%02x ", c & 0xff);            
        //}
        //System.out.println();
        //System.out.println("---------------------------------");
        
        //*** END Changed by RNDMNKIII
        display.flush();
    }
}
