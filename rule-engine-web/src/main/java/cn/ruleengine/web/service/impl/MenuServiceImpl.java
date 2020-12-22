package cn.ruleengine.web.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.service.MenuService;
import cn.ruleengine.web.store.mapper.RuleEngineMenuMapper;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.menu.ListMenuResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/10/22
 * @since 1.0.0
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private RuleEngineMenuMapper ruleEngineMenuMapper;

    /**
     * 根据用户id查询他的菜单
     *
     * @param userId userId
     * @return List
     */
    @Override
    public List<ListMenuResponse> listMenuByUserId(Integer userId) {
        return this.ruleEngineMenuMapper.listMenuByUserId(userId).stream()
                .map(BasicConversion.INSTANCE::convert).collect(Collectors.toList());
    }

    /**
     * 当前用户菜单树
     *
     * @return menuTree
     */
    @Override
    public List<ListMenuResponse> menuTree() {
        Integer id = Context.getCurrentUser().getId();
        List<ListMenuResponse> rootMenu = this.listMenuByUserId(id);
        if (CollUtil.isEmpty(rootMenu)) {
            return null;
        }
        // 排序
        rootMenu.sort(Comparator.comparing(ListMenuResponse::getSort));
        // 最后的结果
        List<ListMenuResponse> menuList = new ArrayList<>();
        // 先找到所有的一级菜单
        for (ListMenuResponse listMenuResponse : rootMenu) {
            // 一级菜单没有parentId
            if (Validator.isEmpty(listMenuResponse.getParentId())) {
                menuList.add(listMenuResponse);
            }
        }
        // 为一级菜单设置子菜单，iterateMenus是递归调用的
        for (ListMenuResponse menu : menuList) {
            menu.setChildren(iterateMenus(rootMenu, menu.getId()));
        }
        return menuList;
    }


    /**
     * 多级菜单查询方法
     *
     * @param menuVoList 不包含最高层次菜单的菜单集合
     * @param pid        父类id
     * @return ListMenuResponse
     */
    private List<ListMenuResponse> iterateMenus(List<ListMenuResponse> menuVoList, Integer pid) {
        List<ListMenuResponse> result = new ArrayList<>();
        for (ListMenuResponse menu : menuVoList) {
            //获取菜单的父id
            Integer parentId = menu.getParentId();
            if (Validator.isNotEmpty(parentId)) {
                if (parentId.equals(pid)) {
                    //获取菜单的id
                    Integer menuId = menu.getId();
                    //递归查询当前子菜单的子菜单
                    List<ListMenuResponse> iterateMenu = iterateMenus(menuVoList, menuId);
                    menu.setChildren(iterateMenu);
                    result.add(menu);
                }
            }
        }
        return result;
    }

}
