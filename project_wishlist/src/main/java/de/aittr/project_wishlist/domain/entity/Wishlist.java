package de.aittr.project_wishlist.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private Date eventDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "wishlist", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Gift> gifts;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "wishlist", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<ShareLink> shareLinks;


    public Wishlist(Long id, String title, String description, Date eventDate, User user, Set<Gift> gifts, Set<ShareLink> shareLinks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.user = user;
        this.gifts = gifts;
        this.shareLinks = shareLinks;
    }

    public Wishlist() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(id, wishlist.id) && Objects.equals(title, wishlist.title) && Objects.equals(description, wishlist.description) && Objects.equals(eventDate, wishlist.eventDate) && Objects.equals(gifts, wishlist.gifts) && Objects.equals(shareLinks, wishlist.shareLinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, eventDate, gifts, shareLinks);
    }

    @Override
    public String toString() {
        return String.format("Wishlist: ID - %d, Title - %s, Description - %s, Event Date - %s", id, title, description, eventDate);
    }

    public Set<ShareLink> getShareLinks() {
        return shareLinks;
    }

    public void setShareLinks(Set<ShareLink> shareLinks) {
        this.shareLinks = shareLinks;
    }
}
