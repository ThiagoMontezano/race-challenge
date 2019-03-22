package com.montezano.race.usecases;

import com.montezano.race.domains.Lap;
import com.montezano.race.http.converter.LapConverter;
import com.montezano.race.http.converter.LapDataContractConverter;
import com.montezano.race.http.converter.PilotDataContractConverter;
import com.montezano.race.http.data.LapDataContract;
import com.montezano.race.http.data.PositionDataContract;
import com.montezano.race.http.data.RaceResultDataContract;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class RaceManagerTest {

    private RaceManager raceManager;

    private LapDataContractConverter lapDataContractConverter;

    private PilotDataContractConverter pilotDataContractConverter;

    @Before
    public void before() {
        pilotDataContractConverter = new PilotDataContractConverter();
        lapDataContractConverter = new LapDataContractConverter(pilotDataContractConverter);
        raceManager = new RaceManager(lapDataContractConverter);
    }

    @Test
    public void getRaceInformation() throws IOException {
        RaceResultDataContract raceInformation = raceManager.getRaceInformation(fixtureListLaps());
        assertNotNull(raceInformation);
        validateBestOfLap(raceInformation.getBestLapOfTheRace());
        validatePositions(raceInformation.getPositions());
    }

    private void validatePositions(List<PositionDataContract> positions) {
        assertThat(positions.stream().count(), is(6L));
        assertThat(positions.get(0).getPilot().getName(), is("F.MASSA"));
        assertThat(positions.get(1).getPilot().getName(), is("K.RAIKKONEN"));
        assertThat(positions.get(2).getPilot().getName(), is("R.BARRICHELLO"));
        assertThat(positions.get(3).getPilot().getName(), is("M.WEBBER"));
        assertThat(positions.get(4).getPilot().getName(), is("F.ALONSO"));
        assertThat(positions.get(5).getPilot().getName(), is("S.VETTEL"));
    }

    private void validateBestOfLap(final LapDataContract bestLapOfTheRace) {
        assertThat(bestLapOfTheRace.getAverageLapSpeed().toString(),is("44.334"));
        assertThat(bestLapOfTheRace.getHour().toString(),is("23:51:14.216"));
        assertThat(bestLapOfTheRace.getNumber(),is(3));
        assertThat(bestLapOfTheRace.getDuration().toString(),is("PT1M2.769S"));
        assertThat(bestLapOfTheRace.getPilot().getId(),is("038"));
        assertThat(bestLapOfTheRace.getPilot().getName(),is("F.MASSA"));

    }

    private List<Lap> fixtureListLaps() throws IOException {
        LapConverter lapConverter = new LapConverter();

        MultipartFile multipartFile = new MockMultipartFile("race.txt",
                new FileInputStream(new File("src/test/resources/file/race.txt")));

        return lapConverter.convert(multipartFile);
    }

}