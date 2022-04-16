import org.junit.Before;

import java.util.Map;

/**
 * 抢菜测试程序
 */
public class ApplicationTest {

    /**
     * 配置配送信息（可以通过抓包charles抓包工具，配合pc端微信叮咚买菜小程序总获取相关配置信息，具体看Readme.md的操作步骤）
     */
    @Before
    public void configAddress(){
        if (UserConfig.addressId.length() == 0) {
            System.err.println("请先执行UserConfig获取配送地址id");
            return;
        }
    }

    public static void main(String[] args) {


        /*// 此为单次执行模式  用于在非高峰期测试下单  也必须满足3个前提条件  1.有收货地址  2.购物车有商品 3.能选择配送信息
        //购物车全选
        Api.allCheck();
        //获取购物车内容
        Map<String, Object> cartMap = Api.getCart(false);
        if (cartMap == null) {
            return;
        }
        //获取配送信息
        Map<String, Object> multiReserveTimeMap = Api.getMultiReserveTime(UserConfig.addressId, cartMap);
        if (multiReserveTimeMap == null) {
            return;
        }
        //获取订单确认信息
        Map<String, Object> checkOrderMap = Api.getCheckOrder(UserConfig.addressId, cartMap, multiReserveTimeMap);
        if (checkOrderMap == null) {
            return;
        }
        //提交订单
        Api.addNewOrder(UserConfig.addressId, cartMap, multiReserveTimeMap, checkOrderMap);*/
    }
}


