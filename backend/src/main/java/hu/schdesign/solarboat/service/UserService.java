package hu.schdesign.solarboat.service;

import hu.schdesign.solarboat.Exceptions.CustomMessageApiException;
import hu.schdesign.solarboat.dao.RoleRepository;
import hu.schdesign.solarboat.dao.UserRepository;
import hu.schdesign.solarboat.model.Role;
import hu.schdesign.solarboat.model.RoleName;
import hu.schdesign.solarboat.model.User;
import hu.schdesign.solarboat.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public Optional<User> getUserById(long id) {
        return this.userRepository.findById(id);
    }

    public User updateUser(UserRequest newUser) throws CustomMessageApiException {
        User oldUser = this.userRepository.findById(newUser.getId()).orElseThrow(() ->
            new CustomMessageApiException("Nincs ilyen felhasználó"));

        boolean newIsAdmin = newUser.getRoles().contains(RoleName.ROLE_ADMIN);
        Role oldRole = oldUser.getRoles().stream().filter(x -> x.getName().equals(RoleName.ROLE_ADMIN)).findFirst().orElse(null);

        if (!newIsAdmin && oldRole != null) {
            List<User> admins = userRepository.findAllAdmin(RoleName.ROLE_ADMIN);
            if (admins.size() == 1) {
                throw new CustomMessageApiException("Nem lehet módosítani az utolsó admint!");
            }
        }
        oldUser.setUsername(newUser.getUsername());
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());

        Set<Role> newRoles = new HashSet<Role>();
        oldUser.clearRoles();

        newUser.getRoles().forEach(role -> {
            Role tempRole = roleRepository.findByName(role).orElseThrow(() ->
                    new CustomMessageApiException("Fail! -> Cause: User Role not found."));
            newRoles.add(tempRole);
//            for(Role r : newUser.getRoles()){
//                Role tmprole = this.roleRepository.findByName(r.getName()).orElseThrow(() -> {throw new RuntimeException("Nincs ilyen szerepkör");});
//                newRoles.add(tmprole);
//            }
        });
        oldUser.setRoles(newRoles);
        return this.userRepository.save(oldUser);
    }

    public User addUser(User user) {
        return this.userRepository.save(user);
    }

    public void deleteUserById(long id) throws CustomMessageApiException{
        User user = userRepository.findById(id).orElseThrow(() -> new CustomMessageApiException("Nincs felhasználó ilyen id-val!"));
        Role isAdmin = user.getRoles().stream().filter(x -> x.getName().equals(RoleName.ROLE_ADMIN)).findFirst().orElse(null);

        if (isAdmin != null) {
            List<User> admins = userRepository.findAllAdmin(RoleName.ROLE_ADMIN);
            if (admins.size() == 1) {
                throw new CustomMessageApiException("Nem lehet kitörölni az utolsó admint!");
            }
        }

        this.userRepository.deleteById(id);
    }
}
