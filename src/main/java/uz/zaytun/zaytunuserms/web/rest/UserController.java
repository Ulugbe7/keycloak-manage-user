package uz.zaytun.zaytunuserms.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zaytun.zaytunuserms.service.UserService;
import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.user.UserDTO;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO request) {
        log.info("REST createUser: {}", request);
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping("/{username}")
    public ResponseEntity<BaseResponse<UserDTO>> getByUsername(@PathVariable String username) {
        log.info("REST getByUsername: {}", username);
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<BaseResponse<String>> deleteByUsername(@PathVariable String username) {
        log.info("REST deleteByUsername: {}", username);
        return ResponseEntity.ok(userService.deleteUserByUsername(username));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<UserDTO>> updateUser(@RequestBody UserDTO request) {
        log.info("REST updateUser: {}", request);
        return ResponseEntity.ok(userService.updateUser(request));
    }

    @PostMapping("/block")
    public ResponseEntity<BaseResponse<String>> block(@RequestBody UserDTO request) {
        log.info("REST block: {}", request.getUsername());
        return ResponseEntity.ok(userService.blockUser(request.getUsername()));
    }

    @PostMapping("/unblock")
    public ResponseEntity<BaseResponse<String>> unblock(@RequestBody UserDTO request) {
        log.info("REST unblock: {}", request.getUsername());
        return ResponseEntity.ok(userService.unblockUser(request.getUsername()));
    }

    @PostMapping("/assign-roles/{userId}")
    public ResponseEntity<BaseResponse<String>> assignRoles(@PathVariable String userId, @RequestBody List<String> roleNames) {
        log.info("REST assignRoles: userId: {}, roleNames: {}", userId, roleNames);
        return ResponseEntity.ok(userService.assignRoles(userId, roleNames));
    }

    @PostMapping("/unassign-roles/{userId}")
    public ResponseEntity<BaseResponse<String>> unassignRoles(@PathVariable String userId, @RequestBody List<String> roleNames) {
        log.info("REST unassignRoles: userId: {}, roleNames: {}", userId, roleNames);
        return ResponseEntity.ok(userService.unassignRoles(userId, roleNames));
    }

    @PostMapping("/join-group/{userId}")
    public ResponseEntity<BaseResponse<String>> joinGroups(@PathVariable String userId, @RequestBody List<String> groupIds) {
        log.info("REST joinGroups: userId: {}, groupIds: {}", userId, groupIds);
        return ResponseEntity.ok(userService.joinGroups(userId, groupIds));
    }

    @PostMapping("/leave-group/{userId}")
    public ResponseEntity<BaseResponse<String>> leaveGroups(@PathVariable String userId, @RequestBody List<String> groupIds) {
        log.info("REST leaveGroups: userId: {}, groupIds: {}", userId, groupIds);
        return ResponseEntity.ok(userService.leaveGroups(userId, groupIds));
    }
}
