package main.java.dto.outbound;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WaveCreationDTO {
    @JsonProperty("orderId")
    private List<Integer> orderId;

    @JsonProperty("routeId")
    private String routeId;

    @JsonProperty("customerId")
    private int customerId;

    @JsonProperty("cityId")
    private int cityId;

    @JsonProperty("itemCategoryId")
    private int itemCategoryId;

    @JsonProperty("itemGroupId")
    private int itemGroupId;

    @JsonProperty("itemSubgroupId")
    private int itemSubgroupId;

    @JsonProperty("itemStatusId")
    private int itemStatusId;

    @JsonProperty("orderTypeCode")
    private String orderTypeCode;

    @JsonProperty("priorityCode")
    private String priorityCode;

    @JsonProperty("vehicleTypeCode")
    private String vehicleTypeCode;

    @JsonProperty("transportMode")
    private String transportMode;

    @JsonProperty("reqArrivalDate")
    private String reqArrivalDate;

    @JsonProperty("scheduledArrivalDate")
    private String scheduledArrivalDate;

    @JsonProperty("expectedShipDate")
    private String expectedShipDate;

    @JsonProperty("orderStopByDate")
    private String orderStopByDate;

    @JsonProperty("fromDate")
    private String fromDate;

    @JsonProperty("toDate")
    private String toDate;

    @JsonProperty("basedOn")
    private String basedOn;

    @JsonProperty("isLineLevel")
    private String isLineLevel;
}
