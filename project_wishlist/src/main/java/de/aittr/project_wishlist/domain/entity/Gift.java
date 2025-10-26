package de.aittr.project_wishlist.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "gift")
public class Gift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "url")
    private String url;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "reservation")
    private boolean isReserved;

    @ManyToOne
    @JoinColumn(name = "wishlist_id")
    @JsonIgnore
    private Wishlist wishlist;

    public Gift(Long id, String title, String description, BigDecimal price, String currency, String url, String imgUrl, boolean isReserved, Wishlist wishlist) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.url = url;
        this.imgUrl = imgUrl;
        this.isReserved = false;
        this.wishlist = wishlist;
    }

    public Gift() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gift gift = (Gift) o;
        return isReserved == gift.isReserved && Objects.equals(id, gift.id) && Objects.equals(title, gift.title) && Objects.equals(description, gift.description) && Objects.equals(price, gift.price) && Objects.equals(currency, gift.currency) && Objects.equals(url, gift.url) && Objects.equals(imgUrl, gift.imgUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, currency, url, imgUrl, isReserved);
    }

    @Override
    public String toString() {
        return String.format("Gift: ID - %d, Title - %s, Url - %s, Image Url - %s, Description - %s, Price - %f", id, title, url, imgUrl, description, price);
    }
}
