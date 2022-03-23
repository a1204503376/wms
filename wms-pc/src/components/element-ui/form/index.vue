<template>
    <basic-container>
        <el-container v-bind:style="{height:formHeight + 'px',screenHeight:screenHeight,}">
            <!-- 内容不够丰富 ，先隐藏起来 v-if="group && group.length > 0 && group[0].label"  -->
            <el-aside v-if="false" width="100px">
                <form-tab :tabs="group"></form-tab>
            </el-aside>
            <el-container>
                <el-main style="overflow: hidden;overflow-y: scroll;">
                    <el-form ref="form"
                             label-position="right"
                             label-width="120px"
                             size="medium"
                             :model="form"
                             style="margin-left:10px;margin-right:10px;"
                    >
                        <div id="form-content" v-loading="loading.content">
                            <!-- 循环分组 -->
                            <template v-for="item in group">
                                <el-row v-if="item.label && getHide(item)">
                                    <h3>{{ item.label }}</h3>
                                </el-row>
                                <form-item :ref="'item_' + item.label" :item="item"></form-item>
                            </template>
                        </div>
                    </el-form>
                </el-main>
                <el-footer height="60px">
                    <form-footer ref="footer"></form-footer>
                </el-footer>
            </el-container>
        </el-container>
    </basic-container>
</template>

<script>

import FormTab from "./form-tab";
import FormFooter from "./form-footer";
import FormItem from "./form-item";
import request from '@/router/axios';

export default {
    name: "index",
    components: {FormItem, FormFooter, FormTab},
    provide() {
        return {
            form: this,
        };
    },
    model: {
        prop: "form",
        event: "change",
    },
    props: {},
    watch: {
        $route: {
            handler(nv, ov) {
                // 两个二级页面来回切换数据会丢失；需要刷新参数重新初始化
                this.refreshParams();
                if (nv.name == 'form') {
                    this.init();
                }
            }
        },
        form: {
            handler(newValue, oldValue) {
            }
        },
        screenHeight(val) {
            this.screenHeight = val
            this.formHeight = this.screenHeight - 150
        }
    },
    data() {
        return {
            title: '',
            loading: {
                content: false,
                button: false
            },
            formHeight: 500,
            screenHeight: window.innerHeight,
            // 分组
            group: [],
            // 表单数据
            form: {},
            columns: [],
            type: '',
            parent: {},
            params: {},
        }
    },
    mounted() {
        this.refreshParams();
        this.init();
        const that = this;
        window.onresize = () => {
            return (() => {
                window.screenHeight = window.innerHeight
                that.screenHeight = window.screenHeight;
            })()
        }
    },
    activated() {
        this.refreshParams();
        // 缓存丢失后（刷新浏览器）后，关闭此页面
        if (this.validatenull(this.params[this.title])) {
            this.close();
        }
        // this.init();
    },
    methods: {
        /**
         * 刷新参数
         */
        refreshParams() {
            this.$set(this, 'title', this.$route.query.title);
            this.$pageList[this.title] = this;
            if (this.validatenull(this.$route.params)) {
                this.params[this.title] = this.$pageList[this.title].params[this.title];
            } else {
                this.params[this.title] = this.$route.params;
            }
            let param = this.params[this.title];
            if (this.validatenull(param)) {
                return;
            }
            this.$set(this, 'group', param.group);
            if (param.type == '新增') {
                this.$set(this, 'form', param.form);
                this.$set(this, 'type', 'new');
            } else if (param.type == '编辑') {
                this.$set(this, 'form', param.form);
                this.$set(this, 'type', 'edit');
            } else {
                this.$set(this, 'form', param.form);
                this.$set(this, 'type', 'view');
            }
            this.$set(this, 'parent', param.parent);
        },
        /**
         * 初始化表单
         */
        init() {
            if (this.initing) {
                return;
            }
            this.initing = true;
            this.formHeight = document.documentElement.clientHeight - this.$refs.form.$el.offsetTop - 70;
            this.beforeLoading();
            // 初始化
            let loadingCount = 0;
            if (this.group) {
                for (let i = 0; i < this.group.length; i++) {
                    let group = this.group[i];
                    for (let j = 0; j < group.column.length; j++) {
                        let col = group.column[j];
                        if (!col || col.type === 'button') {
                            continue;
                        }
                        this.columns.push(col);
                        if (!col['dicData']) {
                            this.$set(col, 'dicData', []);
                        } else {
                            this.$set(col, 'static', true);
                        }
                        if (col.type == 'dynamic') {
                            let indexCol = false;
                            let viewBtn = false;
                            let editBtn = col.editBtn == true;
                            let delBtn = (col.delBtn === undefined || col.delBtn);
                            let addBtn = col.addBtn;
                            let menu = viewBtn || editBtn || delBtn || (col.menuBtn != undefined && col.menuBtn.length > 0);
                            let moveBtn = col.moveBtn;
                            let copyBtn = col.copyBtn;
                            let option = {
                                name: group.label || col.name,
                                align: "center",
                                index: indexCol,
                                indexLabel: "顺序",
                                select: true,
                                operationWidth: 120,
                                addBtn: addBtn,
                                moveBtn: moveBtn,
                                copyBtn: copyBtn,
                                viewBtn: viewBtn,
                                editBtn: editBtn,
                                delBtn: delBtn,
                                menu: menu,
                                saveUrl: col.saveUrl,
                                del: col.del,
                                rowAdd: col.rowAdd,
                                rowSaved: col.rowSaved,
                                column: col.children,
                                children: col.column,
                                prop: col.prop,
                                page: col.page === undefined ? true : col.page,
                                selection: col.selection,
                                menuBtn: col.menuBtn,
                                beforeAdd: col.beforeAdd,
                                edit: col.edit,
                                onDeleted: col.delete
                            };
                            this.$set(col, '$option', option);
                        }
                        // 调用接口，获取数据源
                        if (!col.dicUrl) {
                            continue;
                        }
                        loadingCount++;
                        // 拼接接口参数
                        let requestUrl = col.dicUrl;
                        if (col.search && this.form) {
                            requestUrl += '?';
                            let params = [];
                            for (let key in col.search) {
                                if (this.form[key]) {
                                    params.push(key + '=' + this.form[key]);
                                }
                            }
                            requestUrl += params.join('&');
                        }
                        // 调用接口
                        request({
                            url: requestUrl,
                            method: 'get'
                        }).then(res => {
                            this.$set(col, "dicData", res.data.data);
                        }).finally(() => {
                            loadingCount--;
                        });
                    }
                }
            }
            let self = this;
            let interval = setInterval(function () {
                if (loadingCount === 0 && self.loading.content) {
                    if (self.parent) {
                        let vue = self.$pageList[self.parent.name];
                        if (self.type === 'new') {
                            for (let i = 0; i < self.group.length; i++) {
                                let group = self.group[i];
                                for (let j = 0; j < group.column.length; j++) {
                                    let col = group.column[j];
                                    if (!col || col.type == 'button') {
                                        continue;
                                    }
                                    if (col.type !== 'dynamic') {
                                        // 有设置默认值，并且不为新增的情况下
                                        if (col.default !== undefined) {
                                            let defaultValue = null;
                                            if (typeof col.default === 'function') {
                                                defaultValue = col.default(self.form, self.type);
                                            } else {
                                                defaultValue = col.default;
                                            }
                                            if (col.prop) {
                                                self.$set(self.form, col.prop, defaultValue);
                                                // 设置默认值后，触发 change 事件
                                                if (self.$refs[col.prop]) {
                                                    self.$refs[col.prop].selectChange(defaultValue, null, col);
                                                }
                                            }
                                        } else {
                                            if (col.type === 'select' && col.multiple) {
                                                self.$set(self.form, col.prop, self.form[col.prop] ? self.form[col.prop] : []);
                                            } else if (col.type === 'inputNumber') {
                                                self.$set(self.form, col.prop, self.form[col.prop] ? self.form[col.prop] : 0);
                                            } else {
                                                self.$set(self.form, col.prop, self.form[col.prop] ? self.form[col.prop] : undefined);
                                            }
                                        }
                                    } else if (!self.form[col.prop]) {
                                        self.$set(self.form, col.prop, []);
                                    }
                                    if (col.defaultIndex !== undefined && col.dicData && col.dicData.length > 0) {
                                        // 设置默认值
                                        let defaultObj = col.dicData[col.defaultIndex];
                                        self.$set(self.form, col.prop, defaultObj[col.props.value]);
                                        if (self.$refs[col.prop]) {
                                            self.$refs[col.prop].selectChange(defaultObj[col.props.value], defaultObj, col);
                                        }
                                    }
                                }
                            }
                            if (vue.beforeOpen) {
                                vue.beforeOpen(self.finishLoading, 'new', self.dialogClose);
                            }
                        }
                        // else {
                        //     if (vue.beforeOpen) {
                        //         vue.beforeOpen(self.finishLoading, self.type, self.dialogClose);
                        //     }
                        // 循环所有 column, 存在子级的情况下，触发 change 事件
                        if (self.columns) {
                            self.columns.forEach(col => {
                                if (col.cascaderItem && self.$refs[col.prop]) {
                                    self.$refs[col.prop].selectChange(self.form[col.prop], null, col, false);
                                }
                            });
                        }
                        // }
                    }
                    self.finishLoading();
                    self.$refs.form.clearValidate();
                    self.$forceUpdate();
                    self.initing = false;
                    clearInterval(interval);
                }
            }, 100);
        },
        /**
         * 获取列的显隐状态
         * @param col
         * @returns {boolean|*}
         */
        getHide(col) {
            if (col.hide !== undefined) {
                if (typeof col.hide === 'function') {
                    return col.hide(this.form, this.type);
                } else {
                    return !col.hide;
                }
            } else {
                return true;
            }
        },
        /**
         * 开始加载状态
         * @returns {{button: boolean, content: boolean}}
         */
        beforeLoading() {
            this.loading.save = true;
            this.loading.content = true;
            return this.loading;
        },
        /**
         * 结束加载界面
         * @param type
         * @returns {{button: boolean, content: boolean}}
         */
        finishLoading() {
            this.loading.save = false;
            this.loading.content = false;
            if (this.type !== 'new') {
                // this.form = Object.assign({}, this.dataSource);
            }
            return this.loading;
        },
        /**
         * 保存
         */
        save() {
            this.$refs.form.validate(valid => {
                // 验证子级（如果含有子表格的情况下）
                let valid_count = 0;
                let item_result = [];
                for (let group_name in this.$refs) {
                    let group = this.$refs[group_name][0];
                    if (!group) {
                        continue;
                    }
                    for (let item_name in group.$refs) {
                        if (!item_name || item_name.indexOf('dynamic') < 0) {
                            continue;
                        }
                        let dynamic = group.$refs[item_name][0];
                        if (!dynamic) {
                            continue;
                        }
                        valid_count++;
                        dynamic.validate((dynamic_valid) => {
                            item_result.push(dynamic_valid);
                            valid_count--;
                        });
                        if (item_result.indexOf(false) > -1) {
                            break;
                        }
                    }
                }
                let self = this;
                let interval = setInterval(()=>{
                    if (valid_count == 0) {
                        if (item_result && item_result.indexOf(false) > -1) {
                            return false;
                        }
                        let vue = self.$pageList[self.parent.name];
                        if (valid && vue) {
                            if (self.loading.save) {
                                return false;
                            }
                            self.beforeLoading();
                            const finish = function () {
                                self.finishLoading();
                                // self.clearDataDic();
                                delete self.$pageList[self.title];
                                self.$refs.form.clearValidate();
                                self.form = {};
                                self.$router.$avueRouter.closeTag();
                                self.$router.push({
                                    path: self.parent.path
                                });
                            };

                            vue.$emit(
                                "row-save",
                                self.form,
                                finish,
                                () => {
                                    self.finishLoading();
                                },
                                this.type
                            );
                        }
                        clearInterval(interval);
                    }
                }, 100);

            });
        },
        close() {
            delete this.$pageList[this.title];
            this.form = {};
            this.$router.$avueRouter.closeTag(this.$route.path + '?title=' + encodeURI(this.title));
            this.$router.back();
        },
        /**
         * 清空数据源
         */
        clearDataDic() {
            if (this.columns) {
                this.columns.forEach(col => {
                    if (col.static) {
                        return;
                    }
                    col.dicData = [];
                });
            }
        },
    }
}
</script>

<style scoped>

.form-body .form-content {
    position: relative;
    padding-right: 10px;
    font-size: 18px;
    max-height: 500px;
}

</style>
