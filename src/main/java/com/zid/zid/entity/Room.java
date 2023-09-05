package com.zid.zid.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zid.zid.payload.response.UserDTO;
import com.zid.zid.security.utils.ProductCategorie;

import lombok.Data;
@Data
@Document(collection = "rooms")
public class Room {
    @Id
    @JsonIgnore
    private String id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String productName;
    @NotBlank
    private String productDescription;
    @NotBlank
    @Size(min = 3, max = 20)
    private String storePrice;
    @NotBlank
    @Size(min = 3, max = 20)
    private String startPrice;
    @NotBlank
    Boolean terminated;
    @NotBlank
    private LoadFile loadFile;
    @NotBlank
    @Enumerated(EnumType.STRING)
    ProductCategorie productCategorie;
   @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    Date date;
    @ManyToMany
    @JoinTable(
            name = "user_enrolled",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<UserDTO> enrolledUsers = new HashSet<>();
   
    public void enrolledUsers(UserDTO userDTO) {
        this.enrolledUsers.add(userDTO);
     }
/* 
     @OneToMany(mappedBy = "room")
     private Set<Photo> photos;
     public Set<Photo> getphotos() {
         return this.photos;
     }
     public void enrolledPhotos(Photo photo) {
        this.photos.add(photo);
    }*/
}
