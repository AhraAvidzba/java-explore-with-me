package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFoundException;
import ru.practicum.ewm.exceptions.UnavailableOperationException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserInDto;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserWithFriendshipDto;
import ru.practicum.ewm.user.usersRelation.UsersRelation;
import ru.practicum.ewm.user.usersRelation.UsersRelationId;
import ru.practicum.ewm.user.usersRelation.UsesRelationRepository;
import ru.practicum.ewm.user.usersRelation.dto.UsersRelationDto;
import ru.practicum.ewm.user.usersRelation.dto.UsersRelationMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UsesRelationRepository usersRelationRepository;

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
    public UserWithFriendshipDto sendFriendshipRequest(Long userId, Long friendId) {
        PageRequest pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        List<User> users = userRepository.findUserByIdIn(List.of(userId, friendId), pageable);
        if (users.size() != 2) {
            throw new ContentNotFoundException("Указанные id пользователей не соответствуют данным в базе");
        }

        UsersRelationId usersRelationId = UsersRelationId.builder()
                .userId(userId)
                .friendId(friendId)
                .build();

        usersRelationRepository.findById(UsersRelationId.builder()
                        .userId(userId)
                        .friendId(friendId)
                        .build())
                .ifPresent((x) -> {
                    throw new ContentAlreadyExistException("Запрос дружбы уже был отправлен ранее");
                });

        User user = userId.equals(users.get(0).getId()) ? users.get(0) : users.get(1);
        User friend = friendId.equals(users.get(0).getId()) ? users.get(0) : users.get(1);

        UsersRelation usersRelation = UsersRelation.builder()
                .usersRelationId(usersRelationId)
                .user(user)
                .friend(friend)
                .eventPublishSubscriber(false)
                .eventVisitSubscriber(false)
                .build();
        usersRelationRepository.save(usersRelation);

        return getUserWithFriendshipDto(userId);
    }

    @Override
    public UserWithFriendshipDto subscribeToFriendsEventVisits(Long userId, Long friendId) {
        UsersRelationId usersRelationId = UsersRelationId.builder()
                .userId(userId)
                .friendId(friendId)
                .build();
        Optional<UsersRelation> optUsersRelation = usersRelationRepository.findById(usersRelationId);
        if (optUsersRelation.isEmpty() || !areUsersFriends(userId, friendId)) {
            throw new UnavailableOperationException("Подписаться на события могут только друзья");
        }
        UsersRelation usersRelation = optUsersRelation.get();
        usersRelation.setEventVisitSubscriber(true);
        usersRelationRepository.save(usersRelation);
        return getUserWithFriendshipDto(userId);
    }

    @Override
    public UserWithFriendshipDto subscribeToFriendsEventPublishes(Long userId, Long friendId) {
        UsersRelationId usersRelationId = UsersRelationId.builder()
                .userId(userId)
                .friendId(friendId)
                .build();
        Optional<UsersRelation> usersRelation = usersRelationRepository.findById(usersRelationId);
        if (usersRelation.isEmpty() || !areUsersFriends(userId, friendId)) {
            throw new UnavailableOperationException("Подписаться на события могут только друзья");
        }
        usersRelation.get().setEventPublishSubscriber(true);
        usersRelationRepository.save(usersRelation.get());
        return getUserWithFriendshipDto(userId);
    }

    private UserWithFriendshipDto getUserWithFriendshipDto(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFoundException("Пользователь не найден"));
        List<UsersRelation> confirmedFriends = new ArrayList<>();
        List<UsersRelation> requestSentFriends = new ArrayList<>();
        List<UsersRelation> requestReceivedFriends = new ArrayList<>();

        Set<Long> processedFriendships = new HashSet<>();
        List<UsersRelation> userRelations = usersRelationRepository.findByUsersRelationIdUserIdOrFriendId(userId, userId);
        for (UsersRelation userRelation : userRelations) {
            if (!processedFriendships.contains(userRelation.getUsersRelationId().userId + userRelation.getUsersRelationId().friendId)) {
                if (areUsersFriends(userRelation.getUser().getId(), userRelation.getFriend().getId())) {
                    confirmedFriends.add(userRelation);
                    processedFriendships.add(userRelation.getUsersRelationId().userId + userRelation.getUsersRelationId().friendId);
                } else {
                    if (userRelations.stream().anyMatch(x -> x.getUsersRelationId().getUserId().equals(userId))) {
                        requestSentFriends.add(userRelation);
                    } else if (userRelations.stream().anyMatch(x -> x.getUsersRelationId().getFriendId().equals(userId))) {
                        requestReceivedFriends.add(userRelation);
                    }
                }
            }
        }

        List<UsersRelationDto> usersRelationsDto = new ArrayList<>();
        Stream.of(UsersRelationMapper.toUsersRelationDtoList(confirmedFriends, FriendStatus.CONFIRMED, userId),
                        UsersRelationMapper.toUsersRelationDtoList(requestSentFriends, FriendStatus.REQUEST_SENT, userId),
                        UsersRelationMapper.toUsersRelationDtoList(requestReceivedFriends, FriendStatus.REQUEST_RECEIVED, userId))
                .forEach(usersRelationsDto::addAll);

        return UserWithFriendshipDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .friendship(usersRelationsDto)
                .build();
    }

    private boolean areUsersFriends(Long userId, Long friendId) {
        List<UsersRelation> userRelation = usersRelationRepository.findByUsersRelationIdIn(List.of(
                UsersRelationId.builder()
                        .userId(userId)
                        .friendId(friendId)
                        .build(),
                UsersRelationId.builder()
                        .userId(friendId)
                        .friendId(userId)
                        .build()));
        return userRelation.size() == 2;
    }
}
