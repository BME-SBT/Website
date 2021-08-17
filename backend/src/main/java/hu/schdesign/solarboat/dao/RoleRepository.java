package hu.schdesign.solarboat.dao;

import hu.schdesign.solarboat.model.Role;
import hu.schdesign.solarboat.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}