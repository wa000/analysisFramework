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
package org.b3log.symphony.processor.advice.validate;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.ioc.BeanManager;
import org.b3log.latke.ioc.Singleton;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.advice.ProcessAdvice;
import org.b3log.latke.servlet.advice.RequestProcessAdviceException;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Comment;
import org.b3log.symphony.service.ArticleQueryService;
import org.b3log.symphony.service.CommentQueryService;
import org.b3log.symphony.service.OptionQueryService;
import org.b3log.symphony.util.StatusCodes;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Validates for comment adding locally.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.3.0.4, Apr 7, 2019
 * @since 0.2.0
 */
@Singleton
public class CommentAddValidation extends ProcessAdvice {

    @Override
    public void doAdvice(final RequestContext context) throws RequestProcessAdviceException {
        final HttpServletRequest request = context.getRequest();
        final JSONObject requestJSONObject = context.requestJSON();
        request.setAttribute(Keys.REQUEST, requestJSONObject);
        validateCommentFields(requestJSONObject);
    }

    /**
     * Validates comment fields.
     *
     * @param requestJSONObject the specified request object
     * @throws RequestProcessAdviceException if validate failed
     */
    public static void validateCommentFields(final JSONObject requestJSONObject) throws RequestProcessAdviceException {
        final BeanManager beanManager = BeanManager.getInstance();
        final LangPropsService langPropsService = beanManager.getReference(LangPropsService.class);
        final OptionQueryService optionQueryService = beanManager.getReference(OptionQueryService.class);
        final ArticleQueryService articleQueryService = beanManager.getReference(ArticleQueryService.class);
        final CommentQueryService commentQueryService = beanManager.getReference(CommentQueryService.class);

        final JSONObject exception = new JSONObject();
        exception.put(Keys.STATUS_CODE, StatusCodes.ERR);

        final String commentContent = StringUtils.trim(requestJSONObject.optString(Comment.COMMENT_CONTENT));
        if (StringUtils.isBlank(commentContent) || commentContent.length() > Comment.MAX_COMMENT_CONTENT_LENGTH) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("commentErrorLabel")));
        }

        if (optionQueryService.containReservedWord(commentContent)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("contentContainReservedWordLabel")));
        }

        final String articleId = requestJSONObject.optString(Article.ARTICLE_T_ID);
        if (StringUtils.isBlank(articleId)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("commentArticleErrorLabel")));
        }

        final JSONObject article = articleQueryService.getArticleById(articleId);
        if (null == article) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("commentArticleErrorLabel")));
        }

        if (!article.optBoolean(Article.ARTICLE_COMMENTABLE)) {
            throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("notAllowCmtLabel")));
        }

        final String originalCommentId = requestJSONObject.optString(Comment.COMMENT_ORIGINAL_COMMENT_ID);
        if (StringUtils.isNotBlank(originalCommentId)) {
            final JSONObject originalCmt = commentQueryService.getComment(originalCommentId);
            if (null == originalCmt) {
                throw new RequestProcessAdviceException(exception.put(Keys.MSG, langPropsService.get("commentArticleErrorLabel")));
            }
        }
    }
}
