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
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
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
    private ArgumentCaptor<ParticipationRequest> requestArgumentCaptor;

    @Test
    void getRequests() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(requestRepository.findRequestByRequesterId(anyLong()))
                .thenReturn(List.of(createRequest()));
        List<ParticipationRequestDto> requests = requestService.getRequests(1L);
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
        ParticipationRequestDto participationRequestDto = requestService.addRequest(1L, 1L);
        //then
        verify(eventRepository, never()).save(any());
        assertThat(event.getConfirmedRequests(), equalTo(1));
        assertThat(participationRequestDto.getEvent(), equalTo(1L));
        assertThat(participationRequestDto.getRequester(), equalTo(1L));
        assertThat(participationRequestDto.getStatus(), equalTo(Status.PENDING));
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
        ParticipationRequest participationRequest = requestArgumentCaptor.getValue();
        assertThat(event.getConfirmedRequests(), equalTo(2));
        assertThat(participationRequest.getEvent().getId(), equalTo(1L));
        assertThat(participationRequest.getRequester().getId(), equalTo(1L));
        assertThat(participationRequest.getStatus(), equalTo(Status.CONFIRMED));
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

        ParticipationRequest participationRequest = createRequest();
        participationRequest.setEvent(event);
        participationRequest.setStatus(Status.CONFIRMED);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(participationRequest));
        when(requestRepository.save(any()))
                .thenReturn(participationRequest);
        requestService.cancelRequest(1L, 1L);
        //then
        verify(eventRepository, times(1)).save(any());
        verify(requestRepository, times(1)).save(requestArgumentCaptor.capture());
        ParticipationRequest savedParticipationRequest = requestArgumentCaptor.getValue();
        assertThat(savedParticipationRequest.getEvent().getConfirmedRequests(), equalTo(0));
        assertThat(savedParticipationRequest.getEvent().getId(), equalTo(1L));
        assertThat(savedParticipationRequest.getRequester().getId(), equalTo(1L));
        assertThat(savedParticipationRequest.getStatus(), equalTo(Status.CANCELED));
    }

    @Test
    void cancelRequest_whenRequestNotConfirmed_thenChangeStatusToCanceled() {
        //given
        Event event = createEvent();
        event.setConfirmedRequests(1);
        event.setParticipantLimit(11);
        event.setRequestModeration(false);
        event.setState(State.PUBLISHED);
        event.getInitiator().setId(2L);

        ParticipationRequest participationRequest = createRequest();
        participationRequest.setEvent(event);
        participationRequest.setStatus(Status.REJECTED);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(createUser()));
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(participationRequest));
        when(requestRepository.save(any()))
                .thenReturn(participationRequest);
        requestService.cancelRequest(1L, 1L);
        //then
        verify(eventRepository, never()).save(any());
        verify(requestRepository, times(1)).save(requestArgumentCaptor.capture());
        ParticipationRequest savedParticipationRequest = requestArgumentCaptor.getValue();
        assertThat(savedParticipationRequest.getEvent().getConfirmedRequests(), equalTo(1));
        assertThat(savedParticipationRequest.getEvent().getId(), equalTo(1L));
        assertThat(savedParticipationRequest.getRequester().getId(), equalTo(1L));
        assertThat(savedParticipationRequest.getStatus(), equalTo(Status.CANCELED));
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

    private ParticipationRequest createRequest() {
        return ParticipationRequest.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .requester(createUser())
                .event(createEvent())
                .status(Status.PENDING)
                .build();
    }
}