package com.bamcoreport.web.api.identity.dto.model;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("RejetTypeCount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejetGroupedCountDto {
    private Object value;
    private Long count;
}
