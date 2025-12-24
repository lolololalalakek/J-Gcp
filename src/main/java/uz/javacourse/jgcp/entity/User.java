package uz.javacourse.jgcp.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.javacourse.jgcp.constant.enums.DocumentType;
import uz.javacourse.jgcp.constant.enums.Gender;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    // уникальный идентификатор пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // полное имя пользователя
    @Column(nullable = false)
    private String fullName;

    // адрес проживания
    @Column(nullable = false)
    private String address;

    // номер телефона
    @Column(nullable = false)
    private String phoneNumber;

    // электронная почта
    @Column(nullable = false, unique = true)
    private String email;

    // ссылка на фотографию
    @Column
    private String photoUrl;

    // персональный идентификационный номер (14 символов)
    @Column(nullable = false, unique = true, length = 14)
    private String pinfl;

    // возраст пользователя
    @Column(nullable = false)
    private Integer age;

    // пол (мужской/женский)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    // тип документа (паспорт, id-карта и др.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    // дата выдачи документа
    @Column(nullable = false)
    private LocalDate issueDate;

    // дата окончания срока действия документа
    @Column(nullable = false)
    private LocalDate expiryDate;

    // гражданство
    @Column(nullable = false)
    private String citizenship;

    // дата смерти (если применимо)
    @Column
    private LocalDate deathDate;
}
