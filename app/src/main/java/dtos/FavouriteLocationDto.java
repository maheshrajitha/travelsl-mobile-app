package dtos;

public class FavouriteLocationDto {

    private String id;
    private String userId;
    private String locationId;
    private String image;
    private String country;
    private String city;

    public FavouriteLocationDto(String id, String userId, String locationId, String image, String country, String city) {
        this.id = id;
        this.userId = userId;
        this.locationId = locationId;
        this.image = image;
        this.country = country;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
