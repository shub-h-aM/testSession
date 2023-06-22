package main.java.dto.Inbound;

import main.java.dto.Inbound.GeneratePutItemDTO;

import java.util.List;

public class IBGeneratePutDTO {
    private List<GeneratePutItemDTO> generatePut;

    public List<GeneratePutItemDTO> getGeneratePut() {
        return generatePut;
    }

    public void setGeneratePut(List<GeneratePutItemDTO> generatePut) {
        this.generatePut = generatePut;
    }
}

