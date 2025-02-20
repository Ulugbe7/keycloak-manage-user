package uz.zaytun.zaytunuserms.service;

import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.group.GroupDTO;

import java.util.List;

public interface GroupService {

    BaseResponse<GroupDTO> getByGroupName(String groupName);

    BaseResponse<GroupDTO> create(GroupDTO request);

    BaseResponse<List<GroupDTO>> findAll();

    BaseResponse<GroupDTO> update(GroupDTO request);

    BaseResponse<String> delete(String groupName);

    BaseResponse<String> assignRoles(String groupId, List<String> roleNames);

    BaseResponse<String> unassignRoles(String groupId, List<String> roleNames);
}
