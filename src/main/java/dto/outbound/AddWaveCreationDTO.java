package main.java.dto.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AddWaveCreationDTO {
    @JsonProperty("basedOn")
    private String basedOn;

    @JsonProperty("waveLineLevelDetails")
    private List<WaveLineLevelDetail> waveLineLevelDetails;

    public AddWaveCreationDTO() {
        waveLineLevelDetails = new ArrayList<>();
    }

    public List<WaveLineLevelDetail> getWaveLineLevelDetails() {
        return waveLineLevelDetails;
    }

    public void setWaveLineLevelDetails(List<WaveLineLevelDetail> waveLineLevelDetails) {
        this.waveLineLevelDetails = waveLineLevelDetails;
    }

    public void addWaveLineLevelDetail(int orderId, int orderDetailId) {
        WaveLineLevelDetail waveLineLevelDetail = new WaveLineLevelDetail(orderId, orderDetailId);
        waveLineLevelDetails.add(waveLineLevelDetail);
    }

    private static class WaveLineLevelDetail {
        @JsonProperty("orderId")
        private int orderId;

        @JsonProperty("orderDetailId")
        private int orderDetailId;

        public WaveLineLevelDetail(int orderId, int orderDetailId) {
            this.orderId = orderId;
            this.orderDetailId = orderDetailId;
        }
    }
}
