package dto.Inbound;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneratePutItemDTO {
    private int grnDetailsId;
    private int grnNumber;
    private int grnConfirmationId;
    private int pendingQty;

   }
