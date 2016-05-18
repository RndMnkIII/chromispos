18th May 2016
Relase:modified v0.56.2 branch NO OFFICIAL
***********
 IMPORTANT:
 **********
I've included several changes in the source code of the original 0.56.2 Chromis POS. This repository is related to https://github.com/RndMnkIII/arduinoPOS_Screen and should be seen as a proof of concept, not as a new release or official release
Mainly the class affected are in the pos.uk.chromis.pos.printer package: DisplayAnimator, BaseAnimator, DeviceDisplayBase, ...
and modified code in JPanelTicket adding the methods visorTicketLine2, visorTicketLine3 and modified calls to visorTicketLine to include additional data to display in the customer display (with 4 lines). Other changes affect to the method to count total number of items in the ticked to omit count discount lines. Not related but included there are changes in several classes in the pos.uk.chromis.pos.ticket package: TaxInfo, TicketInfo, TicketLineInfo and TicketTaxInfo including the use of BigDecimal class.

I've tweaked the DeviceDisplaySurePOS.java class to modify initVisor(),repaintLines() and repaint4Lines() (this customization added in DeviceDisplayImp interface) methods to use my custom control characters.
I've planned to create a new class inhereted from DeviceDisplaySerial for this in near future instead tweak this class

Other changes affects the ChromisPOS scripts in resources:
- Printer.TicketLine:
<output>
    <display>
        <line1>
            #if ($ticketline.isProductCom()) 
                <text align="left" length="20">*${ticketline.printName()}</text>
            #else
                 <text align="left" length="20">${ticketline.printName()}</text>
            #end            
        </line1>
        <line2>
           <text align="left" length="5">Prec.</text>
            <text align="right" length="9">${ticketline.printPrice()}</text>
            <text align="left" length="2"> X</text>
            <text align="right" length="4">${ticketline.printMultiply()}</text>
        </line2>
        <line3>
            <text align="left" length="10">Subtotal:</text>
            <text align="right" length="10">${ticketline.printSubValue()}</text>
        </line3>
        <line4>
            <text align="left" length="10">TOTAL+IVA:</text>
            <text align="right" length="10">${ticket.printTotal()}</text>
        </line4>
    </display>
</output>

- Printer.TicketLine2 :
<output>
    <display>
        <line1>
            #if ($ticketline.isProductCom()) 
                <text align="left" length="20">*${ticketline.printName()}</text>
            #else
                 <text align="left" length="20">${ticketline.printName()}</text>
            #end            
        </line1>
        <line2>
           <text align="left" length="5">Prec.</text>
            <text align="right" length="9">${ticketline.printPrice()}</text>
            <text align="left" length="2"> X</text>
            <text align="right" length="4">${ticketline.printMultiply()}</text>
        </line2>
        <line3>
            <text align="left" length="10">Subtotal:</text>
            <text align="right" length="10">${ticketline.printSubValue()}</text>
        </line3>
        <line4>
            <text align="left" length="4">${itemscount}</text>
            <text align="right" length="6">TOTAL</text>
            <text align="right" length="10">${total}</text>
        </line4>
    </display>
</output>
