package com.sys.gerenciador.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.sys.gerenciador.model.Usuario;
import com.sys.gerenciador.repository.IUserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("user not found.");
        }
        return new CustomUser(usuario);
    }
    
}
