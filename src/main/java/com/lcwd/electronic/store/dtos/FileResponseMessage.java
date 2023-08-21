package com.lcwd.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseMessage {
    private String message;
    private boolean success;
    private HttpStatus status;
}
