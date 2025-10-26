package de.aittr.project_wishlist.domain.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "share_link")
public class ShareLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    public ShareLink() {}

    public ShareLink(String uuid, Wishlist wishlist) {
        this.uuid = uuid;
        this.wishlist = wishlist;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShareLink shareLink = (ShareLink) o;
        return Objects.equals(id, shareLink.id) && Objects.equals(uuid, shareLink.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid);
    }

    @Override
    public String toString() {
        return String.format(
                "ShareLink: ID - %d, UUID - %s, Wishlist - %s",
                id, uuid, wishlist
        );
    }
}
