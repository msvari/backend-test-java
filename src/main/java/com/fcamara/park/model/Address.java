package com.fcamara.park.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private String street;

    @NotNull
    private String number;

    private String complement;

    private String zipCode;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private String country;

}



