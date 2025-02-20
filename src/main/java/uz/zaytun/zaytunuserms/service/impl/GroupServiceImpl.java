package uz.zaytun.zaytunuserms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.zaytun.zaytunuserms.config.ApplicationProperties;
import uz.zaytun.zaytunuserms.service.GroupService;
import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.group.GroupDTO;
import uz.zaytun.zaytunuserms.utils.ConversionUtils;

import java.util.List;

import static uz.zaytun.zaytunuserms.utils.ConversionUtils.createGroupDTO;
import static uz.zaytun.zaytunuserms.utils.ConversionUtils.createGroupRepresentation;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    public static final String GROUP_NOT_FOUND = "Group not found";

    private final Keycloak keycloak;
    private final ApplicationProperties applicationProperties;

    @Override
    public BaseResponse<GroupDTO> getByGroupName(String groupId) {
        try {
            var group = groupResource().group(groupId).toRepresentation();
            return new BaseResponse<>(createGroupDTO(group));
        } catch (Exception e) {
            return new BaseResponse<>(false, GROUP_NOT_FOUND);
        }
    }

    @Override
    public BaseResponse<GroupDTO> create(GroupDTO request) {
        var response = groupResource().add(createGroupRepresentation(request));
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            log.warn("RESPONSE form create() -> {}", response.getStatusInfo().getReasonPhrase());
            return new BaseResponse<>(false, response.getStatusInfo().getReasonPhrase());
        }
        String location = response.getHeaderString("Location");
        String groupId = location.substring(location.lastIndexOf('/') + 1);
        request.setId(groupId);
        return new BaseResponse<>(request);
    }

    @Override
    public BaseResponse<List<GroupDTO>> findAll() {
        var groups = groupResource().groups();
        return new BaseResponse<>(groups.stream().map(ConversionUtils::createGroupDTO).toList());
    }

    @Override
    public BaseResponse<GroupDTO> update(GroupDTO request) {
        try {
            var groupResource = groupResource().group(request.getId());
            if (groupResource == null) {
                return new BaseResponse<>(false, GROUP_NOT_FOUND);
            }

            var group = groupResource.toRepresentation();
            group.setName(request.getGroupName());

            groupResource.update(group);
            return new BaseResponse<>(createGroupDTO(group));
        } catch (Exception e) {
            return new BaseResponse<>(false, GROUP_NOT_FOUND);
        }
    }

    @Override
    public BaseResponse<String> delete(String groupId) {
        try {
            groupResource().group(groupId).remove();
            return new BaseResponse<>(true, "Group deleted successfully!!!");
        } catch (Exception e) {
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> assignRoles(String groupId, List<String> roleNames) {
        try {
            RolesResource rolesResource = realmResource().roles();
            GroupsResource groupsResource = groupResource();
            GroupResource groupResource = groupsResource.group(groupId);

            List<RoleRepresentation> roles = roleNames.stream()
                    .map(roleName -> rolesResource.get(roleName).toRepresentation())
                    .toList();

            groupResource.roles().realmLevel().add(roles);

            log.info("Realm Roles {} assigned to Group '{}'", roleNames, groupId);
            return new BaseResponse<>(true, "Roles assigned to Group successfully!!!");
        } catch (Exception e) {
            log.error("Failed to assign realm roles {} to group '{}': {}", roleNames, groupId, e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> unassignRoles(String groupId, List<String> roleNames) {
        try {
            RolesResource rolesResource = realmResource().roles();
            GroupsResource groupsResource = groupResource();
            GroupResource groupResource = groupsResource.group(groupId);

            List<RoleRepresentation> roles = roleNames.stream()
                    .map(roleName -> rolesResource.get(roleName).toRepresentation())
                    .toList();

            groupResource.roles().realmLevel().remove(roles);

            log.info("Realm Roles {} removed from Group '{}'", roleNames, groupId);
            return new BaseResponse<>(true, "Roles removed from Group successfully!!!");
        } catch (Exception e) {
            log.error("Failed to remove realm roles {} from group '{}': {}", roleNames, groupId, e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    private GroupsResource groupResource() {
        return keycloak.realm(applicationProperties.getKeycloak().getRealm()).groups();
    }

    private RealmResource realmResource() {
        return keycloak.realm(applicationProperties.getKeycloak().getRealm());
    }
}
