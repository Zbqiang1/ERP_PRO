package com.erp.modules.system.controller;

import com.erp.common.result.Result;
import com.erp.common.security.SecurityUtils;
import com.erp.modules.system.dto.LoginDTO;
import com.erp.modules.system.dto.UserDTO;
import com.erp.modules.system.service.IUserService;
import com.erp.modules.system.vo.LoginVO;
import com.erp.modules.system.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author ERP Team
 * @since 2026-06-29
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.ok(loginVO);
    }

    /**
     * 获取当前登录用户信息
     * 前端登录后调用此接口获取用户详情（角色、权限等）
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        String userId = SecurityUtils.getCurrentUserId();
        String username = SecurityUtils.getCurrentUsername();

        Map<String, Object> info = new HashMap<>();
        info.put("userId", userId);
        info.put("username", username);

        // 如果已登录，查询完整的用户信息
        if (userId != null) {
            try {
                UserVO userVO = userService.getUserById(Long.valueOf(userId));
                info.put("realName", userVO.getRealName());
                info.put("avatar", userVO.getAvatar());
                info.put("roles", userVO.getRoleNames());
                info.put("permissions", java.util.Collections.emptyList()); // 后续从角色菜单中获取
            } catch (Exception e) {
                info.put("realName", username);
                info.put("avatar", "");
                info.put("roles", java.util.Collections.emptyList());
                info.put("permissions", java.util.Collections.emptyList());
            }
        }

        return Result.ok(info);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody UserDTO userDTO) {
        boolean result = userService.addUser(userDTO);
        return Result.ok(result);
    }

    /**
     * 退出登录
     * JWT 无状态，前端清除本地 token 即可，后端仅记录日志
     */
    @PostMapping("/logout")
    public Result<Boolean> logout() {
        return Result.ok(true);
    }

    /**
     * 修改当前登录用户密码
     */
    @PutMapping("/password")
    public Result<Boolean> updatePassword(@RequestBody Map<String, String> passwordMap) {
        String userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            return Result.fail("未登录或登录已过期");
        }
        String newPassword = passwordMap.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.fail("新密码不能为空");
        }
        try {
            Long id = Long.valueOf(userId);
            boolean result = userService.resetPassword(id, newPassword);
            return Result.ok(result);
        } catch (NumberFormatException e) {
            return Result.fail("用户信息获取失败");
        }
    }
}
