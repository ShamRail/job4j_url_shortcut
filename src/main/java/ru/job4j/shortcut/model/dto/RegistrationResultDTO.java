package ru.job4j.shortcut.model.dto;

import java.util.Objects;

public class RegistrationResultDTO {

    private boolean registration;

    private String login;

    private String password;

    public RegistrationResultDTO() { }

    public RegistrationResultDTO(boolean registration, String login, String password) {
        this.registration = registration;
        this.login = login;
        this.password = password;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistrationResultDTO resultDTO = (RegistrationResultDTO) o;
        return registration == resultDTO.registration &&
                Objects.equals(login, resultDTO.login) &&
                Objects.equals(password, resultDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registration, login, password);
    }
}
