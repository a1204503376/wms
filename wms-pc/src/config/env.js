// 阿里矢量图标库配置
let iconfontVersion = ['567566_pwc3oottzol'];
let iconfontUrl = `//at.alicdn.com/t/font_$key.css`;

let baseUrl = '';
let codeUrl = `${baseUrl}/code`
const env = process.env
if (env.NODE_ENV === 'development') {
    baseUrl = `http://localhost:8088/`; // 开发环境地址
} else if (env.NODE_ENV === 'production') {
    baseUrl = `http://10.168.3.136:6001/`; // 长沙测试环境地址
} else if (env.NODE_ENV === 'tianyitest') {
    baseUrl = `http://192.168.41.173:8088/`; // 天宜测试环境地址
} else if (env.NODE_ENV === 'tianyipro') {
    baseUrl = `http://192.168.41.171:8088/`; // 天宜生产环境地址
}
export {
    baseUrl,
    iconfontUrl,
    iconfontVersion,
    codeUrl,
    env
}
