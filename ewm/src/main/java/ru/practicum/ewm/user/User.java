package ru.practicum.ewm.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"subscribers", "friends"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    @NotEmpty
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @Column(unique = true, length = 254)
    @Email
    @Length(min = 6, max = 254)
    private String email;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "users_subscribers", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "subscriber_id", referencedColumnName = "user_id"))
//    @JsonIgnoreProperties("subscribers")
//    private List<User> subscribers = new ArrayList<>();

}
