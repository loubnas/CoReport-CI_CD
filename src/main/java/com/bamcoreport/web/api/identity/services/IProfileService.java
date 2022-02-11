package com.bamcoreport.web.api.identity.services;

import com.bamcoreport.web.api.identity.dto.model.ProfileDto;
import com.bamcoreport.web.api.identity.dto.model.RoleDto;
import com.bamcoreport.web.api.identity.dto.model.UserDto;

import java.util.List;

public interface IProfileService {
    List<ProfileDto> getAllProfils();
    ProfileDto addProfil(ProfileDto profileDto);
    boolean deleteProfil(long id);
    ProfileDto updateProfil(ProfileDto profileDto);
    ProfileDto getById(long id);

}
