package com.engine.web.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.engine.core.exception.ValidException;
import com.engine.web.interceptor.AuthInterceptor;
import com.engine.web.service.WorkspaceService;
import com.engine.web.store.entity.RuleEngineUser;
import com.engine.web.store.entity.RuleEngineWorkspace;
import com.engine.web.store.manager.RuleEngineWorkspaceManager;
import com.engine.web.vo.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2020/11/21
 * @since 1.0.0
 */
@Slf4j
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Resource
    private RuleEngineWorkspaceManager ruleEngineWorkspaceManager;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 所有用户组
     *
     * @return list
     */
    @Override
    public List<Workspace> list() {
        List<RuleEngineWorkspace> ruleEngineWorkspaces = this.ruleEngineWorkspaceManager.list();
        if (CollUtil.isEmpty(ruleEngineWorkspaces)) {
            return Collections.emptyList();
        }
        return ruleEngineWorkspaces.stream().map(m -> {
            Workspace workspace = new Workspace();
            workspace.setId(m.getId());
            workspace.setName(m.getName());
            return workspace;
        }).collect(Collectors.toList());
    }

    /**
     * 获取当前工作空间
     *
     * @return Workspace
     */
    @Override
    public Workspace currentWorkspace() {
        RuleEngineUser ruleEngineUser = AuthInterceptor.USER.get();
        RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + ruleEngineUser.getId());
        if (bucket.isExists()) {
            Workspace workspace = bucket.get();
            log.info("当前工作空间：" + workspace);
            return workspace;
        } else {
            IPage<RuleEngineWorkspace> page = this.ruleEngineWorkspaceManager.page(new Page<>(1, 1));
            List<RuleEngineWorkspace> records = page.getRecords();
            log.info("Mysql获取工作空间：" + records);
            Optional<RuleEngineWorkspace> first = records.stream().findFirst();
            if (first.isPresent()) {
                RuleEngineWorkspace ruleEngineWorkspace = first.get();
                Workspace workspace = new Workspace();
                workspace.setId(ruleEngineWorkspace.getId());
                workspace.setName(ruleEngineWorkspace.getName());
                bucket.set(workspace);
                return workspace;
            }
        }
        throw new ValidException("没有可用工作空间");
    }

    /**
     * 切换工作空间
     *
     * @param id 工作空间id
     * @return true
     */
    @Override
    public Boolean change(Integer id) {
        RuleEngineWorkspace engineWorkspace = this.ruleEngineWorkspaceManager.getById(id);
        if (engineWorkspace == null) {
            throw new ValidException("找不到此工作空间：" + id);
        }
        RuleEngineUser ruleEngineUser = AuthInterceptor.USER.get();
        RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + ruleEngineUser.getId());
        Workspace workspace = new Workspace();
        workspace.setId(engineWorkspace.getId());
        workspace.setName(engineWorkspace.getName());
        bucket.set(workspace);
        return true;
    }
}
