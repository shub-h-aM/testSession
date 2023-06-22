package dto.Inbound;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GrnItemDto {
    private int grnNumber;

    public GrnItemDto(String grnNumber) {
        this.grnNumber = Integer.parseInt(grnNumber);
    }



}
