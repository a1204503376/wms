// 阿里矢量图标库配置
let iconfontVersion = ['567566_pwc3oottzol'];
let iconfontUrl = `//at.alicdn.com/t/font_$key.css`;

let baseUrl = '';
let codeUrl = `${baseUrl}/code`
const env = process.env
if (env.NODE_ENV === 'development') {
  baseUrl = `http://localhost:8088/`; // 开发环境地址
} else if (env.NODE_ENV === 'production') {
  baseUrl = ``; //生产环境地址
} else if (env.NODE_ENV === 'test') {
  baseUrl = `http://10.168.3.136/`; //测试环境地址
}
export {
  baseUrl,
  iconfontUrl,
  iconfontVersion,
  codeUrl,
  env
}
