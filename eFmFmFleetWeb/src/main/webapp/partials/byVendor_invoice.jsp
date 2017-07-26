<!-- 
@date           03/01/2016
@Author         Saima Aziz
@Description    This template is called by the home.invoice to show the Invoice by Vendor
@State          
@URL              

CODE CHANGE HISTORY
-----------------------------------------------------------------------------
Date        Author          Changes
-----------------------------------------------------------------------------
03/01/2016  Saima Aziz      Initial Creation
04/20/2016  Saima Aziz      Final Creation
-->
         
                           
    <div class = "col-md-4 col-xs-12 col-md-offset-4" ng-show = "byVendorInvoicButtonClicked">
        <img class = "spinner02" src = "images/spinner02.gif" alt = "Getting Result..">
    </div>    

    <!-- Shell Branch Invoice - Contract & Contract Type Values - Single & Contract Type - FDC -->
    <div ng-show="branchCode != 'SBOBNG' && contractTypeValues == 'contractSingle' && invoiceByVendorData.contractType == 'FDC'">
            <div ng-include = "'partials/modals/invoice/Vendor/Vendor_invoice_Shell.jsp'" ></div>
    </div>

    <!-- Shell Branch Invoice - Contract & Contract Type Values - Single & Contract Type - PDDC -->
    <div  ng-show="branchCode != 'SBOBNG' && contractTypeValues == 'contractSingle' && invoiceByVendorData.contractType == 'PDDC'">
            <div ng-include = "'partials/modals/invoice/Vendor/Vendor_invoice_Bangalore.jsp'"></div>
    </div>
    
    <!-- Newt Demo Invoice - Contract & Contract Type - Single-->
    <div  ng-show="branchCode == 'Newt'">
            <div ng-include = "'partials/modals/invoice/Vendor/Vendor_invoice_Shell.jsp'"></div>  
    </div>

    <!-- Bangalore Branch Invoice - Contract Bangalore & Contract Type values - Single & Contract Type - PDDC-->
    <div ng-show="branchCode == 'SBOBNG' && contractTypeValues == 'contractSingle' && invoiceByVendorData.contractType == 'PDDC'">
            <div ng-include = "'partials/modals/invoice/Vendor/Vendor_invoice_Bangalore.jsp'"></div> 
    </div>
    
    <!-- Branch - Shell & Contract Type values - Multiple -->
    <div  ng-show="branchCode != 'SBOBNG' && contractTypeValues == 'contractMultiple'">
        <div ng-include = "'partials/modals/invoice/Vendor/Vendor_invoice_MultipleContract.jsp'"></div>
    </div>

    <!-- Branch - Bangalore & Contract Type values - Multiple -->
     <div  ng-show="branchCode == 'SBOBNG' && contractTypeValues == 'contractMultiple'">
        <div ng-include = "'partials/modals/invoice/Vendor/Vendor_invoice_MultipleContract.jsp'"></div>
    </div>