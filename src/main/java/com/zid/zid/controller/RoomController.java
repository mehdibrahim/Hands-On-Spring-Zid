package com.zid.zid.controller;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.zid.zid.entity.LoadFile;
import com.zid.zid.entity.Room;
import com.zid.zid.entity.User;
import com.zid.zid.payload.response.MessageResponse;
import com.zid.zid.payload.response.UserDTO;
import com.zid.zid.repository.RoomRepository;
import com.zid.zid.repository.UserRepository;
import com.zid.zid.security.services.FileService;
import com.zid.zid.security.utils.ProductCategorie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;



@RestController
@RequestMapping
public class RoomController {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FileService fileService;
 
    

@Operation(summary = "Add New Room",description = "Add new Room",tags ="Room")
@ApiResponse( responseCode = "200",description="Room Added sucessfully",content = {@Content(mediaType ="application/json")})
@PostMapping(value="/add")
 public ResponseEntity<?> createRoom(@RequestParam("productName")String productName,
                                     @RequestParam("productDescription")String productDescription,
                                     @RequestParam("storePrice")String storePrice,
                                     @RequestParam("startPrice")String startPrice,
                                     @RequestParam("productCategorie")ProductCategorie productCategorie,
                                     @RequestParam("date")@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")Date date,
                                     @RequestParam("file") MultipartFile file) throws IOException {
    Room room = new Room();
    LoadFile loadFile=new LoadFile();
    String ID = fileService.addFile(file);
    
    loadFile.setId(ID);
    loadFile.setFilename(file.getName());
    loadFile.setFileType(file.getContentType());
    loadFile.setFile(file.getBytes());
    loadFile.setFileSize(""+file.getSize());
    room.setDate(date);
    room.setProductCategorie(productCategorie);
    room.setTerminated(false);
    room.setProductName(productName);
    room.setStartPrice(startPrice);
    room.setStorePrice(storePrice);
    room.setLoadFile(loadFile);
    room.setProductDescription(productDescription);
    roomRepository.save(room);
    return new ResponseEntity<>("Room Created" , HttpStatus.OK) ;
}
@Operation(summary = "Enroll",description = "Enroll",tags ="Room")
@ApiResponse( responseCode = "200",description="User Enrolled sucessfully",content = {@Content(mediaType ="application/json")})
@PostMapping("/room/{roomId}")
public String addUserToRoom  (
        @PathVariable String roomId,
        @RequestParam("userName") String userName
) {
    Room room ;
    UserDTO  userDTO =new UserDTO();
    User user;
    try {
       room = roomRepository.findById(roomId).get();
        
    } catch (Exception e) {
        return  "You can't Add user for an invalid room ID: "+roomId ;
    }
    try {
        user= userRepository.findByUsername(userName).get();
        
        
    } catch (Exception e) {
        return "You can't Add members for an invalid UserName: "+userName;
    }
    
    userDTO.setUsername(user.getUsername());
    userDTO.setId(user.getId());
    room.enrolledUsers(userDTO);
    roomRepository.save(room);
    return "User with UName "+userName+" successfully added";
}
@Operation(summary = "Get Room Info",description = "Get Room Info",tags ="Room")
@ApiResponse( responseCode = "200",description="Room Deleted",content = {@Content(mediaType ="application/json")})
@GetMapping("/room/{id}")
public ResponseEntity<?> getRoom(@PathVariable String id) throws IOException {
    Room room = roomRepository.findById(id).get();
   // LoadFile loadFile = fileService.downloadFile(room.getLoadFile().getId());
    return ResponseEntity.ok(room);
   /*  return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(loadFile.getFileType() ))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + loadFile.getFilename() + "\"")
            .body(new ByteArrayResource(loadFile.getFile()));*/
}
@Operation(summary = "Delete Room",description = "Delete Project",tags ="Room")
    @ApiResponse( responseCode = "200",description="Room Deleted",content = {@Content(mediaType ="application/json")})
    @DeleteMapping("/room/{roomId}")
    public String deleteProject(@PathVariable String roomId) {
        MessageResponse messageResponse=new MessageResponse("Room Not Found With ID:"+roomId);
        try {
           roomRepository.findById(roomId).get();
           roomRepository.deleteById(roomId);
            return "Room with ID: "+roomId+" deleted ";
             
         } catch (Exception e) {
             return messageResponse.getMessage();
         }
       
       
    }
    @Operation(summary = "Edit Room",description = "Edit Room",tags = "Room")
    @ApiResponse( responseCode = "200",description="Project Edited",content = {@Content(mediaType ="application/json")})
    @PutMapping("/room/{roomId}")
    public String editProject(@RequestParam("productName")String productName,
    @RequestParam("productDescription")String productDescription,
    @RequestParam("storePrice")String storePrice,
    @RequestParam("startPrice")String startPrice,
    @RequestParam("productCategorie")ProductCategorie productCategorie,
    @RequestParam("date")@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")Date date,
    @RequestParam("file") MultipartFile file,
    @PathVariable String roomId) {
        
        try {
            Room room = roomRepository.findById(roomId).get();
            LoadFile loadFile=new LoadFile();
            String ID = fileService.addFile(file);
            
            loadFile.setId(ID);
            loadFile.setFilename(file.getName());
            loadFile.setFileType(file.getContentType());
            loadFile.setFile(file.getBytes());
            loadFile.setFileSize(""+file.getSize());
            room.setDate(date);
    room.setProductCategorie(productCategorie);
    room.setTerminated(false);
    room.setProductName(productName);
    room.setStartPrice(startPrice);
    room.setStorePrice(storePrice);
    room.setLoadFile(loadFile);
    room.setProductDescription(productDescription);
    roomRepository.save(room);


            return "info changed succefully";
              
          } catch (Exception e) {
              return "invalid room";
          }
       
    }

}
