package com.czy.constants;

/**
 * ClassName: SystemCanstants
 * Package: com.czy.constants
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
// 字面值常量在这里定义
public class SystemConstants {
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 文章列表当前查询页数
     */
    public static final int ARTICLE_STATUS_CURRENT = 1;

    /**
     * 文章列表每页显示的数据条数
     */
    public static final int ARTICLE_STATUS_SIZE = 10;

    /**
     * 分类列表为正常发布状态
     */
    public static final String Category_STATUS_NORMAL = "0";

    /**
     * 链接状态为正常
     */
    public static final String Link_STATUS_NORMAL = "0";
    /**
     * 评论为根评论
     */
    public static final String COMMENT_ROOT = "-1";

    /**
     * 评论类型为：友联评论
     */
    public static final String COMMENT_LINK = "1";

    /**
     * 评论类型为：文章类型评论
     */
    public static final String COMMENT_ARTICLE = "0";

    /**
     * 菜单状态为正常
     */
    public static final String MENU_NORMAL = "0";

    public static final String IS_ADMAIN = "1";
}
