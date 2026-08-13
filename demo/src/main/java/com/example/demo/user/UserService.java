package com.example.demo.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = toEntity(request);
        return toResponse(saveUser(user));
    }

    @Transactional
    public void createTwoUsersThenFail(CreateUserRequest request) {
        User firstUser = new User(request.getName(), request.getEmail());
        userRepository.saveAndFlush(firstUser);

        User secondUser = new User(
                request.getName() + " second",
                secondEmail(request.getEmail()));
        userRepository.saveAndFlush(secondUser);

        throw new RuntimeException("Intentional transaction rollback test");
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse findById(Long id) {
        return toResponse(findEntityById(id));
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = findEntityById(id);

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.updateDetails(request.getName(), request.getEmail());
        return toResponse(saveUser(user));
    }

    public void delete(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    private User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private User toEntity(CreateUserRequest request) {
        return new User(request.getName(), request.getEmail());
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    private String secondEmail(String email) {
        int atIndex = email.indexOf('@');
        return "rollback-" + email.substring(0, atIndex) + email.substring(atIndex);
    }

    private User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            // The database unique constraint protects against concurrent duplicate requests.
            throw new EmailAlreadyExistsException(user.getEmail(), exception);
        }
    }
}
