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

package uk.chromis.pos.ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import uk.chromis.format.Formats;

/**
 *
 *   
 */
public class TicketTaxInfo {
    
    private final TaxInfo tax;
    //*** Inicio modificaciones RndMnkIII
    //private double subtotal;
    //private double taxtotal;
    private BigDecimal subtotalBD;
    private BigDecimal taxtotalBD;
    
            
    /** Creates a new instance of TicketTaxInfo
     * @param tax */
    public TicketTaxInfo(TaxInfo tax) {
        this.tax = tax;
        //subtotal = 0.0;
        //taxtotal = 0.0;
        subtotalBD = new BigDecimal("0.0").setScale(8,RoundingMode.HALF_UP);
        taxtotalBD = new BigDecimal("0.0").setScale(8,RoundingMode.HALF_UP);
    }
    
    /**
     *
     * @return
     */
    public TaxInfo getTaxInfo() {
        return tax;
    }
    
    /**
     *
     * @param dValue
     */
    public void add(double dValue) {
        //subtotal += dValue;
        //taxtotal = subtotal * tax.getRate();
        BigDecimal dValueBD=new BigDecimal(String.valueOf(dValue)).setScale(8,RoundingMode.HALF_UP);
        subtotalBD = subtotalBD.add(dValueBD);
        BigDecimal taxRateBD=new BigDecimal(String.valueOf(tax.getRate())).setScale(8,RoundingMode.HALF_UP);
        taxtotalBD = subtotalBD.multiply(taxRateBD).setScale(8,RoundingMode.HALF_UP);
    }
    
    /**
     *
     * @return
     */
    public double getSubTotal() {    
        //return subtotal;
        return subtotalBD.doubleValue();
    }
    
    /**
     *
     * @return
     */
    public double getTax() {       
        //return taxtotal;
        return taxtotalBD.doubleValue();
    }
    
    /**
     *
     * @return
     */
    public double getTotal() {         
        //return subtotal + taxtotal;
        return subtotalBD.add(taxtotalBD).doubleValue();
     //*** Fin modificaciones RndMnkIII  
    }
    
    /**
     *
     * @return
     */
    public String printSubTotal() {
        return Formats.CURRENCY.formatValue(new Double(getSubTotal()));
    }

    /**
     *
     * @return
     */
    public String printTax() {
        return Formats.CURRENCY.formatValue(new Double(getTax()));
    }    

    /**
     *
     * @return
     */
    public String printTotal() {
        return Formats.CURRENCY.formatValue(new Double(getTotal()));
    }    
}
