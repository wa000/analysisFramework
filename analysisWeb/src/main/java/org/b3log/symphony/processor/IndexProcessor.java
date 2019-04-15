/*
 * Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
 * Copyright (C) 2012-present, b3log.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.processor;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.Latkes;
import org.b3log.latke.ioc.Inject;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.servlet.HttpMethod;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.AbstractFreeMarkerRenderer;
import org.b3log.latke.util.Locales;
import org.b3log.latke.util.Paginator;
import org.b3log.latke.util.Stopwatchs;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.processor.advice.AnonymousViewCheck;
import org.b3log.symphony.processor.advice.PermissionGrant;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.service.ArticleQueryService;
import org.b3log.symphony.service.DataModelService;
import org.b3log.symphony.service.UserMgmtService;
import org.b3log.symphony.service.UserQueryService;
import org.b3log.symphony.util.Sessions;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Index processor.
 * <ul>
 * <li>Shows index (/), GET</li>
 * <li>Show recent articles (/recent), GET</li>
 * <li>Show question articles (/qna), GET</li>
 * <li>Show watch relevant pages (/watch/*), GET</li>
 * <li>Show hot articles (/hot), GET</li>
 * <li>Show perfect articles (/perfect), GET</li>
 * <li>Shows about (/about), GET</li>
 * <li>Shows kill browser (/kill-browser), GET</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @version 1.15.0.3, Jan 8, 2019
 * @since 0.2.0
 */
@RequestProcessor
public class IndexProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(IndexProcessor.class);

    /**
     * Article query service.
     */
    @Inject
    private ArticleQueryService articleQueryService;

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;

    /**
     * User management service.
     */
    @Inject
    private UserMgmtService userMgmtService;

    /**
     * Data model service.
     */
    @Inject
    private DataModelService dataModelService;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Shows question articles.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = {"/qna", "/qna/unanswered", "/qna/reward", "/qna/hot"}, method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showQnA(final RequestContext context) {
        final HttpServletRequest request = context.getRequest();

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "qna.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        final int pageNum = Paginator.getPage(request);
        int pageSize = Symphonys.ARTICLE_LIST_CNT;
        final JSONObject user = Sessions.getUser();
        if (null != user) {
            pageSize = user.optInt(UserExt.USER_LIST_PAGE_SIZE);

            if (!UserExt.finshedGuide(user)) {
                context.sendRedirect(Latkes.getServePath() + "/guide");

                return;
            }
        }

        String sortModeStr = StringUtils.substringAfter(context.requestURI(), "/qna");
        int sortMode;
        switch (sortModeStr) {
            case "":
                sortMode = 0;

                break;
            case "/unanswered":
                sortMode = 1;

                break;
            case "/reward":
                sortMode = 2;

                break;
            case "/hot":
                sortMode = 3;

                break;
            default:
                sortMode = 0;
        }

        dataModel.put(Common.SELECTED, Common.QNA);
        final JSONObject result = articleQueryService.getQuestionArticles(sortMode, pageNum, pageSize);
        final List<JSONObject> allArticles = (List<JSONObject>) result.get(Article.ARTICLES);
        dataModel.put(Common.LATEST_ARTICLES, allArticles);

        final JSONObject pagination = result.getJSONObject(Pagination.PAGINATION);
        final int pageCount = pagination.optInt(Pagination.PAGINATION_PAGE_COUNT);
        final List<Integer> pageNums = (List<Integer>) pagination.get(Pagination.PAGINATION_PAGE_NUMS);
        if (!pageNums.isEmpty()) {
            dataModel.put(Pagination.PAGINATION_FIRST_PAGE_NUM, pageNums.get(0));
            dataModel.put(Pagination.PAGINATION_LAST_PAGE_NUM, pageNums.get(pageNums.size() - 1));
        }

        dataModel.put(Pagination.PAGINATION_CURRENT_PAGE_NUM, pageNum);
        dataModel.put(Pagination.PAGINATION_PAGE_COUNT, pageCount);
        dataModel.put(Pagination.PAGINATION_PAGE_NUMS, pageNums);

        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);

        dataModel.put(Common.CURRENT, StringUtils.substringAfter(context.requestURI(), "/qna"));
    }

    /**
     * Shows watch articles or users.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = {"/watch", "/watch/users"}, method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showWatch(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "watch.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        int pageSize = Symphonys.ARTICLE_LIST_CNT;
        final JSONObject user = Sessions.getUser();
        if (null != user) {
            pageSize = user.optInt(UserExt.USER_LIST_PAGE_SIZE);

            if (!UserExt.finshedGuide(user)) {
                context.sendRedirect(Latkes.getServePath() + "/guide");

                return;
            }
        }

        dataModel.put(Common.WATCHING_ARTICLES, Collections.emptyList());
        String sortModeStr = StringUtils.substringAfter(context.requestURI(), "/watch");
        switch (sortModeStr) {
            case "":
                if (null != user) {
                    final List<JSONObject> followingTagArticles = articleQueryService.getFollowingTagArticles(user.optString(Keys.OBJECT_ID), 1, pageSize);
                    dataModel.put(Common.WATCHING_ARTICLES, followingTagArticles);
                }

                break;
            case "/users":
                if (null != user) {
                    final List<JSONObject> followingUserArticles = articleQueryService.getFollowingUserArticles(user.optString(Keys.OBJECT_ID), 1, pageSize);
                    dataModel.put(Common.WATCHING_ARTICLES, followingUserArticles);
                }

                break;
        }

        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);

        dataModel.put(Common.SELECTED, Common.WATCH);
        dataModel.put(Common.CURRENT, StringUtils.substringAfter(context.requestURI(), "/watch"));
    }

    /**
     * Shows index.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = {"", "/"}, method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showIndex(final RequestContext context) {
        final JSONObject currentUser = Sessions.getUser();
        if (null != currentUser) {
            // 自定义首页跳转 https://github.com/b3log/symphony/issues/774
            final String indexRedirectURL = currentUser.optString(UserExt.USER_INDEX_REDIRECT_URL);
            if (StringUtils.isNotBlank(indexRedirectURL)) {
                context.sendRedirect(indexRedirectURL);

                return;
            }
        }

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "index.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        final List<JSONObject> recentArticles = articleQueryService.getIndexRecentArticles();
        dataModel.put(Common.RECENT_ARTICLES, recentArticles);

        final List<JSONObject> perfectArticles = articleQueryService.getIndexPerfectArticles();
        dataModel.put(Common.PERFECT_ARTICLES, perfectArticles);

        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillIndexTags(dataModel);

        dataModel.put(Common.SELECTED, Common.INDEX);
    }

    /**
     * Shows recent articles.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = {"/recent", "/recent/hot", "/recent/good", "/recent/reply"}, method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showRecent(final RequestContext context) {
        final HttpServletRequest request = context.getRequest();

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "recent.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        final int pageNum = Paginator.getPage(request);
        int pageSize = Symphonys.ARTICLE_LIST_CNT;
        final JSONObject user = Sessions.getUser();
        if (null != user) {
            pageSize = user.optInt(UserExt.USER_LIST_PAGE_SIZE);

            if (!UserExt.finshedGuide(user)) {
                context.sendRedirect(Latkes.getServePath() + "/guide");

                return;
            }
        }

        String sortModeStr = StringUtils.substringAfter(context.requestURI(), "/recent");
        int sortMode;
        switch (sortModeStr) {
            case "":
                sortMode = 0;

                break;
            case "/hot":
                sortMode = 1;

                break;
            case "/good":
                sortMode = 2;

                break;
            case "/reply":
                sortMode = 3;

                break;
            default:
                sortMode = 0;
        }

        dataModel.put(Common.SELECTED, Common.RECENT);
        final JSONObject result = articleQueryService.getRecentArticles(sortMode, pageNum, pageSize);
        final List<JSONObject> allArticles = (List<JSONObject>) result.get(Article.ARTICLES);
        final List<JSONObject> stickArticles = new ArrayList<>();
        final Iterator<JSONObject> iterator = allArticles.iterator();
        while (iterator.hasNext()) {
            final JSONObject article = iterator.next();
            final boolean stick = article.optInt(Article.ARTICLE_T_STICK_REMAINS) > 0;
            article.put(Article.ARTICLE_T_IS_STICK, stick);
            if (stick) {
                stickArticles.add(article);
                iterator.remove();
            }
        }

        dataModel.put(Common.STICK_ARTICLES, stickArticles);
        dataModel.put(Common.LATEST_ARTICLES, allArticles);

        final JSONObject pagination = result.getJSONObject(Pagination.PAGINATION);
        final int pageCount = pagination.optInt(Pagination.PAGINATION_PAGE_COUNT);
        final List<Integer> pageNums = (List<Integer>) pagination.get(Pagination.PAGINATION_PAGE_NUMS);
        if (!pageNums.isEmpty()) {
            dataModel.put(Pagination.PAGINATION_FIRST_PAGE_NUM, pageNums.get(0));
            dataModel.put(Pagination.PAGINATION_LAST_PAGE_NUM, pageNums.get(pageNums.size() - 1));
        }

        dataModel.put(Pagination.PAGINATION_CURRENT_PAGE_NUM, pageNum);
        dataModel.put(Pagination.PAGINATION_PAGE_COUNT, pageCount);
        dataModel.put(Pagination.PAGINATION_PAGE_NUMS, pageNums);

        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);

        dataModel.put(Common.CURRENT, StringUtils.substringAfter(context.requestURI(), "/recent"));
    }

    /**
     * Shows hot articles.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/hot", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showHotArticles(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "hot.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        int pageSize = Symphonys.ARTICLE_LIST_CNT;
        final JSONObject user = Sessions.getUser();
        if (null != user) {
            pageSize = user.optInt(UserExt.USER_LIST_PAGE_SIZE);
        }

        final List<JSONObject> indexArticles = articleQueryService.getHotArticles(pageSize);
        dataModel.put(Common.INDEX_ARTICLES, indexArticles);
        dataModel.put(Common.SELECTED, Common.HOT);

        Stopwatchs.start("Fills");
        try {
            dataModelService.fillHeaderAndFooter(context, dataModel);
            dataModelService.fillRandomArticles(dataModel);
            dataModelService.fillSideHotArticles(dataModel);
            dataModelService.fillSideTags(dataModel);
            dataModelService.fillLatestCmts(dataModel);
        } finally {
            Stopwatchs.end();
        }
    }

    /**
     * Shows perfect articles.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/perfect", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showPerfectArticles(final RequestContext context) {
        final HttpServletRequest request = context.getRequest();

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "perfect.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        final int pageNum = Paginator.getPage(request);
        int pageSize = Symphonys.ARTICLE_LIST_CNT;
        final JSONObject user = Sessions.getUser();
        if (null != user) {
            pageSize = user.optInt(UserExt.USER_LIST_PAGE_SIZE);
            if (!UserExt.finshedGuide(user)) {
                context.sendRedirect(Latkes.getServePath() + "/guide");

                return;
            }
        }

        final JSONObject result = articleQueryService.getPerfectArticles(pageNum, pageSize);
        final List<JSONObject> perfectArticles = (List<JSONObject>) result.get(Article.ARTICLES);
        dataModel.put(Common.PERFECT_ARTICLES, perfectArticles);
        dataModel.put(Common.SELECTED, Common.PERFECT);
        final JSONObject pagination = result.getJSONObject(Pagination.PAGINATION);
        final int pageCount = pagination.optInt(Pagination.PAGINATION_PAGE_COUNT);
        final List<Integer> pageNums = (List<Integer>) pagination.get(Pagination.PAGINATION_PAGE_NUMS);
        if (!pageNums.isEmpty()) {
            dataModel.put(Pagination.PAGINATION_FIRST_PAGE_NUM, pageNums.get(0));
            dataModel.put(Pagination.PAGINATION_LAST_PAGE_NUM, pageNums.get(pageNums.size() - 1));
        }

        dataModel.put(Pagination.PAGINATION_CURRENT_PAGE_NUM, pageNum);
        dataModel.put(Pagination.PAGINATION_PAGE_COUNT, pageCount);
        dataModel.put(Pagination.PAGINATION_PAGE_NUMS, pageNums);

        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    /**
     * Shows about.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/about", method = HttpMethod.GET)
    @Before(StopwatchStartAdvice.class)
    @After(StopwatchEndAdvice.class)
    public void showAbout(final RequestContext context) {
        // 关于页主要描述社区愿景、行为准则、内容协议等，并介绍社区的功能
        // 这些内容请搭建后自行编写发布，然后再修改这里进行重定向
        context.sendRedirect(Latkes.getServePath() + "/member/admin");
    }

    /**
     * Shows kill browser page with the specified context.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/kill-browser", method = HttpMethod.GET)
    @Before(StopwatchStartAdvice.class)
    @After(StopwatchEndAdvice.class)
    public void showKillBrowser(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "other/kill-browser.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        final Map<String, String> langs = langPropsService.getAll(Locales.getLocale());
        dataModel.putAll(langs);
        Keys.fillRuntime(dataModel);
        dataModelService.fillMinified(dataModel);
    }
}
