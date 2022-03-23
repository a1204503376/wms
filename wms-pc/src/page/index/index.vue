<template>
    <div class="avue-contail" :class="{'avue--collapse':isCollapse}">
        <div class="avue-header">
            <!-- 顶部导航栏 -->
            <top ref="top"/>
        </div>
        <div class="avue-layout">
            <div class="avue-left">
                <!-- 左侧导航栏 -->
                <sidebar :isCollapse="isCollapse"/>
            </div>
            <div class="avue-main">
                <!-- 顶部标签卡 -->
                <tags/>
                <transition name="fade-scale">
                    <search class="avue-view" v-show="isSearch"></search>
                </transition>
                <!-- 主体视图层 -->
                <div style="overflow-y:auto;overflow-x:hidden;" id="avue-view" v-show="!isSearch">
                    <keep-alive>
                        <!--            <router-view class="avue-view" v-if="$route.meta.keepAlive"/>-->
                        <router-view class="avue-view"
                                     v-if="$route.meta.$keepAlive || $route.meta.$keepAlive === undefined"/>
                    </keep-alive>
                    <!--                    <router-view class="avue-view" v-if="!$route.meta.keepAlive"/>-->
                    <router-view class="avue-view"
                                 v-if="!$route.meta.$keepAlive && $route.meta.$keepAlive !== undefined"/>
                </div>
                <!--                <div class="copyright">Copyrights © 2021 北京节点通网络技术有限公司 All Rights Reserved. WMS v3.1</div>-->
            </div>
        </div>
        <div class="avue-shade" @click="showCollapse"></div>
    </div>
</template>

<script>
import {mapGetters} from "vuex";
import tags from "./tags";
import search from "./search";
import top from "./top/";
import sidebar from "./sidebar/";
import admin from "@/util/admin";
import {validatenull} from "@/util/validate";
import {calcDate} from "@/util/date.js";
import {getStore} from "@/util/store.js";

export default {
    components: {
        top,
        tags,
        search,
        sidebar
    },
    name: "index",
    provide() {
        return {
            index: this
        };
    },
    data() {
        return {
            //搜索控制
            isSearch: false,
            //刷新token锁
            refreshLock: false,
            //刷新token的时间
            refreshTime: "",
            arrKey: [],
            keydown: '',
        };
    },
    created() {
        //实时检测刷新token
        this.refreshToken();
        // 注册快捷键
        document.addEventListener('keydown', this.handleKeyDown)
        document.addEventListener('keyup', this.handleKeyUp)
    },
    destroyed() {
        document.removeEventListener('keydown', this.handleKeyDown)
        document.removeEventListener('keyup', this.handleKeyUp)
    },
    mounted() {
        this.init();
    },
    computed: mapGetters(["isMenu", "isLock", "isCollapse", "website", "menu"]),
    props: [],
    methods: {
        showCollapse() {
            this.$store.commit("SET_COLLAPSE");
        },
        // 初始化
        init() {
            this.$store.commit("SET_SCREEN", admin.getScreen());
            window.onresize = () => {
                setTimeout(() => {
                    this.$store.commit("SET_SCREEN", admin.getScreen());
                }, 0);
            };
            // this.$store.dispatch("FlowRoutes").then(() => {
            // });
        },
        //打开菜单
        openMenu(item = {}) {
            this.$store.dispatch("GetMenu", item.id).then(data => {
                if (data.length !== 0) {
                    this.$router.$avueRouter.formatRoutes(data, true);
                }
                //当点击顶部菜单后默认打开第一个菜单
                if (!this.validatenull(item)) {
                    let itemActive = {},
                        childItemActive = 0;
                    if (item.path) {
                        itemActive = item;
                    } else {
                        if (this.menu[childItemActive].length === 0) {
                            itemActive = this.menu[childItemActive];
                        } else {
                            itemActive = this.menu[childItemActive].children[childItemActive];
                        }
                    }
                    this.$store.commit('SET_MENU_ID', item);
                    this.$router.push({
                        path: this.$router.$avueRouter.getPath({
                            name: (itemActive.label || itemActive.name),
                            src: itemActive.path
                        }, itemActive.meta)
                    });
                }
            });
        },
        // 定时检测token
        refreshToken() {
            this.refreshTime = setInterval(() => {
                const token = getStore({
                    name: "token",
                    debug: true
                }) || {};
                const date = calcDate(token.datetime, new Date().getTime());
                if (validatenull(date)) return;
                if (date.seconds >= this.website.tokenTime && !this.refreshLock) {
                    this.refreshLock = true;
                    this.$store
                        .dispatch("refreshToken")
                        .then(() => {
                            this.refreshLock = false;
                        })
                        .catch(() => {
                            this.refreshLock = false;
                        });
                }
            }, 10000);
        },
        handleKeyDown(e) {
            let that = this;
            if (that.arrKey.length > 0) {
                // a-z的按键 长按去重
                if (that.arrKey.indexOf(e.key.toLowerCase()) >= 0) {
                    return
                }
            }
            that.arrKey.push(e.key.toLowerCase())
            this.keydown = that.arrKey.join('+')
            // 监听按键捕获
            if (this.keydown == 'enter') {
                if (that.forbidKeyboard) {
                    e.preventDefault()
                    return
                }
                this.keydown = ''
                //回车调用onload事件
                let crud = that.$pageList[that.$route.name];
                if (crud && crud.$refs.menu) {
                    crud.$refs.menu.onLoad();
                }
                e.preventDefault()//取消浏览器原有的操作
            }
        },
        handleKeyUp(e) {
            this.arrKey.splice(this.arrKey.indexOf(e.key.toLowerCase()), 1)
            this.keydown = this.arrKey.join('+')
            e.preventDefault()
        },
    }
};
</script>
<style>
.copyright {
    line-height: 24px;
    text-align: center;
}
</style>
