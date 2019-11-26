package com.dykj.zhonganxiao.bean;

/**
 * @file: UserInfo 用户信息
 * @author: guokang
 * @date: 2019-08-05
 */
public class UserInfo {
//    basic：用户基本信息
//        nickname：昵称
//        sex：性别   0-保密；1-男；2-女
//        birthday： 0-保密，其他情况 正常日期格式 ：2019-08-21
//        user_money：用户余额
//        forzen_money：用户冻结金额
//        pay_points：用户积分
//        collect_count：收藏数量
//        coupon_count：可用优惠券数量
//        notice_count：未读消息数量
//        not_pay_count：待支付订单数量
//        not_shipping_count：待发货订单数量
//        not_confirm_count：待收货订单数量
//        not_comment_count：待评论订单数量
//        after_sale_count：退换/售后订单数量
//        is_supplier：是否是商家  0-否，supplier值为null；1-是

//    supplier：商家身份信息，非商家时为null
//        supplier_status：商家店铺状态：0-待审核；1-审核通过；2-审核被拒
//        supplier_money：店铺可体现金额
//        forzen_money：店铺冻结金额
//        not_pay_count：待支付订单数量
//        not_shipping_count：待发货订单数量
//        not_confirm_count：待确认订单数量
//        finish_count：已完成订单数量
//        after_sale_count：售后订单数量

    private BasicBean basic;
    private SupplierBean supplier;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public SupplierBean getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierBean supplier) {
        this.supplier = supplier;
    }

    /**
     * 用户基本信息
     */
    public class BasicBean{
        private String nickname;
        private String photo;//头像
        private String sex;
        private String birthday;

        private String user_money;
        private String frozen_money;

        private String all_money;

        private String pay_points;
        private String collect_count;
        private String coupon_count;
        private String notice_count;
        private String not_pay_count;
        private String not_shipping_count;
        private String not_confirm_count;
        private String not_comment_count;
        private String after_sale_count;
        private String is_supplier;

        private String invite_code;//邀请码
        private String is_pay_passwd;//1 已设置支付密码 0未设置

        private String mobile;


        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIs_pay_passwd() {
            return is_pay_passwd;
        }

        public void setIs_pay_passwd(String is_pay_passwd) {
            this.is_pay_passwd = is_pay_passwd;
        }

        public String getAll_money() {
            return all_money;
        }

        public void setAll_money(String all_money) {
            this.all_money = all_money;
        }

        public String getInvite_code() {
            return invite_code;
        }

        public void setInvite_code(String invite_code) {
            this.invite_code = invite_code;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getUser_money() {
            return user_money;
        }

        public void setUser_money(String user_money) {
            this.user_money = user_money;
        }

        public String getFrozen_money() {
            return frozen_money;
        }

        public void setFrozen_money(String frozen_money) {
            this.frozen_money = frozen_money;
        }

        public String getPay_points() {
            return pay_points;
        }

        public void setPay_points(String pay_points) {
            this.pay_points = pay_points;
        }

        public String getCollect_count() {
            return collect_count;
        }

        public void setCollect_count(String collect_count) {
            this.collect_count = collect_count;
        }

        public String getCoupon_count() {
            return coupon_count;
        }

        public void setCoupon_count(String coupon_count) {
            this.coupon_count = coupon_count;
        }

        public String getNotice_count() {
            return notice_count;
        }

        public void setNotice_count(String notice_count) {
            this.notice_count = notice_count;
        }

        public String getNot_pay_count() {
            return not_pay_count;
        }

        public void setNot_pay_count(String not_pay_count) {
            this.not_pay_count = not_pay_count;
        }

        public String getNot_shipping_count() {
            return not_shipping_count;
        }

        public void setNot_shipping_count(String not_shipping_count) {
            this.not_shipping_count = not_shipping_count;
        }

        public String getNot_confirm_count() {
            return not_confirm_count;
        }

        public void setNot_confirm_count(String not_confirm_count) {
            this.not_confirm_count = not_confirm_count;
        }

        public String getNot_comment_count() {
            return not_comment_count;
        }

        public void setNot_comment_count(String not_comment_count) {
            this.not_comment_count = not_comment_count;
        }

        public String getAfter_sale_count() {
            return after_sale_count;
        }

        public void setAfter_sale_count(String after_sale_count) {
            this.after_sale_count = after_sale_count;
        }

        public String getIs_supplier() {
            return is_supplier;
        }

        public void setIs_supplier(String is_supplier) {
            this.is_supplier = is_supplier;
        }
    }

    /**
     * 商家身份信息
     */

    public class SupplierBean{

       private String supplier_status;
//       private String supplier_money;
//       private String frozen_money;
       private String not_pay_count;
       private String not_shipping_count;
       private String not_confirm_count;
       private String finish_count;
       private String after_sale_count;

        public String getSupplier_status() {
            return supplier_status;
        }

        public void setSupplier_status(String supplier_status) {
            this.supplier_status = supplier_status;
        }


        public String getNot_pay_count() {
            return not_pay_count;
        }

        public void setNot_pay_count(String not_pay_count) {
            this.not_pay_count = not_pay_count;
        }

        public String getNot_shipping_count() {
            return not_shipping_count;
        }

        public void setNot_shipping_count(String not_shipping_count) {
            this.not_shipping_count = not_shipping_count;
        }

        public String getNot_confirm_count() {
            return not_confirm_count;
        }

        public void setNot_confirm_count(String not_confirm_count) {
            this.not_confirm_count = not_confirm_count;
        }

        public String getFinish_count() {
            return finish_count;
        }

        public void setFinish_count(String finish_count) {
            this.finish_count = finish_count;
        }

        public String getAfter_sale_count() {
            return after_sale_count;
        }

        public void setAfter_sale_count(String after_sale_count) {
            this.after_sale_count = after_sale_count;
        }
    }
}
