package com.example.saqayatask.services;

import com.example.saqayatask.Configuration.security.JwtService;
import com.example.saqayatask.domain.User;
import com.example.saqayatask.dtos.UserDto;
import com.example.saqayatask.exceptions.RecordNotFoundEX;
import com.example.saqayatask.repositories.UserRepo;
import com.example.saqayatask.responses.RegisterResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.weaver.ast.Var;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Service;


import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.DateTimeException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final Environment env;
    private final ModelMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public RegisterResponse register(UserDto userDto) {
    try {
        User user =mapper.map(userDto,User.class);
        user.setId(generateSHAId(user.getEmail()));
        userRepo.save(user);
        return RegisterResponse.builder()
               .Id(user.getId())
               .accessToken(generateToken(user)).build();

    }
    catch (Exception ex){
        throw new DataIntegrityViolationException(ex.getMessage());
    }
    }

    public UserDto getUser(String Id){
     try {
         User user=userRepo.findById(Id).orElseThrow( RecordNotFoundEX::new);
         UserDto userDto=mapper.map(user,UserDto.class);
         if(!userDto.isMarketingConsent())
             userDto.setEmail(null);
         return userDto;
     }catch (Exception exception)
     {
         throw new RecordNotFoundEX(exception.getMessage());
     }

    }
    private String generateSHAId(String email) {

        // Salt value
        String salt = env.getProperty("sha1.salt");
        byte[] digest = (email+salt).getBytes(StandardCharsets.UTF_8);
        // Generate SHA1 hash of email + salt
        String id = DigestUtils.sha1Hex(digest);
        return id;
    }

    private String generateToken(UserDetails user){
        List<String> authorities = user.getAuthorities().stream()
                .map(grantedAuth -> grantedAuth.getAuthority()).collect(Collectors.toList());
        Map<String, List<String>> claims = new HashMap<>();
        claims.put("Role", authorities);
       return jwtService.generateJwt(claims, user);

    }


}
