package com.montezano.race.usecases;

import com.montezano.race.domains.Lap;
import com.montezano.race.domains.Pilot;
import com.montezano.race.http.converter.LapDataContractConverter;
import com.montezano.race.http.data.PilotDataContract;
import com.montezano.race.http.data.PositionDataContract;
import com.montezano.race.http.data.RaceResultDataContract;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RaceManager {

    private final LapDataContractConverter lapDataContractConverter;

    public RaceResultDataContract getRaceInformation(final List<Lap> laps) {
        return RaceResultDataContract.builder()
                .bestLapOfTheRace(lapDataContractConverter.convert(getBestLap(laps)))
                .positions(getPositions(laps))
                .build();
    }

    private List<PositionDataContract> getPositions(final List<Lap> laps) {
        final List<PositionDataContract> dataContracts = new ArrayList<>();
        final AtomicInteger position = new AtomicInteger(0);
        final Map<Pilot, Lap> allLastLaps = getAllLastLaps(laps);

        laps
                .stream()
                .map(lapRecord -> lapRecord.getPilot().getId())
                .distinct()
                .forEach(id -> {
                    final List<Lap> allLapsOfThePilot = getAllTheLapsOfThePilot(laps, id);
                    final Duration duration = getDurationOfThePilotRace(allLapsOfThePilot);
                    dataContracts.add(createPosition(allLapsOfThePilot, duration, allLastLaps));
                });

        final List<PositionDataContract> sortedPositions = sortPositionByDuration(dataContracts);

        final PositionDataContract firstPosition = sortedPositions.stream().findFirst().get();

        return  sortedPositions
                        .stream()
                        .map(positionDataContract -> addDelayAfterTheWinnerAndNumberOfPosition(position, firstPosition, positionDataContract))
                        .collect(Collectors.toList());

    }

    private PositionDataContract createPosition(final List<Lap> allLapsOfThePilot,
                                                final Duration duration,
                                                final Map<Pilot, Lap> allLastLaps) {
        final Pilot pilot = allLapsOfThePilot.get(0).getPilot();
        final Lap pilotLastLap = allLastLaps.get(pilot);

        return PositionDataContract.builder()
                .averageSpeed(getAverageSpeed(allLapsOfThePilot))
                .bestLap(lapDataContractConverter.convert(getBestLap(allLapsOfThePilot)))
                .completedLaps(pilotLastLap.getNumber())
                .lastLap(lapDataContractConverter.convert(pilotLastLap))
                .duration(duration)
                .pilot(PilotDataContract.builder()
                        .id(pilot.getId())
                        .name(pilot.getName())
                        .build())
                .build();
    }

    private PositionDataContract addDelayAfterTheWinnerAndNumberOfPosition(final AtomicInteger position,
                                                                           final PositionDataContract firstPosition,
                                                                           final PositionDataContract positionDataContract) {
        return PositionDataContract.builder()
                .averageSpeed(positionDataContract.getAverageSpeed())
                .bestLap(positionDataContract.getBestLap())
                .completedLaps(positionDataContract.getCompletedLaps())
                .duration(positionDataContract.getDuration())
                .pilot(positionDataContract.getPilot())
                .delayAfterTheWinner(getDelay(firstPosition.getDuration(), positionDataContract.getDuration()))
                .position(position.incrementAndGet())
                .build();
    }

    private List<PositionDataContract> sortPositionByDuration(final List<PositionDataContract> dataContracts) {
        return dataContracts
                .stream()
                .sorted(Comparator.comparing(PositionDataContract::getDuration))
                .collect(Collectors.toList());
    }

    private Duration getDurationOfThePilotRace(final List<Lap> allLapsOfThePilot) {
        return allLapsOfThePilot
                .stream()
                .map(lapRecord -> lapRecord.getDuration())
                .reduce((firstValue, secondValue) -> firstValue.plus(secondValue))
                .get();
    }

    private List<Lap> getAllTheLapsOfThePilot(final List<Lap> laps, final String pilotId) {
        return laps
                .stream()
                .filter(lap -> lap.getPilot().getId().equalsIgnoreCase(pilotId))
                .collect(Collectors.toList());
    }

    private Map<Pilot, Lap> getAllLastLaps(final List<Lap> laps) {
        return laps.stream().collect(
                Collectors.groupingBy(Lap::getPilot,
                        Collectors.collectingAndThen(
                                Collectors.reducing((Lap l1, Lap l2) -> l1.getNumber() > l2.getNumber() ? l1 : l2),
                                Optional::get)));
    }

    private Lap getBestLap(final List<Lap> laps){
        return laps.stream().min(Comparator.comparing(Lap::getDuration)).get();
    }

    private BigDecimal getAverageSpeed(final List<Lap> laps){
        return laps
                .stream()
                .map(lapRecord -> lapRecord.getAverageLapSpeed())
                .reduce((firstValue, secondValue) -> firstValue.add(secondValue))
                .get()
                .divide(new BigDecimal(laps.size()), RoundingMode.UP);
    }

    private Duration getDelay(final Duration durationFromFirstPosition, final Duration durationFromOtherPosition){
        return durationFromOtherPosition.minus(durationFromFirstPosition);
    }
}
