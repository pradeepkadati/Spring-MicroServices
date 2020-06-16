package com.appsdeveloperblog.photoapp.api.users.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.appsdeveloperblog.photoapp.api.users.data.AlbumsServiceClient;
import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.data.UserRepository;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;

import feign.FeignException;

@Service
public class UserServiceImpl implements UsersService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    RestTemplate restTemplate;
    Environment env;
    AlbumsServiceClient albumsServiceClient;
    
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    public UserServiceImpl(UserRepository userRepository, 
            BCryptPasswordEncoder bCryptPasswordEncoder,
            RestTemplate restTemplate,
            Environment env,
            AlbumsServiceClient albumsServiceClient) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restTemplate = restTemplate;
        this.env = env;
        this.albumsServiceClient = albumsServiceClient; 
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        // TODO Auto-generated method stub

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDetails, UserEntity.class);

        userRepository.save(userEntity);

        UserDto returnValue = mapper.map(userEntity, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        ModelMapper mapper = new ModelMapper();
        UserDto returnValue = mapper.map(userEntity, UserDto.class);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }
        ModelMapper mapper = new ModelMapper();
        UserDto returnValue = mapper.map(userEntity, UserDto.class);
        String url = String.format(env.getProperty("albums.url"), userId);
       /* ResponseEntity<List<AlbumResponseModel>> albumsResponse;
        albumsResponse = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<AlbumResponseModel>>(){});
        List<AlbumResponseModel> albums = albumsResponse.getBody();*/
        
        logger.info("Before calling album microservice");
        
        List<AlbumResponseModel> albums = null;
		try {
			albums = albumsServiceClient.getAlbums(userId);
		} catch (FeignException e) {
			
			logger.debug("Feign Exception while fetching albums");
		}
        returnValue.setAlbums(albums);
        return returnValue;
    }

}
