package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    //have a reference of Service Layer to call its method
    @Autowired
    private UserService userService;

    //have a reference of FileService to upload/fetch images
    @Autowired
    private FileService fileService;

    //fetch path where User images need to be uploaded
    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    //create
    @PostMapping()
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUserDto = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @Valid
            @PathVariable String userId,
            @RequestBody UserDto userDto
    ) {
        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUserDto);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(
            @PathVariable String userId
    ) {
        userService.deleteUser(userId);
        ApiResponseMessage apiResponseMessage
                = ApiResponseMessage
                .builder()
                .message("User Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseMessage);
    }

    //getUserById
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable String userId
    ) {
        UserDto userById = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userById);
    }


    //getAllUsers
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ) {
        PageableResponse<UserDto> allusers = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(allusers);
    }

    //getUserByEmail
    @GetMapping("/email/{emailId}")
    public ResponseEntity<UserDto> getUserByEmail(
            @PathVariable String emailId
    ) {
        UserDto userByEmail = userService.getUserByEmail(emailId);
        return ResponseEntity.status(HttpStatus.OK).body(userByEmail);
    }

    //searchUser
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> searchUser(
            @PathVariable String keywords
    ) {
        List<UserDto> userDtos = userService.searchUser(keywords);
        return ResponseEntity.status(HttpStatus.OK).body(userDtos);
    }

    //upload user Image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(
            @RequestParam("userImage") MultipartFile image,
            @PathVariable String userId
    ) throws IOException {

        String imageName = fileService.uploadFile(image, imageUploadPath);

        //update the ImageName in User database table
        //fetch user using UserId
        UserDto userById = userService.getUserById(userId);
        //set ImageName in respective User Object
        userById.setImageName(imageName);
        //update the entry in database table
        userService.updateUser(userById, userId);

        ImageResponse imageResponse = ImageResponse.builder()
                .imageName(imageName)
                .message("Image uploaded Successfully")
                .success(true)
                .status(HttpStatus.CREATED)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(imageResponse);
    }

    //get User Image
    @GetMapping("/image/{userId}")
    public void serveUserImage(
            @PathVariable String userId,
            HttpServletResponse response
    ) throws IOException {
        //fetch user using UserId
        UserDto user = userService.getUserById(userId);
        
        //fetch imageName of user
        String imageName = user.getImageName();
        
        //fetch details of image as InputStream
        InputStream resource = fileService.getResource(imageUploadPath, imageName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

}
