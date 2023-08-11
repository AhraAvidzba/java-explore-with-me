package ru.practicum.ewm.location.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class LocationDto {
    private float lat; //ширина
    private float lon; //долгота
}
