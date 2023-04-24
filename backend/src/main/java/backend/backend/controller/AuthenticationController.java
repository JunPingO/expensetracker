package backend.backend.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.http.HttpHeaders;

import backend.backend.config.JwtService;
import backend.backend.models.UserLogin;
import backend.backend.services.AuthenticationService;
import backend.backend.services.DatabaseService;
import io.jsonwebtoken.Jwts;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = "/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authSvc;
    private final DatabaseService databaseSvc;
    private final JwtService jwtSvc;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserLogin request) {
        Optional<UserLogin> opt = databaseSvc.findUserByEmail(request.getEmail());
        if (opt.isPresent()) {
            String message = request.getEmail() + " is already taken";
            return ResponseEntity.badRequest().body(Json.createObjectBuilder().add("message", message).build().toString());
        }
        return ResponseEntity.ok(authSvc.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLogin request) {
        return ResponseEntity.ok(authSvc.authenticate(request));
    }

    @GetMapping("/authJWT")
    public ResponseEntity<String> authenticate(@RequestHeader HttpHeaders headers) {
        String jwt = headers.getFirst("Authorization").substring(7);
        String email = jwtSvc.extractUsername(jwt);
        JsonObject jsonObj = Json.createObjectBuilder()
            .add("email", email)
            .build();
        System.out.printf("%s authenticated\n", email);
        return ResponseEntity.ok(jsonObj.toString());
    }
}
