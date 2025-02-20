package uz.zaytun.zaytunuserms.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zaytun.zaytunuserms.service.GroupService;
import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.group.GroupDTO;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/{groupName}")
    public ResponseEntity<BaseResponse<GroupDTO>> getByGroupName(@PathVariable String groupName) {
        log.info("REST getByGroupName: {}", groupName);
        return ResponseEntity.ok(groupService.getByGroupName(groupName));
    }

    @GetMapping("/find-all")
    public ResponseEntity<BaseResponse<List<GroupDTO>>> findAll() {
        log.info("REST findAll");
        return ResponseEntity.ok(groupService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<GroupDTO>> createGroup(@Valid @RequestBody GroupDTO request) {
        log.info("REST createGroup: {}", request);
        return ResponseEntity.ok(groupService.create(request));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<GroupDTO>> updateGroup(@RequestBody GroupDTO request) {
        log.info("REST updateGroup: {}", request);
        return ResponseEntity.ok(groupService.update(request));
    }

    @DeleteMapping("/delete/{groupName}")
    public ResponseEntity<BaseResponse<String>> deleteByGroupName(@PathVariable String groupName) {
        log.info("REST deleteByGroupName: {}", groupName);
        return ResponseEntity.ok(groupService.delete(groupName));
    }

    @PostMapping("/assign-roles/{groupId}")
    public ResponseEntity<BaseResponse<String>> assignRoles(@PathVariable String groupId, @RequestBody List<String> roleNames) {
        log.info("REST assignRoles: groupId: {}, roleNames: {}", groupId, roleNames);
        return ResponseEntity.ok(groupService.assignRoles(groupId, roleNames));
    }

    @PostMapping("/unassign-roles/{groupId}")
    public ResponseEntity<BaseResponse<String>> unassignRoles(@PathVariable String groupId, @RequestBody List<String> roleNames) {
        log.info("REST unassignRoles: groupId: {}, roleNames: {}", groupId, roleNames);
        return ResponseEntity.ok(groupService.unassignRoles(groupId, roleNames));
    }
}
