package main.java.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserLoginDTO {

        private String username;
        private String password;
        private int source;
        private int forceLogin;

        public UserLoginDTO(String username, String password, int source, int forceLogin) {
            this.username = username;
            this.password = password;
            this.source = source;
            this.forceLogin = forceLogin;
        }

}
