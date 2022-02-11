package com.bamcoreport.web.api.identity.repositories;

import com.bamcoreport.web.api.identity.dto.model.RejetGroupedCountDto;
import com.bamcoreport.web.api.identity.entities.Rejet;
import com.bamcoreport.web.api.identity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface  RejetRepository extends JpaRepository<Rejet,Long> {

    @Query("select count(r)  as nbr from Rejet  r where r.TakenBy=?1 ")
    int NbrRejetUser(User user);


    @Query("select count(r)  as total from Rejet r")
    int NbrRejetTotal();


    @Query("select new com.bamcoreport.web.api.identity.dto.model.RejetGroupedCountDto(r.flowType,count(r))  from Rejet r group by r.flowType")
    List<RejetGroupedCountDto> NbrRejetType();


    @Query("select  new com.bamcoreport.web.api.identity.dto.model.RejetGroupedCountDto(r.declarationDate,count(r))  from  Rejet r group by r.declarationDate")
    List<RejetGroupedCountDto> NbrRejetDate();


}
