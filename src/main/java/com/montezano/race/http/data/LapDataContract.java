package com.montezano.race.http.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class LapDataContract {

    private LocalTime hour;

    private PilotDataContract pilot;

    private Integer number;

    private Duration duration;

    private BigDecimal averageLapSpeed;

}
