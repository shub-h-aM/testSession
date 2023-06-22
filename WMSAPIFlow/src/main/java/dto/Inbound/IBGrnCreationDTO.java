package dto.Inbound;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IBGrnCreationDTO {
    private String grnType;
    private String grnDate;
    private int supplierId;
    private String supplierOrderNo;
    private String invoiceNo;
    private double invoiceValue;
    private String invoiceDate;
    private int taxGroupId;
    private String baName;
    private String vehicleNo;
    private String dockNo;
    private String ref1;
    private String ref2;
    private String ref3;
    private String specialInstructions1;
    private String specialInstructions2;
    private String specialInstructions3;
    private String forceUpload;


}

