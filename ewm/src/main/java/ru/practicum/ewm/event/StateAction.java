package ru.practicum.ewm.event;

public enum StateAction {
    //для админов
    PUBLISH_EVENT, REJECT_EVENT,
    //для пользователей
    SEND_TO_REVIEW, CANCEL_REVIEW
}
