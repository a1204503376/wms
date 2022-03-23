<template>
    <el-drawer
        lock-scroll
        title="列显隐"
        :modal-append-to-body="false"
        :visible.sync="visible"
        :size="isMobile ? '100%' : '50%'"
        :before-close="beforeClose"
        v-loading="true"
        @open="onOpen"
        @opened="onOpened">
        <el-scrollbar style="height:calc(100% - 140px);">
            <el-table ref="table"
                      class="dialog-column-table"
                      size="small"
                      row-key="name"
                      border
                      v-loading="loading.content"
                      :key="Math.random()"
                      :data="list">
                <af-table-column
                    type="index"
                    align="center"
                    width="50">
                </af-table-column>
                <template v-for="col in this.columns">
                    <af-table-column align="center"
                                     :width="col.width"
                                     header-align="center"
                                     :prop="col.prop"
                                     :label="col.label">
                        <template slot-scope="scope">
                            <template v-if="col.type === 'input'">
                                <el-input
                                    :placeholder="!col.readonly ? '请输入' : ''"
                                    v-model="scope.row[col.prop]"
                                    clearable
                                    size="small"
                                    :maxlength="col.maxlength"
                                    :minlength="col.minlength"
                                    show-word-limit
                                >
                                </el-input>
                            </template>
                            <template v-else-if="col.type === 'checkbox'">
                                <el-checkbox v-model="scope.row[col.prop]"
                                             size="small"></el-checkbox>
                            </template>
                            <template v-else-if="col.type === 'slider'">
                                <el-slider :min="0"
                                           :max="2000"
                                           size="small"
                                           v-model="scope.row[col.prop]"></el-slider>
                            </template>
                            <template v-else>
                                {{ scope.row[col.prop] }}
                            </template>
                        </template>
                    </af-table-column>
                </template>
            </el-table>
        </el-scrollbar>
        <div class="dialog-column-footer">
            <el-button type="primary" size="small" @click="onSave" :loading="loading.saveBtn">保存</el-button>
            <el-button size="small" @click="beforeClose">关闭</el-button>
        </div>
    </el-drawer>
</template>

<script>

import {setStore, getStore} from '@/util/store';
import request from '@/router/axios';
import Sortable from 'sortablejs';

export default {
    name: "dialog-column",
    props: {
        visible: {type: Boolean, default: false},
        dataSource: {
            type: Array, default: function () {
                return [];
            }
        },
        saveUrl: {type: String, default: undefined}
    },
    computed: {
        isMobile() {
            return window.document.body.clientWidth <= 768;
        }
    },
    mounted() {
    },
    watch: {
        dataSource: {
            handler: function () {
                this.init();
            }
        },
    },
    data() {
        return {
            columns: [
                {
                    label: '列名',
                    prop: 'label',
                    type: 'text',
                    width: 100,
                },
                {
                    label: '别名',
                    prop: 'aliasName',
                    type: 'input',
                    width: 120
                },
                {
                    label: '隐藏',
                    prop: 'hide',
                    type: 'checkbox',
                    width: 50
                },
                {
                    label: '冻结',
                    prop: 'fixed',
                    type: 'checkbox',
                    width: 50
                },
                {
                    label: '宽度',
                    prop: 'width',
                    type: 'slider'
                },
            ],
            list: [],
            loading: {
                content: false,
                saveBtn: false
            },
            crudColumn: getStore({name: "crudColumn"}) || [],
            menuAll: getStore({name: 'menuAll'}) || [],
        }
    },
    methods: {
        rowDrop() {
            const el = document.querySelectorAll(
                ".dialog-column-table .el-table__body-wrapper > table > tbody"
            )[0];
            Sortable.create(el, {
                disabled: false, // 拖拽不可用? false 启用
                ghostClass: 'sortable-ghost', //拖拽样式
                animation: 150, // 拖拽延时，效果更好看
                onEnd: (e) => { // 拖拽结束时的触发
                    let arr = this.list; // 获取表数据
                    // 数据处理，获取最新的表格数据
                    arr.splice(e.newIndex, 0, arr.splice(e.oldIndex, 1)[0]);
                    arr.forEach((value, index)=>{
                        value.order = index;
                    });
                    this.$nextTick(function () {
                        this.list = arr;
                        this.rowDrop();
                    })
                },
            });
        },
        init() {
            this.list = [];
            if (this.dataSource) {
                this.dataSource.forEach(item => {
                    let row = {
                        prop: item.prop,
                        label: item.label,
                        aliasName: item.aliasName,
                        hide: item.hide,
                        fixed: item.fixed,
                        width: item.width,
                        order: this.dataSource.indexOf(item)
                    }
                    this.list.push(row);
                });
            }
        },
        getMenu(menuList, route) {
            return menuList.find(u => {
                if (u.name == route.name && u.path == route.path) {
                    return u;
                } else if (u.children && u.children.length > 0) {
                    return this.getMenu(u.children, route);
                }
            });
        },
        onOpen() {
            this.init();
        },
        onOpened() {
            this.rowDrop();
        },
        onSave() {
            let menu = this.getMenu(this.menuAll, this.$route);
            if (!menu) {
                this.$message.error('获取功能菜单失败! ');
                return;
            }
            this.loading.content = true;
            this.loading.saveBtn = true;
            let columnList = Object.assign([], this.list);
            columnList.sort((a, b)=>{
                let x = a['order'];
                let y = b['order'];
                return ((x < y) ? -1 : (x > y) ? 1 : 0);
            });
            // 开始封装数据
            let column = {
                menuId: menu.id,
                menuName: menu.name,
                columnList: columnList
            };
            let loading_count = 0;
            if (this.saveUrl) {
                loading_count++;
                request({
                    url: this.saveUrl,
                    method: 'post',
                    data: column
                }).then(res => {
                }).finally(() => {
                    loading_count--;
                })
            }
            let self = this;
            let interval = setInterval(function () {
                if (loading_count !== 0) {
                    return;
                }
                clearInterval(interval);
                let index = self.crudColumn.findIndex(u => {
                    return u.menuId == menu.id;
                });
                if (index < 0) {
                    self.crudColumn.push(column);
                } else {
                    self.crudColumn.splice(index, 1, column);
                }
                setStore({name: 'crudColumn', content: self.crudColumn, type:'session'});
                self.loading.content = false;
                self.loading.saveBtn = false;
                self.beforeClose();
            });
        },
        beforeClose() {
            this.$emit('close');
        }
    }
}
</script>

<style>
.sortable-ghost {
    opacity: 0.4;
    background-color: steelblue;
}

.dialog-column-table .el-table__row {
    cursor: move;
}

.dialog-column-footer {
    margin: 10px auto;
    text-align: center;
}
</style>
