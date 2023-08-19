package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserInDto;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        properties = "spring.datasource.url=jdbc:h2:mem:ewm",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles(profiles = "test")
@Transactional
class UserServiceITest {
    private final UserService userService;

    @Test
    void getAllUsers() {
        //given
        List<UserInDto> sourceUsers = List.of(
                makeShortUserDto("ivan@email", "Ivan"),
                makeShortUserDto("petr@email", "Petr"),
                makeShortUserDto("vasilii@email", "Vasilii")
        );
        sourceUsers.forEach(userService::saveUser);
        //when
        List<UserDto> targetUsers = userService.getUsers(List.of(1L, 2L, 3L), 0, 10);
        //then
        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (UserInDto sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    private UserInDto makeShortUserDto(String email, String name) {
        return UserInDto.builder()
                .email(email)
                .name(name)
                .build();
    }
}