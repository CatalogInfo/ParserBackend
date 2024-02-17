package com.example.backend_parser.service.endpoints;

import com.example.backend_parser.entities.Options;
import com.example.backend_parser.repos.OptionsRepo;
import com.example.backend_parser.dtos.OptionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionsService {

    @Autowired
    OptionsRepo optionsRepo;

    public OptionsDto getOptions() {
        return new OptionsDto(
                Integer.parseInt(getOptionValue("minAmount", "2000")),
                Integer.parseInt(getOptionValue("minSpread", "2")),
                Integer.parseInt(getOptionValue("maxSpread", "20")));
    }

    public void setOptions(OptionsDto optionsDto) {
        updateOptionOrCreateIt("minAmount", "2000", optionsDto.getMinAmount());
        updateOptionOrCreateIt("minSpread", "2", optionsDto.getMinSpread());
        updateOptionOrCreateIt("maxSpread", "20", optionsDto.getMaxSpread());
    }

    private void updateOptionOrCreateIt(String name, String defaultValue, int value) {
        if(optionsRepo.existsByName(name)) {
            Options option = optionsRepo.findByName(name).get(0);
            option.setValue(String.valueOf(value));
            optionsRepo.save(option);
        } else {
            optionsRepo.save(new Options(name, defaultValue));
        }
    }

    private String getOptionValue(String name, String defaultValue) {
        if(!optionsRepo.existsByName(name)) {
            return new Options(name, defaultValue).getValue();
        }
        return optionsRepo.findByName(name).get(0).getValue();
    }
}
