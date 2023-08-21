package com.lcwd.electronic.store.services.Implementation;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImplementation implements UserService {

    //logging purpose
    Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String userImagePath;

    @Override
    public UserDto createUser(UserDto userDto) {
        //generate a unique Id
        String userId = UUID.randomUUID().toString();

        //set userId
        userDto.setUserId(userId);

        //convert object of UserDto into User
        User user = mapper.map(userDto, User.class);
        //save User object in Database
        User createdUser = userRepository.save(user);

        //convert savedUser object into UserDto to return it
        UserDto createdDto = mapper.map(createdUser, UserDto.class);
        return createdDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found with this Id"));

        //now update details
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);

        //convert updatedUser into UserDto
        UserDto updatedDto = mapper.map(updatedUser, UserDto.class);
        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found with this Id"));

        //fetch user Image
        String imageName = user.getImageName();
        log.info("imageName:" + imageName);

        //form complete path where image is stored
        String fullPath = userImagePath + imageName;
        log.info("fullPath:" + fullPath);

        //delete image from fullPath location
        try {
            Path path = Paths.get(fullPath);
            log.info("path:" + path);
            Files.delete(path);
        }
        catch (NoSuchFileException ex) {
            log.info("NoSuchFileException occurred");
            ex.printStackTrace();
        } catch (IOException ex) {
            log.info("IOException occurred");
            ex.printStackTrace();
        }

        //delete user
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

        //create object of sort
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);

        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class, mapper);
        return pageableResponse;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No user found with this Id"));

        //convert user to UserDto
        UserDto userDto = mapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("No user found with this Id"));

        //convert  user to UserDto
        UserDto userDto = mapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);

        //convert users to UserDtos
        List<UserDto> allUserDtos = users.stream().map(user -> mapper.map(user, UserDto.class)).collect(Collectors.toList());
        return allUserDtos;
    }
}
