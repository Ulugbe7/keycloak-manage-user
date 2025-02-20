package uz.zaytun.zaytunuserms.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.springframework.stereotype.Service;
import uz.zaytun.zaytunuserms.config.ApplicationProperties;
import uz.zaytun.zaytunuserms.service.RoleService;
import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.permission.RoleDTO;
import uz.zaytun.zaytunuserms.utils.ConversionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final Keycloak keycloak;
    private final ApplicationProperties applicationProperties;

    @Override
    public BaseResponse<RoleDTO> getByRoleName(String roleName) {
        try {
            var role = roleResource().get(roleName).toRepresentation();
            return new BaseResponse<>(ConversionUtils.createRoleDTO(role));
        } catch (Exception e) {
            log.error("Error getByRoleName: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<RoleDTO> create(RoleDTO request) {
        try {
            roleResource().create(ConversionUtils.createRoleRepresentation(request));
            return new BaseResponse<>(true, "Role created successfully!!!");
        } catch (Exception e) {
            log.error("Error creating role: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<List<RoleDTO>> findAll() {
        return new BaseResponse<>(roleResource().list().stream().map(ConversionUtils::createRoleDTO).toList());
    }

    @Override
    public BaseResponse<RoleDTO> update(RoleDTO request) {
        try {
            var roleResource = roleResource().get(request.getRoleName());
            var role = roleResource.toRepresentation();
            role.setDescription(request.getDescription());
            roleResource.update(role);
            return new BaseResponse<>(true, "Role updated successfully!!!");
        } catch (Exception e) {
            log.error("Error updating role: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    @Override
    public BaseResponse<String> delete(String roleName) {
        try {
            roleResource().deleteRole(roleName);
            return new BaseResponse<>("Role deleted successfully");
        } catch (Exception e) {
            log.error("Error to delete role: {}", e.getMessage());
            return new BaseResponse<>(false, e.getMessage());
        }
    }

    private RolesResource roleResource() {
        return keycloak.realm(applicationProperties.getKeycloak().getRealm()).roles();
    }
}
