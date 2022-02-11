package com.bamcoreport.web.api.identity.controller;

import com.bamcoreport.web.api.identity.dto.model.UserDto;
import com.bamcoreport.web.api.identity.entities.User;
import com.bamcoreport.web.api.identity.services.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/identity/user")
@Api(tags = "Utilisateur", value = "Utilisateur Controller")
public class UserController {

    static  final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(UserController.class.getName());

    @Autowired
    IUserService userService;

    //------- All users : -------------------------------------------------------------------

    @GetMapping("/")
    @ApiOperation(value = "Afficher la liste des utilisateurs ", notes ="Cette methode permet d'afficher une liste des utilisateurs ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Liste utilisateurs trouvé dans BD"),
            @ApiResponse(code = 404, message = "Liste utilisateurs n'existe pas dans BD"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")


    })
    public ResponseEntity<List<UserDto>> getAllUsers()  {

        List<UserDto> userDto = userService.getUsers();
        log4j.info("liste des utilisateurs");

        return ResponseEntity.ok(userDto);
    }

    //-------------------------------------------------------------------------------------



    //------- Add user : ------------------------------------------------------------------

    @PostMapping("/addUser")
    @ApiOperation(value = "Ajouter un utilisateur  ", notes ="Cette methode permet d'ajouter un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Utilisateur ajouter a la DB"),
            @ApiResponse(code = 404, message = "Utilisateur n'est pas ajouter a la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto){
        UserDto uc = userService.addUser(userDto);
        log4j.info("Ajouter un utilisateur");
        return ResponseEntity.ok(uc);
    }

    //-------------------------------------------------------------------------------------



    //------- get info user contact   : ----------------------------------------------------

    @GetMapping("/Userinfo/{id}")
    @ApiOperation(value = "Les informations d'un utilisateur   ", notes ="Cette methode permet de nous donner les informations d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Informations d'un utilisateur trouver dans la DB"),
            @ApiResponse(code = 404, message = "Information d'un utilisateur n'existe pas dans la  DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<UserDto> getinfoUserById(@PathVariable long id) {
        UserDto ud = userService.getById(id);
        log4j.info("afficher les info d'un utilisateur");
        return ResponseEntity.ok(ud);
    }

    //-------------------------------------------------------------------------------------



    //------- Delete user : --------------------------------------------------------------

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "Supprimer un utilisateur  ", notes ="Cette methode permet de supprimer un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Utilisateur supprimer de la  DB"),
            @ApiResponse(code = 404, message = "Utilisateur n'est pas supprimer de la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        boolean deleted = userService.deleteUser(id);
        log4j.info("Supprimer user");

        return new ResponseEntity<String>("{\"User\":\"" + id + "\",\"deleted\":\"" + deleted + "\"}", HttpStatus.OK);



    }

    //-------------------------------------------------------------------------------------



    //--------- Update User : --------------------------------------------------------------

    @PutMapping ("/UpdateUser")
    @ApiOperation(value = "Updater un utilisateur  ", notes ="Cette methode permet de faire une mise à jour d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Utilisateur updater dans la DB"),
            @ApiResponse(code = 404, message = "Utilisateur n'est pas updater dans la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<UserDto> UpdateUser(@RequestBody UserDto userDto){
        UserDto uc = userService.updateUser(userDto);
        log4j.info("Updater un utilisateur");
        return ResponseEntity.ok(uc);
    }

    //---------------------------------------------------------------------------------------------





    @PostMapping("/password")
    @ApiOperation(value = "Changement de password  ", notes ="Cette methode permet de changer un password ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password updater dans la DB"),
            @ApiResponse(code = 404, message = "Password n'est pas updater dans la DB"),
            @ApiResponse(code = 500, message = "Une erreur système s'est produite"),
            @ApiResponse(code = 401, message = "Pas d'autorisation"),
            @ApiResponse(code = 403, message = "Acces interdit")
    })

    public ResponseEntity<UserDto>changepass(@RequestBody UserDto userdto ,HttpServletRequest req) throws Exception {
        UserDto u=userService.getById(((User)req.getSession().getAttribute("connectedUser")).getId());
        if(u!=null){
            UserDto uc = userService.changepass(userdto, u);
            return ResponseEntity.ok(uc);
        }


        return  new ResponseEntity<UserDto>(HttpStatus.LOCKED);

    }


}

