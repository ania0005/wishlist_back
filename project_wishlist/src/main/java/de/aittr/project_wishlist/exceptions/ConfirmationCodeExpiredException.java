package de.aittr.project_wishlist.exceptions;

public class ConfirmationCodeExpiredException extends IllegalStateException {
    public ConfirmationCodeExpiredException(String message) {
        super(message);
    }
}
