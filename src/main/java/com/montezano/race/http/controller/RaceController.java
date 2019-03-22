package com.montezano.race.http.controller;

import com.montezano.race.http.UrlMapping;
import com.montezano.race.http.converter.LapConverter;
import com.montezano.race.http.data.RaceResultDataContract;
import com.montezano.race.usecases.RaceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(UrlMapping.RACE)
@RequiredArgsConstructor
public class RaceController {

    private final RaceManager raceManager;

    private final LapConverter lapConverter;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = {
            "multipart/form-data", MediaType.APPLICATION_JSON_VALUE })
    public RaceResultDataContract uploadFile(@RequestParam("file") MultipartFile file){
        return raceManager.getRaceInformation(lapConverter.convert(file));
    }

}
