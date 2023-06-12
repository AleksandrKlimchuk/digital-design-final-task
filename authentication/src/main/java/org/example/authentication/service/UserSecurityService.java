package org.example.authentication.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.store.model.Employee;
import org.example.store.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    EmployeeRepository repository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        final Employee employee = repository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Employee with account %s doesn't exists".formatted(account)
                ));
        return new User(employee.getAccount(), employee.getAccount(), Collections.EMPTY_LIST);
    }
}
