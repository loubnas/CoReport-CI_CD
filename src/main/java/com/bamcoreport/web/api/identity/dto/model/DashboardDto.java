package com.bamcoreport.web.api.identity.dto.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("Dashboard")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDto {
    private int NbrRejetUser;
    private int NbrRejetTotal;
    private List<RejetGroupedCountDto> NbrRejetType;
    private  List<RejetGroupedCountDto> NbrRejetDate;


}
