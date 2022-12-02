package com.example.demo.entity.user;

import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.notification.Notification;

import com.example.demo.validator.group.BasicInfo;
import com.example.demo.validator.group.RegistrationInfo;
import com.example.demo.validator.password.PasswordMatches;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

@Data
@PasswordMatches(groups = RegistrationInfo.class)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "user_name")
    @Length(min = 5, max = 30, message = "*Ваше имя пользователя должно содержать от 5 до 30 символов", groups = RegistrationInfo.class)
    @NotBlank(message = "*Пожалуйста, укажите имя пользователя", groups = RegistrationInfo.class)
    @Pattern(regexp = "^[A-Za-z0-9\\_\\.]+$", message = "*Имя пользователя может содержать только \"A-Z\", \"a-z\", \"0-9\", \"_\" и \".\"", groups = RegistrationInfo.class)
    private String userName;

    @Column(name = "password")
    @Length(min = 5, message = "*Ваш пароль должен содержить минимум 5 символов", groups = RegistrationInfo.class)
    @NotBlank(message = "*Пожалуйста, укажите пароль", groups = RegistrationInfo.class)
    private String password;

    @Transient
    private String matchingPassword;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "email")
    @Email(message = "*Пожалуйста, укажите корректную почту", groups = BasicInfo.class)
    @NotBlank(message = "*Пожалуйста, укажите почту", groups = BasicInfo.class)
    private String email;

    @Column(name = "phone")
    @NotBlank(message = "*Пожалуйста, введите свой номер", groups = BasicInfo.class)
    @Pattern(regexp = "^\\+375\\((25|29|33|44)\\)\\d{3}\\-\\d{2}\\-\\d{2}$", message = "*Пожалуйста, используйте данный шаблон +375(XX)XXX-XX-XX", groups = BasicInfo.class)
    private String phone;

    @Column(name = "first_name")
    @NotBlank(message = "*Пожалуйста, укажите своё имя", groups = BasicInfo.class)
    @Pattern(regexp = "^[ЁёA-Za-zА-Яа-я\\-]+$", message = "*Пожалуйста, введите корректно своё имя", groups = BasicInfo.class)
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "*Пожалуйста, укажите свою фамилию", groups = BasicInfo.class)
    @Pattern(regexp = "^[ЁёA-Za-zА-Яа-я\\-]+$", message = "*Пожалуйста, введите корректно свою фамилию", groups = BasicInfo.class)
    private String lastName;

    @Column(name = "active")
    private boolean active;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user")
    private List<Advertisement> advertisements;

    @ManyToMany
    @JoinTable(name = "user_advertisement",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "advertisement_id_id"))
    private List<Advertisement> favoritesAdvertisement;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_to_id")
    private List<Notification> notifications;
}
