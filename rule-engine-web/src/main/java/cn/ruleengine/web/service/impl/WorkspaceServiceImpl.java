package cn.ruleengine.web.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.collection.CollUtil;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.AccessKey;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.ruleengine.core.exception.ValidException;
import cn.ruleengine.web.interceptor.AuthInterceptor;
import cn.ruleengine.web.service.WorkspaceService;
import cn.ruleengine.web.store.entity.RuleEngineWorkspace;
import cn.ruleengine.web.store.manager.RuleEngineWorkspaceManager;
import cn.ruleengine.web.store.mapper.RuleEngineWorkspaceMapper;
import cn.ruleengine.web.vo.workspace.Workspace;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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
    @Resource
    private RuleEngineWorkspaceMapper ruleEngineWorkspaceMapper;

    /**
     * 内存缓存
     */
    private LRUCache<String, AccessKey> accessKeyCache = CacheUtil.newLRUCache(100);

    /**
     * 用户有权限的工作空间
     *
     * @return list
     */
    @Override
    public List<Workspace> list() {
        UserData userData = AuthInterceptor.USER.get();
        Boolean isAdmin = userData.getIsAdmin();
        if (isAdmin) {
            List<RuleEngineWorkspace> ruleEngineWorkspaces = this.ruleEngineWorkspaceManager.list();
            if (CollUtil.isEmpty(ruleEngineWorkspaces)) {
                return Collections.emptyList();
            } else {
                return ruleEngineWorkspaces.stream().map(m -> {
                    Workspace workspace = new Workspace();
                    workspace.setId(m.getId());
                    workspace.setName(m.getName());
                    return workspace;
                }).collect(Collectors.toList());
            }
        }
        Integer userId = userData.getId();
        return this.ruleEngineWorkspaceMapper.listWorkspaceByUserId(userId);
    }

    /**
     * 普通用户是否有这个工作空间权限
     *
     * @param workspaceId 工作空间id
     * @param userId      用户id
     * @return true有权限
     */
    @Override
    public boolean hasWorkspacePermission(Integer workspaceId, Integer userId) {
        Integer count = this.ruleEngineWorkspaceMapper.countWorkspace(workspaceId, userId);
        return count != null && count > 0;
    }

    /**
     * 获取当前工作空间
     *
     * @return Workspace
     */
    @Override
    public Workspace currentWorkspace() {
        UserData userData = AuthInterceptor.USER.get();
        RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + userData.getId());
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
                workspace.setCode(ruleEngineWorkspace.getCode());
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
        UserData userData = AuthInterceptor.USER.get();
        if (!userData.getIsAdmin()) {
            // 如果不是超级管理员，查看是否有此工作空间的工作空间权限
            if (!this.hasWorkspacePermission(id, userData.getId())) {
                throw new ValidException("你没有此工作空间权限");
            }
        }
        RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + userData.getId());
        Workspace workspace = new Workspace();
        workspace.setId(engineWorkspace.getId());
        workspace.setName(engineWorkspace.getName());
        workspace.setCode(engineWorkspace.getCode());
        bucket.set(workspace);
        return true;
    }


    /**
     * 当前工作空间AccessKey
     *
     * @param code              工作空间code
     * @return AccessKey
     */
    @Override
    public AccessKey accessKey(String code) {
        AccessKey accessKey = this.accessKeyCache.get(code);
        if (accessKey != null) {
            return accessKey;
        }
        RuleEngineWorkspace engineWorkspace = this.ruleEngineWorkspaceManager.lambdaQuery()
                .eq(RuleEngineWorkspace::getCode, code).one();
        if (engineWorkspace == null) {
            throw new ValidException("找不到此工作空间：" + code);
        }
        accessKey = new AccessKey();
        accessKey.setId(engineWorkspace.getId());
        accessKey.setAccessKeyId(engineWorkspace.getAccessKeyId());
        accessKey.setAccessKeySecret(engineWorkspace.getAccessKeySecret());
        this.accessKeyCache.put(code, accessKey, 1000 * 60 * 60);
        return accessKey;
    }

}
