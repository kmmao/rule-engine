package cn.ruleengine.web.service;

import cn.ruleengine.web.vo.menu.ListMenuResponse;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/10/22
 * @since 1.0.0
 */
public interface MenuService {
    /**
     * 根据用户id查询他的菜单
     *
     * @param userId userId
     * @return List
     */
    List<ListMenuResponse> listMenuByUserId(Integer userId);

    /**
     * 当前用户菜单树
     *
     * @return list
     */
    List<ListMenuResponse> menuTree();
}
