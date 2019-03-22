package com.montezano.race.http.converter;

import com.montezano.race.domains.Lap;
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

public class LapConverterTest {

    private LapConverter lapConverter;

    @Test
    public void convert() throws IOException {
        lapConverter = new LapConverter();

        MultipartFile multipartFile = new MockMultipartFile("race.txt",
                new FileInputStream(new File("src/test/resources/file/race.txt")));

        assertNotNull(multipartFile);

        List<Lap> laps = lapConverter.convert(multipartFile);

        assertNotNull(laps);
        assertThat(laps.size(), is(23));

        checkFirstLine(laps.get(0));

    }

    private void checkFirstLine(final Lap lap) {
        assertThat(lap.getAverageLapSpeed().toString(), is("44.275"));
        assertThat(lap.getHour().toString(), is("23:49:08.277"));
        assertThat(lap.getPilot().getId(), is("038"));
        assertThat(lap.getPilot().getName(), is("F.MASSA"));
        assertThat(lap.getNumber(), is(1));
        assertThat(lap.getDuration().toString(), is("PT1M2.852S"));

    }
}