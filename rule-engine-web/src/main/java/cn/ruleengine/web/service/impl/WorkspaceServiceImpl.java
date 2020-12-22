package cn.ruleengine.web.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.lang.Validator;
import cn.ruleengine.web.config.Context;
import cn.ruleengine.web.util.PageUtils;
import cn.ruleengine.web.vo.base.request.PageRequest;
import cn.ruleengine.web.vo.base.response.PageBase;
import cn.ruleengine.web.vo.base.response.PageResponse;
import cn.ruleengine.web.vo.base.response.PageResult;
import cn.ruleengine.web.vo.base.response.Rows;
import cn.ruleengine.web.vo.convert.BasicConversion;
import cn.ruleengine.web.vo.user.UserData;
import cn.ruleengine.web.vo.workspace.AccessKey;
import cn.ruleengine.web.vo.workspace.ListWorkspaceRequest;
import cn.ruleengine.web.vo.workspace.ListWorkspaceResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.ruleengine.core.exception.ValidException;
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
     * @param pageRequest 模糊查询参数
     * @return list
     */
    @Override
    public PageResult<ListWorkspaceResponse> list(PageRequest<ListWorkspaceRequest> pageRequest) {
        UserData userData = Context.getCurrentUser();
        Boolean isAdmin = userData.getIsAdmin();
        ListWorkspaceRequest query = pageRequest.getQuery();
        // 如果是管理员，有所有工作空间权限
        if (isAdmin) {
            return PageUtils.page(this.ruleEngineWorkspaceManager, pageRequest.getPage(), () -> {
                QueryWrapper<RuleEngineWorkspace> queryWrapper = new QueryWrapper<>();
                if (Validator.isNotEmpty(query.getCode())) {
                    queryWrapper.lambda().like(RuleEngineWorkspace::getCode, query.getCode());
                }
                if (Validator.isNotEmpty(query.getName())) {
                    queryWrapper.lambda().like(RuleEngineWorkspace::getName, query.getName());
                }
                PageUtils.defaultOrder(pageRequest.getOrders(), queryWrapper);
                return queryWrapper;
            }, BasicConversion.INSTANCE::convert);
        }
        Integer userId = userData.getId();
        PageBase page = pageRequest.getPage();
        Integer total = this.ruleEngineWorkspaceMapper.totalWorkspace(userId, query, page);
        if (total == null || total == 0) {
            return new PageResult<>();
        }
        List<ListWorkspaceResponse> listWorkspaceResponses = BasicConversion.INSTANCE.convert(this.ruleEngineWorkspaceMapper.listWorkspace(userId, query, page));
        PageResult<ListWorkspaceResponse> pageResult = new PageResult<>();
        pageResult.setData(new Rows<>(listWorkspaceResponses, new PageResponse(page.getPageIndex(), page.getPageSize(), total)));
        return pageResult;
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
        UserData userData = Context.getCurrentUser();
        RBucket<Workspace> bucket = this.redissonClient.getBucket(CURRENT_WORKSPACE + userData.getId());
        if (bucket.isExists()) {
            Workspace workspace = bucket.get();
            log.info("当前工作空间：" + workspace);
            return workspace;
        } else {
            RuleEngineWorkspace ruleEngineWorkspace = this.ruleEngineWorkspaceMapper.getFirstWorkspace();
            if (ruleEngineWorkspace != null) {
                Workspace workspace = new Workspace();
                workspace.setId(ruleEngineWorkspace.getId());
                workspace.setName(ruleEngineWorkspace.getName());
                workspace.setCode(ruleEngineWorkspace.getCode());
                bucket.set(workspace);
                return workspace;
            } else {
                throw new ValidException("没有可用工作空间");
            }
        }
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
        UserData userData = Context.getCurrentUser();
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
     * @param code 工作空间code
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
