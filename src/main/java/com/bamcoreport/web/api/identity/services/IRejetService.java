package com.bamcoreport.web.api.identity.services;

import com.bamcoreport.web.api.identity.dto.model.RejetDto;
import com.bamcoreport.web.api.identity.dto.model.RejetGroupedCountDto;
import com.bamcoreport.web.api.identity.dto.model.UserDto;
import com.bamcoreport.web.api.identity.entities.Rejet;

import java.time.LocalDateTime;
import java.util.List;

public interface IRejetService {
    List<RejetDto> getAllRejets();
    RejetDto addRejet(RejetDto rejet);
    boolean deleteRejet(long id);
    RejetDto updateRejet(RejetDto rejet);
    RejetDto getById(long id);

    // DASHBOARD //
    int NbrRejetUser(UserDto user);
    int NbrRejetTotal();
    List<RejetGroupedCountDto> NbrRejetType();
    List<RejetGroupedCountDto> NbrRejetDate();


}
