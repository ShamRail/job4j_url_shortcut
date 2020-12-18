package ru.job4j.shortcut.model.dto;

import java.util.Objects;

public class LinkRegistrationDTO {

    private String code;

    public LinkRegistrationDTO() { }

    public LinkRegistrationDTO(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkRegistrationDTO that = (LinkRegistrationDTO) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
