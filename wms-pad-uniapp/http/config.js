import {
	apiUrl,
	contentType
} from '@/common/setting'

var options = {
	baseURL: apiUrl,
	header: {
		'Content-Type': contentType,
		'User-Type': 'pda',
	},
	method: 'POST',
	dataType: 'json',
	// #ifndef MP-ALIPAY || APP-PLUS
	responseType: 'text',
	// #endif
	// 注：如果局部custom与全局custom有同名属性，则后面的属性会覆盖前面的属性，相当于Object.assign(全局，局部)
	custom: {}, // 全局自定义参数默认值
	// #ifdef MP-ALIPAY || MP-WEIXIN
	timeout: 30000,
	// #endif
	// #ifdef APP-PLUS
	sslVerify: true,
	// #endif
	// #ifdef H5
	// 跨域请求时是否携带凭证（cookies）仅H5支持（HBuilderX 2.6.15+）
	//withCredentials: false,
	// #endif
	// #ifdef APP-PLUS
	firstIpv4: false, // DNS解析时优先使用ipv4 仅 App-Android 支持 (HBuilderX 2.8.0+)
	// #endif
	// 局部优先级高于全局，返回当前请求的task,options。请勿在此处修改options。非必填
	// getTask: (task, options) => {
	// 相当于设置了请求超时时间500ms
	//   setTimeout(() => {
	//     task.abort()
	//   }, 500)
	// },
	// 全局自定义验证器。参数为statusCode 且必存在，不用判断空情况。
	// validateStatus: (statusCode) => { // statusCode 必存在。此处示例为全局默认配置
	// 	return statusCode >= 200 && statusCode < 300
	// }
};
export { options };
