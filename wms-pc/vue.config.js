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
        extract: {
            ignoreOrder: true,
            extract: false
        }
    },
    //开发模式反向代理配置，生产模式请使用Nginx部署并配置反向代理
    devServer: {
        port: 8080,
        proxy: {
            '/api': {
                //本地服务接口地址
                target: 'http://localhost:8088',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '/'
                }
            }
        }
    }
};
