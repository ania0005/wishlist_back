package de.aittr.project_wishlist.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.aittr.project_wishlist.domain.entity.Gift;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class WishlistDto {

    private Long id;

    private String title;

    private String description;

    private Date eventDate;

    private Set<Gift> gifts;

    public WishlistDto() {
    }

    public WishlistDto(Object o, LocalDate now) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Set<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(Set<Gift> gifts) {
        this.gifts = gifts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistDto that = (WishlistDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(eventDate, that.eventDate) && Objects.equals(gifts, that.gifts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, eventDate, gifts);
    }

    @Override
    public String toString() {
        return String.format("Wishlist: ID - %d, Title - %s, Description - %s, Event Date - %s", id, title, description, eventDate);
    }
}
