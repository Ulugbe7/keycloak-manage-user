package uz.zaytun.zaytunuserms.utils;

import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import uz.zaytun.zaytunuserms.service.dto.group.GroupDTO;
import uz.zaytun.zaytunuserms.service.dto.permission.RoleDTO;
import uz.zaytun.zaytunuserms.service.dto.user.UserDTO;

import java.util.List;

@UtilityClass
public class ConversionUtils {

    public static UserRepresentation createUserRepresentation(UserDTO request) {
        var user = new UserRepresentation();
        user.setEmailVerified(true);
        user.setEmail(request.getEmail());
        user.setEnabled(true);
        user.setUsername(request.getUsername());
        user.setLastName(request.getLastname());
        user.setFirstName(request.getFirstname());

        var credential = new CredentialRepresentation();
        credential.setValue(request.getPassword());
        credential.setTemporary(Boolean.FALSE);
        credential.setType(CredentialRepresentation.PASSWORD);

        user.setCredentials(List.of(credential));
        return user;
    }

    public static UserRepresentation updateUserFields(List<UserRepresentation> users, UserDTO request) {
        UserRepresentation user = users.get(0);
        if (request.getEmail() != null)
            user.setEmail(request.getEmail());
        if (request.getFirstname() != null)
            user.setFirstName(request.getFirstname());
        if (request.getLastname() != null)
            user.setLastName(request.getLastname());
        return user;
    }

    public static UserDTO createUserDTO(UserRepresentation request) {
        return UserDTO.builder()
                .id(request.getId())
                .username(request.getUsername())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .enabled(request.isEnabled())
                .build();
    }

    public static GroupRepresentation createGroupRepresentation(GroupDTO request) {
        var group = new GroupRepresentation();
        group.setName(request.getGroupName());
        return group;
    }

    public static GroupDTO createGroupDTO(GroupRepresentation request) {
        return GroupDTO.builder()
                .id(request.getId())
                .groupName(request.getName())
                .build();
    }

    public static RoleRepresentation createRoleRepresentation(RoleDTO request) {
        var role = new RoleRepresentation();
        role.setName(request.getRoleName());
        role.setDescription(request.getDescription());
        return role;
    }

    public static RoleDTO createRoleDTO(RoleRepresentation request) {
        return RoleDTO.builder()
                .roleName(request.getName())
                .description(request.getDescription())
                .build();
    }
}
