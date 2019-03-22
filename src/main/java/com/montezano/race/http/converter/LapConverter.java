package com.montezano.race.http.converter;

import com.montezano.race.domains.Lap;
import com.montezano.race.domains.Pilot;
import com.montezano.race.http.FileConstants;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.montezano.race.http.FileConstants.AVERAGE_LAP_SPEED;

@Component
@NoArgsConstructor
public class LapConverter implements Converter<MultipartFile, List<Lap> > {

    @Override
    public List<Lap> convert(final MultipartFile file) {
        final List<String> fileLines = readFile(file);

        return fileLines.stream()
                .skip(1)
                .map(this::convertFileLineInLap)
                .collect(Collectors.toList());
    }

    private List<String> readFile(final MultipartFile source) {
        List<String> result = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(source.getInputStream()));
            result = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private Lap convertFileLineInLap(final String line) {
        if (!line.isEmpty()) {
            List<String> data = Arrays.asList(line.split(FileConstants.SPLIT_CHAR))
                    .stream()
                    .filter(d->!d.isEmpty())
                    .collect(Collectors.toList());

            return Lap
                    .builder()
                    .hour(LocalTime.parse(data.get(FileConstants.HOUR).trim()))
                    .pilot(createPilot(data.get(FileConstants.PILOT).trim()))
                    .number(Integer.valueOf(data.get(FileConstants.LAP_NUMBER).trim()))
                    .duration(createDuration(data.get(FileConstants.LAP_TIME).trim()))
                    .averageLapSpeed(getAverageLapSpeed(data.get(AVERAGE_LAP_SPEED)))
                    .build();
        }
        return Lap.builder().build();
    }

    public Pilot createPilot(final String pilotData) {
        final String pilot[] = pilotData.split(FileConstants.PILOT_SPLIT_CHAR);
        return new Pilot(pilot[0], pilot[1]);
    }

    public BigDecimal getAverageLapSpeed(final String averageLapSpeedData) {
        return new BigDecimal(averageLapSpeedData.replace(FileConstants.COMMA, FileConstants.DOT).trim());
    }

    public Duration createDuration(final String lapTimeData) {
        final String timeStamp = lapTimeData.replace(FileConstants.DOT, FileConstants.COLON);
        final String[] values = timeStamp.split(FileConstants.COLON);

        final Integer minutes = Integer.parseInt(values[0]);
        final Integer seconds = Integer.parseInt(values[1]);
        final Integer milliseconds = Integer.parseInt(values[2]);

        return Duration.ofMillis((60000 * minutes) + (1000 * seconds) + milliseconds);
    }
}
