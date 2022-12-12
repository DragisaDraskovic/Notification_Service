package com.backend.intern.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MessageDto implements Serializable {

    private String courtName;

    private String playerEmail;

    private Date reservationDate;
}
