package com.engine.web.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.engine.web.service.MenuService;
import com.engine.web.store.mapper.RuleEngineMenuMapper;
import com.engine.web.vo.menu.ListMenuResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        return ruleEngineMenuMapper.listMenuByUserId(userId).stream()
                .map(m -> {
                    ListMenuResponse listMenuResponse = new ListMenuResponse();
                    BeanUtil.copyProperties(m,listMenuResponse);
                    return listMenuResponse;
                }).collect(Collectors.toList());
    }
}
