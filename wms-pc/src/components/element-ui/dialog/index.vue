<template>
    <el-dialog :title="title"
               :visible.sync="visible"
               :close-on-click-modal="false"
               :before-close="dialogClose"
               :destroy-on-close="true"
               v-dialogDrag
               class="dialogs"
               width="80%"
               top="5vh"
               append-to-body>
        <div class="dialog-body">
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
                                    <dialog-item :ref="'item_' + item.label" :item="item"></dialog-item>
                                </template>
                            </div>
                        </el-form>
                    </el-main>
                </el-container>
            </el-container>
        </div>
        <div slot="footer" class="dialog-footer">
            <form-footer ref="footer"></form-footer>
        </div>
    </el-dialog>
</template>

<script>
import request from '@/router/axios';
import formTab from "../form/form-tab";
import formFooter from "../form/form-footer";

import {mapState} from 'vuex';

export default {
    name: "nodes-dialog",
    provide() {
        return {
            form: this,
        };
    },
    props: {
        // 界面显示字段（group为 undefined时使用)
        column: {
            type: Array, default: function () {
                return []
            }
        },
        // 保存按钮显隐
        saveBtn: {type: Boolean, default: true},
        // 父级对象
        owner: {type: Object, default: null},
        root: {type: Object, default: null},
    },
    components: {
        dialogItem: () => import("../form/form-item"),
        formFooter,
        formTab
    },
    created() {

    },
    mounted() {
    },
    watch: {
        // form: {
        //     handler(newValue, oldValue) {
        //         this.$emit('change', newValue);
        //     },
        //     deep: true,
        //     immediate: true
        // },
        screenHeight(val) {
            this.screenHeight = val
            this.formHeight = this.screenHeight - 150
        }
    },
    data() {
        return {
            title: '对话框',
            formHeight: 500,
            screenHeight: window.innerHeight,
            visible: false,
            group: [],
            loading: {
                content: false,
                save: false
            },
            form: {},
            type:'new',
            index:-1,



            prop: 'content',
            // 所有列（所有分组下的所有列）
            columns: [],
            callback: {
                visible: false,
                success: false,
                data: null,
                type:'new',
                loading: function () {
                }
            },
            editType: undefined
        }
    },
    methods: {
        open(group, form, type, title, index) {
            this.formHeight = document.documentElement.clientHeight - 300;
            this.group = group;
            this.form = form;
            this.type = type;
            let type_str = '新增';
            if (type == 'edit') {
                type_str = '编辑';
            } else if (type == 'copy') {
                type_str = '复制';
            }
            this.title = title + '-' + type_str;
            this.index = index;
            this.visible = true;
            this.init();
        },
        init() {
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
                            let editBtn = col.editBtn === true;
                            let delBtn = (col.delBtn === undefined || col.delBtn);
                            let addBtn = col.addBtn;
                            let menu = editBtn || delBtn || (col.menuBtn != undefined && col.menuBtn.length > 0);
                            let moveBtn = col.moveBtn;
                            let copyBtn = col.copyBtn;
                            let option = {
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
                    }
                    // 循环所有 column, 存在子级的情况下，触发 change 事件
                    if (self.columns) {
                        self.columns.forEach(col => {
                            if (col.cascaderItem && self.$refs[col.prop]) {
                                self.$refs[col.prop].selectChange(self.form[col.prop], null, col, false);
                            }
                        });
                    }
                    self.finishLoading();
                    self.$refs.form.clearValidate();
                    self.$forceUpdate();
                    clearInterval(interval);
                }
            }, 100);
        },
        // 对话框关闭
        dialogClose() {
            let hasData = false;
            for (let key in this.form) {
                if (this.form[key] && this.form[key].length > 0) {
                    hasData = true;
                    break;
                }
            }
            if (hasData && this.editType == 'new' && false) {
                this.$confirm("是否确认放弃本页面?", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.close();
                }).catch(() => {
                });
            } else {
                this.close();
            }
        },
        close() {
            this.$refs.form.clearValidate();
            this.callback.visible = false;
            this.callback.success = false;
            this.callback.data = null;
            this.$emit('callback', this.callback);
            this.visible = false;
        },
        // 保存
        save() {
            this.$refs.form.validate(valid => {
                // 验证子级（如果含有子表格的情况下）
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
                        dynamic.validate((dynamic_valid)=>{
                            item_result.push(dynamic_valid);
                        });
                        if (item_result.indexOf(false) > -1) {
                            break;
                        }
                    }
                }
                if (item_result && item_result.indexOf(false) > -1) {
                    return false;
                }
                if (valid) {
                    if (this.loading.save) {
                        return false;
                    }
                    this.beforeLoading();
                    this.callback.visible = false;
                    this.callback.success = true;
                    this.callback.type = this.type;
                    this.callback.data = this.form;
                    this.callback.index = this.index;
                    let self = this;
                    this.callback.loading = function () {
                        self.finishLoading();
                    };
                    this.$refs.form.clearValidate();
                    this.$emit('callback', this.callback);
                    this.visible = false;
                } else {
                    return false;
                }
            });
        },
        // 获取禁用状态
        getDisabled(col) {
            if (this.isView) {
                return true;
            }

            if (col.disabled !== undefined) {
                if (typeof col.disabled === 'function') {
                    return col.disabled(this.form, this);
                } else {
                    return col.disabled;
                }
            } else {
                return false;
            }
        },
        // 获取是否显示
        getHide(col) {
            if (col.hide !== undefined) {
                if (typeof col.hide === 'function') {
                    return col.hide(this.form, this.editType);
                } else {
                    return !col.hide;
                }
            } else {
                return true;
            }
        },
        // 启动加载界面
        beforeLoading() {
            this.loading.save = true;
            this.loading.content = true;
            return this.loading;
        },
        // 结束加载界面
        finishLoading(type) {
            this.loading.save = false;
            this.loading.content = false;
            return this.loading;
        },
    }
}
</script>

<style lang="scss" scope>
</style>

