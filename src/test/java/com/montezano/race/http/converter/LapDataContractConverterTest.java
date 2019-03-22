package com.montezano.race.http.converter;

import com.montezano.race.domains.Lap;
import com.montezano.race.domains.Pilot;
import com.montezano.race.http.data.LapDataContract;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LapDataContractConverterTest {

    private LapDataContractConverter lapDataContractConverter;

    private PilotDataContractConverter pilotDataContractConverter;

    @Test
    public void convert() {
        pilotDataContractConverter = new PilotDataContractConverter();
        lapDataContractConverter = new LapDataContractConverter(pilotDataContractConverter);

        Lap lap = fixtureLap();
        LapDataContract dataContract = lapDataContractConverter.convert(lap);

        assertThat(lap.getAverageLapSpeed(), is(dataContract.getAverageLapSpeed()));
        assertThat(lap.getHour(), is(dataContract.getHour()));
        assertThat(lap.getPilot().getId(), is(dataContract.getPilot().getId()));
        assertThat(lap.getPilot().getName(), is(dataContract.getPilot().getName()));
        assertThat(lap.getNumber(), is(dataContract.getNumber()));
        assertThat(lap.getDuration(), is(dataContract.getDuration()));

    }

    private Lap fixtureLap() {
        return Lap.builder()
                .averageLapSpeed(BigDecimal.TEN)
                .duration(Duration.ZERO)
                .hour(LocalTime.MIDNIGHT)
                .number(1)
                .pilot(fixturePilot())
                .build();
    }

    private Pilot fixturePilot() {
        return new Pilot("01","RodaDura");
    }
}