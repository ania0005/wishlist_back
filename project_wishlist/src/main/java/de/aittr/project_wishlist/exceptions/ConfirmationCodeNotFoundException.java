package de.aittr.project_wishlist.exceptions;

public class ConfirmationCodeNotFoundException extends IllegalArgumentException {
    public ConfirmationCodeNotFoundException(String message) {
        super(message);
    }
}