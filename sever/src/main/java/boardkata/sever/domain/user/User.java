package boardkata.sever.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    protected User() {}

    @Builder
    private User(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
    }
}
