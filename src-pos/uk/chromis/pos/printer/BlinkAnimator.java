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

/**
 *
 * @author adrianromero
 */
public class BlinkAnimator extends BaseAnimator {
    
    /**
     *
     * @param line1
     * @param line2
     */
    public BlinkAnimator(String line1, String line2) {
        baseLine1 = DeviceTicket.alignLeft(line1, 20);
        baseLine2 = DeviceTicket.alignLeft(line2, 20);
        baseLine3 = null;
        baseLine4 = null;
    }
    
    /**
     *
     * @param line1
     * @param line2
     * @param line3
     * @param line4
     */
    public BlinkAnimator(String line1, String line2,String line3, String line4) {
        baseLine1 = DeviceTicket.alignLeft(line1, 20);
        baseLine2 = DeviceTicket.alignLeft(line2, 20);
        baseLine3 = DeviceTicket.alignLeft(line3, 20);
        baseLine4 = DeviceTicket.alignLeft(line4, 20);
    }
    
    /**
     *
     * @param i
     */
    @Override
    public void setTiming(int i) {
        
        if ((i % 10) < 5) {
            currentLine1 = "";
            currentLine2 = "";
        } else {
            currentLine1 = baseLine1;
            currentLine2 = baseLine2;
        }
//        if ((i % 10) < 5) {
//            currentLine1 = "";
//            currentLine2 = "";
//            currentLine3 = "";
//            currentLine4 = "";
//        } else {
//            currentLine1 = baseLine1;
//            currentLine2 = baseLine2;
//            currentLine3 = baseLine3;
//            currentLine4 = baseLine4;
//        }
    }
    
     /**
     *
     * @param i
     */
    @Override
    public void setTiming4lines(int i) {
        
        if ((i % 10) < 5) {
            currentLine1 = "";
            currentLine2 = "";
            currentLine3 = "";
            currentLine4 = "";
        } else {
            currentLine1 = baseLine1;
            currentLine2 = baseLine2;
            currentLine3 = baseLine3;
            currentLine4 = baseLine4;
        }
    }
}
