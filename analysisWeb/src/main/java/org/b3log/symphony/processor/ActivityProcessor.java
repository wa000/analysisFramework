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
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.servlet.HttpMethod;
import org.b3log.latke.servlet.RequestContext;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.AbstractFreeMarkerRenderer;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.model.Pointtransfer;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.processor.advice.CSRFCheck;
import org.b3log.symphony.processor.advice.CSRFToken;
import org.b3log.symphony.processor.advice.LoginCheck;
import org.b3log.symphony.processor.advice.PermissionGrant;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.processor.advice.validate.Activity1A0001CollectValidation;
import org.b3log.symphony.processor.advice.validate.Activity1A0001Validation;
import org.b3log.symphony.service.*;
import org.b3log.symphony.util.Sessions;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Activity processor.
 * <ul>
 * <li>Shows activities (/activities), GET</li>
 * <li>Daily checkin (/activity/daily-checkin), GET</li>
 * <li>Shows 1A0001 (/activity/1A0001), GET</li>
 * <li>Bets 1A0001 (/activity/1A0001/bet), POST</li>
 * <li>Collects 1A0001 (/activity/1A0001/collect), POST</li>
 * <li>Shows character (/activity/character), GET</li>
 * <li>Submit character (/activity/character/submit), POST</li>
 * <li>Shows eating snake (/activity/eating-snake), GET</li>
 * <li>Starts eating snake (/activity/eating-snake/start), POST</li>
 * <li>Collects eating snake(/activity/eating-snake/collect), POST</li>
 * <li>Shows gobang (/activity/gobang), GET</li>
 * <li>Starts gobang (/activity/gobang/start), POST</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @author <a href="http://zephyr.b3log.org">Zephyr</a>
 * @version 1.9.1.15, Jan 21, 2019
 * @since 1.3.0
 */
@RequestProcessor
public class ActivityProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ActivityProcessor.class);

    /**
     * Activity management service.
     */
    @Inject
    private ActivityMgmtService activityMgmtService;

    /**
     * Activity query service.
     */
    @Inject
    private ActivityQueryService activityQueryService;

    /**
     * Character query service.
     */
    @Inject
    private CharacterQueryService characterQueryService;

    /**
     * Pointtransfer query service.
     */
    @Inject
    private PointtransferQueryService pointtransferQueryService;

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
     * Shows 1A0001.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/character", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({CSRFToken.class, PermissionGrant.class, StopwatchEndAdvice.class})
    public void showCharacter(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "activity/character.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);

        final JSONObject user = Sessions.getUser();
        final String userId = user.optString(Keys.OBJECT_ID);

        String activityCharacterGuideLabel = langPropsService.get("activityCharacterGuideLabel");

        final String character = characterQueryService.getUnwrittenCharacter(userId);
        if (StringUtils.isBlank(character)) {
            dataModel.put("noCharacter", true);

            return;
        }

        final int totalCharacterCount = characterQueryService.getTotalCharacterCount();
        final int writtenCharacterCount = characterQueryService.getWrittenCharacterCount();
        final String totalProgress = String.format("%.2f", (double) writtenCharacterCount / (double) totalCharacterCount * 100);
        dataModel.put("totalProgress", totalProgress);

        final int userWrittenCharacterCount = characterQueryService.getWrittenCharacterCount(userId);
        final String userProgress = String.format("%.2f", (double) userWrittenCharacterCount / (double) totalCharacterCount * 100);
        dataModel.put("userProgress", userProgress);

        activityCharacterGuideLabel = activityCharacterGuideLabel.replace("{character}", character);
        dataModel.put("activityCharacterGuideLabel", activityCharacterGuideLabel);
    }

    /**
     * Submits character.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/character/submit", method = HttpMethod.POST)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({StopwatchEndAdvice.class})
    public void submitCharacter(final RequestContext context) {
        final HttpServletRequest request = context.getRequest();
        context.renderJSON().renderFalseResult();

        JSONObject requestJSONObject;
        try {
            requestJSONObject = context.requestJSON();
            request.setAttribute(Keys.REQUEST, requestJSONObject);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Submits character failed", e);

            context.renderJSON(false).renderMsg(langPropsService.get("activityCharacterRecognizeFailedLabel"));

            return;
        }

        final JSONObject currentUser = Sessions.getUser();
        final String userId = currentUser.optString(Keys.OBJECT_ID);
        final String dataURL = requestJSONObject.optString("dataURL");
        final String dataPart = StringUtils.substringAfter(dataURL, ",");
        final String character = requestJSONObject.optString("character");

        final JSONObject result = activityMgmtService.submitCharacter(userId, dataPart, character);
        context.renderJSON(result);
    }

    /**
     * Shows activity page.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activities", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showActivities(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "home/activities.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);

        dataModel.put("pointActivityCheckinMin", Pointtransfer.TRANSFER_SUM_C_ACTIVITY_CHECKIN_MIN);
        dataModel.put("pointActivityCheckinMax", Pointtransfer.TRANSFER_SUM_C_ACTIVITY_CHECKIN_MAX);
        dataModel.put("pointActivityCheckinStreak", Pointtransfer.TRANSFER_SUM_C_ACTIVITY_CHECKINT_STREAK);
        dataModel.put("activitYesterdayLivenessRewardMaxPoint", Symphonys.ACTIVITY_YESTERDAY_REWARD_MAX);
    }

    /**
     * Shows daily checkin page.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/checkin", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showDailyCheckin(final RequestContext context) {
        final JSONObject user = Sessions.getUser();
        final String userId = user.optString(Keys.OBJECT_ID);
        if (activityQueryService.isCheckedinToday(userId)) {
            context.sendRedirect(Latkes.getServePath() + "/member/" + user.optString(User.USER_NAME) + "/points");

            return;
        }

        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "activity/checkin.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    /**
     * Daily checkin.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/daily-checkin", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After(StopwatchEndAdvice.class)
    public void dailyCheckin(final RequestContext context) {
        final JSONObject user = Sessions.getUser();
        final String userId = user.optString(Keys.OBJECT_ID);
        activityMgmtService.dailyCheckin(userId);

        context.sendRedirect(Latkes.getServePath() + "/member/" + user.optString(User.USER_NAME) + "/points");
    }

    /**
     * Yesterday liveness reward.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/yesterday-liveness-reward", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After(StopwatchEndAdvice.class)
    public void yesterdayLivenessReward(final RequestContext context) {
        final HttpServletRequest request = context.getRequest();
        final JSONObject user = Sessions.getUser();
        final String userId = user.optString(Keys.OBJECT_ID);

        activityMgmtService.yesterdayLivenessReward(userId);

        context.sendRedirect(Latkes.getServePath() + "/member/" + user.optString(User.USER_NAME) + "/points");
    }

    /**
     * Shows 1A0001.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/1A0001", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({CSRFToken.class, PermissionGrant.class, StopwatchEndAdvice.class})
    public void show1A0001(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "activity/1A0001.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        final JSONObject currentUser = Sessions.getUser();
        final String userId = currentUser.optString(Keys.OBJECT_ID);

        final boolean closed = Symphonys.ACTIVITY_1A0001_CLOSED;
        dataModel.put(Common.CLOSED, closed);

        final Calendar calendar = Calendar.getInstance();
        final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        final boolean closed1A0001 = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
        dataModel.put(Common.CLOSED_1A0001, closed1A0001);

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final boolean end = hour > 14 || (hour == 14 && minute > 55);
        dataModel.put(Common.END, end);

        final boolean collected = activityQueryService.isCollected1A0001Today(userId);
        dataModel.put(Common.COLLECTED, collected);

        final boolean participated = activityQueryService.is1A0001Today(userId);
        dataModel.put(Common.PARTICIPATED, participated);

        while (true) {
            if (closed) {
                dataModel.put(Keys.MSG, langPropsService.get("activityClosedLabel"));
                break;
            }

            if (closed1A0001) {
                dataModel.put(Keys.MSG, langPropsService.get("activity1A0001CloseLabel"));
                break;
            }

            if (collected) {
                dataModel.put(Keys.MSG, langPropsService.get("activityParticipatedLabel"));
                break;
            }

            if (participated) {
                dataModel.put(Common.HOUR, hour);

                final List<JSONObject> records = pointtransferQueryService.getLatestPointtransfers(userId,
                        Pointtransfer.TRANSFER_TYPE_C_ACTIVITY_1A0001, 1);
                final JSONObject pointtransfer = records.get(0);
                final String data = pointtransfer.optString(Pointtransfer.DATA_ID);
                final String smallOrLarge = data.split("-")[1];
                final int sum = pointtransfer.optInt(Pointtransfer.SUM);
                String msg = langPropsService.get("activity1A0001BetedLabel");
                final String small = langPropsService.get("activity1A0001BetSmallLabel");
                final String large = langPropsService.get("activity1A0001BetLargeLabel");
                msg = msg.replace("{smallOrLarge}", StringUtils.equals(smallOrLarge, "0") ? small : large);
                msg = msg.replace("{point}", String.valueOf(sum));

                dataModel.put(Keys.MSG, msg);

                break;
            }

            if (end) {
                dataModel.put(Keys.MSG, langPropsService.get("activityEndLabel"));
                break;
            }

            break;
        }

        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }

    /**
     * Bets 1A0001.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/1A0001/bet", method = HttpMethod.POST)
    @Before({StopwatchStartAdvice.class, LoginCheck.class, CSRFCheck.class, Activity1A0001Validation.class})
    @After(StopwatchEndAdvice.class)
    public void bet1A0001(final RequestContext context) {
        context.renderJSON().renderFalseResult();

        final JSONObject requestJSONObject = (JSONObject) context.attr(Keys.REQUEST);

        final int amount = requestJSONObject.optInt(Common.AMOUNT);
        final int smallOrLarge = requestJSONObject.optInt(Common.SMALL_OR_LARGE);

        final JSONObject currentUser = Sessions.getUser();
        final String fromId = currentUser.optString(Keys.OBJECT_ID);

        final JSONObject ret = activityMgmtService.bet1A0001(fromId, amount, smallOrLarge);
        if (ret.optBoolean(Keys.STATUS_CODE)) {
            String msg = langPropsService.get("activity1A0001BetedLabel");
            final String small = langPropsService.get("activity1A0001BetSmallLabel");
            final String large = langPropsService.get("activity1A0001BetLargeLabel");
            msg = msg.replace("{smallOrLarge}", smallOrLarge == 0 ? small : large);
            msg = msg.replace("{point}", String.valueOf(amount));

            context.renderTrueResult().renderMsg(msg);
        }
    }

    /**
     * Collects 1A0001.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/1A0001/collect", method = HttpMethod.POST)
    @Before({StopwatchStartAdvice.class, LoginCheck.class, Activity1A0001CollectValidation.class})
    @After(StopwatchEndAdvice.class)
    public void collect1A0001(final RequestContext context) {
        final JSONObject currentUser = Sessions.getUser();
        final String userId = currentUser.optString(Keys.OBJECT_ID);

        final JSONObject ret = activityMgmtService.collect1A0001(userId);

        context.renderJSON(ret);
    }

    /**
     * Shows eating snake.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/eating-snake", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({CSRFToken.class, PermissionGrant.class, StopwatchEndAdvice.class})
    public void showEatingSnake(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "activity/eating-snake.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);

        final List<JSONObject> maxUsers = activityQueryService.getTopEatingSnakeUsersMax(10);
        dataModel.put("maxUsers", maxUsers);

        final List<JSONObject> sumUsers = activityQueryService.getTopEatingSnakeUsersSum(10);
        dataModel.put("sumUsers", sumUsers);

        final JSONObject user = Sessions.getUser();
        final String userId = user.optString(Keys.OBJECT_ID);
        final int startPoint = activityQueryService.getEatingSnakeAvgPoint(userId);

        String pointActivityEatingSnake = langPropsService.get("activityStartEatingSnakeTipLabel");
        pointActivityEatingSnake = pointActivityEatingSnake.replace("{point}", String.valueOf(startPoint));
        dataModel.put("activityStartEatingSnakeTipLabel", pointActivityEatingSnake);
    }

    /**
     * Starts eating snake.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/eating-snake/start", method = HttpMethod.POST)
    @Before({StopwatchStartAdvice.class, LoginCheck.class, CSRFCheck.class})
    @After(StopwatchEndAdvice.class)
    public void startEatingSnake(final RequestContext context) {
        final HttpServletRequest request = context.getRequest();
        final JSONObject currentUser = Sessions.getUser();
        final String fromId = currentUser.optString(Keys.OBJECT_ID);

        final JSONObject ret = activityMgmtService.startEatingSnake(fromId);

        context.renderJSON(ret);
    }

    /**
     * Collects eating snake.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/eating-snake/collect", method = HttpMethod.POST)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({CSRFToken.class, StopwatchEndAdvice.class})
    public void collectEatingSnake(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "activity/eating-snake.ftl");

        JSONObject requestJSONObject;
        try {
            requestJSONObject = context.requestJSON();
            final int score = requestJSONObject.optInt("score");
            final JSONObject user = Sessions.getUser();
            final JSONObject ret = activityMgmtService.collectEatingSnake(user.optString(Keys.OBJECT_ID), score);

            context.renderJSON(ret);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Collects eating snake game failed", e);

            context.renderJSON(false).renderMsg("err....");
        }
    }

    /**
     * Shows gobang.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/gobang", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After({CSRFToken.class, PermissionGrant.class, StopwatchEndAdvice.class})
    public void showGobang(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "activity/gobang.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);

        String pointActivityGobang = langPropsService.get("activityStartGobangTipLabel");
        pointActivityGobang = pointActivityGobang.replace("{point}", String.valueOf(Pointtransfer.TRANSFER_SUM_C_ACTIVITY_GOBANG_START));
        dataModel.put("activityStartGobangTipLabel", pointActivityGobang);
    }

    /**
     * Starts gobang.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/activity/gobang/start", method = HttpMethod.POST)
    @Before({StopwatchStartAdvice.class, LoginCheck.class})
    @After(StopwatchEndAdvice.class)
    public void startGobang(final RequestContext context) {
        final JSONObject ret = new JSONObject().put(Keys.STATUS_CODE, false);
        final JSONObject currentUser = Sessions.getUser();

        final boolean succ = currentUser.optInt(UserExt.USER_POINT) - Pointtransfer.TRANSFER_SUM_C_ACTIVITY_GOBANG_START >= 0;
        ret.put(Keys.STATUS_CODE, succ);
        final String msg = succ ? "started" : langPropsService.get("activityStartGobangFailLabel");
        ret.put(Keys.MSG, msg);

        context.renderJSON(ret);
    }
}
