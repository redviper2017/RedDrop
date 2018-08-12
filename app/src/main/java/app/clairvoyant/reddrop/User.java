package app.clairvoyant.reddrop;

public class User {

    private String name, email, password, phone, area, city, bloodGroup, image;

    public User() {
    }

    public User(String name, String email, String password, String phone, String area, String city, String bloodGroup, String image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.area = area;
        this.city = city;
        this.bloodGroup = bloodGroup;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
