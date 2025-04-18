package entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

public class Donateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    @Email
    private String email;

    private String telephone;

    @NotNull
    private Double objectifMontant;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;
}
