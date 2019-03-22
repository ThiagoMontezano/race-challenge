package com.montezano.race.domains;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

@Builder
@Getter
@Setter
public class Lap {

	private LocalTime hour;

	private Pilot pilot;

	private Integer number;

	private Duration duration;

	private BigDecimal averageLapSpeed;

}
