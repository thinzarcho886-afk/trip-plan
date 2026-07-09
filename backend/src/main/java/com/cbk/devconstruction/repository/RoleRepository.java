package com.cbk.devconstruction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbk.devconstruction.entity.Role;
import com.cbk.devconstruction.enums.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(UserRole roleName);

}
