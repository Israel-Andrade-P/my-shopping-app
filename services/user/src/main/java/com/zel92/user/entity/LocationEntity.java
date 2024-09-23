package com.zel92.user.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "locations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class LocationEntity extends BaseEntity{
    @NotNull
    @Column(name = "country", nullable = false)
    private String country;
    @NotNull
    @Column(name = "city")
    private String city;
    @NotNull
    @Column(name = "street")
    private String street;
    @NotNull
    @Column(name = "zip_code")
    private String zipCode;
    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
