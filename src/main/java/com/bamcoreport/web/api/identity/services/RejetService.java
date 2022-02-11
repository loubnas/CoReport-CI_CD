package com.bamcoreport.web.api.identity.services;

import com.bamcoreport.web.api.identity.dto.model.RejetDto;
import com.bamcoreport.web.api.identity.dto.model.RejetGroupedCountDto;
import com.bamcoreport.web.api.identity.dto.model.RoleDto;
import com.bamcoreport.web.api.identity.dto.model.UserDto;
import com.bamcoreport.web.api.identity.dto.services.IMapClassWithDto;
import com.bamcoreport.web.api.identity.entities.Rejet;
import com.bamcoreport.web.api.identity.entities.Role;
import com.bamcoreport.web.api.identity.entities.User;
import com.bamcoreport.web.api.identity.helpers.HelpUpdate;
import com.bamcoreport.web.api.identity.repositories.RejetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RejetService implements IRejetService {

    static final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(RejetService.class.getName());


    @Autowired
    RejetRepository rejetRepository;

    @Autowired
    IMapClassWithDto<Rejet, RejetDto> rejetMapping;
    @Autowired
    IMapClassWithDto<User, UserDto> userMapping;


    //---- Get all rejets  : --------------------------------------------------------------------


    @Override
    public List<RejetDto> getAllRejets() {
        List<Rejet> rejets = rejetRepository.findAll();
        return rejetMapping.convertListToListDto(rejets, RejetDto.class);
    }

    //---------------------------------------------------------------------------------------------


    //---- Ajouter un role : --------------------------------------------------------------------

    @Override
    public RejetDto addRejet(RejetDto rejet) {

        Rejet rejetEntity = rejetMapping.convertToEntity(rejet, Rejet.class);
        rejetEntity = rejetRepository.save(rejetEntity);
        return rejetMapping.convertToDto(rejetEntity, RejetDto.class);
    }

    //---------------------------------------------------------------------------------------------


    //-- Supprimer un rejet -------------------------------------------------------------------------

    @Override
    public boolean deleteRejet(long id) {
        try {
            rejetRepository.deleteById(id);
        } catch (Exception ex) {
            log4j.error(ex.getMessage());
            return false;
        }
        return true;
    }

    //----------------------------------------------------------------------------------------------


    // --- Update un role : -------------------------------------------------------------------------

    @Override
    public RejetDto updateRejet(RejetDto rejet) {
        RejetDto saved = rejetMapping.convertToDto(rejetRepository.getById(rejet.getId()), RejetDto.class); // DB
        HelpUpdate.copyNonNullProperties(rejet, saved);
        Rejet rejetEntity = rejetMapping.convertToEntity(saved, Rejet.class);
        rejetEntity = rejetRepository.save(rejetEntity);
        return rejetMapping.convertToDto(rejetEntity, RejetDto.class);
    }

    //---------------------------------------------------------------------------------------------


    //----------------- Get rejet by id  : -------------------------------------------------------

    @Override
    public RejetDto getById(long id) {
        Rejet rejet = rejetRepository.getById(id);
        return rejetMapping.convertToDto(rejet, RejetDto.class);
    }

    //------------------------------------------------------------------------------------------------


    //-------- DASHBOARD :----------------------------------------
    //-------------------------------------------------------------


    //------ nombre de rejet par utilisateur ----------------------------------------------------------

    @Override
    public int NbrRejetUser(UserDto user) {
        User u= userMapping.convertToEntity(user,User.class);
        int rejet= rejetRepository.NbrRejetUser(u);
        return rejet;
    }

    //--------------------------------------------------------------------------------------------------


    //------ nombre total des rejets :------------------------------------------------------------------

    @Override
    public int NbrRejetTotal() {
     int rejet= rejetRepository.NbrRejetTotal();
        return rejet;
    }

    //-------------------------------------------------------------------------------------------------


    //------------------ nombre rejet par type :------------------------------------------------------

    @Override
    public  List<RejetGroupedCountDto> NbrRejetType() {
        List<RejetGroupedCountDto> rejet = rejetRepository.NbrRejetType();
        return rejet;
    }

    //-------------------------------------------------------------------------------------------------


    //--------------- nombre rejet par date : ---------------------------------------------------------

    @Override
    public  List<RejetGroupedCountDto> NbrRejetDate() {
        List<RejetGroupedCountDto> rejet=rejetRepository.NbrRejetDate();
        return rejet;
    }

    //-------------------------------------------------------------------------------------------------




}