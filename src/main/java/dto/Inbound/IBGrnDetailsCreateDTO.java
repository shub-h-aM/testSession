package main.java.dto.Inbound;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IBGrnDetailsCreateDTO {
    private int grnNumber;
    private int ibPreGrnDetailsId;
    private String ibRef;
    private String ibDate;
    private String itemCode;
    private int uomId;
    private int packUomId;
    private int itemStatusId;
    private int grnQty;
    private String taxPercent;
    private String batchNo;
    private String serialNo;
    private String lotNo;
    private String lpnNo;
    private String mfgDate;
    private String expDate;
    private String shipDate;
    private String expArrivalDate;
    private int countryId;
    private String eanCode;
    private String upcCode;
    private String hsnCode;
    private String colourCode;
    private String itemSize;
    private double itemCost;
    private double itemMrp;
    private String remarks;
    private String innerPackUomId;
    private int packCodeSeq;
    private String packingType;
    private String remarks2;
    private String remarks3;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String seqNo;


}


