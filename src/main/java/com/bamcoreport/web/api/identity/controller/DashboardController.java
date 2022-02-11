package com.bamcoreport.web.api.identity.controller;


import com.bamcoreport.web.api.identity.dto.model.DashboardDto;
import com.bamcoreport.web.api.identity.dto.model.UserDto;
import com.bamcoreport.web.api.identity.entities.User;
import com.bamcoreport.web.api.identity.services.IRejetService;
import com.bamcoreport.web.api.identity.services.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/identity/dashboard")
@Api(tags = "dashboard ", value = "dashboard controller")

public class DashboardController {

    static  final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(GroupController.class.getName());

    @Autowired
    IRejetService rejetService;
    @Autowired
    IUserService userService;

    //------- Nombre rejet par user   : ----------------------------------------------------

    @GetMapping("/")
    @ApiOperation(value = "dahsboard", notes ="Cette methode permet de nous donner nombre de rejet par user & total des rejets & nombre de rejet par type et par date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "nombre rejet par user & total & type & date trouver dans la DB"),
            @ApiResponse(code = 404, message = "nombre rejet n'existe pas dans la  DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<DashboardDto> getNbrRejetParUser(HttpServletRequest req) {
        User cu=((User)req.getSession().getAttribute("connectedUser"));
        if(cu!=null) {
            UserDto u = userService.getById(cu.getId());
            DashboardDto d = new DashboardDto();
            d.setNbrRejetUser(rejetService.NbrRejetUser(u));
            d.setNbrRejetTotal(rejetService.NbrRejetTotal());
            d.setNbrRejetType(rejetService.NbrRejetType());
            d.setNbrRejetDate(rejetService.NbrRejetDate());
            log4j.info("nbr rejet par user");
            return ResponseEntity.ok(d);
        }else
            return new ResponseEntity<DashboardDto>(HttpStatus.LOCKED);
    }

    //-------------------------------------------------------------------------------------



}
