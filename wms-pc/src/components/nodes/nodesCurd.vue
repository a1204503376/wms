<template>
    <div id="nodes_form_container" ref="container">
        <dialog-column :data-source="filterColumnList"
                       :visible="dialogFilterColumn.visible"
                       save-url='/api/nodesCurdColumn/submit'
                       @close="filterColumnClose"></dialog-column>
        <search-panel
            ref="searchPanel"
            :column="searchPanel.column"
            :visible="searchPanel.visible"
            @dialogResult="searchPanelCallback"
        ></search-panel>
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
            :saveBtn="option.saveBtn"
            :title="dialogEdit.title"
            :visible="dialogEdit.visible"
            :width="dialogEdit.width"
            @callback="callbackEdit"
            @change="dialogEditChange"
            @updateGroup="updateGroup"
            @before-open="beforeOpen"
            @dynamic-before-edit="dynamicBeforeEdit"
            @dynamic-saved="dynamicSaved"
        ></editDialog>
        <el-row :gutter="20" type="flex">
            <el-col :span="12">
                <div class="menu_left">
                    <template v-if="permission.add">
                        <el-button
                            v-show="option.newBtn"
                            icon="el-icon-plus"
                            size="mini"
                            type="primary"
                            @click="onNew(undefined)"
                        >新增
                        </el-button>
                    </template>
                    <template v-if="permission.delete">
                        <el-button
                            v-show="option.multiDelBtn"
                            icon="el-icon-delete"
                            plain
                            size="mini"
                            type="danger"
                            @click="onMultiDel"
                        >删除
                        </el-button>
                    </template>

                    <slot name="menuLeft"></slot>
                </div>
            </el-col>
            <el-col :span="12" align="right">
                <div class="menu_right" style="min-height: 28px">
                    <slot name="menuRight"></slot>
                    <el-tooltip
                        v-if="option.columnBtn === undefined ? true : option.columnBtn"
                        :enterable="false"
                        class="item"
                        content="显隐"
                        effect="dark"
                        placement="top"
                    >
                        <el-button
                            circle
                            icon="el-icon-menu"
                            size="mini"
                            @click="filterColumn"
                        ></el-button>
                    </el-tooltip>
                    <el-tooltip
                        v-if="option.search === undefined ? true : option.search"
                        :enterable="false"
                        class="item"
                        content="搜索"
                        effect="dark"
                        placement="top"
                    >
                        <el-button
                            circle
                            icon="el-icon-search"
                            size="mini"
                            @click="onSearch"
                        ></el-button>
                    </el-tooltip>
                    <el-tooltip
                        v-if="option.refresh === undefined ? true : option.refresh"
                        :enterable="false"
                        class="item"
                        content="刷新"
                        effect="dark"
                        placement="top"
                    >
                        <el-button
                            circle
                            icon="el-icon-refresh"
                            size="mini"
                            @click="refresh"
                        ></el-button>
                    </el-tooltip>
                </div>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="24">
                <div id="myTable-box" class="myTable-box">
                    <rowSearch
                        v-if="option.columnFilter"
                        :columnOperate="columnOperate"
                        :columns="option.column"
                        :option="option"
                        :top="rowSearchTop"
                        @filterable="filterable"
                    />
                    <div class="table-content-box">
                        <el-table
                            ref="table"
                            v-loading="tableLoading"
                            :data="props.tableData"
                            :default-sort="{ prop: 'amount', order: 'descending' }"
                            :header-cell-style="{ background: 'none' }"
                            :height="contentHeight"
                            :load="loadList"
                            :row-key="option.rowKey"
                            :show-summary="option.showSummary"
                            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                            border
                            class="nodes-curd-table"
                            element-loading-spinner="el-icon-loading"
                            element-loading-text="数据正在加载中"
                            highlight-current-row
                            lazy
                            size="mini"
                            style="width: 100%; margin-top: 10px"
                            @header-dragend="headerDragend"
                            @selection-change="selectionChange"
                            @sort-change="changeTableSort"
                            @row-click="rowClick"
                        >
                            <el-table-column
                                v-if="option.selection === undefined ? true : false"
                                fixed
                                type="selection"
                                width="50"
                            ></el-table-column>
                            <template v-for="(item, index) in option.column">
                                <el-table-column
                                    v-if="!item.hide"
                                    :key="index"
                                    :align="item.align"
                                    :column-key="item.prop"
                                    :fixed="item.fixed"
                                    :label="item.aliasName || item.label"
                                    :min-width="item.width"
                                    :prop="item.prop"
                                    :sort-by="[item.sortProp ? item.sortProp : item.prop]"
                                    :sortable="item.sortable"
                                    filter-placement="bottom-end"
                                    show-overflow-tooltip
                                >
                                    <template slot-scope="scope">
                                        <span v-if="item.slot">
                                          <slot :name="item.prop" v-bind="scope.row"></slot>
                                        </span>
                                        <!-- 查看 -->
                                        <span v-else-if="item.view ">
                                          <el-button
                                              size="medium"
                                              type="text"
                                              @click="onView(scope.row, scope.$index)"
                                          >{{ scope.row[item.prop] }}</el-button
                                          >
                                        </span>
                                        <span v-else>{{ scope.row[item.prop] }}</span>
                                    </template>
                                    <template v-for="childItem in item.children">
                                        <el-table-column
                                            v-if="!childItem.hide"
                                            :key="childItem.label"
                                            :column-key="childItem.prop"
                                            :label="childItem.label"
                                            :min-width="childItem.width"
                                            :prop="childItem.prop"
                                            filter-placement="bottom-end"
                                            show-overflow-tooltip
                                        ></el-table-column>
                                    </template>
                                </el-table-column>
                            </template>
                            <el-table-column
                                v-if="option.menu"
                                :align="columnOperate.align"
                                :fixed="columnOperate.fixed"
                                :label="columnOperate.label"
                                :width="columnOperate.width"
                            >
                                <template slot-scope="scope">
                                    <slot name="menu" v-bind="scope"></slot>
                                    <el-dropdown
                                        v-if="permission"
                                        class="btn"
                                        trigger="click"
                                        @command="executeCommand($event, scope.row, scope.$index)"
                                    >
                                        <span class="el-dropdown-link">
                                          更多
                                          <i class="el-icon-arrow-down el-icon--right"></i>
                                        </span>
                                        <el-dropdown-menu slot="dropdown">
                                            <template v-if="permission.edit">
                                                <el-dropdown-item
                                                    v-if="option.editBtn"
                                                    command="9999998"
                                                >编辑
                                                </el-dropdown-item>
                                            </template>
                                            <template v-if="permission.delete">
                                                <el-dropdown-item v-if="option.delBtn" command="9999999"
                                                >删除
                                                </el-dropdown-item>
                                            </template>
                                            <template v-if="pathRouter == '/core/menu'">
                                                <template v-for="cmdItem in option.menuItem">
                                                    <el-dropdown-item
                                                        :key="cmdItem.label"
                                                        :command="cmdItem.command"
                                                        :divided="cmdItem.divided"
                                                    >{{ cmdItem.label }}
                                                    </el-dropdown-item>
                                                </template>
                                            </template>
                                            <template v-else>
                                                <template v-for="cmdItem in option.menuItem">
                                                    <template>
                                                        <el-dropdown-item
                                                            v-if="permission[cmdItem.label]"
                                                            :key="cmdItem.label"
                                                            :command="cmdItem.command"
                                                            :divided="cmdItem.divided"
                                                        >{{ cmdItem.label }}
                                                        </el-dropdown-item>
                                                    </template>
                                                </template>
                                            </template>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </div>
            </el-col>
        </el-row>
        <el-row
            :gutter="20"
            justify="space-between"
            style="flex-grow: 1; padding-bottom: 10px"
            type="flex"
        >
            <el-col :span="8" class="stockcount">
                <slot name="stock"></slot>
            </el-col>
            <el-col v-if="option.page" :gutter="20" :span="16" class="pageData">
                <el-pagination
                    :current-page="page.currentPage"
                    :page-size="page.pageSize"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    @hideSingleOnPage="hideOnSinglePage"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                ></el-pagination>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import tableColumn from "@/components/nodes/tableColumn";
import searchPanel from "@/components/nodes/searchPanel";
import editDialog from "@/components/nodes/editDialog";
import dialogColumn from "@/components/element-ui/crud/dialog-column";
import {mapGetters, mapState} from "vuex";
import rowSearch from "./rowSearch";
import {getStore} from '@/util/store';
import Sortable from 'sortablejs';

let timer = null;

export default {
    name: "nodesCurd",
    components: {
        tableColumn,
        searchPanel,
        editDialog,
        rowSearch,
        dialogColumn
    },
    computed: {
        ...mapState("leftMenu", ["buttonsData"]),
        ...mapState(["hiddenObj"]),

        ...mapGetters(["permission"]),
        permissionList() {
            return {
                addBtn: this.vaildData(this.permission.add, false),
                viewBtn: this.vaildData(this.permission.view, false),
                delBtn: this.vaildData(this.permission.delete, false),
                editBtn: this.vaildData(this.permission.edit, false),
                cmdBtn: this.validData(this.permission.command, false),
            };
        },
    },
    props: {
        option: {
            // 表格列配置
            column: {type: Array, default: []},
            // 表格行键
            rowKey: {type: String, default: "stockId"},
            // 新增按钮显隐
            newBtn: {type: Boolean, default: true},
            // 查看按钮显隐
            viewBtn: {type: Boolean, default: true},
            // 编辑按钮显隐
            editBtn: {type: Boolean, default: true},
            // 删除按钮显隐
            delBtn: {type: Boolean, default: true},
            // 多选删除按钮显隐
            multiDelBtn: {type: Boolean, default: true},
            // 表格操作栏显隐
            menu: {type: Boolean, default: true},
            // 表格操作栏宽度
            menuWidth: {type: Number, default: 90},
            // 分页显隐
            page: {type: Boolean, default: true},
            // 是否自定义 新增/编辑/查看 操作
            custom: {type: Boolean, default: true},
            // 二级界面分组配置
            group: {
                type: Array,
                default: function () {
                    return [];
                },
            },
            // 二级界面保存按钮显隐
            saveBtn: {type: Boolean, default: true},
            // 二级界面宽度
            dialogWidth: {type: [String, Number], default: undefined},
        },
        // 表格加载状态
        tableLoading: {type: Boolean, default: false},
        // 表格数据
        data: {type: Array, default: []},
        // 分页组建配置
        page: {
            type: Object,
            default: () => {
                return {
                    total: 0,
                    pageSize: 20,
                    currentPage: 1,
                    ascs: "", //正序
                    descs: "", //倒序
                };
            },
        },
        // 对话框打开的时候触发
        beforeOpen: {
            type: Function,
            default: () => {
            },
        },
        form: {
            type: Object,
            default: () => {
                return {};
            },
        },
        // 表格高度
        height: {type: [String, Number], default: undefined},
    },
    model: {
        prop: "form",
        event: "datasource-change",
    },
    data() {
        return {
            rowSearchTop: 31,
            tableBodyWrapper: null, //表格主体
            searchTableBodyWrapper: null, //搜索表格主体
            scrollTimer: null,
            timerStart: "",
            timerEnd: "",
            valid: true,

            props: {
                tableData: [],
            },
            pathRouter: "",
            buttonState: [],
            filterColumnList: [],
            contentHeight: 0,
            columnIndex: {
                width: 60,
            },
            columnOperate: {
                label: "操作",
                fixed: "right",
                align: "center",
                width: 90,
            },
            dialogFilterColumn: {
                visible: false,
                column: [],
            },
            dialogEdit: {
                visible: false,
                isNew: false,
                isView: false,
                isEdit: false,
                dataSource: this.form,
                group: [],
                width: "80%",
            },
            searchPanel: {
                visible: false,
                column: {type: Array, default: []},
                data: {},
            },
        };
    },
    mounted() {
        let self = this;
        this.$nextTick(function () {
            this.initCurd();
            this.updateColumn();
            self.filterColumnList = Object.assign(
                [],
                self.option.column.filter((col) => {
                    return col.hide === true ? false : true;
                })
            );
            // this.columnDrop();
        });
        // 监听窗口大小变化

        window.onresize = function () {
            self.autoColumnWidth();
            self.autoTableHeight();
            if (self.$refs.table) {
                self.$nextTick(() => {
                    self.$refs.table.doLayout();
                });
            }
            self.headerDragend();
        };
        this.initSearch();
        this.headerDragend();
        this.initTableScroll();
        this.refresh();
    },
    created() {
    },
    destroyed() {
        clearInterval(timer);
    },
    watch: {
        "option.menuWidth": function (val) {
            this.columnOperate.width = this.option && val ? val : 90;
        },
        option: {
            handler: function (newValue, oldValue) {
                this.initCurd();
            },
            deep: true,
            immediate: true,
        },
        form: {
            handler: function (newValue, oldValue) {
                this.dialogEdit.dataSource = newValue;
            },
            deep: true,
            immediate: true,
        },
        data: {
            handler: function (newValue, oldValue) {
                if (newValue) {
                    this.props.tableData = Object.assign([], newValue);
                }
                if (newValue && newValue.length <= 0 && this.page.currentPage > 1) {
                    this.page.currentPage--;
                    this.onLoad(this.page, {});
                }
                if (this.$refs.table) {
                    this.$refs.table.doLayout();
                }
            },
            deep: true,
            immediate: true,
        },
    },
    methods: {
        initTableScroll() {
            setTimeout(() => {
                this.searchTableBodyWrapper = document.querySelector(
                    "#myTable-box .row-search .el-table--scrollable-x .el-table__body-wrapper"
                );
                this.tableBodyWrapper = document.querySelector(
                    "#myTable-box .table-content-box .el-table--scrollable-x .el-table__body-wrapper"
                );
                if (this.searchTableBodyWrapper && this.tableBodyWrapper) {
                    this.tableBodyWrapper.addEventListener(
                        "scroll",
                        this.handleTableScroll,
                        false
                    );
                    this.searchTableBodyWrapper.addEventListener(
                        "scroll",
                        this.handleTableScroll,
                        false
                    );
                }
            }, 0);
        },
        handleTableScroll(e) {
            if (!this.valid) {
                return;
            }
            this.valid = false;

            this.scrollTimer = setTimeout(() => {
                const tableScrollLeft = this.tableBodyWrapper.scrollLeft;
                this.searchTableBodyWrapper.scrollLeft = tableScrollLeft;
                this.valid = true;
            }, 20);
        },
        initSearch() {
            if (this.option.columnFilter) {
                //需要显示搜索
                const table_el = this.$refs["table"].$el.querySelector(
                    ".el-table__body-wrapper"
                );
                table_el.style.marginTop = this.rowSearchTop + "px";
                setTimeout(() => {
                    document.querySelector(
                        "#myTable-box .table-content-box .el-table__fixed .el-table__fixed-body-wrapper"
                    ).style.marginTop = this.rowSearchTop + "px";
                    document.querySelector(
                        "#myTable-box .table-content-box .el-table__fixed-right .el-table__fixed-body-wrapper"
                    ).style.marginTop = this.rowSearchTop + "px";
                }, 0);
            }
        },
        headerDragend() {
            if (this.option.columnFilter) {
                setTimeout(() => {
                    const colgroup_html = this.$refs["table"].$el.querySelector(
                        ".el-table__header-wrapper colgroup"
                    ).innerHTML;
                    document.querySelector(
                        "#myTable-box .row-search colgroup"
                    ).innerHTML = colgroup_html;
                }, 90);
            }
        },
        //行搜索
        filterable(search_tableData) {
            this.onLoad(this.page, search_tableData);
        },
        loadList(tree, treeNode, resolve) {
            this.$emit("load-List", tree, treeNode, resolve);
        },
        initCurd() {
            // 确认搜索元素
            if (this.option && this.option.column) {
                this.searchPanel.column = this.option.column.filter((col) => {
                    return col.search === true;
                });
            }
            let _group = null;
            // 当不建立分组的情况下，默认一个空分组，字段为column
            if (this.option.group && this.option.group.length > 0) {
                if (this.option.group[0].label) {
                    //this.dialogEdit.group = Object.assign([], this.option.group);
                    _group = Object.assign([], this.option.group);
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
                    //this.dialogEdit.group = Object.assign([], group);
                    _group = Object.assign([], group);
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
                //this.dialogEdit.group = Object.assign([], group);
                _group = Object.assign([], group);
            }
            this.$set(this.dialogEdit, "group", _group);
            let cloneGroup = JSON.parse(JSON.stringify(_group));
            this.$set(this.dialogEdit, "cloneGroup", cloneGroup);
            if (this.option.dialogWidth) {
                this.dialogEdit.width = this.option.dialogWidth;
            }
            this.columnOperate.width =
                this.option && this.option.menuWidth ? this.option.menuWidth : 90;
            this.option.page =
                this.option && this.option.page !== undefined ? this.option.page : true;
            // let self = this;
            // timer = setInterval(() => {
            //   if (self.$refs.table) {
            //     self.$refs.table.doLayout();
            //   }
            // }, 500);
            this.autoTableHeight();
        },
        changeTableSort({column, prop, order}) {
            let sort_prop = column.sortBy ? column.sortBy[0] : "";

            if (order == "ascending") {
                this.page.ascs = sort_prop;
                this.page.descs = "";
            } else if (order == "descending") {
                this.page.descs = sort_prop;
                this.page.ascs = "";
            } else {
                this.page.ascs = "";
                this.page.descs = "";
            }
            this.searchPanel.data.descs = this.page.descs;
            this.searchPanel.data.ascs = this.page.ascs;

            this.onLoad(this.page, this.searchPanel.data);
        },
        handleColumnHide(route, key) {
            let hiddenObj = this.hiddenObj;
            if (!hiddenObj[route]) return;
            if (!hiddenObj[route][key]) return;
            let columns = hiddenObj[route][key];
            let group = this.option.column;
            for (let j = 0; j < group.length; j++) {
                let prop = group[j].prop;
                let exist = columns.some((v) => {
                    return v.prop == prop;
                });
                if (exist) {
                    columns.splice(j, 1);
                    group.splice(j, 1);
                }
            }
        },
        updateGroup() {
            let group = JSON.parse(JSON.stringify(this.dialogEdit.cloneGroup));
            this.$set(this.dialogEdit, "group", group);
        },
        onLoad(page, params) {
            this.$emit("on-load", page, params);
        },
        handleSizeChange(val) {
            this.page.pageSize = val;
            this.onLoad(this.page, this.searchPanel.data);
        },
        handleCurrentChange(val) {
            this.page.currentPage = val;
            this.onLoad(this.page, this.searchPanel.data);
        },
        hideOnSinglePage() {
            return this.page && this.page.total <= this.page.pageSize;
        },
        selectionChange(val) {
            this.$emit("selection-change", val);
        },
        onNew(obj) {
            if (this.option.custom === undefined || this.option.custom) {
                this.$emit("on-new");
            } else {
                this.dialogEdit.isNew = true;
                this.dialogEdit.isView = false;
                this.dialogEdit.isEdit = false;
                if (obj) {
                    this.dialogEdit.dataSource = obj;
                } else {
                    this.dialogEdit.dataSource = {};
                }
                this.dialogEdit.visible = true;
            }
        },
        // 为了模拟 Avue 的方式创建的方法
        rowAdd(obj) {
            this.onNew(obj);
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
        onView(row, index) {
            if (this.option.custom === undefined || this.option.custom) {
                this.$emit("on-view", row, index);
            } else {
                this.dialogEdit.isNew = false;
                this.dialogEdit.isView = true;
                this.dialogEdit.isEdit = false;
                this.dialogEdit.dataSource = Object.assign({}, row);
                this.dialogEdit.visible = true;
            }
        },
        onEdit(row, index) {
            if (this.option.custom === undefined || this.option.custom) {
                this.$emit("on-edit", row, index);
            } else {
                this.dialogEdit.isNew = false;
                this.dialogEdit.isView = false;
                this.dialogEdit.isEdit = true;
                this.dialogEdit.dataSource = Object.assign({}, row);
                this.dialogEdit.visible = true;
            }
        },
        onShowData(row, index, title) {
            this.dialogEdit.title = title;
            this.dialogEdit.isNew = false;
            this.dialogEdit.isView = false;
            this.dialogEdit.isEdit = false;
            this.dialogEdit.dataSource = Object.assign({}, row);
            this.dialogEdit.visible = true;
        },
        onDel(row, index) {
            this.$emit("on-del", row, index);
        },
        onMultiDel() {
            this.$emit("on-multi-del");
        },
        toggleSelection() {
            if (this.$refs.table) {
                this.$refs.table.clearSelection();
            }
        },
        onSearch() {
            this.searchPanel.visible = !this.searchPanel.visible;
        },
        refresh() {
            this.$refs.searchPanel.resetForm();
            this.searchPanel.data = {};
            this.$refs.table.clearSort();
            this.onLoad(this.page, {});
        },
        filterColumn() {
            this.dialogFilterColumn.visible = true;
        },
        filterColumnClose() {
            this.dialogFilterColumn.visible = false;
            this.updateColumn();
        },
        filterColumnChange(tableColumnChecked) {
            this.option.column.forEach((column) => {
                if (tableColumnChecked.includes(column.prop)) {
                    column.hide = false;
                } else {
                    column.hide = true;
                }
            });
            this.autoColumnWidth();
            this.$nextTick(() => {
                this.$refs.table.doLayout();
            });
            this.$forceUpdate();
        },
        searchPanelCallback(dialogResult) {
            this.searchPanel.visible = dialogResult.visible;
            if (!dialogResult.result) {
                return;
            }
            this.page.currentPage = 1;
            let data = Object.assign({}, dialogResult.data);
            for (let key in data) {
                if (data[key] instanceof Array) {
                    data[key] = JSON.stringify(data[key]).replace(/"/g, "'");
                }
            }
            this.searchPanel.data = data;
            this.$emit("search-change", data);
            this.onLoad(this.page, data);
        },
        // 自动调整列宽
        autoColumnWidth() {
            // 设置动态显隐后，计算当前所有显示列的宽度是否大于等于表格宽度
            if (!this.$refs.table) {
                return;
            }
            let tableWidth = parseInt(this.$refs.table.bodyWrapper.offsetWidth);
            let columnList = this.option.column.filter((col) => {
                return !col.hide;
            });
            let colWidth = 0;
            let hasNoWidth = false;
            columnList.forEach((col) => {
                if (!col.width) {
                    hasNoWidth = true;
                    return;
                }
                colWidth += parseInt(col.width);
            });
            // 不存在未设置宽度的列（自适应）
            if (!hasNoWidth) {
                let totalWidth = tableWidth - 110 - parseInt(this.columnOperate.width);
                // 自适应【序号】列的宽度
                if (colWidth === 0) {
                    this.columnIndex.width = totalWidth + 60;
                } else {
                    this.columnIndex.width = 60;
                    if (colWidth <= totalWidth) {
                        // 自适应剩下列的宽度
                        const avgWidth = totalWidth / columnList.length;
                        columnList.forEach((col) => {
                            // 记录该列当前的状态
                            let column = Object.assign({}, col);
                            let existObj = this.dialogFilterColumn.column.find((item) => {
                                return item.prop === col.prop;
                            });
                            if (!existObj) {
                                this.dialogFilterColumn.column.push(column);
                            }
                            // 修改列的宽度
                            let width = parseInt(avgWidth);
                            col.width = totalWidth >= width ? width : totalWidth - width;
                            totalWidth -= col.width;
                        });
                    } else {
                        // 还原之前自适应列的宽度
                        if (
                            this.dialogFilterColumn.column &&
                            this.dialogFilterColumn.column.length > 0
                        ) {
                            for (let i = 0; i < this.dialogFilterColumn.column.length; i++) {
                                for (let j = 0; j < this.option.column.length; j++) {
                                    let filterColumn = this.dialogFilterColumn.column[i];
                                    let column = this.option.column[j];
                                    if (column.prop === filterColumn.prop) {
                                        column.width = filterColumn.width;
                                        this.dialogFilterColumn.column.splice(i);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.$refs.table.doLayout();
        },
        // 自动调整表格高度
        autoTableHeight() {
            if (this.height) {
                this.contentHeight = this.height;
                return;
            }
            if (this.option.page) {
                this.contentHeight = window.innerHeight - 250;
            } else {
                this.contentHeight = window.innerHeight - 168;
            }
        },
        dialogEditChange(val) {
            this.$emit("datasource-change", val);
        },
        dynamicBeforeEdit(row, prop) {
            this.$emit("table-row-before-edit", row, prop);
        },
        dynamicSaved(row, prop) {
            this.$emit("table-row-saved", row, prop);
        },
        executeCommand(cmd, row, index) {
            switch (parseInt(cmd)) {
                case 9999998: //编辑
                    this.onEdit(row, index);
                    break;
                case 9999999: //删除
                    this.onDel(row, index);
                    break;
                default:
                    this.$emit("menu-command", parseInt(cmd), row, index);
                    break;
            }
        },
        rowClick(row, column, cell, event) {
            this.$emit("row-click", row);
        },
        columnDrop() {
            const wrapperTr = document.querySelector('.nodes-curd-table .el-table__header-wrapper tr');
            this.sortable = Sortable.create(wrapperTr, {
                animation: 180,
                delay: 0,
                onEnd: evt => {
                    if (evt.oldIndex === 0 || (this.option.menu && evt.oldIndex > this.option.column.length)) {
                        return;
                    }
                    let oldIndex = evt.oldIndex;
                    let newIndex = evt.newIndex;
                    if (this.option.selection === undefined || this.option.selection) {
                        oldIndex = evt.oldIndex - 1;
                        // newIndex = evt.newIndex - 1;
                    }
                    const oldItem = this.option.column[oldIndex];
                    this.option.column.splice(oldIndex, 1);
                    this.option.column.splice(newIndex, 0, oldItem);
                }
            })
        },
        updateColumn() {
            let crudColumn = getStore({name: "curdColumn"}) || [];
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

            this.option.column.sort((a, b) => {
                let x = a['sort'];
                let y = b['sort'];
                return ((x < y) ? -1 : (x > y) ? 1 : 0);
            });
            this.$nextTick(() => {
                this.$refs.table.doLayout();
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
    },
};
</script>

<style lang="scss">
.el-dropdown-link {
    .el-icon--right {
        margin-left: 0px;
    }
}

#nodes_form_container {
    padding-top: 10px;
    padding-left: 10px;
    padding-right: 10px;
    display: flex;
    height: inherit;
    background: #fff;
    flex-direction: column;
}

#nodes_form_container .stockcount {
    display: flex;
    flex-direction: row;
    align-items: center;
}

#nodes_form_container .pageData {
    // height: 45px;
    // line-height: 45px;
    justify-content: flex-end;
    display: flex;
    flex-direction: row;
    align-items: center;
    // background: #fff;
    // z-index: 999;
}

.myTable-box {
    position: relative;
}

.myTable-box .el-table {
    overflow: inherit;
}

/* #nodes_form_container .page .el-pagination {
            position: fixed;
            right: 10px;
            bottom: 5px;
        } */
</style>
