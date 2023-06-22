package main.java.dto.Inbound;

import dto.Inbound.ConfirmPutItemDTO;

import java.util.List;

public class IBPutConfirmDtoListData {

    private List<ConfirmPutItemDTO> confirmPut;

    public List<ConfirmPutItemDTO> getConfirmPut() {
        return confirmPut;
    }

    public void setConfirmPut(List<ConfirmPutItemDTO> confirmPut) {
        this.confirmPut = confirmPut;
    }
}

