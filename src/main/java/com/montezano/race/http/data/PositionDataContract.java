package com.montezano.race.http.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Duration;

@Getter
@Setter
@Builder
@ToString
public class PositionDataContract {

    private Integer position;

    private PilotDataContract pilot;

    private Integer completedLaps;

    private Duration duration;

    private BigDecimal averageSpeed;

    private Duration delayAfterTheWinner;

    private LapDataContract bestLap;

    @JsonIgnore
    private LapDataContract lastLap;

}
