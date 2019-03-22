package com.montezano.race.http.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PilotDataContract {

    private String id;

    private String name;
}
