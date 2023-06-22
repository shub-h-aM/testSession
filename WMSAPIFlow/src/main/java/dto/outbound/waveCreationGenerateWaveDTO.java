package dto.outbound;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class waveCreationGenerateWaveDTO {
    private List<Integer> orderId;
    private String routeId;
    private int customerId;
    private int cityId;
    private int itemCategoryId;
    private int itemGroupId;
    private int itemSubgroupId;
    private int itemStatusId;
    private String orderTypeCode;
    private String priorityCode;
    private String vehicleTypeCode;
    private String transportMode;
    private String reqArrivalDate;
    private String scheduledArrivalDate;
    private String expectedShipDate;
    private String orderStopByDate;
    private String fromDate;
    private String toDate;
    private String basedOn;
    private String isLineLevel;


}
