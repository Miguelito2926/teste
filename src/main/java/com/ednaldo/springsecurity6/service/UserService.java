package com.ednaldo.springsecurity6.service;

import com.ednaldo.springsecurity6.dto.CreateUserProfileRequest;
import com.ednaldo.springsecurity6.entities.Role;
import com.ednaldo.springsecurity6.entities.User;
import com.ednaldo.springsecurity6.repositories.RoleRepository;
import com.ednaldo.springsecurity6.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(CreateUserProfileRequest request) throws RoleNotFoundException {
        // Validação de dados
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Buscar papéis (roles) no repositório
        Set<Role> roles = roleRepository.findByName(request.getNameRole());
        if (roles.isEmpty()) {
            throw new RoleNotFoundException("Role not found: " + request.getNameRole());
        }

        // Criar novo usuário
        User user = new User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setRoles(roles);

        // Salvando usuário no banco
        User savedUser = userRepository.save(user);

        return savedUser;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}