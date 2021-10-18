package com.tcsp.digitalwrite.shared;

public interface Constants {

    String DELETE_SESSION = "User successfully logged out";

    String NOT_EXIST_SESSION = "Session doesn't exist";

    String SUCCESS_AUTHORIZED = "User is successfully authorized";

    String SUCCESS_REGISTERED = "User registered successfully";

    String DELETE_USER = "User deleted successfully";

    String NOT_EXIST_USER = "User doesn't exist";

    String ROLE_EMPTY = "Role name can't be empty";

    String CREATE_ROLE = "Role is successfully created";

    String CHANGE_ROLES = "Roles changed";

    String NOT_EXIST_ROLE = "Role doesn't exist";

    String NOT_USER_ROLE = "User doesn't have the role";

    String SYSTEM_NAME_EMPTY = "System name can't be empty";

    String DELETE_SYSTEM = "System deleted successfully";

    String NOT_EXIST_SYSTEM = "System doesn't exist";

    String ERROR_SERVICE = "Error in the service";

    int EXPIRED_SECONDS = 60 * 60;
}
