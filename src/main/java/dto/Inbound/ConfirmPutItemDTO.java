package main.java.dto.Inbound;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmPutItemDTO {
    private int putId;
    private int putDetailsId;
    private int putTransId;
    private int putterId;
    private int confirmQty;
    private int parentId;
    private int liId;

}
