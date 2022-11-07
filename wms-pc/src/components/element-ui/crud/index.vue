<template>
    <div>
        <!-- 头部菜单 -->
        <header-menu ref="headerMenu">
            <template slot="menuLeft"
                      slot-scope="scope">
                <slot name="menuLeft" v-bind="scope"></slot>
            </template>
            <template slot="menuRight"
                      slot-scope="scope">
                <slot name="menuRight" v-bind="scope"></slot>
            </template>
        </header-menu>
        <slot name="header"></slot>
        <el-table
            v-if="reload"
            ref="table"
            v-loading="tableLoading"
            :border="tableOption.border !== false"
            :data="data"
            :height="tableHeight"
            :load="loadList"
            :max-height="isAutoHeight ? tableHeight : tableOption.maxHeight"
            :row-key="option.rowKey"
            :screenHeight="screenHeight"
            :size="config.controlSize"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            lazy
            @selection-change="selectionChange"
            @cell-click="onCellClick"
            @cell-dblclick="onCellDblClick"
            @header-dragend="onHeaderDragend"
        >
            <!-- 暂无数据提醒 -->
            <template slot="empty">
                <div class="nodes-crud__empty">
                    <slot v-if="$slots.empty"
                          name="empty"></slot>
                    <avue-empty v-else
                                :desc="tableOption.emptyText || '暂无数据'"
                                image="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNDEiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiAgPGcgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMCAxKSIgZmlsbD0ibm9uZSIgZmlsbC1ydWxlPSJldmVub2RkIj4KICAgIDxlbGxpcHNlIGZpbGw9IiNGNUY1RjUiIGN4PSIzMiIgY3k9IjMzIiByeD0iMzIiIHJ5PSI3Ii8+CiAgICA8ZyBmaWxsLXJ1bGU9Im5vbnplcm8iIHN0cm9rZT0iI0Q5RDlEOSI+CiAgICAgIDxwYXRoIGQ9Ik01NSAxMi43Nkw0NC44NTQgMS4yNThDNDQuMzY3LjQ3NCA0My42NTYgMCA0Mi45MDcgMEgyMS4wOTNjLS43NDkgMC0xLjQ2LjQ3NC0xLjk0NyAxLjI1N0w5IDEyLjc2MVYyMmg0NnYtOS4yNHoiLz4KICAgICAgPHBhdGggZD0iTTQxLjYxMyAxNS45MzFjMC0xLjYwNS45OTQtMi45MyAyLjIyNy0yLjkzMUg1NXYxOC4xMzdDNTUgMzMuMjYgNTMuNjggMzUgNTIuMDUgMzVoLTQwLjFDMTAuMzIgMzUgOSAzMy4yNTkgOSAzMS4xMzdWMTNoMTEuMTZjMS4yMzMgMCAyLjIyNyAxLjMyMyAyLjIyNyAyLjkyOHYuMDIyYzAgMS42MDUgMS4wMDUgMi45MDEgMi4yMzcgMi45MDFoMTQuNzUyYzEuMjMyIDAgMi4yMzctMS4zMDggMi4yMzctMi45MTN2LS4wMDd6IiBmaWxsPSIjRkFGQUZBIi8+CiAgICA8L2c+CiAgPC9nPgo8L3N2Zz4K"
                                size="50"></avue-empty>
                </div>
            </template>
            <column ref="column"
                    :columnOption="option.column"
                    :tableOption="tableOption">
                <template v-for="item in option.column"
                          :slot="item.prop"
                          slot-scope="scope">
                    <slot :name="item.prop" v-bind="scope"></slot>
                </template>
                <column-default ref="columnDefault"
                                slot="header"
                                :tableOption="tableOption">
                    <template slot="expand"
                              slot-scope="{row, index}">
                        <slot :index="index"
                              :row="row"
                              name="expand"></slot>
                    </template>
                </column-default>
                <column-menu ref="menu"
                             slot="footer"
                             :tableOption="tableOption">
                    <template slot="menu"
                              slot-scope="scope">
                        <slot name="menu"
                              v-bind="scope"></slot>
                    </template>
                    <template slot="menuBtn"
                              slot-scope="scope">
                        <slot name="menuBtn"
                              v-bind="scope"></slot>
                    </template>
                </column-menu>
            </column>
        </el-table>
        <slot name="footer"></slot>
        <!-- 分页 -->
        <table-page ref="tablePage">
            <template slot="page">
                <slot name="page"></slot>
            </template>
        </table-page>
        <!-- 列显隐 -->
        <div v-if="dialogColumn.visible">
            <dialog-column ref="dialogColumn"
                           :data-source="dialogColumn.list"
                           :visible="dialogColumn.visible"
                           @close="onDialogColumnClose">
            </dialog-column>
        </div>
        <!-- 二级弹框 -->
        <editDialog
            ref="dialogEdit"
            v-model="form"
            :dataSource="dialogEdit.dataSource"
            :group="dialogEdit.group"
            :groupButton="option.groupButton"
            :isEdit="dialogEdit.isEdit"
            :isNew="dialogEdit.isNew"
            :isView="dialogEdit.isView"
            :root="$parent"
            :title="dialogEdit.title"
            :visible="dialogEdit.visible"
            :width="dialogEdit.width"
            @callback="callbackEdit"
            @change="dialogEditChange"
            @before-open="beforeOpen"
        ></editDialog>
        <!-- 查看 -->
        <dialog-view ref="dialogView"></dialog-view>
        <!-- 对话框 -->
        <nodes-dialog
            ref="nodesDialog"
            :dataSource="dialogEdit.dataSource"
            :group="dialogEdit.group"
            :isEdit="dialogEdit.isEdit"
            :isNew="dialogEdit.isNew"
            :isView="dialogEdit.isView"
            :owner="form"
            :width="dialogEdit.width"
            @callback="callbackEdit"
            @before-open="beforeOpen"
        ></nodes-dialog>
    </div>
</template>

<script>
import headerMenu from "./header-menu";
import tablePage from "./table-page";
import init from "./init";
import config from "./config";
import column from "./column";
import columnDefault from "./column-default";
import columnMenu from "./column-menu";
import dialogColumn from "./dialog-column";
import editDialog from "@/components/nodes/editDialog";
import {getStore} from '@/util/store';
import selectTableUser from "../select/select-table-user";
import selectTree from "../select/select-tree";
import dialogView from "./dialog-view";
import nodesDialog from "../dialog/index"
import func from "@/util/func";

export default {
    name: "nodes-crud",
    mixins: [init()],
    components: {
        dialogView,
        headerMenu,     // 头部菜单
        tablePage,      // 分页
        column,
        columnDefault,
        columnMenu,
        dialogColumn,
        editDialog,
        selectTableUser,
        selectTree,
        nodesDialog,
    },
    provide() {
        return {
            crud: this
        };
    },
    computed: {
        isAutoHeight() {
            // return this.tableOption.height === "auto"
            return true;
        },
        columnOption() {
            return this.option.column || [];
        },
        propOption() {
            return this.tableOption.column || []
        },
        mainSlot() {
            return this.propOption.filter(ele => ele.slot);
        },
    },
    model: {
        prop: "form",
        event: "form-change",
    },
    watch: {
        data() {
            this.$nextTick(() => {
                this.doLayout();
            });
        },
        form: {
            handler: function (newValue, oldValue) {
                this.dialogEdit.dataSource = newValue;
            },
            deep: true,
            immediate: true,
        },
        'tableOption.column': {
            handler: function (newValue, oldValue) {
                if (newValue == oldValue) {
                    return;
                }
                this.tableOption.column.forEach(item => {
                    this.$set(item, '$search', undefined);
                });
            },
            deep: true,
            immediate: true
        },
        screenHeight(val) {
            this.screenHeight = val
            this.tableHeight = this.screenHeight - 250
        }
    },
    created() {
        this.$pageList[this.$route.name] = this;
        // 初始化组件
        let crudColumn = getStore({name: "crudColumn"}) || [];
        let menuAll = getStore({name: 'menuAll'}) || [];
        let menu = this.getMenu(menuAll, this.$route);
        if (!menu) {
            return;
        }
        let column = crudColumn.find(u => {
            return u.menuId = menu.id;
        });
        if (!column || !column.columnList) {
            return;
        }
        column.columnList.forEach(config => {
            this.option.column.forEach(col => {
                if (col.prop !== config.prop) {
                    return;
                }
                col.aliasName = config.aliasName;
                col.hide = config.hide;
                col.fixed = config.fixed;
                col.width = config.width == 0 ? undefined : config.width;
                col.order = config.order;
            })
        });
        this.dialogColumn.list = Object.assign(
            [],
            this.option.column
        );
    },
    mounted() {
        this.refreshTable(() => {
            this.page = this.$refs.tablePage.defaultPage;
            this.title = this.$route.name;
            // 当不建立分组的情况下，默认一个空分组，字段为column
            if (this.option.group && this.option.group.length > 0) {
                if (this.option.group[0].label) {
                    this.dialogEdit.group = Object.assign([], this.option.group);
                    this.dialogEdit.width = "80%";
                } else {
                    let columns = Object.assign([], this.option.group[0].column);
                    if (columns) {
                        // 字段数量大于等于 6 个，设置二级界面宽度为 60%，每行显示 2 个字段
                        if (columns && columns.length > 6) {
                            this.dialogEdit.width = "60%";
                            columns.forEach((col) => {
                                if (!col.span) {
                                    this.$set(col, "span", 12);
                                }
                            });
                        } else {
                            // 设置二级界面宽度为40%，没行显示 1 个字段
                            this.dialogEdit.width = "40%";
                            columns.forEach((col) => {
                                if (!col.span) {
                                    this.$set(col, "span", 24);
                                }
                            });
                        }
                    }
                    let group = [
                        {
                            label: null,
                            column: columns,
                        },
                    ];
                    this.dialogEdit.group = Object.assign([], group);
                }
            } else {
                let columns = Object.assign([], this.option.column);
                if (columns) {
                    // 字段数量大于等于 6 个，设置二级界面宽度为 60%，每行显示 2 个字段
                    if (columns && columns.length >= 6) {
                        this.dialogEdit.width = "60%";
                        columns.forEach((col) => {
                            if (!col.span) {
                                this.$set(col, "span", 12);
                            }
                        });
                    } else {
                        // 设置二级界面宽度为40%，没行显示 1 个字段
                        this.dialogEdit.width = "40%";
                        columns.forEach((col) => {
                            if (!col.span) {
                                this.$set(col, "span", 24);
                            }
                        });
                    }
                }
                let group = [
                    {
                        label: null,
                        column: columns,
                    },
                ];
                this.dialogEdit.group = Object.assign([], group);
            }
            if (this.option.dialogWidth) {
                this.dialogEdit.width = this.option.dialogWidth;
            }
            this.updateColumn(false);
            //动态计算表格高度
            this.getTableHeight();
        })
        const that = this
        window.onresize = () => {
            return (() => {
                window.screenHeight = window.innerHeight
                that.screenHeight = window.screenHeight;
            })()
        }
        window.onunload = e => {
            let tagList = that.$store.getters.tagList;
            tagList.forEach(tag => {
                if (tag && tag.value && tag.value.indexOf('/form/index') > -1) {
                    that.$router.$avueRouter.closeTag(tag.value);
                }
            });
        }
        //  配置按钮权限
        this.option.newBtn = this.permission.add;
        this.option.multiDelBtn = this.permission.view;
        this.option.delBtn = this.permission.delete;
        this.option.multiDelBtn = this.permission.delete;
        this.option.editBtn = this.permission.edit;
        this.option.导出 = this.permission.导出;
        this.option.导入 = this.permission.导入;
    },
    activated() {
        this.doLayout();
    },
    destroyed() {
        delete this.$pageList[this.$route.name];
    },
    data() {
        return {
            default: {},
            reload: true,
            config: config,
            list: [],
            tableHeight: "500",
            screenHeight: window.innerHeight,
            sumsList: [],
            dialogColumn: {
                visible: false,
                list: [],
            },
            queryParams: {},
            dialogEdit: {
                visible: false,
                isNew: false,
                isView: false,
                isEdit: false,
                dataSource: this.form,
                group: [],
                width: "80%",
            },
            page: {},
            title: 'crud',
            type: '',
        }
    },
    props: {
        data: {
            type: Array,
            required: true,
            default: () => {
                return [];
            }
        },
        tableLoading: {
            type: Boolean,
            default: false,
        },
        permission: {
            type: [Function, Object],
            default: () => {
                return {};
            }
        },
        sortBy: Function,
        sortOrders: Array,
        sortMethod: Function,
        spanMethod: Function,
        summaryMethod: Function,
        rowStyle: Function,
        cellStyle: Function,
        beforeClose: Function,
        beforeOpen: {
            type: Function, default: function () {
            }
        },
        rowClassName: Function,
        cellClassName: Function,
        headerCellClassName: Function,
        uploadBefore: Function,
        uploadAfter: Function,
        uploadDelete: Function,
        uploadPreview: Function,
        uploadError: Function,
        uploadExceed: Function,
        form: {
            type: Object,
            default: () => {
                return {};
            },
        },
    },
    methods: {
        getPermission(key, row, index) {
            if (typeof this.permission === "function") {
                return this.permission(key, row, index)
            } else if (!this.validatenull(this.permission[key])) {
                return this.permission[key]
            } else {
                return true;
            }
        },
        /**
         * 获取表格高度
         */
        getTableHeight() {
            if (this.isAutoHeight) {
                this.$nextTick(() => {
                    const tableRef = this.$refs.table;
                    const tablePageRef = this.$refs.tablePage;
                    if (!tableRef) {
                        return
                    }
                    const tableStyle = tableRef.$el;
                    const pageStyle = tablePageRef && tablePageRef.$el.offsetHeight ? tablePageRef.$el.offsetHeight : 0;
                    this.tableHeight = config.clientHeight - tableStyle.offsetTop - pageStyle - 80;
                });
            } else {
                this.tableHeight = this.tableOption.height;
            }
        },
        /**
         * 对 Table 进行重新布局。当 Table 或其祖先元素由隐藏切换为显示时，可能需要调用此方法
         */
        doLayout() {
            if (this.$refs.table) {
                this.$refs.table.doLayout();
            }
        },
        /**
         * 刷新表格
         * @param callback
         */
        refreshTable(callback) {
            this.reload = false;
            this.$nextTick(() => {
                this.reload = true;
                //是否开启表格排序
                // setTimeout(() => this.$refs.columnDefault.setSort())
                callback && callback()
            })
        },
        rowAdd(obj) {
            if (this.option.custom === undefined || this.option.custom) {
                this.$emit("on-new");
            } else {
                // this.dialogEdit.isNew = true;
                // this.dialogEdit.isView = false;
                // this.dialogEdit.isEdit = false;
                // if (obj) {
                //     this.dialogEdit.dataSource = obj;
                // } else {
                //     this.dialogEdit.dataSource = {};
                // }
                // this.dialogEdit.visible = true;
                let params = {
                    type: '新增',
                    title: this.$route.name + '-' + '新增',
                    form: obj ? obj : {},
                    group: this.dialogEdit.group,
                    parent: {
                        path: this.$route.path,
                        name: this.$route.name
                    }
                };
                this.$router.push({
                    name: 'form',
                    params: params,
                    query: {
                        title: params.title
                    },
                    meta: {
                        parent: this
                    }
                });
            }
        },
        rowDel(row, index) {
            this.$emit("on-del", row, index);
        },
        onMultiDel() {
            this.$emit("on-multi-del");
        },
        rowView(row, index) {
            if (this.option.custom === undefined || this.option.custom) {
                this.$emit("on-view", row, index);
            } else {
                // this.dialogEdit.isNew = false;
                // this.dialogEdit.isView = true;
                // this.dialogEdit.isEdit = false;
                // this.dialogEdit.dataSource = Object.assign({}, row);
                // this.dialogEdit.visible = true;
                // this.$emit('form-change', this.dialogEdit.dataSource);
                this.type = 'view';
                this.$emit('form-change', row);
                this.loading = true;
                if (this.beforeOpen) {
                    this.beforeOpen(this.finishLoading, this.type, () => {
                    });
                }
            }
        },
        rowEdit(row, index) {
            if (this.option.custom === undefined || this.option.custom) {
                this.$emit("on-edit", row, index);
            } else {
                // this.dialogEdit.isNew = false;
                // this.dialogEdit.isView = false;
                // this.dialogEdit.isEdit = true;
                // this.dialogEdit.dataSource = Object.assign({}, row);
                // this.$emit('form-change', this.dialogEdit.dataSource);
                // this.dialogEdit.visible = true;
                this.type = 'edit';
                this.$emit('form-change', row);
                this.loading = true;
                if (this.beforeOpen) {
                    this.beforeOpen(this.finishLoading, this.type, () => {
                    });
                }
            }
        },
        finishLoading() {
            this.loading = false;
            if (this.type == 'edit') {
                let params = {
                    type: '编辑',
                    title: this.$route.name + '-' + '编辑',
                    form: this.dialogEdit.dataSource,
                    group: this.dialogEdit.group,
                    parent: {
                        path: this.$route.path,
                        name: this.$route.name
                    }
                };
                this.$router.push({
                    name: 'form',
                    params: params,
                    query: {
                        title: params.title
                    }
                });
            } else if (this.type == 'view') {
                this.$refs.dialogView.open(
                    this.dialogEdit.group,
                    this.dialogEdit.dataSource,
                    this.$route.name);
            } else if (this.type == 'copy') {
                this.$refs.nodesDialog.open(this.option.group, this.dialogEdit.dataSource, this.type, this.$route.name, -1);
            }
        },
        onShowData(row, index, type) {
            this.type = type;
            this.dialogEdit.dataSource = this.deepClone(row);
            this.$emit('form-change', this.dialogEdit.dataSource);
            this.loading = true;
            if (this.beforeOpen) {
                this.beforeOpen(this.finishLoading, this.type, () => {
                });
            }
            // this.dialogEdit.title = title;
            // this.dialogEdit.isNew = false;
            // this.dialogEdit.isView = false;
            // this.dialogEdit.isEdit = false;
            //
            // this.dialogEdit.visible = true;
            // this.$refs.nodesDialog.open(this.option.group, this.deepClone(row), type, this.$route.name, index);
        },
        loadList(tree, treeNode, resolve) {
            this.$emit("load-List", tree, treeNode, resolve);
        },
        selectionChange(val) {
            this.$emit("selection-change", val);
        },
        onHeaderDragend(newWidth, oldWidth, column, event) {
            this.doLayout();
        },
        // 刷新事件
        onRefresh() {
            this.onLoad(this.queryParams);
        },
        // 控制搜索行的显隐
        onSearch() {
            this.search.visible = !this.search.visible;
            this.initSearch();
        },
        // 选中实例
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.table.toggleRowSelection(row);
                });
            } else {
                this.$refs.table.clearSelection();
            }
        },
        tableDrop(el, callback) {
            if (this.isSortable) {
                if (!window.Sortable) {
                    // packages.logs("Sortable")
                    return
                }
                window.Sortable.create(el, {
                    ghostClass: config.ghostClass,
                    chosenClass: config.ghostClass,
                    animation: 500,
                    delay: 0,
                    onEnd: evt => callback(evt)
                })
            }
        },
        onLoad(param = {}) {
            if (this.validatenull(param)) {
                param = this.queryParams;
            }
            this.$emit('on-load', this.$refs.tablePage.defaultPage, param);
        },
        onDialogColumnClose() {
            this.dialogColumn.visible = false;
            this.updateColumn(true);
        },
        onDialogColumnOpen() {
            this.dialogColumn.list = Object.assign([], this.option.column);
            this.dialogColumn.visible = true;
        },
        updateColumn(state) {
            let crudColumn = getStore({name: "crudColumn"}) || [];
            let menuAll = this.$store.getters.menuAll;
            let menu = this.getMenu(menuAll, this.$route);
            if (!menu) {
                return;
            }
            let column = crudColumn.find(u => {
                return u.menuId === menu.id;
            });
            if (!column || !column.columnList) {
                return;
            }
            column.columnList.forEach(config => {
                this.option.column.forEach(col => {
                    if (col.prop !== config.prop) {
                        return;
                    }
                    col.aliasName = config.aliasName;
                    col.hide = config.hide;
                    col.fixed = config.fixed;
                    col.align = config.align;
                    col.width = config.width === 0 ? undefined : config.width;
                    col.order = config.order;
                })
            });
            this.option.column.sort((a, b) => {
                let x = a['sort'];
                let y = b['sort'];
                return ((x < y) ? -1 : (x > y) ? 1 : 0);
            });
            let column_back = this.deepClone(this.option.column);
            this.option.column = [];
            this.$nextTick(() => {
                let restore = setTimeout(() => {
                    this.option.column = column_back;
                    let layout = setTimeout(() => {
                        this.$refs.table.doLayout();
                        clearTimeout(layout);
                    }, 100);
                    clearTimeout(restore);
                }, 100);
            });
        },
        getMenu(menuList) {
            let route = this.$route;
            return menuList.find(u => {
                if (u.name == route.name && u.path == route.path) {
                    return u;
                } else if (u.children && u.children.length > 0) {
                    return this.getMenu(u.children, route);
                }
            });
        },
        callbackEdit(callback) {
            if (callback.success) {
                let dialogEdit = this.dialogEdit;
                let type = undefined;
                if (this.dialogEdit.isView) {
                    type = "view";
                } else if (this.dialogEdit.isEdit) {
                    type = "edit";
                } else if (this.dialogEdit.isNew) {
                    type = "new";
                }
                let self = this;
                const finish = function () {
                    callback.loading();
                    self.$refs.dialogEdit.clearDataDic();
                    dialogEdit.visible = callback.visible;
                };
                this.$emit(
                    "row-save",
                    callback.data,
                    finish,
                    () => {
                        callback.loading();
                    },
                    type
                );
            } else {
                this.dialogEdit.visible = callback.visible;
            }
        },
        dialogEditChange(val) {
            this.$emit("input", val);
        },
        getHide(col) {
            if (col.hide !== undefined) {
                if (typeof col.hide === 'function') {
                    return col.hide(this);
                } else {
                    return !col.hide;
                }
            } else {
                return true;
            }
        },
        getColumnWidth(column) {
            if (column && column.type == 'date-picker' && (!column.width || column.width < 240)) {
                return 240;
            } else {
                return column.width || 100;
            }
        },
        getColumnSearchType(column) {
            if (!column) {
                return '';
            }
            return column.type;
        },
        getColumnData(column) {
            return this.DIC[column.prop] || [];
        },
        getArrayValue(row, column, array) {
            return array.find(u => {
                let result = false;
                if (u.children && u.children.length > 0) {
                    result = this.getArrayValue(row, column, u.children) !== undefined;
                } else {
                    if (!(row[column.prop] instanceof String)) {
                        let arr = row[column.prop].split();
                        let value = '';
                        arr.forEach(item => {
                            value += u[column.props.value] == row[item];
                        })
                        result = value.length > 0;
                    } else {
                        result = u[column.props.value] == row[column.prop];
                    }
                }
                return result;
            });
        },
        handleDetail(row, column) {
            if (['select', 'select-table-user', 'select-tree'].includes(column.type)) {
                let array = this.DIC[column.prop];
                if (array) {
                    let obj = this.getArrayValue(row, column, array);
                    if (obj) {
                        return obj[column.props.label];
                    }
                }
            } else {
                return row[column.prop];
            }
            return '';
        },
        onCellClick() {
        },
        onCellDblClick(row, column, cell, event) {
            let col = this.option.column.find(u => u.prop == column.property);
            if (func.isEmpty(col)) {
                return;
            }
            let content = this.handleDetail(row, col);
            if (content && content.length > 0) {
                this.$Clipboard({
                    text: content
                }).then(() => {
                    this.$message({message: '复制内容：' + content, type: 'success'});
                }).catch(() => {
                    this.$message({message: '复制失败！：', type: 'error'});
                });
            }
        }
    }
}
</script>

<style scoped>

</style>
