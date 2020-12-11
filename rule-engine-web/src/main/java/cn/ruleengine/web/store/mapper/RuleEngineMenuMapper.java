package cn.ruleengine.web.store.mapper;

import cn.ruleengine.web.store.entity.RuleEngineMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dqw
 * @since 2020-09-24
 */
public interface RuleEngineMenuMapper extends BaseMapper<RuleEngineMenu> {


    /**
     * 根据用户id获取角色的菜单
     *
     * @param userId 用户id
     * @return BootMenu
     */
    List<RuleEngineMenu> listMenuByUserId(@Param("userId") Integer userId);

}
