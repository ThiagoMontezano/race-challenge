package com.montezano.race.http.converter;

import com.montezano.race.domains.Pilot;
import com.montezano.race.http.data.PilotDataContract;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PilotDataContractConverter implements Converter<Pilot, PilotDataContract> {

    @Override
    public PilotDataContract convert(final Pilot source) {
        return PilotDataContract.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

}
