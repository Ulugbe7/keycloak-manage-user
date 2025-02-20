package uz.zaytun.zaytunuserms.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zaytun.zaytunuserms.service.RoleService;
import uz.zaytun.zaytunuserms.service.dto.BaseResponse;
import uz.zaytun.zaytunuserms.service.dto.permission.RoleDTO;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/{roleName}")
    public ResponseEntity<BaseResponse<RoleDTO>> getByRoleName(@PathVariable String roleName) {
        log.info("REST getByRoleName: {}", roleName);
        return ResponseEntity.ok(roleService.getByRoleName(roleName));
    }

    @GetMapping("/find-all")
    public ResponseEntity<BaseResponse<List<RoleDTO>>> findAll() {
        log.info("REST findAll");
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<RoleDTO>> createRole(@Valid @RequestBody RoleDTO request) {
        log.info("REST createRole: {}", request);
        return ResponseEntity.ok(roleService.create(request));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<RoleDTO>> updateRole(@RequestBody RoleDTO request) {
        log.info("REST updateRole: {}", request);
        return ResponseEntity.ok(roleService.update(request));
    }

    @DeleteMapping("/delete/{roleName}")
    public ResponseEntity<BaseResponse<String>> deleteByRoleName(@PathVariable String roleName) {
        log.info("REST deleteByRoleName: {}", roleName);
        return ResponseEntity.ok(roleService.delete(roleName));
    }
}
