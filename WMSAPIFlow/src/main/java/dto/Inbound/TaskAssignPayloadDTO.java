package dto.Inbound;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class TaskAssignPayloadDTO {

    private List<Integer> taskId;
    private int assignedTo;


    public void addTaskId(Integer taskId) {
    }

    public void addAssignTo(Integer userId) {
    }
}


