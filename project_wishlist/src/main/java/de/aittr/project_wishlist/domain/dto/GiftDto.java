package de.aittr.project_wishlist.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.aittr.project_wishlist.domain.entity.Wishlist;

import java.math.BigDecimal;
import java.util.Objects;

public class GiftDto {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private String currency;

    private String url;

    private String imgUrl;

    private boolean isReserved;

    private Wishlist wishlist;


    public GiftDto() {
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

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftDto giftDto = (GiftDto) o;
        return isReserved == giftDto.isReserved && Objects.equals(id, giftDto.id) && Objects.equals(title, giftDto.title) && Objects.equals(description, giftDto.description) && Objects.equals(price, giftDto.price) && Objects.equals(currency, giftDto.currency) && Objects.equals(url, giftDto.url) && Objects.equals(imgUrl, giftDto.imgUrl) && Objects.equals(wishlist, giftDto.wishlist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, price, currency, url, imgUrl, isReserved, wishlist);
    }

    @Override
    public String toString() {
        return String.format("Gift: ID - %d, Title - %s, Url - %s, Image Url - %s, Description - %s, Price - %f", id, title, url, imgUrl, description, price);
    }
}
