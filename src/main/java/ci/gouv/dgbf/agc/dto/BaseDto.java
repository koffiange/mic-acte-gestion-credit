package ci.gouv.dgbf.agc.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class BaseDto {
    @Getter @Setter
    private String uuid;
    @Getter @Setter
    private LocalDateTime createdDate;
    @Getter @Setter
    private LocalDateTime updatedDate;
}
