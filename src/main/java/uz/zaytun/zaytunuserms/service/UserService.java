package uz.zaytun.zaytunuserms.service;


import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    BaseResponse<UserDTO> createUser(UserDTO request);

    BaseResponse<UserDTO> getByUsername(String username);

    BaseResponse<String> deleteUserByUsername(String username);

    BaseResponse<UserDTO> updateUser(UserDTO request);

    BaseResponse<String> blockUser(String username);

    BaseResponse<String> unblockUser(String username);

    BaseResponse<String> assignRoles(String userId, List<String> roleNames);

    BaseResponse<String> unassignRoles(String userId, List<String> roleNames);

    BaseResponse<String> joinGroups(String userId, List<String> groupIds);

    BaseResponse<String> leaveGroups(String userId, List<String> groupIds);

}