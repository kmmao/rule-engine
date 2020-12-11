package cn.ruleengine.web.vo.menu;

import lombok.Data;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 丁乾文
 * @create 2019/10/24
 * @since 1.0.0
 */
@Data
public class ListMenuResponse {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 用户id
     */
    private String name;

    private String description;
    /**
     * 父级菜单
     */
    private Integer parentId;

    /**
     * 1导航栏菜单,2侧边栏菜单
     */
    private Integer type;

    private String url;
    /**
     * 图标
     */
    private String icon;
    /**
     * 排序
     */
    private Integer sort;
    private String menuPath;
    /**
     * 子节点集合
     */
    private List<ListMenuResponse> children;
}
