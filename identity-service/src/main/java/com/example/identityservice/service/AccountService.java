package com.example.identityservice.service;

import com.example.identityservice.dto.request.AccountCreateRequest;
import com.example.identityservice.dto.request.AccountUpdateRequest;
import com.example.identityservice.dto.response.AccountResponse;
import com.example.identityservice.entity.Account;
import com.example.identityservice.entity.Role;
import com.example.identityservice.entity.User;
import com.example.identityservice.enums.ErrorCode;
import com.example.identityservice.enums.TypeOfRole;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.mapper.AccountMapper;
import com.example.identityservice.repository.AccountRepository;
import com.example.identityservice.repository.RoleRepository;
import com.example.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AccountService {
    AccountRepository accountRepository;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    KafkaTemplate<String, String> kafkaTemplate;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public AccountResponse createAccount(AccountCreateRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.EXISTED);
        }
       Account account = accountMapper.toAccount(request);
       account.setPassword(passwordEncoder.encode(account.getPassword()));
       var roles = request.getRoles();
       Set<Role> roleSet = new HashSet<>();
       roles.forEach(role->
               roleSet.add(roleRepository.findByTypeOfRole(TypeOfRole.valueOf(role)))
               );
       account.setRoles(roleSet);
       User user =userRepository.findById(request.getUser()).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
       account.setUser(user);
       accountRepository.save(account);
       kafkaTemplate.send("account-create",account.getId());
       return accountMapper.toAccountResponse(account);
    }
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::toAccountResponse).toList();
    }
    public AccountResponse updateAccount(String accountID, AccountUpdateRequest request) {
        Account account = accountRepository.findById(accountID).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        accountMapper.updateAccount(request, account);
        var roles = request.getRoles();
        Set<Role> roleSet = new HashSet<>();
        roles.forEach(role->
                roleSet.add(roleRepository.findByTypeOfRole(TypeOfRole.valueOf(role)))
        );
        account.setRoles(roleSet);
        return accountMapper.toAccountResponse(accountRepository.save(account));
    }
    public void deleteAccount(String accountID) {
        accountRepository.deleteById(accountID);
    }
}
