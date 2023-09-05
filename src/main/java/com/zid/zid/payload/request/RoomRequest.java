package com.zid.zid.payload.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;



import com.zid.zid.security.utils.ProductCategorie;

import lombok.Data;

@Data
public class RoomRequest {
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
    private ProductCategorie productCategorie;
   
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;

}
