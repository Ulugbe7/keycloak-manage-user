package uz.zaytun.zaytunuserms.service;

import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.permission.RoleDTO;

import java.util.List;

public interface RoleService {

    BaseResponse<RoleDTO> getByRoleName(String roleName);

    BaseResponse<RoleDTO> create(RoleDTO request);

    BaseResponse<List<RoleDTO>> findAll();

    BaseResponse<RoleDTO> update(RoleDTO request);

    BaseResponse<String> delete(String roleId);
}
