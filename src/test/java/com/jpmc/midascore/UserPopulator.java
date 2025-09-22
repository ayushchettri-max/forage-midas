package com.jpmc.midascore;

import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator {
    private final UserRepository userRepository;

    public UserPopulator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void populate() {
        // Example users
        User waldorf = new User();
        waldorf.setUsername("waldorf");
        waldorf.setBalance(1000f);
        userRepository.save(waldorf);

        User statler = new User();
        statler.setUsername("statler");
        statler.setBalance(500f);
        userRepository.save(statler);
    }
}
