package com.engine.web.store.manager.impl;

import com.engine.web.store.entity.RuleEngineUser;
import com.engine.web.store.mapper.RuleEngineUserMapper;
import com.engine.web.store.manager.RuleEngineUserManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 规则引擎用户表 服务实现类
 * </p>
 *
 * @author dqw
 * @since 2020-09-24
 */
@Service
public class RuleEngineUserManagerImpl extends ServiceImpl<RuleEngineUserMapper, RuleEngineUser> implements RuleEngineUserManager {

}
