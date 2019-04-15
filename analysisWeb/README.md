<p align = "center">
<img alt="Sym" src="https://user-images.githubusercontent.com/873584/52320309-9555a100-2a09-11e9-9252-f04dc47b0a31.png">
<br><br>
下一代的社区系统，为未来而构建
<br><br>
<a title="Build Status" target="_blank" href="https://travis-ci.org/b3log/symphony"><img src="https://img.shields.io/travis/b3log/symphony.svg?style=flat-square"></a>
<a title="Code Size" target="_blank" href="https://github.com/b3log/symphony"><img src="https://img.shields.io/github/languages/code-size/b3log/symphony.svg?style=flat-square"></a>
<a title="AGPLv3" target="_blank" href="https://www.gnu.org/licenses/agpl-3.0.txt"><img src="http://img.shields.io/badge/license-AGPLv3-orange.svg?style=flat-square"></a>
<a title="Releases" target="_blank" href="https://github.com/b3log/symphony/releases"><img src="https://img.shields.io/github/release/b3log/symphony.svg?style=flat-square"></a>
<a title="Downloads" target="_blank" href="https://github.com/b3log/symphony/releases"><img src="https://img.shields.io/github/downloads/b3log/symphony/total.svg?style=flat-square"></a>
<a title="Docker Pulls" target="_blank" href="https://hub.docker.com/r/b3log/symphony"><img src="https://img.shields.io/docker/pulls/b3log/symphony.svg?style=flat-square&color=blueviolet"></a>
<a title="Hits" target="_blank" href="http://hits.dwyl.io/b3log/symphony"><img src="http://hits.dwyl.io/b3log/symphony.svg"></a>
</p>

## 简介

[Symphony](https://sym.b3log.org)（[ˈsɪmfəni]，n.交响乐）是一个现代化的社区平台，因为它：

* 实现了面向内容讨论的论坛
* 实现了面向知识问答的社区
* 包含了面向用户分享、交友、游戏的社交网络
* `100%` 开源

欢迎到 [Sym 官方讨论区](https://hacpai.com/tag/sym)了解更多。

## 动机

很多社区论坛系统：

* 界面风格老式，没有跟上时代发展的步伐
* 缺少创新、好玩的特性，缺少现代化的交互元素和用户体验
* 缺乏考虑实际运营需求，管理功能过于单一
* 细节不够精致、缺乏长期维护 

## 客户案例

社区版：

* [AIQ-机器学习](http://www.6aiq.com)
* [宽客网](http://www.cnq.net)
* [许昌IT圈](http://www.xcitq.com)
* [艺赛旗 RPA](http://support.i-search.com.cn)
* [凤凰匯](https://www.fengd.com)
* [猪玩派 | 喜欢游玩  热爱生活  乐于分享！](http://pigplay.net)
* [俩猴网](http://www.xfx77.cn)
* [听雨轩](http://nucode.cn)
* ......

商业版：

* [黑客派](https://hacpai.com)
* [IT遇岛](https://www.ityudao.com)
* [汇桔网](https://bbs.wtoip.com)
* [TapDealing](http://www.tapdealing.com)
* [艺术家与艺术爱好者](http://www.dizmix.com)
* [乾学院](http://c.raqsoft.com.cn)
* [GeeCall极客社区](http://geecall.com)
* [金蝶精斗云社区](https://cs.jdy.com)
* ......

## 功能

* [Sym 简介幻灯片](https://sym.b3log.org/syme-intro.pptx)
* [Sym 功能点脑图](http://naotu.baidu.com/file/cd31354ac9abc047569c73c560a5a913?token=b9750ae13f39ef9a)

## 界面

以下截图来自 Sym 商业版。

**首页**

![index](https://user-images.githubusercontent.com/873584/54031688-6e49e500-41ea-11e9-8679-78c9953698e9.png)

**列表**

![list](https://user-images.githubusercontent.com/873584/52256098-629ba200-2950-11e9-8641-3122e59db229.png)

**帖子**

![article](https://user-images.githubusercontent.com/873584/52264908-b799e100-296d-11e9-81e7-ed5e3e2c101a.png)

**发帖**

![post](https://user-images.githubusercontent.com/873584/52256100-63343880-2950-11e9-9bba-38bf9c8bc8f2.png)

**用户**

![home](https://user-images.githubusercontent.com/873584/52256096-629ba200-2950-11e9-87de-31d3e235578d.png)

## 安装

先在 MySQL 中手动建库（库名 `symphony`，字符集使用 `utf8mb4`，排序规则 `utf8mb4_general_ci`），然后按照如下方式之一启动服务。

### war 包启动

[下载](https://github.com/b3log/symphony/releases)最新的 Sym 包解压，进入解压目录执行：

* Windows: `java -cp "WEB-INF/lib/*;WEB-INF/classes" org.b3log.symphony.Starter`
* Unix-like: `java -cp "WEB-INF/lib/*:WEB-INF/classes" org.b3log.symphony.Starter`

如果要将 war 包部署到 Servlet 容器中启动请参考[安装指南](https://hacpai.com/article/1486188905847)。

### Docker 部署

获取最新镜像：

```shell
docker pull b3log/symphony
```

启动容器：

```shell
docker run --detach --name sym --network=host \
    --env RUNTIME_DB="MYSQL" \
    --env JDBC_USERNAME="root" \
    --env JDBC_PASSWORD="123456" \
    --env JDBC_DRIVER="com.mysql.cj.jdbc.Driver" \
    --env JDBC_URL="jdbc:mysql://127.0.0.1:3306/symphony?useUnicode=yes&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC" \
    b3log/symphony --listen_port=8080 --server_scheme=http --server_host=localhost 
```
为了简单，使用了主机网络模式来连接主机上的 MySQL。
 
启动参数说明：

* `--listen_port`：进程监听端口
* `--server_scheme`：最终访问协议，如果反代服务启用了 HTTPS 这里也需要改为 `https`
* `--server_host`：最终访问域名或公网 IP，不要带端口号

完整启动参数的说明可以使用 `-h` 来查看。

## 文档

* [《提问的智慧》精读注解版](https://hacpai.com/article/1536377163156)
* [Sym 安装指南](https://hacpai.com/article/1486188905847)
* [Sym 配置项说明](https://hacpai.com/article/1524715380797)
* [Sym 贡献指南](https://github.com/b3log/symphony/blob/master/CONTRIBUTING.md)

## 授权

* 社区版：使用 AGPLv3 开源，如果你选择使用社区版，则必须完全遵守 AGPLv3 的相关条款
* 商业版：提供完整源码以便二开，报价 ¥20000，请联系 QQ 845765 进行细节咨询

**关于商业版和社区版的对比请看[这里](https://hacpai.com/article/1500543226433)，企业网站、经营性网站、以营利为目的或实现盈利的网站请购买商业版。**

## 社区

* [讨论区](https://hacpai.com/tag/sym)
* [报告问题](https://github.com/b3log/symphony/issues/new/choose)

## 鸣谢

* [jQuery](https://github.com/jquery/jquery)：前端 JavaScript 工具库
* [Vditor](https://github.com/b3log/vditor)： 浏览器端的 Markdown 编辑器
* [Highlight.js](https://github.com/isagalaev/highlight.js)：前端代码高亮库
* [pjax](https://github.com/defunkt/jquery-pjax)：pushState + ajax = pjax
* [MathJax](https://github.com/mathjax/MathJax)：前端数学公式渲染引擎
* [Sass](http://sass-lang.com)：前端 CSS 处理工具
* [jsoup](https://github.com/jhy/jsoup)：Java HTML 解析器
* [flexmark](https://github.com/vsch/flexmark-java)：Java Markdown 处理库
* [Apache Commons](http://commons.apache.org)：Java 工具库集
* [Jodd](https://github.com/oblac/jodd)：Java 工具库集
* [Latke](https://github.com/b3log/latke)：以 JSON 为主的 Java Web 框架

安全方面特别感谢：

* [米斯特安全团队](http://www.hi-ourlife.com)
* [@gh0stkey](https://github.com/gh0stkey)
* [@SeagullGR](https://github.com/SeagullGR)
* [长亭科技](https://www.chaitin.cn)
