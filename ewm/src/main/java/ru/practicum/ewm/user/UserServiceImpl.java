package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserInDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserWithFriendsDto;
import ru.practicum.ewm.user.usersRelation.UsesRelationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UsesRelationRepository usesRelationRepository;
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        List<User> users;
        if (ids.size() == 1 && ids.get(0) == -1) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findUserByIdIn(ids, pageable);
        }
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(UserInDto userInDto) {
        Optional<User> userEmail = userRepository.findUserByEmail(userInDto.getEmail());
        userEmail.ifPresent(x -> {
            throw new ContentAlreadyExistException("Пользователь с email = " + userInDto.getEmail() + " уже существует");
        });

        User user = UserMapper.toUser(userInDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFoundException("Пользователя с id = " + userId + " не существует"));
        userRepository.deleteById(userId);
    }

    @Override
    public UserWithFriendsDto sendFriendshipRequest(Long userId, Long friendId) {
        return null;
    }

    @Override
    public UserWithFriendsDto subscribeToFriendsEventVisits(Long userId, Long friendId) {
        return null;
    }

    @Override
    public UserWithFriendsDto subscribeToFriendsEventPublishes(Long userId, Long friendId) {
        return null;
    }
}
