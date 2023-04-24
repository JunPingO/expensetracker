package backend.backend.services;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.backend.config.JwtService;
import backend.backend.models.Role;
import backend.backend.models.UserLogin;
import backend.backend.repositories.SQLRepository;
import jakarta.json.Json;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final SQLRepository sqlRepo;
    private final PasswordEncoder pwdEncoder;
    private final JwtService jwtSvc;
    private final AuthenticationManager authManager;
    
    // private final TokenRepository

    public String register(UserLogin user){
        user.setPassword(pwdEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        sqlRepo.registerUser(user);

        //user created, generate jwt token back to client
        String jwtToken = jwtSvc.generateToken(user);
        return Json.createObjectBuilder().add("token", jwtToken).build().toString();
    }

    public String authenticate(UserLogin user){

        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        Optional<UserLogin> opt = sqlRepo.findUserByEmail(user.getEmail());
        
        String jwtToken = jwtSvc.generateToken(opt.get());
        return Json.createObjectBuilder().add("token", jwtToken).build().toString();
    }

}
