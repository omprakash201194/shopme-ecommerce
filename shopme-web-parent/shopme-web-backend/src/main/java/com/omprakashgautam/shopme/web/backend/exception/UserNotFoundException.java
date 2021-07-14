package com.omprakashgautam.shopme.web.backend.exception;

/**
 * @author omprakash gautam
 * Created on 13-Jul-21 at 10:52 PM.
 */
public class UserNotFoundException extends Exception{

    public UserNotFoundException(String message){
        super(message);
    }
}
