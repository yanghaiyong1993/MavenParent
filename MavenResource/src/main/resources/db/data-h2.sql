DELETE
FROM sys_user;

INSERT INTO sys_user (id, name, age, email, deleted)
VALUES (1, 'Jone', 18, 'test1@baomidou.com', 0),
       (2, 'Jack', 20, 'test2@baomidou.com', 0),
       (3, 'Tom', 28, 'test3@baomidou.com', 1),
       (4, 'Sandy', 21, 'test4@baomidou.com', 0),
       (5, 'Billie', 24, 'test5@baomidou.com', 0);

DELETE
FROM sys_address;

INSERT INTO sys_address (id, name, user_id)
VALUES (1, 'Jone', 1),
       (2, 'Jack', 1),
       (3, 'Tom', 2),
       (4, 'Sandy', 3),
       (5, 'Billie', 2);

INSERT INTO ums_admin VALUES ('1', 'test', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/timg.jpg', 'test@qq.com', '测试账号', null, '2018-09-29 13:55:30', '2018-09-29 13:55:39', '1');
INSERT INTO ums_admin VALUES ('3', 'admin', '$2a$10$.E1FokumK5GIXWgKlg.Hc.i/0/2.qdAwYFL1zc5QHdyzpXOr38RZO', 'http://macro-oss.oss-cn-shenzhen.aliyuncs.com/mall/images/20180607/timg.jpg', 'admin@163.com', '系统管理员', '系统管理员', '2018-10-08 13:32:47', '2019-04-20 12:45:16', '1');
INSERT INTO ums_admin VALUES ('4', 'macro', '$2a$10$Bx4jZPR7GhEpIQfefDQtVeS58GfT5n6mxs/b4nLLK65eMFa16topa', 'string', 'macro@qq.com', 'macro', 'macro专用', '2019-10-06 15:53:51', '2020-02-03 14:55:55', '1');
INSERT INTO ums_admin VALUES ('6', 'productAdmin', '$2a$10$6/.J.p.6Bhn7ic4GfoB5D.pGd7xSiD1a9M6ht6yO0fxzlKJPjRAGm', null, 'product@qq.com', '商品管理员', '只有商品权限', '2020-02-07 16:15:08', null, '1');
INSERT INTO ums_admin VALUES ('7', 'orderAdmin', '$2a$10$UqEhA9UZXjHHA3B.L9wNG.6aerrBjC6WHTtbv1FdvYPUI.7lkL6E.', null, 'order@qq.com', '订单管理员', '只有订单管理权限', '2020-02-07 16:15:50', null, '1');


INSERT INTO ums_admin_role_relation VALUES ('26', '3', '5');
INSERT INTO ums_admin_role_relation VALUES ('27', '6', '1');
INSERT INTO ums_admin_role_relation VALUES ('28', '7', '2');
INSERT INTO ums_admin_role_relation VALUES ('29', '1', '5');
INSERT INTO ums_admin_role_relation VALUES ('30', '4', '5');



INSERT INTO ums_menu VALUES ('1', '0', '2020-02-02 14:50:36', '商品', '0', '0', 'pms', 'product', '0');
INSERT INTO ums_menu VALUES ('2', '1', '2020-02-02 14:51:50', '商品列表', '1', '0', 'product', 'product-list', '0');
INSERT INTO ums_menu VALUES ('3', '1', '2020-02-02 14:52:44', '添加商品', '1', '0', 'addProduct', 'product-add', '0');
INSERT INTO ums_menu VALUES ('4', '1', '2020-02-02 14:53:51', '商品分类', '1', '0', 'productCate', 'product-cate', '0');
INSERT INTO ums_menu VALUES ('5', '1', '2020-02-02 14:54:51', '商品类型', '1', '0', 'productAttr', 'product-attr', '0');
INSERT INTO ums_menu VALUES ('6', '1', '2020-02-02 14:56:29', '品牌管理', '1', '0', 'brand', 'product-brand', '0');
INSERT INTO ums_menu VALUES ('7', '0', '2020-02-02 16:54:07', '订单', '0', '0', 'oms', 'order', '0');
INSERT INTO ums_menu VALUES ('8', '7', '2020-02-02 16:55:18', '订单列表', '1', '0', 'order', 'product-list', '0');
INSERT INTO ums_menu VALUES ('9', '7', '2020-02-02 16:56:46', '订单设置', '1', '0', 'orderSetting', 'order-setting', '0');
INSERT INTO ums_menu VALUES ('10', '7', '2020-02-02 16:57:39', '退货申请处理', '1', '0', 'returnApply', 'order-return', '0');
INSERT INTO ums_menu VALUES ('11', '7', '2020-02-02 16:59:40', '退货原因设置', '1', '0', 'returnReason', 'order-return-reason', '0');
INSERT INTO ums_menu VALUES ('12', '0', '2020-02-04 16:18:00', '营销', '0', '0', 'sms', 'sms', '0');
INSERT INTO ums_menu VALUES ('13', '12', '2020-02-04 16:19:22', '秒杀活动列表', '1', '0', 'flash', 'sms-flash', '0');
INSERT INTO ums_menu VALUES ('14', '12', '2020-02-04 16:20:16', '优惠券列表', '1', '0', 'coupon', 'sms-coupon', '0');
INSERT INTO ums_menu VALUES ('16', '12', '2020-02-07 16:22:38', '品牌推荐', '1', '0', 'homeBrand', 'product-brand', '0');
INSERT INTO ums_menu VALUES ('17', '12', '2020-02-07 16:23:14', '新品推荐', '1', '0', 'homeNew', 'sms-new', '0');
INSERT INTO ums_menu VALUES ('18', '12', '2020-02-07 16:26:38', '人气推荐', '1', '0', 'homeHot', 'sms-hot', '0');
INSERT INTO ums_menu VALUES ('19', '12', '2020-02-07 16:28:16', '专题推荐', '1', '0', 'homeSubject', 'sms-subject', '0');
INSERT INTO ums_menu VALUES ('20', '12', '2020-02-07 16:28:42', '广告列表', '1', '0', 'homeAdvertise', 'sms-ad', '0');
INSERT INTO ums_menu VALUES ('21', '0', '2020-02-07 16:29:13', '权限', '0', '0', 'ums', 'ums', '0');
INSERT INTO ums_menu VALUES ('22', '21', '2020-02-07 16:29:51', '用户列表', '1', '0', 'admin', 'ums-admin', '0');
INSERT INTO ums_menu VALUES ('23', '21', '2020-02-07 16:30:13', '角色列表', '1', '0', 'role', 'ums-role', '0');
INSERT INTO ums_menu VALUES ('24', '21', '2020-02-07 16:30:53', '菜单列表', '1', '0', 'menu', 'ums-menu', '0');
INSERT INTO ums_menu VALUES ('25', '21', '2020-02-07 16:31:13', '资源列表', '1', '0', 'resource', 'ums-resource', '0');
       

INSERT INTO ums_permission VALUES ('1', '0', '商品', null, null, '0', null, '1', '2018-09-29 16:15:14', '0');
INSERT INTO ums_permission VALUES ('2', '1', '商品列表', 'pms:product:read', null, '1', '/pms/product/index', '1', '2018-09-29 16:17:01', '0');
INSERT INTO ums_permission VALUES ('3', '1', '添加商品', 'pms:product:create', null, '1', '/pms/product/add', '1', '2018-09-29 16:18:51', '0');
INSERT INTO ums_permission VALUES ('4', '1', '商品分类', 'pms:productCategory:read', null, '1', '/pms/productCate/index', '1', '2018-09-29 16:23:07', '0');
INSERT INTO ums_permission VALUES ('5', '1', '商品类型', 'pms:productAttribute:read', null, '1', '/pms/productAttr/index', '1', '2018-09-29 16:24:43', '0');
INSERT INTO ums_permission VALUES ('6', '1', '品牌管理', 'pms:brand:read', null, '1', '/pms/brand/index', '1', '2018-09-29 16:25:45', '0');
INSERT INTO ums_permission VALUES ('7', '2', '编辑商品', 'pms:product:update', null, '2', '/pms/product/updateProduct', '1', '2018-09-29 16:34:23', '0');
INSERT INTO ums_permission VALUES ('8', '2', '删除商品', 'pms:product:delete', null, '2', '/pms/product/delete', '1', '2018-09-29 16:38:33', '0');
INSERT INTO ums_permission VALUES ('9', '4', '添加商品分类', 'pms:productCategory:create', null, '2', '/pms/productCate/create', '1', '2018-09-29 16:43:23', '0');
INSERT INTO ums_permission VALUES ('10', '4', '修改商品分类', 'pms:productCategory:update', null, '2', '/pms/productCate/update', '1', '2018-09-29 16:43:55', '0');
INSERT INTO ums_permission VALUES ('11', '4', '删除商品分类', 'pms:productCategory:delete', null, '2', '/pms/productAttr/delete', '1', '2018-09-29 16:44:38', '0');
INSERT INTO ums_permission VALUES ('12', '5', '添加商品类型', 'pms:productAttribute:create', null, '2', '/pms/productAttr/create', '1', '2018-09-29 16:45:25', '0');
INSERT INTO ums_permission VALUES ('13', '5', '修改商品类型', 'pms:productAttribute:update', null, '2', '/pms/productAttr/update', '1', '2018-09-29 16:48:08', '0');
INSERT INTO ums_permission VALUES ('14', '5', '删除商品类型', 'pms:productAttribute:delete', null, '2', '/pms/productAttr/delete', '1', '2018-09-29 16:48:44', '0');
INSERT INTO ums_permission VALUES ('15', '6', '添加品牌', 'pms:brand:create', null, '2', '/pms/brand/add', '1', '2018-09-29 16:49:34', '0');
INSERT INTO ums_permission VALUES ('16', '6', '修改品牌', 'pms:brand:update', null, '2', '/pms/brand/update', '1', '2018-09-29 16:50:55', '0');
INSERT INTO ums_permission VALUES ('17', '6', '删除品牌', 'pms:brand:delete', null, '2', '/pms/brand/delete', '1', '2018-09-29 16:50:59', '0');
INSERT INTO ums_permission VALUES ('18', '0', '首页', null, null, '0', null, '1', '2018-09-29 16:51:57', '0');


INSERT INTO ums_resource VALUES ('1', '2020-02-04 17:04:55', '商品品牌管理', '/brand/**', null, '1');
INSERT INTO ums_resource VALUES ('2', '2020-02-04 17:05:35', '商品属性分类管理', '/productAttribute/**', null, '1');
INSERT INTO ums_resource VALUES ('3', '2020-02-04 17:06:13', '商品属性管理', '/productAttribute/**', null, '1');
INSERT INTO ums_resource VALUES ('4', '2020-02-04 17:07:15', '商品分类管理', '/productCategory/**', null, '1');
INSERT INTO ums_resource VALUES ('5', '2020-02-04 17:09:16', '商品管理', '/product/**', null, '1');
INSERT INTO ums_resource VALUES ('6', '2020-02-04 17:09:53', '商品库存管理', '/sku/**', null, '1');
INSERT INTO ums_resource VALUES ('8', '2020-02-05 14:43:37', '订单管理', '/order/**', '', '2');
INSERT INTO ums_resource VALUES ('9', '2020-02-05 14:44:22', ' 订单退货申请管理', '/returnApply/**', '', '2');
INSERT INTO ums_resource VALUES ('10', '2020-02-05 14:45:08', '退货原因管理', '/returnReason/**', '', '2');
INSERT INTO ums_resource VALUES ('11', '2020-02-05 14:45:43', '订单设置管理', '/orderSetting/**', '', '2');
INSERT INTO ums_resource VALUES ('12', '2020-02-05 14:46:23', '收货地址管理', '/companyAddress/**', '', '2');
INSERT INTO ums_resource VALUES ('13', '2020-02-07 16:37:22', '优惠券管理', '/coupon/**', '', '3');
INSERT INTO ums_resource VALUES ('14', '2020-02-07 16:37:59', '优惠券领取记录管理', '/couponHistory/**', '', '3');
INSERT INTO ums_resource VALUES ('15', '2020-02-07 16:38:28', '限时购活动管理', '/flash/**', '', '3');
INSERT INTO ums_resource VALUES ('16', '2020-02-07 16:38:59', '限时购商品关系管理', '/flashProductRelation/**', '', '3');
INSERT INTO ums_resource VALUES ('17', '2020-02-07 16:39:22', '限时购场次管理', '/flashSession/**', '', '3');
INSERT INTO ums_resource VALUES ('18', '2020-02-07 16:40:07', '首页轮播广告管理', '/home/advertise/**', '', '3');
INSERT INTO ums_resource VALUES ('19', '2020-02-07 16:40:34', '首页品牌管理', '/home/brand/**', '', '3');
INSERT INTO ums_resource VALUES ('20', '2020-02-07 16:41:06', '首页新品管理', '/home/newProduct/**', '', '3');
INSERT INTO ums_resource VALUES ('21', '2020-02-07 16:42:16', '首页人气推荐管理', '/home/recommendProduct/**', '', '3');
INSERT INTO ums_resource VALUES ('22', '2020-02-07 16:42:48', '首页专题推荐管理', '/home/recommendSubject/**', '', '3');
INSERT INTO ums_resource VALUES ('23', '2020-02-07 16:44:56', ' 商品优选管理', '/prefrenceArea/**', '', '5');
INSERT INTO ums_resource VALUES ('24', '2020-02-07 16:45:39', '商品专题管理', '/subject/**', '', '5');
INSERT INTO ums_resource VALUES ('25', '2020-02-07 16:47:34', '后台用户管理', '/admin/**', '', '4');
INSERT INTO ums_resource VALUES ('26', '2020-02-07 16:48:24', '后台用户角色管理', '/role/**', '', '4');
INSERT INTO ums_resource VALUES ('27', '2020-02-07 16:48:48', '后台菜单管理', '/menu/**', '', '4');
INSERT INTO ums_resource VALUES ('28', '2020-02-07 16:49:18', '后台资源分类管理', '/resourceCategory/**', '', '4');
INSERT INTO ums_resource VALUES ('29', '2020-02-07 16:49:45', '后台资源管理', '/resource/**', '', '4');


INSERT INTO ums_resource_category VALUES ('1', '2020-02-05 10:21:44', '商品模块', '0');
INSERT INTO ums_resource_category VALUES ('2', '2020-02-05 10:22:34', '订单模块', '0');
INSERT INTO ums_resource_category VALUES ('3', '2020-02-05 10:22:48', '营销模块', '0');
INSERT INTO ums_resource_category VALUES ('4', '2020-02-05 10:23:04', '权限模块', '0');
INSERT INTO ums_resource_category VALUES ('5', '2020-02-07 16:34:27', '内容模块', '0');
INSERT INTO ums_resource_category VALUES ('6', '2020-02-07 16:35:49', '其他模块', '0');       


INSERT INTO ums_role VALUES ('1', '商品管理员', '只能查看及操作商品', '0', '2020-02-03 16:50:37', '1', '0');
INSERT INTO ums_role VALUES ('2', '订单管理员', '只能查看及操作订单', '0', '2018-09-30 15:53:45', '1', '0');
INSERT INTO ums_role VALUES ('5', '超级管理员', '拥有所有查看和操作功能', '0', '2020-02-02 15:11:05', '1', '0');


INSERT INTO ums_role_menu_relation VALUES ('33', '1', '1');
INSERT INTO ums_role_menu_relation VALUES ('34', '1', '2');
INSERT INTO ums_role_menu_relation VALUES ('35', '1', '3');
INSERT INTO ums_role_menu_relation VALUES ('36', '1', '4');
INSERT INTO ums_role_menu_relation VALUES ('37', '1', '5');
INSERT INTO ums_role_menu_relation VALUES ('38', '1', '6');
INSERT INTO ums_role_menu_relation VALUES ('53', '2', '7');
INSERT INTO ums_role_menu_relation VALUES ('54', '2', '8');
INSERT INTO ums_role_menu_relation VALUES ('55', '2', '9');
INSERT INTO ums_role_menu_relation VALUES ('56', '2', '10');
INSERT INTO ums_role_menu_relation VALUES ('57', '2', '11');
INSERT INTO ums_role_menu_relation VALUES ('72', '5', '1');
INSERT INTO ums_role_menu_relation VALUES ('73', '5', '2');
INSERT INTO ums_role_menu_relation VALUES ('74', '5', '3');
INSERT INTO ums_role_menu_relation VALUES ('75', '5', '4');
INSERT INTO ums_role_menu_relation VALUES ('76', '5', '5');
INSERT INTO ums_role_menu_relation VALUES ('77', '5', '6');
INSERT INTO ums_role_menu_relation VALUES ('78', '5', '7');
INSERT INTO ums_role_menu_relation VALUES ('79', '5', '8');
INSERT INTO ums_role_menu_relation VALUES ('80', '5', '9');
INSERT INTO ums_role_menu_relation VALUES ('81', '5', '10');
INSERT INTO ums_role_menu_relation VALUES ('82', '5', '11');
INSERT INTO ums_role_menu_relation VALUES ('83', '5', '12');
INSERT INTO ums_role_menu_relation VALUES ('84', '5', '13');
INSERT INTO ums_role_menu_relation VALUES ('85', '5', '14');
INSERT INTO ums_role_menu_relation VALUES ('86', '5', '16');
INSERT INTO ums_role_menu_relation VALUES ('87', '5', '17');
INSERT INTO ums_role_menu_relation VALUES ('88', '5', '18');
INSERT INTO ums_role_menu_relation VALUES ('89', '5', '19');
INSERT INTO ums_role_menu_relation VALUES ('90', '5', '20');
INSERT INTO ums_role_menu_relation VALUES ('91', '5', '21');
INSERT INTO ums_role_menu_relation VALUES ('92', '5', '22');
INSERT INTO ums_role_menu_relation VALUES ('93', '5', '23');
INSERT INTO ums_role_menu_relation VALUES ('94', '5', '24');
INSERT INTO ums_role_menu_relation VALUES ('95', '5', '25');


INSERT INTO ums_role_permission_relation VALUES ('1', '1', '1');
INSERT INTO ums_role_permission_relation VALUES ('2', '1', '2');
INSERT INTO ums_role_permission_relation VALUES ('3', '1', '3');
INSERT INTO ums_role_permission_relation VALUES ('4', '1', '7');
INSERT INTO ums_role_permission_relation VALUES ('5', '1', '8');
INSERT INTO ums_role_permission_relation VALUES ('6', '2', '4');
INSERT INTO ums_role_permission_relation VALUES ('7', '2', '9');
INSERT INTO ums_role_permission_relation VALUES ('8', '2', '10');
INSERT INTO ums_role_permission_relation VALUES ('9', '2', '11');
INSERT INTO ums_role_permission_relation VALUES ('10', '3', '5');
INSERT INTO ums_role_permission_relation VALUES ('11', '3', '12');
INSERT INTO ums_role_permission_relation VALUES ('12', '3', '13');
INSERT INTO ums_role_permission_relation VALUES ('13', '3', '14');
INSERT INTO ums_role_permission_relation VALUES ('14', '4', '6');
INSERT INTO ums_role_permission_relation VALUES ('15', '4', '15');
INSERT INTO ums_role_permission_relation VALUES ('16', '4', '16');
INSERT INTO ums_role_permission_relation VALUES ('17', '4', '17');


INSERT INTO ums_role_resource_relation VALUES ('103', '2', '8');
INSERT INTO ums_role_resource_relation VALUES ('104', '2', '9');
INSERT INTO ums_role_resource_relation VALUES ('105', '2', '10');
INSERT INTO ums_role_resource_relation VALUES ('106', '2', '11');
INSERT INTO ums_role_resource_relation VALUES ('107', '2', '12');
INSERT INTO ums_role_resource_relation VALUES ('142', '5', '1');
INSERT INTO ums_role_resource_relation VALUES ('143', '5', '2');
INSERT INTO ums_role_resource_relation VALUES ('144', '5', '3');
INSERT INTO ums_role_resource_relation VALUES ('145', '5', '4');
INSERT INTO ums_role_resource_relation VALUES ('146', '5', '5');
INSERT INTO ums_role_resource_relation VALUES ('147', '5', '6');
INSERT INTO ums_role_resource_relation VALUES ('148', '5', '8');
INSERT INTO ums_role_resource_relation VALUES ('149', '5', '9');
INSERT INTO ums_role_resource_relation VALUES ('150', '5', '10');
INSERT INTO ums_role_resource_relation VALUES ('151', '5', '11');
INSERT INTO ums_role_resource_relation VALUES ('152', '5', '12');
INSERT INTO ums_role_resource_relation VALUES ('153', '5', '13');
INSERT INTO ums_role_resource_relation VALUES ('154', '5', '14');
INSERT INTO ums_role_resource_relation VALUES ('155', '5', '15');
INSERT INTO ums_role_resource_relation VALUES ('156', '5', '16');
INSERT INTO ums_role_resource_relation VALUES ('157', '5', '17');
INSERT INTO ums_role_resource_relation VALUES ('158', '5', '18');
INSERT INTO ums_role_resource_relation VALUES ('159', '5', '19');
INSERT INTO ums_role_resource_relation VALUES ('160', '5', '20');
INSERT INTO ums_role_resource_relation VALUES ('161', '5', '21');
INSERT INTO ums_role_resource_relation VALUES ('162', '5', '22');
INSERT INTO ums_role_resource_relation VALUES ('163', '5', '23');
INSERT INTO ums_role_resource_relation VALUES ('164', '5', '24');
INSERT INTO ums_role_resource_relation VALUES ('165', '5', '25');
INSERT INTO ums_role_resource_relation VALUES ('166', '5', '26');
INSERT INTO ums_role_resource_relation VALUES ('167', '5', '27');
INSERT INTO ums_role_resource_relation VALUES ('168', '5', '28');
INSERT INTO ums_role_resource_relation VALUES ('169', '5', '29');
INSERT INTO ums_role_resource_relation VALUES ('170', '1', '1');
INSERT INTO ums_role_resource_relation VALUES ('171', '1', '2');
INSERT INTO ums_role_resource_relation VALUES ('172', '1', '3');
INSERT INTO ums_role_resource_relation VALUES ('173', '1', '4');
INSERT INTO ums_role_resource_relation VALUES ('174', '1', '5');
INSERT INTO ums_role_resource_relation VALUES ('175', '1', '6');
INSERT INTO ums_role_resource_relation VALUES ('176', '1', '23');
INSERT INTO ums_role_resource_relation VALUES ('177', '1', '24');


