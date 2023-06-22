package main.java.dto.Inbound;

import java.util.List;

public class GRNDetailsRequest {
    private List<IBGrnDetailsCreateDTO> grnDetails;

    public GRNDetailsRequest(List<IBGrnDetailsCreateDTO> grnDetails) {
        this.grnDetails = grnDetails;
    }

    public List<IBGrnDetailsCreateDTO> getGrnDetails() {
        return grnDetails;
    }

    public void setGrnDetails(List<IBGrnDetailsCreateDTO> grnDetails) {
        this.grnDetails = grnDetails;
    }}
