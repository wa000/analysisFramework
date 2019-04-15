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
package org.b3log.symphony.service;

import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Latkes;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.processor.FileUploadProcessor;
import org.b3log.symphony.util.Sessions;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User avatar query service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.5.2.6, Mar 25, 2019
 * @since 0.3.0
 */
@Service
public class AvatarQueryService {

    /**
     * Default avatar URL.
     */
    public static final String DEFAULT_AVATAR_URL = Latkes.getStaticServePath() + "/images/user-thumbnail.png";

    /**
     * Fills the specified user thumbnail URL.
     *
     * @param user the specified user
     */
    public void fillUserAvatarURL(final JSONObject user) {
        user.put(UserExt.USER_AVATAR_URL + "210", getAvatarURLByUser(user, "210"));
        user.put(UserExt.USER_AVATAR_URL + "48", getAvatarURLByUser(user, "48"));
        user.put(UserExt.USER_AVATAR_URL + "20", getAvatarURLByUser(user, "20"));
    }

    /**
     * Gets the default avatar URL with the specified size.
     *
     * @param size the specified size
     * @return the default avatar URL
     */
    public String getDefaultAvatarURL(final String size) {
        if (!Symphonys.QN_ENABLED) {
            return DEFAULT_AVATAR_URL;
        }

        return DEFAULT_AVATAR_URL + "?imageView2/1/w/" + size + "/h/" + size + "/interlace/0/q/100";
    }

    /**
     * Gets the avatar URL for the specified user with the specified size.
     *
     * @param user the specified user
     * @param size the specified size
     * @return the avatar URL
     */
    public String getAvatarURLByUser(final JSONObject user, final String size) {
        if (null == user) {
            return getDefaultAvatarURL(size);
        }

        final int viewMode = Sessions.getAvatarViewMode();

        String originalURL = user.optString(UserExt.USER_AVATAR_URL);
        if (StringUtils.isBlank(originalURL)) {
            originalURL = DEFAULT_AVATAR_URL;
        }
        if (StringUtils.isBlank(originalURL) || Strings.contains(originalURL, new String[]{"<", ">", "\"", "'"})) {
            originalURL = DEFAULT_AVATAR_URL;
        }

        String avatarURL = StringUtils.substringBeforeLast(originalURL, "?");
        final boolean qiniuEnabled = Symphonys.QN_ENABLED;
        if (UserExt.USER_AVATAR_VIEW_MODE_C_ORIGINAL == viewMode) {
            if (qiniuEnabled) {
                final String qiniuDomain = Symphonys.UPLOAD_QINIU_DOMAIN;

                if (!StringUtils.startsWith(avatarURL, qiniuDomain)) {
                    return DEFAULT_AVATAR_URL + "?imageView2/1/w/" + size + "/h/" + size + "/interlace/0/q/100";
                } else {
                    return avatarURL + "?imageView2/1/w/" + size + "/h/" + size + "/interlace/0/q/100";
                }
            } else {
                return avatarURL;
            }
        } else if (qiniuEnabled) {
            final String qiniuDomain = Symphonys.UPLOAD_QINIU_DOMAIN;

            if (!StringUtils.startsWith(avatarURL, qiniuDomain)) {
                return DEFAULT_AVATAR_URL + "?imageView2/1/w/" + size + "/h/" + size + "/format/jpg/interlace/0/q/100";
            } else {
                return avatarURL + "?imageView2/1/w/" + size + "/h/" + size + "/format/jpg/interlace/0/q/100";
            }
        } else {
            return avatarURL;
        }
    }

    /**
     * Creates a avatar image with the specified hash string and size.
     * <p>
     * Refers to: https://github.com/superhj1987/awesome-identicon
     * </p>
     *
     * @param hash the specified hash string
     * @param size the specified size
     * @return buffered image
     */
    public BufferedImage createAvatar(final String hash, final int size) {
        final boolean[][] array = new boolean[6][5];

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                array[i][j] = false;
            }
        }

        for (int i = 0; i < hash.length(); i += 2) {
            final int s = i / 2;

            final boolean v = Math.random() > 0.5;
            if (s % 3 == 0) {
                array[s / 3][0] = v;
                array[s / 3][4] = v;
            } else if (s % 3 == 1) {
                array[s / 3][1] = v;
                array[s / 3][3] = v;
            } else {
                array[s / 3][2] = v;
            }
        }

        final int ratio = Math.round(size / 5);

        final BufferedImage ret = new BufferedImage(ratio * 5, ratio * 5, BufferedImage.TYPE_3BYTE_BGR);
        final Graphics graphics = ret.getGraphics();

        graphics.setColor(new Color(Integer.parseInt(String.valueOf(hash.charAt(0)), 16) * 16,
                Integer.parseInt(String.valueOf(hash.charAt(1)), 16) * 16,
                Integer.parseInt(String.valueOf(hash.charAt(2)), 16) * 16));
        graphics.fillRect(0, 0, ret.getWidth(), ret.getHeight());

        graphics.setColor(new Color(Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 1)), 16) * 16,
                Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 2)), 16) * 16,
                Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 3)), 16) * 16));
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (array[i][j]) {
                    graphics.fillRect(j * ratio, i * ratio, ratio, ratio);
                }
            }
        }

        return ret;
    }
}
