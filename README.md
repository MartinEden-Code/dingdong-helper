# dingdong-helper
叮咚自动下单 并发调用接口方式 多人实战反馈10秒以内成功 自动将购物车能买的商品全部下单 只需自行编辑购物车和最后支付即可

当前时间2022-04-14根据自身和身边朋友反馈正常下单，但不能保证每一个人都能成功下单，如果此程序完全不可用，我会更新到这个位置

2022-04-13 重要更新 重要更新 重要更新 新增二种执行策略 时间触发 哨兵模式

2022-04-14 重要更新 重要更新 重要更新 还原抢菜线程间隔 13号更新加长间隔导致效率下降

# 特别强调 注意事项
1. 此程序只用来帮助在上海急需买菜的程序猿，请勿商用，issues中非技术问题本人不参与也不会阻止大家讨论
2. 不要删除Application中的保护线程，此段代码控制程序并发执行时2分钟未下单自动终止，避免对叮咚服务器造成压力，也避免封号
3. 根据反馈有不少人出现当天下单多次之后无法下单或无法支付的情况，请换号或隔一段时间再试
4. 接口如果出现405状态有以3种可能 1.偶发，无需处理 2.不要长时间运行程序，参考上面的第2点  3.一个账号下单数有时会有限制 参考上面的第3点
5. 根据反馈有少部分人的站点是假库存，可能是怕大家闹事，开放购买之前能看到购物车里有，但是根本就不可能买到，第一秒下单瞬间很多东西就没了，我也是，几百块的购物车最后下单几十块，我同时用app人工操作了购物车确实是没货了，不是程序问题。
6. 日期20220412我这个站点是6点开始陆续上东西，6点之前购物车没有东西可以买
7. 叮咚的策略是6点和8点30更新当天配送时间，全天都有可能会上架货品，所以每天最佳抢菜时间是6点（当天第一轮允许下单），如果6点-8点30之间一直能配送那么8点30不会有任何变化，8点30主要是更新配送时间，只有当8点30之前无法配送的时候（当天第二轮允许下单）才需要在这个时间抢配送额度

## 环境
1. intellij idea
2. jdk 8
3. maven(可用idea内置maven)

使用idea - file - open - 项目文件夹 - 等待右下角maven构建进度条

## 步骤

1. 通过Charles（我截图和教程是Charles，用Charles会更方便对比）等抓包工具抓取微信中叮咚买菜小程序中的接口信息中的用户信息配置到UserConfig.java中，比如openId、userId，详情见下截图，此操作每个用户只需要做一次。注意事项：其中有一个参数叫station_id，headers和body中都有，一定要确认抓包时你的站点信息设置是对的（非常重要 非常重要 非常重要），进入小程序后左上角确认站点信息后再抓包
2. 运行UserConfig.java获取addressId填入addressId变量并确认站点信息是否正确，如果执行显示用户过期则检查参数少配或配错，重点关注headers中ddmc-device-id、cookie、ddmc-uid ，body中uid、open_id、device_token
3. 将需要买的菜自行通过APP放入购物车
4. 5-8执行模式根据使用需求自选
5. 测试模式（单线程）: 执行ApplicationTest低峰期单次下单
6. 人工执行（多线程并发）：设置Application中的policy变量1并运行，如果当前购车有商品并有配送时间则会在10秒内执行成功
7. 时间触发（多线程并发）：设置Application中的policy变量2或3并运行，当系统时间到达5点59分30秒或8点29分30秒自动执行，如果购买成功将播放一分钟的提示音（请确保电脑外放无静音）
8. 哨兵模式（单线程）：设置Sentinel中最小下单金额并运行，当金额超过设置金额时尝试下单，请注意此模式下不并发，所以在6点和8点30左右的高峰期可能会存在长时间无法正常下单，高峰期买菜使用6或者7策略，如果购买成功将播放一分钟的提示音（请确保电脑外放无静音）
9. 等待程序结束，如果成功下单请在5分钟内付款，否则订单会取消，用手机打开叮咚买菜app-我的订单-待支付-点击支付
10. 每次抢之前跑一下UserConfig中的main方法确认登录状态是否准确，如果状态不对则重新抓包更新UserConfig数据
11. 如果想用自己的号帮别人下单，只需要手动在APP中设置一下默认地址再运行UserConfig 获取到addressId和stationId进行替换

## 程序自动结束的几个条件

1. 购物车无可购买商品（时间触发和哨兵模式会持续执行）
2. 下单成功
3. 用户登录信息失效

## 快捷抓包

小程序已经有PC版了，手机进入小程序右上角3个点->在电脑中打开即可，送上一个参考文章https://blog.csdn.net/z2181745/article/details/123002569 比手机抓包方便太多。

注意事项
1. Charles安装和配置好后再打开或重新打开电脑端叮咚小程序，如果在之前打开可能会抓不到
2. 如果使用电脑端小程序抓包，则不要去碰手机微信里的叮咚小程序，否则session会失效，反过来也一样，其他操作在app上操作不影响，但不能同时在两个端的小程序操作，互斥

## 设备分工（同上面的步骤，把设备关系说清楚）

#### 手机&电脑

1. 打开微信小程序中的叮咚买菜，通过电脑抓包软件抓取信息填入代码中，在token不失效的情况下可以一直使用
2. 也可以使用电脑端小程序进行抓包会比手机方便很多

#### 手机

1. 在开放购买前选择商品到购物车
2. 等待下单成功后去待支付订单页面支付

#### 电脑

1. 运行UserConfig获取addressId并填入变量addressId
2. 开放购买前1分钟运行Application，前30秒左右会获取基本信息直到看到提交订单失败的信息则代表基本信息获取完毕等待开放时间一到即可成功

## 思路

虽然我家吃的很多，但是时间长了也受不了这几天每天早上起来抢菜，手都点抽经了都买不到，看着购物车里的菜越来越少心急如焚，作为程序员只能靠自己的双手了，吃完午饭开干，晚上6点成功下单
1. 抓app的包没抓到
2. 抓小程序的包可以，但是小程序无法做登录，拿不到open id，所以只能通过自行抓包解决。另看到请求参数中有一些签名字段，心想麻烦哟
3. 准备研究如何签名，解包微信小程序，初步研究签名相关代码，搞不定就去研究app hook，但那耗费精力太大，留着当后手
4. 先写一个获取地址的请求，发现那几个看着像签名的参数可以不用传，省了一大笔精力，应该一开始就用Charles的breakpoint删除参数再repeat尝试无签名是否可访问，被唬住了，早知道就可以省略步骤3
5. 梳理下单需要的参数和步骤，数据量非常庞大，眼睛都看晕了，需要细心
6. 看到下单成功很开心，这就是乐趣

最后希望疫情早日结束大家伙都能吃上饭

## 更新记录

### 2022.04.11
1. 新增自动勾选购物车
2. 优化请求量过大和持续时间过长被网关拦截提示
3. 执行UserConfig时新增站点信息确认，如站点信息错误将导致购物车在手机上看有货程序执行无货或无法下单

### 2022.04.12
1. 叮咚更新了异常返回数据结构，修改异常日志输出
2. 修复无法使用优惠券的问题
3. 修复明明显示有配送信息但下单时报该时间段不能配送的问题

### 2022.04.13
1. 新增平常时间段哨兵模式（长间隔单线程），设置最小下单金额，成功下单后会播放一分钟的铃声，请将电脑音量打开到合适的音量
2. 新增5点59和8点29两个时间触发程序（并发），如需使用 请设置Application中的policy变量，成功下单后会播放一分钟的铃声，请将电脑音量打开到合适的音量
3. 新增并发时保护程序，默认并发执行2分钟，避免封号和对叮咚服务器造成的压力

### 2022.04.14
1. 提交订单的间隔时间拉长后下单效率明显降低，30秒才成功，改回原配置

## 抓包截图 将你的信息填入

这个图有时候会挂，直接从项目里面看也一样，就是路径image/headers.jpeg 和 body.jpeg  对应到UserConfig中的headers和body方法里的参数
![请求头信息](https://github.com/JannsenYang/dingdong-helper/blob/8a16a972185cd4e560c24b57137dcd90b929efcb/image/headers.jpg)
![请求体信息](https://github.com/JannsenYang/dingdong-helper/blob/8a16a972185cd4e560c24b57137dcd90b929efcb/image/body.jpg)

## 20220410实战记录

用了的全部秒抢，我自己傻逼了，为了提交github，收货地址id在运行的时候忘记填了，跑了几分钟才后知后觉，随即补上了失败时的返回信息。
![实战记录1](https://github.com/JannsenYang/dingdong-helper/blob/3f1847b6f5c363168de733380d9f3cb02a64b8a6/image/20220410-1.png)
![实战记录2](https://github.com/JannsenYang/dingdong-helper/blob/f6e20d377aa482063732a5be614e3dae3d4c5091/image/20220410-2.png)



### 版权说明

**本项目为 GPL3.0 协议，请所有进行二次开发的开发者遵守 GPL3.0协议，并且不得将代码用于商用。**
