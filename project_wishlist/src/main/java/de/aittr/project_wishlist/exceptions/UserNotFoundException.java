package de.aittr.project_wishlist.exceptions;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {

    public UserNotFoundException(String message) {
        super(message);
    }

}

