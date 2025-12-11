package mx.edu.utez.back.service;

import mx.edu.utez.back.model.User;
import mx.edu.utez.back.model.Store;
import mx.edu.utez.back.repository.UserRepository;
import mx.edu.utez.back.repository.StoreRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, StoreRepository storeRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        try {
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public List<User> findAll() {
        try {
            List<User> users = userRepository.findAll();

            // Populate store for each user
            for (User user : users) {
                if (user.getMainStoreId() != null) {
                    try {
                        Optional<Store> store = storeRepository.findById(user.getMainStoreId());
                        store.ifPresent(user::setMainStore);
                    } catch (Exception e) {
                        // Store not found, leave null
                    }
                }
            }

            return users;
        } catch (Exception e) {
            throw new RuntimeException("Error finding users", e);
        }
    }

    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public Optional<User> findById(Long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                // Populate store if user has mainStoreId
                if (user.getMainStoreId() != null) {
                    try {
                        Optional<Store> store = storeRepository.findById(user.getMainStoreId());
                        store.ifPresent(user::setMainStore);
                    } catch (Exception e) {
                        // Store not found, leave null
                    }
                }
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding user", e);
        }
    }

    public User assignStore(Long userId, Long storeId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new RuntimeException("User not found");
            }
            
            Optional<Store> optionalStore = storeRepository.findById(storeId);
            if (!optionalStore.isPresent()) {
                throw new RuntimeException("Store not found");
            }

            User user = optionalUser.get();
            user.setMainStoreId(storeId);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error assigning store to user", e);
        }
    }

    public Optional<User> authenticate(String email, String password) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error during authentication", e);
        }
    }

    public void saveFcmToken(Long userId, String token) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setFcmToken(token);
                userRepository.save(user);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving FCM token", e);
        }
    }
}