package com.cwn.wizbank.report.web.controller;

import com.cwn.wizbank.report.entity.UserTest;
import com.cwn.wizbank.report.web.error.ResponseError;
import com.cwn.wizbank.report.web.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 
 * @author Andrew.xiao 2018/4/24
 */
@RestController
@RequestMapping("example")
public class ExampleController {
    @RequestMapping(value = "v1/{id}")
    public UserTest getUser(@PathVariable("id") Long id){
        UserTest user = new UserTest();
        user.setId(id);
        return user;
    }

    @RequestMapping(value = "v2/{id}")
    public ResponseEntity<?> getUser2(@PathVariable("id") Long id){
        UserTest user = new UserTest();
        user.setId(id);
        if (id == 0) {
            ResponseError responseError = new ResponseError(4, "User [" + id + "] not found");
            return new ResponseEntity<ResponseError>(responseError, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserTest>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "v3/{id}")
    public UserTest getUser3(@PathVariable("id") Long id){
        UserTest user = new UserTest();
        user.setId(id);
        if (id == 0) {
            throw new ResourceNotFoundException("user", id);
        }
        return user;
    }

    @RequestMapping(method= RequestMethod.POST , consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public UserTest saveUser(@RequestBody UserTest user) {
        return user;
    }
}
