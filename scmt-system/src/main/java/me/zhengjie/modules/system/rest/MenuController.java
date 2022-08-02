package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import me.zhengjie.aop.log.Log;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SecurityUtil;
import me.zhengjie.common.vo.Result;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.system.domain.Menu;
import me.zhengjie.system.service.MenuService;
import me.zhengjie.system.service.RoleService;
import me.zhengjie.system.service.UserService;
import me.zhengjie.system.service.dto.MenuDto;
import me.zhengjie.system.service.dto.MenuQueryCriteria;
import me.zhengjie.system.service.dto.RoleSmallDto;
import me.zhengjie.system.service.dto.UserDto;
import me.zhengjie.utils.SecurityUtils;

import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@Api(tags = "系统：菜单管理")
@RestController
@RequestMapping("/api/menus")
@SuppressWarnings("unchecked")
public class MenuController {

    private final MenuService menuService;

    private final UserService userService;

    private final RoleService roleService;

    private static final String ENTITY_NAME = "menu";

    @Autowired
    private SecurityUtil securityUtil;

    public MenuController(MenuService menuService, UserService userService, RoleService roleService) {
        this.menuService = menuService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @Log("导出菜单数据")
    @ApiOperation("导出菜单数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteria criteria) throws IOException {
        menuService.download(menuService.queryAll(criteria), response);
    }

    @ApiOperation("获取前端所需菜单")
    @GetMapping(value = "/build")
    public ResponseEntity<Object> buildMenus() {
        UserDto user = userService.findByName(SecurityUtils.getUsername());
        List<MenuDto> menuDtoList = menuService.findByRoles(roleService.findByUsersId(user.getId()));
        List<MenuDto> menuDtos = (List<MenuDto>) menuService.buildTree(menuDtoList).get("content");
        return new ResponseEntity<>(menuService.buildMenus(menuDtos), HttpStatus.OK);
    }

    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/tree")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Object> getMenuTree() {
        //List<Menu> byPid = menuService.findByPid(0L);
        List<Menu> byLevel = menuService.findByLevel(0);
        return new ResponseEntity<>(menuService.getMenuTree(byLevel), HttpStatus.OK);
    }

    @Log("查询菜单")
    @ApiOperation("查询菜单")
    @GetMapping("/getMenus")
    @PreAuthorize("@el.check('menu:list')")
    public Result<Object> getMenus(MenuQueryCriteria menuQueryCriteria) {
        List<MenuDto> menuDtoList = menuService.queryAll(menuQueryCriteria);
        return new ResultUtil<>().setData(menuService.buildMenuTree(menuDtoList));
    }

    @Log("新增菜单")
    @ApiOperation("新增菜单")
    @PostMapping("/addMeuns")
    @PreAuthorize("@el.check('menu:add')")
    public Result<Object> create(@RequestBody Menu resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        MenuDto menuDto = menuService.create(resources);
        if (menuDto != null) {
            return new ResultUtil<>().setSuccessMsg("添加成功！");
        } else {
            return new ResultUtil<>().setErrorMsg("添加失败！");
        }
    }

    @Log("修改菜单")
    @ApiOperation("修改菜单")
    @PostMapping("/editMenus")
    @PreAuthorize("@el.check('menu:edit')")
    public Result<Object> update(@RequestBody Menu resources) {
        menuService.update(resources);
        return new ResultUtil<>().setSuccessMsg("修改成功！");
    }

    @Log("删除菜单")
    @ApiOperation("删除菜单")
    @PostMapping("/delete")
    @PreAuthorize("@el.check('menu:del')")
    public Result<Object> delete(@RequestParam(name = "ids[]") Long[] ids) {
        Set<Menu> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<Menu> menuList = menuService.findByPid(id);
            menuSet.add(menuService.findOne(id));
            menuSet = menuService.getDeleteMenus(menuList, menuSet);
        }
        menuService.delete(menuSet);
        return new ResultUtil<>().setSuccessMsg("删除成功！");
    }


    @ApiOperation("获取前端左侧树所需菜单")
    @GetMapping(value = "/getLeftMenuTree")
    public Result<Object> getLeftMenuTree(MenuQueryCriteria menuQueryCriteria,Long roleId) {
        List<MenuDto> menuDtoList =  new ArrayList<>();
        if(roleId!=0){
            menuDtoList = menuService.getLeftMenuTreeByRoleId(roleId,menuQueryCriteria);
        }
        else {
            menuDtoList = menuService.getLeftMenuTree(securityUtil.getCurrUser().getId().toString(), menuQueryCriteria);
        }
        return new ResultUtil<>().setData(menuService.buildMenuTree(menuDtoList));
    }

}
