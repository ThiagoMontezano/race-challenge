package com.montezano.race.http.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class RaceResultDataContract implements Serializable {

    private List<PositionDataContract> positions;

    private LapDataContract bestLapOfTheRace;

}
