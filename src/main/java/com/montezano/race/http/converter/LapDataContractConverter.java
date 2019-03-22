package com.montezano.race.http.converter;

import com.montezano.race.domains.Lap;
import com.montezano.race.http.data.LapDataContract;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static org.springframework.beans.BeanUtils.copyProperties;

@Component
@RequiredArgsConstructor
public class LapDataContractConverter implements Converter<Lap, LapDataContract> {

    private final PilotDataContractConverter pilotDataContractConverter;

    @Override
    public LapDataContract convert(final Lap lap) {
        LapDataContract dataContract = copy(lap, new LapDataContract());
        dataContract.setPilot(pilotDataContractConverter.convert(lap.getPilot()));
        return dataContract;
    }

    private <I,O> O copy(I source, O destination){
        copyProperties(source, destination);
        return destination;
    }

}
