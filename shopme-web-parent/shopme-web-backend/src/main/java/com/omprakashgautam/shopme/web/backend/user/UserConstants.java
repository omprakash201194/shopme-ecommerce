package com.omprakashgautam.shopme.web.backend.user;

/**
 * @author omprakash gautam
 * Created on 18-Jul-21 at 1:06 PM.
 */
public interface UserConstants {
    String REDIRECT_TO_USERS = "redirect:/users";
    String REDIRECT_TO_A_USER = "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=";
    String REDIRECT_TO_ACCOUNT = "redirect:/account";

    String VIEW_USERS_FORM = "users/users_form";
    String VIEW_ALL_USERS = "users/users";
    String VIEW_ACCOUNT_FORM = "users/account_form";
}
