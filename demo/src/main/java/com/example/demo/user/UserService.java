package com.example.demo.user;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(String name, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = new User(name, email);
        return saveUser(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User update(Long id, String name, String email) {
        User user = findById(id);

        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistsException(email);
        }

        user.updateDetails(name, email);
        return saveUser(user);
    }

    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
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
