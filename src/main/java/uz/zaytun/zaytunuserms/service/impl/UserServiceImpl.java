package uz.zaytun.zaytunuserms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.zaytun.zaytunuserms.config.ApplicationProperties;
import uz.zaytun.zaytunuserms.service.UserService;
import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.user.UserDTO;

import java.util.List;

import static uz.zaytun.zaytunuserms.utils.ConversionUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND = "User not found";

    private final Keycloak keycloak;
    private final ApplicationProperties applicationProperties;

    @Override
    public BaseResponse<UserDTO> createUser(UserDTO request) {

        var response = userResource().create(createUserRepresentation(request));

        if (response.getStatus() != HttpStatus.CREATED.value()) {
            log.warn("RESPONSE form createUser() -> {}", response.getStatusInfo().getReasonPhrase());
            return new BaseResponse<>(false, response.getStatusInfo().getReasonPhrase());
        }
        String location = response.getHeaderString("Location");
        String userId = location.substring(location.lastIndexOf('/') + 1);
        request.setId(userId);
        log.info("RESPONSE from createUser() -> {}", response.getStatusInfo().getReasonPhrase());
        response.close();

        return new BaseResponse<>(request);
    }

    @Override
    public BaseResponse<UserDTO> getByUsername(String username) {
        var userRepresentations = userResource().search(username);

        log.info("Keycloak response getByUsername: {}", userRepresentations);

        if (userRepresentations.isEmpty()) {
            log.info("Keycloak response getByUsername: {} not found", username);
            return new BaseResponse<>(false, USER_NOT_FOUND);
        }

        return new BaseResponse<>(createUserDTO(userRepresentations.get(0)));
    }

    @Override
    public BaseResponse<String> deleteUserByUsername(String username) {

        var userRepresentations = userResource().search(username, true);

        if (userRepresentations.isEmpty()) {
            log.warn("Keycloak response deleteUserByUsername: {} not found", username);
            return new BaseResponse<>(false, USER_NOT_FOUND);
        }

        var user = userRepresentations.get(0);
        userResource().get(user.getId()).remove();

        return new BaseResponse<>(true, "User successfully deleted!!!");
    }

    @Override
    public BaseResponse<UserDTO> updateUser(UserDTO request) {

        List<UserRepresentation> userRepresentations = userResource().search(request.getUsername(), true);

        if (userRepresentations.isEmpty()) {
            log.info("Keycloak response updateUser: {} not found", request.getUsername());
            return new BaseResponse<>(false, USER_NOT_FOUND);
        }

        var updatedUser = updateUserFields(userRepresentations, request);

        try {
            userResource().get(updatedUser.getId()).update(updatedUser);
        } catch (RuntimeException ex) {
            log.info("Error happened while updateUser -> {}", ex.getMessage());
            throw ex;
        }

        return new BaseResponse<>(createUserDTO(updatedUser));
    }

    @Override
    public BaseResponse<String> blockUser(String username) {
        var userRepresentations = userResource().search(username, true);

        if (userRepresentations.isEmpty()) {
            log.info("Keycloak response blockUser: {} not found", username);
            return new BaseResponse<>(false, USER_NOT_FOUND);
        }

        var userRepresentation = userRepresentations.get(0);
        userRepresentation.setEnabled(Boolean.FALSE);

        var userResource = userResource().get(userRepresentation.getId());
        userResource.update(userRepresentation);

        log.warn("RESPONSE from blockUser() -> SUCCESS");

        return new BaseResponse<>(true, "User successfully blocked!!!");
    }

    @Override
    public BaseResponse<String> unblockUser(String username) {
        var userRepresentations = userResource().search(username, true);

        if (userRepresentations.isEmpty()) {
            log.info("Keycloak response unblockUser: {} not found", username);
            return new BaseResponse<>(false, USER_NOT_FOUND);
        }

        var userRepresentation = userRepresentations.get(0);
        userRepresentation.setEnabled(Boolean.TRUE);

        var userResource = userResource().get(userRepresentation.getId());
        userResource.update(userRepresentation);

        log.warn("RESPONSE from unblockUser() -> SUCCESS");

        return new BaseResponse<>(true, "User successfully unblocked!!!");
    }

    @Override
    public BaseResponse<String> assignRoles(String userId, List<String> roleNames) {
        try {
            RolesResource rolesResource = realmResource().roles();
            UsersResource usersResource = userResource();

            List<RoleRepresentation> roles = roleNames.stream()
                    .map(roleName -> rolesResource.get(roleName).toRepresentation())
                    .toList();

            UserResource userResource = usersResource.get(userId);

            userResource.roles().realmLevel().add(roles);

            log.info("Realm Roles {} assigned to User '{}'", roleNames, userId);
            return new BaseResponse<>(true, "Roles assigned to User successfully!!!");
        } catch (Exception e) {
            log.error("Failed to assign realm roles {} to user '{}': {}", roleNames, userId, e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> unassignRoles(String userId, List<String> roleNames) {
        try {
            RolesResource rolesResource = realmResource().roles();
            UsersResource usersResource = userResource();

            List<RoleRepresentation> roles = roleNames.stream()
                    .map(roleName -> rolesResource.get(roleName).toRepresentation())
                    .toList();

            UserResource userResource = usersResource.get(userId);

            userResource.roles().realmLevel().remove(roles);

            log.info("Realm Roles {} removed from User '{}'", roleNames, userId);
            return new BaseResponse<>(true, "Roles removed from User successfully!!!");
        } catch (Exception e) {
            log.error("Failed to remove realm roles {} from user '{}': {}", roleNames, userId, e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> joinGroups(String userId, List<String> groupIds) {
        try {
            UsersResource usersResource = userResource();
            UserResource userResource = usersResource.get(userId);

            for (String groupId : groupIds) {
                userResource.joinGroup(groupId);
            }

            log.info("User '{}' added to Groups '{}'", userId, groupIds);
            return new BaseResponse<>(true, "User joined to Groups successfully!!!");
        } catch (Exception e) {
            log.error("Failed to add User '{}' to Groups '{}': {}", userId, groupIds, e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> leaveGroups(String userId, List<String> groupIds) {
        try {
            UsersResource usersResource = userResource();
            UserResource userResource = usersResource.get(userId);

            for (String groupId : groupIds) {
                userResource.leaveGroup(groupId);
            }

            log.info("User '{}' removed from Groups '{}'", userId, groupIds);
            return new BaseResponse<>(true, "User left to Groups successfully!!!");
        } catch (Exception e) {
            log.error("Failed to remove User '{}' from Groups '{}': {}", userId, groupIds, e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    private UsersResource userResource() {
        return keycloak.realm(applicationProperties.getKeycloak().getRealm()).users();
    }

    private RealmResource realmResource() {
        return keycloak.realm(applicationProperties.getKeycloak().getRealm());
    }
}
