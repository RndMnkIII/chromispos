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
public abstract class BaseAnimator implements DisplayAnimator {
    
    /**
     *
     */
    protected String baseLine1;

    /**
     *
     */
    protected String baseLine2;
        /**
     *
     */
    protected String baseLine3;

    /**
     *
     */
    protected String baseLine4;

    /**
     *
     */
    protected String currentLine1;

    /**
     *
     */
    protected String currentLine2;
    
        /**
     *
     */
    protected String currentLine3;

    /**
     *
     */
    protected String currentLine4;

    /**
     *
     */
    public BaseAnimator() {
        baseLine1 = null;
        baseLine2 = null;
        baseLine3 = null;
        baseLine4 = null;
    }

    /**
     *
     * @param line1
     * @param line2
     */
    public BaseAnimator(String line1, String line2) {
        baseLine1 = line1;
        baseLine2 = line2;
        //baseLine3 = null;
        //baseLine4 = null;
    }
    
    public BaseAnimator(String line1, String line2, String line3, String line4) {
        baseLine1 = line1;
        baseLine2 = line2;
        baseLine3 = line3;
        baseLine4 = line4;
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
