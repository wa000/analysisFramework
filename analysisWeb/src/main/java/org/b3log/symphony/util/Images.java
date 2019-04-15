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
package org.b3log.symphony.util;


import org.apache.commons.lang.StringUtils;
import org.b3log.symphony.processor.FileUploadProcessor;

/**
 * Image utilities.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.2, Feb 10, 2019
 * @since 3.2.0
 */
public final class Images {

    /**
     * Qiniu image processing.
     *
     * @param content the specified article content
     * @return processed content
     */
    public static String qiniuImgProcessing(final String content) {
        String ret = content;

        if (!Symphonys.QN_ENABLED) {
            return ret;
        }

        final String qiniuDomain = Symphonys.UPLOAD_QINIU_DOMAIN;
        final String html = Markdowns.toHTML(content);

        final String[] imgSrcs = StringUtils.substringsBetween(html, "<img src=\"", "\"");
        if (null == imgSrcs) {
            return ret;
        }

        for (final String imgSrc : imgSrcs) {
            if (!StringUtils.startsWith(imgSrc, qiniuDomain) || StringUtils.contains(imgSrc, ".gif")
                    || StringUtils.containsIgnoreCase(imgSrc, "?imageView2")) {
                continue;
            }

            ret = StringUtils.replace(ret, imgSrc, imgSrc + "?imageView2/2/w/768/format/webp/interlace/1");
        }

        return ret;
    }

    /**
     * Private constructor.
     */
    private Images() {
    }
}
