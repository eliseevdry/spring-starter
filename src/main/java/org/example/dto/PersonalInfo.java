package org.example.dto;

import org.springframework.beans.factory.annotation.Value;

public interface PersonalInfo {

    String getFirstname();

    String getLastname();

    String getBirthDate();

    @Value("#{target.firstname + ' ' + target.lastname}")
    String getFullName();
}
