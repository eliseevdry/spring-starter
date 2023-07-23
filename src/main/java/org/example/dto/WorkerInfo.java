package org.example.dto;

import java.time.LocalDate;

public record WorkerInfo(String firstname, String lastname, LocalDate birthDate) {
}
