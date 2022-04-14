const config = {
	
	// 产品名称
	productName: 'XINGZHU Wms Mobile',
	
	// 公司名称
	companyName: 'Notes',
	
	// 产品版本号
	productVersion: 'V1.0.0',
	
	// 版本检查标识
	appCode: 'android',
	
	// 内部版本号码
	appVersion: 1,
	
}

// 设置后台接口服务的基础地址
//config.baseUrl = 'https://wmse.elinkpoc.spic.com.cn/wmsmb';   //正式环境	
//config.baseUrl = 'https://wmse.elinkpoc.spic.com.cn/wmsmbtest';   //正式测试环境	
//config.baseUrl = 'http://localhost:8088';  //本地环境
config.baseUrl = 'http://114.215.69.116:9098';  //公司云环境
// 建议：打开下面注释，方便根据环境，自动设定服务地址
if (process.env.NODE_ENV === 'development'){
	// config.baseUrl = '/../js'; // 代理模式 vue.config.js 中找到 devServer 设置的地址
	// config.baseUrl = 'http://127.0.0.1:8980/js';
}

export default config;