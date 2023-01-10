<template>
    <el-drawer
        :before-close="beforeClose"
        :modal-append-to-body="false"
        :size="isMobile ? '100%' : '50%'"
        :visible.sync="visible"
        @open="onOpen"
        @opened="onOpened"
        lock-scroll
        title="列显隐"
        v-loading="loading.content">
        <el-scrollbar style="height:calc(100% - 140px);">
            <el-table :data="list"
                      :key="Math.random()"
                      border
                      class="dialog-column-table"
                      id="editRankingList"
                      ref="table"
                      row-key="name"
                      size="small"
                      v-loading="loading.content">
                <af-table-column
                    align="center"
                    type="index"
                    width="50">
                </af-table-column>
                <template v-for="col in this.columns">
                    <af-table-column :label="col.label"
                                     :prop="col.prop"
                                     :width="col.width"
                                     align="center"
                                     header-align="center">
                        <template v-slot="{row}">
                            <template v-if="col.type === 'input'">
                                <el-input
                                    :maxlength="col.maxlength"
                                    :minlength="col.minlength"
                                    :placeholder="!col.readonly ? '请输入' : ''"
                                    clearable
                                    show-word-limit
                                    size="small"
                                    v-model="row[col.prop]"
                                >
                                </el-input>
                            </template>
                            <template v-else-if="col.type === 'checkbox'">
                                <el-checkbox size="small"
                                             v-model="row[col.prop]"></el-checkbox>
                            </template>
                            <template v-else-if="col.type === 'inputNumber'">
                                <el-input-number :max="2000"
                                                 :min="1"
                                                 controls-position="right"
                                                 size="small"
                                                 v-model="row[col.prop]">
                                </el-input-number>
                            </template>
                            <template v-else-if="col.type === 'select'">
                                <el-select size="small"
                                           v-model="row[col.prop]">
                                    <el-option label="居左" value="left"></el-option>
                                    <el-option label="居中" value="center"></el-option>
                                    <el-option label="居右" value="right"></el-option>
                                </el-select>
                            </template>
                            <template v-else>
                                {{ row[col.prop] }}
                            </template>
                        </template>
                    </af-table-column>
                </template>
            </el-table>
        </el-scrollbar>
        <div class="dialog-column-footer">
            <el-button :loading="loading.saveBtn" @click="onSave" size="small" type="primary">保存</el-button>
            <el-button @click="beforeClose('')" size="small">关闭</el-button>
        </div>
    </el-drawer>
</template>

<script>

    import {getStore, setStore} from '@/util/store';
    import {submit} from "@/api/core/column";
    import {menuMixin} from "@/mixins/menu";
    import func from "@/util/func";
    import {deepClone} from "@/util/util";
    import sortable from "sortablejs";
    import {listMixin} from "@/mixins/list";

    export default {
        name: "dialog-column",
        mixins: [menuMixin,listMixin],
        props: {
            visible: {type: Boolean, default: false},
            dataSource: {
                type: Array,
                default: function () {
                    return [];
                }
            }
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
                },

            },
        },
        data() {
            return {
                columns: [
                    {
                        label: '列名',
                        prop: 'label',
                        type: 'text',
                        width: 150,
                    },
                    {
                        label: '别名',
                        prop: 'aliasName',
                        type: 'input',
                        width: 150
                    },
                    {
                        label: '隐藏',
                        prop: 'hide',
                        type: 'checkbox',
                        width: 80
                    },
                    {
                        label: '冻结',
                        prop: 'fixed',
                        type: 'checkbox',
                        width: 80
                    },
                    {
                        label: '对齐方式',
                        prop: 'align',
                        type: 'select',
                        width: 100
                    },
                    {
                        label: '宽度(px)',
                        prop: 'width',
                        type: 'inputNumber'
                    }
                ],
                list: [],
                loading: {
                    content: false,
                    saveBtn: false
                },
                crudColumn: getStore({name: "crudColumn"}) || []
            }
        },
        created() {
            this.list = [];
            this.init();
            console.log(this.dataSource);
            this.$nextTick(function () {
                this.rowDrop();
            });
        },
        destroyed() {
            this.$parent.handleRefreshTable();
            // this.$emit("handleRefreshTable");
        },
        methods: {
            rowDrop() {
                const el = document.querySelectorAll(
                    ".dialog-column-table .el-table__body-wrapper > table > tbody"
                )[0];

                sortable.create(el, {
                    disabled: false, // 拖拽不可用? false 启用
                    ghostClass: 'sortable-ghost', //拖拽样式
                    animation: 150, // 拖拽延时，效果更好看
                    onEnd: (e) => { // 拖拽结束时的触发
                        let arr = this.list; // 获取表数据
                        // 数据处理，获取最新的表格数据
                        arr.splice(e.newIndex, 0, arr.splice(e.oldIndex, 1)[0]);
                        arr.forEach((value, index) => {
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
                let tempData = func.isNotEmpty(this.dataSource)
                    ? this.dataSource
                    : (
                        func.isFunction(this.$parent['getColumnDataSource'])
                            ? this.$parent['getColumnDataSource']()
                            : []
                    );
                if (tempData) {
                    tempData.forEach(item => {
                        // "0":是,"1":否
                        let row = {
                            prop: item.prop,
                            label: item.label,
                            aliasName: func.strDefaultEmpty(item.aliasName),
                            hide: func.toBoolean(item.hide),
                            fixed: func.toBoolean(item.fixed),
                            width: func.toInt(item.width, 150),
                            align: func.strDefault(item.align, 'left'),
                            order: func.toInt(item.order, this.dataSource.indexOf(item))
                        }
                        this.list.push(row);
                    });

                }
            },
            onOpen() {
                this.init();
            },
            onOpened() {
                this.rowDrop();
            },
            onSave() {
                let that = this;
                let menus = this.getMenu();
                let menu = {};
                menus.children.forEach(function (item, index) {
                    if(item.path == that.$route.path){
                        menu = item;
                    }
                });
                if (!menu) {
                    this.$message.error('获取功能菜单失败! ');
                    return;
                }
                this.loading.content = true;
                this.loading.saveBtn = true;
                let columnList = deepClone(this.list);
                columnList.sort((a, b) => {
                    let x = a['order'];
                    let y = b['order'];
                    return ((x < y) ? -1 : (x > y) ? 1 : 0);
                });
                // 开始封装数据
                columnList.forEach(d => {
                    d.menuId = menu.id
                });
                // 缓存本地的数据格式
                let column = {
                    menuId: menu.id,
                    columnList: columnList
                }
                let loadingCount = 0;

                loadingCount++;
                submit(columnList).then(() => {
                }).finally(() => {
                    loadingCount--;
                })

                let self = this;
                let interval = setInterval(function () {
                    if (loadingCount !== 0) {
                        return;
                    }

                    let index = self.crudColumn.findIndex(u => {
                        return u.menuId === menu.id;
                    });
                    if (index < 0) {
                        self.crudColumn.push(column);
                    } else {
                        self.crudColumn.splice(index, 1, column);
                    }
                    // setStore({name: 'crudColumn', content: self.crudColumn})
                    setStore({name: 'crudColumn', content: self.crudColumn, type: 'session'});
                    self.loading.content = false;
                    self.loading.saveBtn = false;
                    self.beforeClose(column);
                    clearInterval(interval);
                });
            },
            beforeClose(column) {
                this.$emit('close', column);
                // location.reload();
            },
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
