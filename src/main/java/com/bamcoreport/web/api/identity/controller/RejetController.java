package com.bamcoreport.web.api.identity.controller;

import com.bamcoreport.web.api.identity.dto.model.RejetDto;
import com.bamcoreport.web.api.identity.dto.model.RoleDto;
import com.bamcoreport.web.api.identity.dto.model.UserDto;
import com.bamcoreport.web.api.identity.dto.model.UserMembershipDto;
import com.bamcoreport.web.api.identity.entities.Rejet;
import com.bamcoreport.web.api.identity.entities.User;
import com.bamcoreport.web.api.identity.entities.UserMembership;
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
import java.util.List;

@RestController
@RequestMapping("/api/identity/rejet")
@Api(tags = "Rejet  d'un utilisateur ", value = "Rejet controller")

public class RejetController {

    static  final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(RejetController.class.getName());

    @Autowired
    IRejetService rejetService;
    @Autowired
    IUserService userService;



    //------- All rejets : -------------------------------------------------------------------

    @GetMapping("/")
    @ApiOperation(value = "Afficher la liste des rejts  ", notes ="Cette methode permet d'afficher une liste des rejets ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Liste des rejets trouvé dans BD"),
            @ApiResponse(code = 404, message = "Liste des rejets  n'existe pas dans BD"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")


    })

    public ResponseEntity<List<RejetDto>> getListRejets(){
        List<RejetDto> rejet =rejetService.getAllRejets();
        log4j.info("Liste des rejets");
        return ResponseEntity.ok(rejet);
    }

    //-------------------------------------------------------------------------------------


    // ----> Un admin peut créer un nouveau rejet :

    @PostMapping(value="/addRejet", headers="Accept=application/json")
    @ApiOperation(value = "Ajouter un rejet", notes ="Cette methode permet d'ajouter un rejet")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rejet ajouter a la DB"),
            @ApiResponse(code = 404, message = "Rejet n'est pas ajouter a la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

        public ResponseEntity<RejetDto> addRejet(@RequestBody RejetDto rejetDto, HttpServletRequest req) {
        UserDto u=userService.getById(((User)req.getSession().getAttribute("connectedUser")).getId());
       if(u!=null){
        // test admin
           boolean isAdmin=false;
        for(UserMembership ums:u.getUserMemberships()){
            if(ums.getRoleId().getName().equals("ADMIN")){
                isAdmin=true;
                break;
            }
        }
        if(isAdmin) {
            RejetDto rj = rejetService.addRejet(rejetDto);
            log4j.info("Ajouter un rejet");
            return ResponseEntity.ok(rj);
        }else{
            return  new ResponseEntity<RejetDto>(HttpStatus.LOCKED);
        }
        }
        return  new ResponseEntity<RejetDto>(HttpStatus.LOCKED);

    }

    //-------------------------------------------------------------------------------------



    //---> Un super admin / admin fonctionnel peut corriger le rejet lui-même :

    @PutMapping ("/UpdateRejet")
    @ApiOperation(value = "Updater un rejet", notes ="Cette methode permet de faire une mise à jour d'un rejet")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rejet updater dans la DB"),
            @ApiResponse(code = 404, message = "Rejet n'est pas updater dans la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<RejetDto> UpdateRejet(@RequestBody RejetDto rejetDto,HttpServletRequest req){
        UserDto u=userService.getById(((User)req.getSession().getAttribute("connectedUser")).getId());
        if(u!=null){

            boolean isAllowed=false;

            //---> Un utilisateur peut prendre le rejet en charge, et le corriger

            RejetDto rdb=rejetService.getById(rejetDto.getId()); //DB
            if(rdb.getTakenBy()!=null){
                if(rdb.getTakenBy().getId()==u.getId()){
                    isAllowed=true;
                }
            }
            // test Super Admin & F_admin
            for(UserMembership ums:u.getUserMemberships()){
                if(ums.getRoleId().getName().equals("S_ADMIN") || ums.getRoleId().getName().equals("F_ADMIN")  ){
                    isAllowed=true;
                    break;
                }
            }
            if(isAllowed) {

                rejetDto.setTakenBy(null); //request
                RejetDto rjt = rejetService.updateRejet(rejetDto);
                log4j.info("Updater un rejet");
                return ResponseEntity.ok(rjt);
            }else{
                return  new ResponseEntity<RejetDto>(HttpStatus.LOCKED);
            }
        }
        return  new ResponseEntity<RejetDto>(HttpStatus.LOCKED);
    }


    // ----> Un super admin / admin fonctionnel peut affecter un rejet à un utilisateur :

    @PutMapping ("/setTakenBy")
    @ApiOperation(value = "Updater un rejet", notes ="Cette methode permet de faire une mise à jour d'un rejet")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rejet updater dans la DB"),
            @ApiResponse(code = 404, message = "Rejet n'est pas updater dans la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<RejetDto> setTakenBy(@RequestBody RejetDto rejetDto,HttpServletRequest req){
        UserDto u=userService.getById(((User)req.getSession().getAttribute("connectedUser")).getId());
        if(u!=null){
            // test admin
            boolean isAdmin=false;
            for(UserMembership ums:u.getUserMemberships()){
                if(ums.getRoleId().getName().equals("S_ADMIN") || ums.getRoleId().getName().equals("F_ADMIN")  ){
                    isAdmin=true;
                    break;
                }
            }
            // update takenBy :
            if(isAdmin) {
                RejetDto nr=new RejetDto();
                nr.setId(rejetDto.getId());
                nr.setTakenBy(rejetDto.getTakenBy());

                RejetDto rjt = rejetService.updateRejet(nr);
                log4j.info("Updater un rejet");
                return ResponseEntity.ok(rjt);
            }else{
                return  new ResponseEntity<RejetDto>(HttpStatus.LOCKED);
            }
        }
        return  new ResponseEntity<RejetDto>(HttpStatus.LOCKED);


    }

    //----------------------------------------------------------------------------------------------



    //---- Supprimer un rejet : ------------------------------------------------------------------

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Supprimer un rejet", notes ="Cette methode permet de supprimer un rejet")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Rejet supprimer de la  DB"),
            @ApiResponse(code = 404, message = "Rejet n'est pas supprimer de la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })
    public ResponseEntity<String> delete(@PathVariable long id) {
        boolean deleted = rejetService.deleteRejet(id);
        log4j.info("Supprimer un rejet");

        return new ResponseEntity<String>("{\"Rejet\":\"" + id + "\",\"deleted\":\"" + deleted + "\"}", HttpStatus.OK);

    }

    //-----------------------------------------------------------------------------------------------

    //------- get rejet by id   : ----------------------------------------------------

    @GetMapping("/GetRejet/{id}")
    @ApiOperation(value = "Rejet by id", notes ="Cette methode permet de nous donner un rejet by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Informations d'un utilisateur trouver dans la DB"),
            @ApiResponse(code = 404, message = "Information d'un utilisateur n'existe pas dans la  DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<RejetDto> getRejetById(@PathVariable long id) {
        RejetDto rjt = rejetService.getById(id);
        log4j.info("rejet by id");
        return ResponseEntity.ok(rjt);
    }

    //-------------------------------------------------------------------------------------
}

//----------------------------------------------------------------
//ADMIN 52 : login : TEST
//           password : soussi


//S_ADMIN & F_ADMIN 55 : login: superadmin
//                   password : superadmin


//user 54 :  login : user
//          password : user111

//----------------------------------------------------------------

