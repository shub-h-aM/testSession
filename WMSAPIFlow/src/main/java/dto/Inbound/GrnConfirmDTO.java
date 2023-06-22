package dto.Inbound;

import java.util.List;

public class GrnConfirmDTO {
    private List<GrnItemDto> grn;

    public GrnConfirmDTO(List<GrnItemDto> grn) {
        this.grn = grn;
    }

    public List<GrnItemDto> getGrn() {
        return grn;
    }

    public void setGrn(List<GrnItemDto> grn) {
        this.grn = grn;
    }
}

