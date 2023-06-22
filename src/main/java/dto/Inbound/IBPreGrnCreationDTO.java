package main.java.dto.Inbound;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IBPreGrnCreationDTO {

        private String ibpgDate;
        private String ibpgType;
        private String invoiceNo;
        private int invoiceValue;
        private String invoiceDate;
        private String vehicleNo;
        private String dockNo;
        private int supplierId;
        private String supplierOrderNo;
        private int taxGroupId;
        private String baName;
        private String ref1;
        private String ref2;
        private String ref3;
        private String specialInstructions1;
        private String specialInstructions2;
        private String specialInstructions3;


    }
