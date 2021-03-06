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
public class NullAnimator implements DisplayAnimator {
    
    protected String currentLine1;
    protected String currentLine2;
    protected String currentLine3;
    protected String currentLine4;

    /**
     *
     * @param line1
     * @param line2
     */
    public NullAnimator(String line1, String line2) {
        currentLine1 = DeviceTicket.alignLeft(line1, 20);
        currentLine2 = DeviceTicket.alignLeft(line2, 20);
        currentLine3 = null;
        currentLine4 = null;
    }
    
        /**
     *
     * @param line1
     * @param line2
     * @param line3
     * @param line4
     */
    public NullAnimator(String line1, String line2,String line3, String line4) {
        currentLine1 = DeviceTicket.alignLeft(line1, 20);
        currentLine2 = DeviceTicket.alignLeft(line2, 20);
        currentLine3 = DeviceTicket.alignLeft(line3, 20);
        currentLine4 = DeviceTicket.alignLeft(line4, 20);
    }

    /**
     *
     * @param i
     */
    @Override
    public void setTiming(int i) {
    }
    
    /**
     *
     * @param i
     */
    @Override
    public void setTiming4lines(int i) {
    }

    /**
     *
     * @return
     */
    @Override
    public String getLine1() {
        return currentLine1;
    }

    /**
     *
     * @return
     */
    @Override
    public String getLine2() {
        return currentLine2;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getLine3() {
        return currentLine3;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getLine4() {
        return currentLine4;
    }
}
