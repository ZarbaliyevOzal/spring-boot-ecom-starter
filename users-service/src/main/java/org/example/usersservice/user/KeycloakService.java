package org.example.usersservice.user;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.userprofile.config.UPConfig;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    private final Keycloak keycloak;

    public KeycloakService() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:9090") // keycloak url
                .realm("master")                    // admin realm
                .username("admin")                  // admin user
                .password("admin")                  // admin password
                .clientId("admin-cli")              // Default admin client
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    public UserRepresentation createUser(User savedUser, String password) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(savedUser.getEmail());
        user.setEmail(savedUser.getEmail());
        user.setFirstName(savedUser.getFirstName());
        user.setLastName(savedUser.getLastName());
        user.setAttributes(Map.of("userId", List.of(savedUser.getId().toString())));
        user.setEnabled(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false); // set true if you want user to reset password

        user.setCredentials(Collections.singletonList(credential));

        keycloak.realm("ecom_starter").users().create(user);
        System.out.println("User created: " + user.getId());

        return user;
    }

    public void updateUser(User savedUser) {
        UsersResource usersResource = keycloak.realm("ecom_starter").users();
        List<UserRepresentation> users = usersResource.search(savedUser.getEmail());
        UserRepresentation user = users.getFirst();

        UserResource userResource = usersResource.get(user.getId());

        user.setFirstName(savedUser.getFirstName());
        user.setLastName(savedUser.getLastName());

        // update user
        userResource.update(user);
    }

    public void disableOrEnableUser(User user, Boolean enable) {
        UsersResource usersResource = keycloak.realm("ecom_starter").users();
        List<UserRepresentation> users = usersResource.search(user.getEmail());
        UserRepresentation keycloakUser = users.getFirst();

        UserResource userResource = usersResource.get(keycloakUser.getId());

        keycloakUser.setEnabled(enable);

        userResource.update(keycloakUser);
    }
}
