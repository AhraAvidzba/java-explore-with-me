package ru.practicum.ewm.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFoundException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.UserServiceImpl;
import ru.practicum.ewm.user.dto.ShortUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private RequestServiceImpl requestService;
    @Captor
    private ArgumentCaptor<Request> requestArgumentCaptor;

    @Test
    void getRequests() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(requestRepository.findRequestByRequesterId(anyLong()))
                .thenReturn(List.of(createRequest()));
        List<RequestDto>  requests = requestService.getRequests(1L);
        assertThat(requests.size(), equalTo(1));
        assertThat(requests.get(0).getRequester(), equalTo(1L));
    }

    @Test
    void getRequests_whenUserNotFound_thenContentNotFoundExceptionThrown() {
        Assertions.assertThrows(
                ContentNotFoundException.class,
                () -> requestService.getRequests(11L));
    }

    @Test
    void addRequest() {
        //given
        Event event = createEvent();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(11);
        event.setRequestModeration(true);
        event.setState(State.PUBLISHED);
        event.getInitiator().setId(2L);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(eventRepository.findById(anyLong()))
                .thenReturn(Optional.of(event));
        when(requestRepository.save(any()))
                .thenReturn(createRequest());
        RequestDto requestDto = requestService.addRequest(1L, 1L);
        //then
        verify(eventRepository, never()).save(any());
        assertThat(event.getConfirmedRequests(), equalTo(1));
        assertThat(requestDto.getEvent(), equalTo(1L));
        assertThat(requestDto.getRequester(), equalTo(1L));
        assertThat(requestDto.getStatus(), equalTo(Status.PENDING));
    }

    @Test
    void addRequest_whenRequestConfirmed_thenChangeStatusAndIncreaseEventConfirmedRequests() {
        //given
        Event event = createEvent();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(11);
        event.setRequestModeration(false);
        event.setState(State.PUBLISHED);
        event.getInitiator().setId(2L);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(eventRepository.findById(anyLong()))
                .thenReturn(Optional.of(event));
        when(requestRepository.save(any()))
                .thenReturn(createRequest());
        requestService.addRequest(1L, 1L);
        //then
        verify(eventRepository, times(1)).save(any());
        verify(requestRepository, times(1)).save(requestArgumentCaptor.capture());
        Request request = requestArgumentCaptor.getValue();
        assertThat(event.getConfirmedRequests(), equalTo(2));
        assertThat(request.getEvent().getId(), equalTo(1L));
        assertThat(request.getRequester().getId(), equalTo(1L));
        assertThat(request.getStatus(), equalTo(Status.CONFIRMED));
    }

    @Test
    void addRequest_whenRequesterIsInitiatorOfEvent_ThenExceptionThrown() {
        //given
        Event event = createEvent();
        event.getInitiator().setId(1L);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(eventRepository.findById(anyLong()))
                .thenReturn(Optional.of(event));
        //then
        verify(eventRepository, never()).save(any());
        verify(requestRepository, never()).save(any());
        Assertions.assertThrows(
                ContentAlreadyExistException.class,
                () -> requestService.addRequest(1L, 1L));
    }

    @Test
    void addRequest_whenEventIsUnpublished_ThenExceptionThrown() {
        //given
        Event event = createEvent();
        event.setState(State.PUBLISHED);
        event.setRequestModeration(false);
        event.setConfirmedRequests(11);
        event.setParticipantLimit(11);
        event.getInitiator().setId(2L);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(eventRepository.findById(anyLong()))
                .thenReturn(Optional.of(event));
        //then
        Assertions.assertThrows(
                ContentAlreadyExistException.class,
                () -> requestService.addRequest(1L, 1L));
    }

    @Test
    void addRequest_whenUserRepeatRequest_ThenExceptionThrown() {
        //given
        Event event = createEvent();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(11);
        event.setRequestModeration(false);
        event.setState(State.PUBLISHED);
        event.getInitiator().setId(2L);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(eventRepository.findById(anyLong()))
                .thenReturn(Optional.of(event));
        when(requestRepository.findRequestByRequesterIdAndEventId(anyLong(), anyLong()))
                .thenReturn(Optional.of(createRequest()));
        //then
        Assertions.assertThrows(
                ContentAlreadyExistException.class,
                () -> requestService.addRequest(1L, 1L));
    }

    @Test
    void cancelRequest_whenRequestConfirmed_thenChangeStatusAndDecreaseEventConfirmedRequests() {
        //given
        Event event = createEvent();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(11);
        event.setRequestModeration(false);
        event.setState(State.PUBLISHED);
        event.getInitiator().setId(2L);

        Request request = createRequest();
        request.setEvent(event);
        request.setStatus(Status.CONFIRMED);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));
        when(requestRepository.save(any()))
                .thenReturn(request);
        requestService.cancelRequest(1L, 1L);
        //then
        verify(eventRepository, times(1)).save(any());
        verify(requestRepository, times(1)).save(requestArgumentCaptor.capture());
        Request savedRequest = requestArgumentCaptor.getValue();
        assertThat(savedRequest.getEvent().getConfirmedRequests(), equalTo(0));
        assertThat(savedRequest.getEvent().getId(), equalTo(1L));
        assertThat(savedRequest.getRequester().getId(), equalTo(1L));
        assertThat(savedRequest.getStatus(), equalTo(Status.PENDING));
    }

    @Test
    void cancelRequest_whenRequestNotConfirmed_thenThrowException() {
        //given
        Event event = createEvent();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(11);
        event.setRequestModeration(false);
        event.setState(State.PUBLISHED);
        event.getInitiator().setId(2L);

        Request request = createRequest();
        request.setEvent(event);
        request.setStatus(Status.REJECTED);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));
        //then
        verify(eventRepository, never()).save(any());
        verify(requestRepository, never()).save(any());

        Assertions.assertThrows(
                ContentNotFoundException.class,
                () -> requestService.cancelRequest(1L, 1L));
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("akhraa1@yandex.ru")
                .name("Akhra")
                .build();
    }

    private Event createEvent() {
        return Event.builder()
                .id(1L)
                .initiator(createUser())
                .build();
    }

    private Request createRequest() {
        return Request.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .requester(createUser())
                .event(createEvent())
                .status(Status.PENDING)
                .build();
    }
}