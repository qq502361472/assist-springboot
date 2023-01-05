package com.hjrpc.assistspringboot.dto;

import com.hjrpc.assistspringboot.annoation.DictValue;
import lombok.Data;

import java.util.List;

@Data
public class PersonDTO {
    @DictValue(dictCode = "DICT_STATUS")
    private int status;
    @DictValue(targetFieldName = "ageValue",dictCode = "DICT_AGE")
    private int age;

    private String name;

    private List<PersonDTO> child;
}
