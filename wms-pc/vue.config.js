module.exports = {
    //路径前缀
    publicPath: "/",
    lintOnSave: true,
    productionSourceMap: false,
    chainWebpack: (config) => {
        //忽略的打包文件
        config.externals({
            'vue': 'Vue',
            'vue-router': 'VueRouter',
            'vuex': 'Vuex',
            'axios': 'axios',
            'element-ui': 'ELEMENT',
        });
        const entry = config.entry('app');
        entry.add('babel-polyfill').end();
        entry.add('classlist-polyfill').end();
    },
    css: {
        extract: {ignoreOrder: true}
    },
    //开发模式反向代理配置，生产模式请使用Nginx部署并配置反向代理
    devServer: {
        port: 8080,
        // port: 8091,
        proxy: {
            '/api': {
                //本地服务接口地址
                //target: 'http://localhost:8090',
                target: 'http://localhost:8088',
                // target: 'http://192.168.1.221:5000',
                //远程演示服务地址,可用于直接启动项目
                // target: 'http://114.215.69.116:8098/',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '/'
                }
            }
        }
    }
};
