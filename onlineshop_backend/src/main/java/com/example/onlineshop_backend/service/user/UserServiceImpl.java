package com.example.onlineshop_backend.service.user;

import com.example.onlineshop_backend.dto.SignupDTO;
import com.example.onlineshop_backend.dto.UserDTO;
import com.example.onlineshop_backend.entities.Order;
import com.example.onlineshop_backend.entities.User;
import com.example.onlineshop_backend.enums.OrderStatus;
import com.example.onlineshop_backend.enums.UserRole;
import com.example.onlineshop_backend.repository.OrderRepository;
import com.example.onlineshop_backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByUserRole(UserRole.ADMIN);
        if (adminAccount == null) {
            User user = new User();
            user.setUserRole(UserRole.ADMIN);
            user.setEmail("admin@test.com");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setName("admin");
            userRepository.save(user);
        }
    }

    @Override
    public UserDTO createUser(SignupDTO signupDTO) {
        User user = new User();
        user.setName(signupDTO.getName());
        user.setEmail(signupDTO.getEmail());
        user.setUserRole(UserRole.USER);
        user.setPassword(new BCryptPasswordEncoder().encode(signupDTO.getPassword()));
        User createdUser = userRepository.save(user);
        Order order = new Order();
        order.setPrice(0L);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(createdUser);
        orderRepository.save(order);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(createdUser.getId());
        userDTO.setName(createdUser.getName());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setUserRole(createdUser.getUserRole());
        return userDTO;
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }
}
