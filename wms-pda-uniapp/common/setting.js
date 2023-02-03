/**
 * 全局变量配置
 */
module.exports = {
	// 应用名
	name: 'WMS3.3',
	// 应用logo，支持本地路径和网络路径
	logo: '/static/images/tyicon.png',
	// 版本号
	version: '1.4.0',
	// 租户
	tenantId: '000000',
	// api请求默认地址url
	apiUrl: 'http://192.168.41.171:8088',
	// 后端数据的接收方式application/json;charset=UTF-8或者application/x-www-form-urlencoded;charset=UTF-8
	contentType: 'application/json;charset=UTF-8',
	// 操作正常code
	successCode: 200,
	// 客户端ID,对应数据库blade_client表中的client_id字段
	clientId: 'saber',
	// 客户端密钥,对应数据库blade_client表中的client_secret字段
	clientSecret: 'saber_secret',
	// 自定义导航栏的颜色
	customNavigationBarBackgroundColor: '#33cbcc'
}
