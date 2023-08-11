package ru.practicum.ewm.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.user.dto.ShortUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private ShortUserDto createUser() {
        return ShortUserDto.builder()
                .email("akhraa1@yandex.ru")
                .name("Akhra")
                .build();
    }

    @Test
    public void getAllUsers_whenInvoked_thenReturnUsersCollection() {
        when(userRepository.findUserByIdIn(anyList(), any()))
                .thenReturn(List.of(UserMapper.toUser(createUser()), UserMapper.toUser(createUser())));
        List<UserDto>  users = userService.getUsers(List.of(1L, 2L), 0, 10);
        assertThat(users.size(), equalTo(2));
        assertThat(users.get(0).getEmail(), equalTo("akhraa1@yandex.ru"));
        assertThat(users.get(0).getName(), equalTo("Akhra"));
    }

}