package com.capitan.encuestas.controllers;

import javax.validation.Valid;

import com.capitan.encuestas.entities.UserEntity;
import com.capitan.encuestas.models.requests.UserRegisterRequestModel;
import com.capitan.encuestas.models.responses.UserRest;
import com.capitan.encuestas.services.UserService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    
    @PostMapping()
    public UserRest createUser(@RequestBody @Valid UserRegisterRequestModel userModel) {
        UserEntity user = userService.createUser(userModel);

        UserRest userRest = new UserRest();

        BeanUtils.copyProperties(user, userRest);

        return userRest;
    }

    @GetMapping
    public UserRest getUser(Authentication authentication) {
        
        UserEntity user = userService.getUser(
            authentication.getPrincipal().toString()
        );

        UserRest userRest = new UserRest();

        BeanUtils.copyProperties(user, userRest);

        return userRest;
    }
}
