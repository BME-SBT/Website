package hu.schdesign.solarboat.api;


import hu.schdesign.solarboat.Security.jwt.JwtProvider;
import hu.schdesign.solarboat.dao.RoleRepository;
import hu.schdesign.solarboat.dao.UserRepository;
import hu.schdesign.solarboat.message.Request.LoginForm;
import hu.schdesign.solarboat.message.Request.SignUpForm;
import hu.schdesign.solarboat.message.Response.JwtResponse;
import hu.schdesign.solarboat.message.Response.ResponseMessage;
import hu.schdesign.solarboat.model.Role;
import hu.schdesign.solarboat.model.RoleName;
import hu.schdesign.solarboat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestApi {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

//    @PostMapping("/setup/clean_users")
//    public ResponseEntity<?> cleanUsers() {
//
//        userRepository.deleteAll();
//
//        return new ResponseEntity<>(new ResponseMessage("Roles have been succesfully removed!"),
//                HttpStatus.OK);
//    }
//    @PostMapping("/setup/roles")
//    public ResponseEntity<?> setupRoles() {
//
//        //hozzáadom a használt szerepeket
//        roleRepository.save(new Role(RoleName.ROLE_ADMIN));
//        roleRepository.save(new Role(RoleName.ROLE_USER));
//        roleRepository.save(new Role(RoleName.ROLE_EDITOR));
//
//        return new ResponseEntity<>(new ResponseMessage("Admin, editor and user roles successfully added!"),
//                HttpStatus.OK);
//    }
    @PostMapping("/setup/admin")
    public ResponseEntity<?> setupAdmin() {

        //leellenőrzöm hogy létre lett-e már hozva ilyen felhasználó
        if (userRepository.existsByUsername("sbt-admin")) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        //hozzáadom az új szerepkört
        roleRepository.save(new Role(RoleName.ROLE_EDITOR));

        //hozzáadom az admint
        //username: sbt-admin
        //password: uszikAhajo!
        User user = new User("SBT Admin User", "sbt-admin", "sbtadmin@solarboatteam.hu",
                encoder.encode("uszikAhajo!"));

        Set<Role> roles = new HashSet<>();
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find. Run POST request /api/auth/setup/roles first!"));
        roles.add(adminRole);
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find. Run POST request /api/auth/setup/roles first!"));
        roles.add(userRole);
        Role editorRole = roleRepository.findByName(RoleName.ROLE_EDITOR)
                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find. Run POST request /api/auth/setup/roles first!"));
        roles.add(editorRole);

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("Admin user succesfully added!"),
                HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<RoleName> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
//            switch (role) {
//                case "admin":
                    Role adminRole = roleRepository.findByName(role)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
                    roles.add(adminRole);
//                    break;
//                case "editor":
//                    Role pmRole = roleRepository.findByName(RoleName.ROLE_EDITOR)
//                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
//                    roles.add(pmRole);
//                    break;
//                default:
//                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
//                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not found."));
//                    roles.add(userRole);
//            }
        });

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}
