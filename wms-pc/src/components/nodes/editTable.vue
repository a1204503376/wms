<template>
    <div class="dynamic-table-box">
        <el-table
            :id="option.prop"
            ref="dynamicTable"
            v-loading="loading"
            :data="tableData"
            :height="height ? height : 220"
            :row-class-name="rowClassName"
            border
            class="table-style"
            show-overflow-tooltip
            style="width: 100%"
            @selection-change="selectionChange"
        >
            <!-- 索引列 -->
            <el-table-column
                :align="option.align"
                :index="indexMethod"
                fixed="left"
                type="index"
                width="55"
            >
                <!--                v-if="option.index"-->
                <!--                <template slot="header">{{option.indexLabel || '#'}}</template>-->
                <template slot="header">
                    <el-button
                        v-if="option.addBtn === undefined ? true : option.addBtn"
                        circle
                        icon="el-icon-plus"
                        size="mini"
                        style="padding: 4px"
                        type="primary"
                        @click="rowAdd"
                    ></el-button>
                    <span v-else> # </span>
                </template>
            </el-table-column>
            <!-- 选择列 -->
            <el-table-column
                v-if="option.selection"
                :align="option.align"
                fixed="left"
                type="selection"
                width="55"
            >
            </el-table-column>
            <!-- 数据列 -->
            <template v-for="col in column">
                <el-table-column
                    v-if="getHide(col)"
                    :key="col.prop"
                    :align="col.align"
                    :class-name="'is_' + col.prop"
                    :column-key="col.prop"
                    :fixed="col.fixed"
                    :label="col.label"
                    :min-width="col.width"
                    :prop="col.prop"
                    :sortable="col.sortable"
                    filter-placement="bottom-end"
                    show-overflow-tooltip
                >
                    <template slot-scope="scope">
                        <!-- 普通文本 -->
                        <template v-if="col.type == 'text'">
                            <template
                            ><span>{{ scope.row[col.prop] }}</span></template
                            >
                        </template>
                        <!-- 物品 -->
                        <template v-else-if="col.type == 'selectSku'">
                            <template v-if="scope.row.isEdit">
                                <SelectSku
                                    v-model="scope.row[col.prop]"
                                    :before-open="col.beforeOpen(scope)"
                                    :search="col.searchData ? col.searchData : {}"
                                    :text="
                    col.props ? scope.row[col.props.label] : scope.row[col.prop]
                  "
                                    size="medium"
                                    @change="(val, obj) => onChange(val, obj, col, scope)"
                                ></SelectSku>
                            </template>
                            <template v-else>
                                <span>{{
                                        col.show && col.show.length === 1
                                            ? scope.row[col.show[0]]
                                            : scope.row[col.prop]
                                    }}</span>
                            </template>
                        </template>
                        <!-- 物品包装 -->
                        <template v-else-if="col.type == 'selectSkuPackage'">
                            <template v-if="scope.row.isEdit">
                                <selectSkuPackage
                                    v-model="scope.row[col.prop]"
                                    :disabled="col.disabled == undefined ? false : col.disabled"
                                    :search="col.searchData ? col.searchData : {}"
                                    :text="
                    col.props ? scope.row[col.props.label] : scope.row[col.prop]
                  "
                                    size="medium"
                                    @change="(val, obj) => onChange(val, obj, col, scope)"
                                ></selectSkuPackage>
                            </template>
                            <template v-else>
                <span>{{
                        col.show && col.show.length === 1
                            ? scope.row[col.show[0]]
                            : scope.row[col.prop]
                    }}</span>
                            </template>
                        </template>
                        <!-- 来往企业 -->
                        <template v-else-if="col.type == 'selectEnterprise'">
                            <template v-if="scope.row.isEdit">
                                <!-- <selectEnterprise
                                                        v-model="scope.row[col.prop]"
                                                        :text="scope.row[col.prop+'_text']"
                                                        :search="col.searchData ? col.searchData : {}"
                                                        size="medium"
                                                        @change="(val, obj)=>onChange({id: val, obj, scope, type: 'selectEnterprise'})"
                                                ></selectEnterprise> -->
                                <selectEnterprise
                                    v-model="scope.row[col.prop]"
                                    :search="col.searchData ? col.searchData : {}"
                                    :text="
                    col.props ? scope.row[col.props.label] : scope.row[col.prop]
                  "
                                    size="medium"
                                    @change="(val, obj) => onChange(val, obj, col, scope)"
                                ></selectEnterprise>
                            </template>
                            <template v-else>
                <span>{{
                        col.show && col.show.length === 1
                            ? scope.row[col.show[0]]
                            : scope.row[col.prop]
                    }}</span>
                            </template>
                        </template>
                        <!-- 下拉框 -->
                        <template v-else-if="col.type == 'select'">
                            <template v-if="scope.row.isEdit">
                                <el-select
                                    v-model="scope.row[col.prop]"
                                    :clearable="col.clearable"
                                    :disabled="col.disabled"
                                    clearable
                                    collapse-tags
                                    filterable
                                    placeholder="请选择"
                                    size="medium"
                                    style="width: 100%"
                                    @change="(val) => onChange(val, null, col, scope)"
                                >
                                    <el-option
                                        v-for="item in col.dicData"
                                        :key="col.props ? item[col.props.value] : item['value']"
                                        :label="col.props ? item[col.props.label] : item['label']"
                                        :value="col.props ? item[col.props.value] : item['value']"
                                    ></el-option>
                                </el-select>
                            </template>
                            <template v-else show-overflow-tooltip>
                <span>{{
                        col.show && col.show.length === 1
                            ? scope.row[col.show[0]]
                            : scope.row[col.prop]
                    }}</span>
                            </template>
                        </template>
                        <!-- 下拉列表框（树形）-->
                        <template v-else-if="col.type === 'select-tree'">
                            <template v-if="scope.row.isEdit">
                                <treeSelect
                                    v-model="scope.row[col.prop]"
                                    :accordion="col.accordion"
                                    :clearable="col.clearable"
                                    :options="col.dicData"
                                    :placeholder="col.placeholder || '请选择'"
                                    :props="col.props"
                                    style="width: 100%"
                                    @change="(val) => onChange(val, null, col, scope)"
                                />
                            </template>
                            <template v-else show-overflow-tooltip>
                <span>{{
                        col.show && col.show.length === 1
                            ? scope.row[col.show[0]]
                            : scope.row[col.prop]
                    }}</span>
                            </template>
                        </template>
                        <!-- 计数器 -->
                        <template v-else-if="col.type == 'inputNumber'">
                            <template v-if="scope.row.isEdit || col.isEdit">
                                <el-input-number
                                    v-model="scope.row[col.prop]"
                                    :max="col.max !== undefined ? col.max : 2147483647"
                                    :min="col.min !== undefined ? col.min : 0"
                                    controls-position="right"
                                    size="medium"
                                    style="width: 100%"
                                    @input="onChange($event, $event, col, scope)"
                                ></el-input-number>
                            </template>
                            <template v-else>{{ scope.row[col.prop] }}</template>
                        </template>
                        <!-- switch开关 -->
                        <template v-else-if="col.type == 'switch'">
                            <template v-if="scope.row.isEdit">
                                <el-switch
                                    v-model="scope.row[col.prop]"
                                    :active-text="col.dicData[0].label"
                                    :active-value="col.dicData[0].value"
                                    :inactive-text="col.dicData[1].label"
                                    :inactive-value="col.dicData[1].value"
                                    size="medium"
                                ></el-switch>
                            </template>
                            <template v-else>
                                <template v-for="item in col.dicData">
                  <span
                      v-if="item.value == scope.row[col.prop]"
                      :key="item.value"
                  >{{ item.label }}</span
                  >
                                </template>
                            </template>
                        </template>
                        <!-- 日期 -->
                        <template v-else-if="col.type == 'date'">
                            <template v-if="scope.row.isEdit">
                                <el-date-picker
                                    v-model="scope.row[col.prop]"
                                    :format="col.format ? col.format : 'yyyy-MM-dd'"
                                    :value-format="
                    col.valueFormat ? col.valueFormat : 'yyyy-MM-dd'
                  "
                                    placeholder="选择日期"
                                    size="medium"
                                    style="width: 100%"
                                    type="date"
                                    @change="onChange($event, $event, col, scope)"
                                ></el-date-picker>
                            </template>
                            <template v-else>{{ scope.row[col.prop] }}</template>
                        </template>
                        <!-- 默认：输入框 -->
                        <template v-else>
                            <template v-if="scope.row.isEdit">
                                <el-input
                                    v-model="scope.row[col.prop]"
                                    :maxlength="col.maxlength"
                                    :minlength="col.minlength"
                                    :placeholder="!col.readonly ? '请输入' : ''"
                                    :readonly="col.readonly"
                                    clearable
                                    show-word-limit
                                    size="medium"
                                    @input="onChange($event, $event, col, scope)"
                                >
                                </el-input>
                            </template>
                            <template v-else>{{ scope.row[col.prop] }}</template>
                        </template>
                    </template>
                </el-table-column>
            </template>
            <!-- 操作列 -->
            <el-table-column
                v-if="option.menu !== undefined ? option.menu : true"
                :key="Math.random()"
                :align="option.align"
                :width="option.operationWidth || 200"
                fixed="right"
                label="操作"
            >
                <!--                <template slot="header">-->
                <!--                    <div style="float: left;">操作</div>-->
                <!--                    <div v-if="option.addBtn === undefined || option.addBtn"-->
                <!--                         style="cursor:pointer"-->
                <!--                         @click="rowAdd">添加-->
                <!--                    </div>-->
                <!--                </template>-->

                <template slot-scope="scope">
                    <!-- 查看 -->
                    <el-tooltip
                        v-if="option.viewBtn"
                        :enterable="false"
                        class="item"
                        content="查看"
                        effect="dark"
                        placement="top"
                    >
                        <el-button type="text" @click="rowView(scope)">
                            <i class="el-icon-view"></i>
                        </el-button>
                    </el-tooltip>
                    <!-- 保存 / 编辑 -->
                    <template v-if="option.editBtn">
                        <template v-if="scope.row.isEdit">
                            <el-tooltip
                                :enterable="false"
                                class="item"
                                content="保存"
                                effect="dark"
                                placement="top"
                            >
                                <el-button
                                    icon="el-icon-circle-check"
                                    type="text"
                                    @click="rowSave(scope.row, scope.$index)"
                                >
                                </el-button>
                            </el-tooltip>
                        </template>
                        <template v-else>
                            <el-tooltip
                                :enterable="false"
                                class="item"
                                content="编辑"
                                effect="dark"
                                placement="top"
                            >
                                <el-button type="text" @click="rowEdit(scope)">
                                    <i class="el-icon-edit"></i>
                                </el-button>
                            </el-tooltip>
                        </template>
                    </template>
                    <!-- 删除 -->
                    <el-tooltip
                        v-if="option.delBtn"
                        :enterable="false"
                        class="item"
                        content="删除"
                        effect="dark"
                        placement="top"
                    >
                        <el-button type="text" @click="rowDel(scope)">
                            <i class="el-icon-delete"></i>
                        </el-button>
                    </el-tooltip>
                    <!-- 复制 -->
                    <el-tooltip
                        v-if="option.copyBtn"
                        :enterable="false"
                        class="item"
                        content="复制"
                        effect="dark"
                        placement="top"
                    >
                        <el-button type="text" @click="rowCopy(scope.$index)">
                            <i class="el-icon-copy-document"></i>
                        </el-button>
                    </el-tooltip>
                    <!-- 调整顺序 -->
                    <template v-if="option.moveBtn">
                        <el-button v-if="!scope.row.isEdit" type="text">
                            <i
                                class="el-icon-caret-top"
                                @click="rowMoveup(scope.row, scope.$index)"
                            ></i>
                        </el-button>
                        <el-button v-if="!scope.row.isEdit" type="text">
                            <i
                                class="el-icon-caret-bottom"
                                @click="rowMovedown(scope.row, scope.$index)"
                            ></i>
                        </el-button>
                    </template>
                    <!-- 自定义按钮 -->
                    <template v-for="menuBtn in option.menuBtn">
                        <el-tooltip
                            :content="menuBtn.label"
                            :enterable="false"
                            class="item"
                            effect="dark"
                            placement="top"
                        >
                            <el-button type="text" @click="menuBtnClick(menuBtn, scope)">
                                <i :class="menuBtn.icon"></i>
                            </el-button>
                        </el-tooltip>
                    </template>
                    <slot
                        :rowData="{ row: scope.row, index: scope.$index }"
                        name="operation"
                    ></slot>
                </template>
            </el-table-column>
        </el-table>
        <div
            v-if="option.page === undefined ? true : option.page"
            class="demonstration"
        >
            <el-pagination
                :current-page.sync="pageOption.pageNum"
                :hide-on-single-page="
          this.value && this.value.length <= pageOption.pageSize
        "
                :page-size="pageOption.pageSize"
                :page-sizes="[5, 10, 20]"
                :total="pageOption.total"
                background
                layout="total, prev, pager, next, jumper"
                @current-change="currentChange"
                @size-change="sizeChange"
            >
            </el-pagination>
        </div>
        <childEditDialog
            :dataSource="dialogEdit.dataSource"
            :group="dialogEdit.group"
            :isEdit="dialogEdit.isEdit"
            :isNew="dialogEdit.isNew"
            :isView="dialogEdit.isView"
            :owner="owner"
            :visible="dialogEdit.visible"
            :width="dialogEdit.width"
            @callback="callbackEdit"
            @before-open="beforeOpen"
        ></childEditDialog>
    </div>
</template>

<script>
import schema from "async-validator";
import request from "@/router/axios";

const STR_DIALOG = "dialog";
const STR_IS_EDIT = "isEdit";
const STR_ROW_KEY = "nodesCurdRowId";
// 表格行是否编辑过(新增、编辑时为true)
const STR_HAS_EDIT = "nodesTableRowHasEdit";

let timer = null;

export default {
    name: "dynamicTable",
    components: {
        SelectSku: () => import("@/components/nodes/selectSku"),
        selectSkuPackage: () => import("@/components/nodes/selectSkuPackage"),
        selectEnterprise: () => import("@/components/nodes/selectEnterprise"),
        childEditDialog: () => import("@/components/nodes/editDialog"),
        treeSelect: () => import("@/components/nodes/treeSelect"),
    },
    props: {
        value: {
            type: Array,
            required: true,
            default: () => [],
        },
        option: {
            type: Object,
            required: true,
            default: function () {
                return {
                    index: true, // 是否显示序号列
                    indexLabel: "顺序", //序号列显示文字
                    align: "center", //对齐方式（left、center、right）
                    selection: true, //是否显示选择框
                    menu: true, //是否显示操作栏
                    menuBtn: [], // 操作栏自定义按钮
                    operationWidth: 200, //操作栏的宽度（默认 200）
                    addBtn: true, // 操作栏 - 添加按钮
                    viewBtn: false, // 操作栏 - 查看按钮
                    editBtn: true, // 操作栏 - 编辑按钮
                    delBtn: true, // 操作栏 - 删除按钮
                    moveBtn: true, // 操作栏 - 上下移动按钮
                    copyBtn: true, // 操作栏 - 复制按钮
                    addType: "", //数据添加类型（dialog:对话框添加，否则行内添加）
                    saveUrl: "", // 行保存接口地址
                    column: [],
                    children: [],
                    prop: "table",
                    page: true,
                };
            },
        },
        rowClassName: {
            type: Function,
            default: function ({row, rowIndex}) {
                return "";
            },
        },
        height: {type: Number, default: 240},
        owner: {
            type: Object, default: function () {
                return {};
            }
        },
        form: {type: Object, default: null},
        root: {type: Object, default: null},
    },
    data() {
        return {
            tableData: [], //表格数据
            column: [], //表头数据
            selectionList: [],
            loading: false,
            // 分页组建配置
            pageOption: {
                pageNum: 1,
                pageSize: 5,
                total: 0,
            },
            validFields: [],
            dialogEdit: {
                visible: false,
                isNew: false,
                isView: false,
                isEdit: false,
                group: [],
                dataSource: null,
                index: -1,
            },
            // 备份行的对象集合
            backupList: [],
        };
    },
    created() {
        this.$nextTick(function () {
            this.initData();
        });
        let self = this;
        timer = setInterval(() => {
            this.$nextTick(() => {
                if(self.$refs.dynamicTable){
                    self.$refs.dynamicTable.doLayout();
                }
            });
        }, 100);
    },
    mounted() {

    },
    destroyed() {
        this.option.column = [];
        this.tableData = [];
        clearInterval(timer);
    },
    computed: {
        maxPageSize() {
            let pageNum = this.pageOption.pageNum;
            let pageSize = this.pageOption.pageSize;
            let maxNum = pageNum * pageSize;
            return maxNum;
        },
        minPageSize() {
            let pageNum = this.pageOption.pageNum;
            let pageSize = this.pageOption.pageSize;

            let minNum = (pageNum - 1) * pageSize;
            return minNum || 1;
        },
    },
    watch: {
        value: {
            handler: function (newValue, oldValue) {
                this.initTableData();
            },
            deep: false,
        },
        option() {
            this.initColumn();
        }
    },
    methods: {
        // 初始化表格、数据
        initData() {
            this.initTableData();
            this.initColumn();
        },
        // 初始化表格
        initTableData() {
            let data = [];
            for (let i = 0; i < this.value.length; i++) {
                let row = this.value[i];
                // 对所有行生成唯一id
                this.$set(row, STR_ROW_KEY, this.guid());
                // 初始化需要显示的数据
                if (this.option.page) {
                    if (i < this.pageOption.pageSize) {
                        data.push(Object.assign({}, row));
                    }
                } else {
                    data.push(Object.assign({}, row));
                }
            }
            this.tableData = data;
            this.$set(this.pageOption, "total", this.value.length);
            this.pageOption.pageNum = 1;
            // if (this.option.page) {
            //     let data = [];
            //     for (let i = 0; i < this.value.length; i++) {
            //         let row = this.value[i];
            //         // 对所有行生成唯一id
            //         this.$set(row, STR_ROW_KEY, this.guid());
            //         // 初始化需要显示的数据
            //         if (i < this.pageOption.pageSize) {
            //             data.push(Object.assign({}, row));
            //         }
            //     }
            //     this.tableData = data;
            //     this.$set(this.pageOption, 'total', this.value.length);
            //     this.pageOption.pageNum = 1;
            // } else {
            //     this.tableData = this.value;
            // }
        },
        // 初始化表格列
        initColumn() {
            this.column = this.option.column;
            if (this.column) {
                this.column.forEach((col) => {
                    if (!col["dicData"] || col["dicData"].length === 0) {
                        this.$set(col, "dicData", []);
                        this.$set(col, "static", false);
                    } else {
                        this.$set(col, "static", true);
                    }
                });
            }
            // if (this.column) {
            //     this.column.forEach(col => {
            //
            //     });
            // }
            this.option.operationWidth = 0;
            if (this.option.viewBtn) {
                this.option.operationWidth += 30;
            }
            if (this.option.editBtn) {
                this.option.operationWidth += 30;
            }
            if (this.option.delBtn) {
                this.option.operationWidth += 30;
            }
            if (this.option.moveBtn) {
                this.option.operationWidth += 60;
            }
            if (this.option.copyBtn) {
                this.option.operationWidth += 30;
            }
            if (this.option.menuBtn && this.option.menuBtn.length > 0) {
                this.option.operationWidth += this.option.menuBtn.length * 30;
            }
            if (this.option.operationWidth < 80) {
                this.option.operationWidth = 80;
            }
            this.$forceUpdate();
        },
        // 表格序号生成方式
        indexMethod(index) {
            //表格序号显示规则
            return index + 1;
        },
        // 判断表格中是否存在处于编辑状态的行
        hasEdit() {
            //判断当前是否有编辑项
            let index = this.tableData.findIndex((v) => {
                return v.isEdit;
            });
            if (index >= 0) {
                this.validate(this.tableData[index], index, (valid) => {
                    if (valid) {
                        this.$message.warning("请保存数据");
                    }
                });
            }
            return index >= 0 ? true : false;
        },
        // 验证指定行
        validate(row, index, callback) {
            let rules = {};
            this.column.forEach((col) => {
                if (col.rules) {
                    this.$set(rules, col.prop, col.rules);
                }
            });
            //验证表单
            const validator = new schema(rules);
            let columns = document
                .querySelectorAll(
                    "#" + this.option.prop + " .el-table__body-wrapper tbody tr"
                )
                .item(index)
                .querySelectorAll("td");
            for (let i of columns) {
                i.classList.remove("is_error");
            }
            validator
                .validate(row)
                .then(() => {
                    callback && callback(true);
                })
                .catch(({errors}) => {
                    errors.forEach((v) => {
                        for (let i of columns) {
                            if (i.classList.contains("is_" + v.field)) {
                                i.classList.add("is_error");
                            }
                        }
                    });
                    callback && callback(false);
                });
        },
        // 行添加
        rowAdd() {
            // 判断是行内编辑还是对话框编辑
            if (this.option.children) {
                // 对话框添加数据
                this.dialogEdit.group = this.option.children;
                this.dialogEdit.isNew = true;
                this.dialogEdit.isView = false;
                this.dialogEdit.isEdit = false;
                this.dialogEdit.dataSource = null;
                this.dialogEdit.visible = true;
            } else {
                // 行内添加
                let hasEdit = this.hasEdit();
                if (hasEdit) {
                    // this.$message.warning('请保存数据')
                } else {
                    let newRow = {
                        isEdit: true,
                    };
                    this.$set(newRow, STR_HAS_EDIT, true);
                    let loadingCount = 0;
                    this.loading = true;
                    this.$set(newRow, STR_ROW_KEY, this.guid());
                    if (this.column) {
                        this.column.forEach((col) => {
                            if (col.type === "index") {
                                this.$set(
                                    newRow,
                                    col.prop,
                                    this.indexMethod(this.value.length)
                                );
                            }
                            if (!col.static) {
                                this.$set(col, "dicData", []);
                            }
                            // 调用接口，获取数据源
                            if (col.dicUrl) {
                                loadingCount++;
                                // 拼接接口参数
                                let requestUrl = col.dicUrl;

                                if (col.search && (this.owner || this.form)) {
                                    requestUrl += "?";
                                    let params = [];
                                    // 如果在上级没有找到对应字段，就往上上级找
                                    for (let key in col.search) {
                                        if (this.owner) {
                                            if (this.owner[key]) {
                                                params.push(key + "=" + this.owner[key]);
                                            }
                                        }
                                        if (this.form) {
                                            if (this.form[key]) {
                                                params.push(key + "=" + this.form[key]);
                                            }
                                        }
                                    }

                                    requestUrl += params.join("&");
                                }
                                request({
                                    url: requestUrl,
                                    method: "get",
                                })
                                    .then((res) => {
                                        this.$set(col, "dicData", res.data.data);
                                    })
                                    .finally(() => {
                                        loadingCount--;
                                    });
                            }
                            if (col.default !== undefined) {
                                let value;
                                if (typeof col.default === "function") {
                                    value = col.default(this.owner ? this.owner : this.value);
                                } else {
                                    value = col.default;
                                }
                                this.$set(newRow, col.prop, value);
                            } else {
                                if (col.type === "inputNumber") {
                                    this.$set(newRow, col.prop, col.min ? col.min : 0);
                                } else {
                                    this.$set(newRow, col.prop, "");
                                }
                            }
                        });
                    }
                    let self = this;
                    let interval = setInterval(function () {
                        if (loadingCount === 0) {
                            self.$emit("before-add", newRow);
                            if (self.tableData.length >= self.pageOption.pageSize) {
                                self.tableData.unshift(newRow);
                                // 超出每页显示的，删除掉
                                self.tableData.splice(self.tableData.length - 1, 1);
                            } else {
                                self.tableData.unshift(newRow);
                            }
                            if (self.option.rowAdd) {
                                self.option.rowAdd(newRow, self.root);
                            }
                            self.loading = false;
                            clearInterval(interval);
                        }
                    });
                }
            }
        },
        // 行保存
        rowSave(row, index) {
            // 保存行（进行校验）
            this.validate(row, index, (valid) => {
                if (valid) {
                    if (this.option.beforeAdd) {
                        let result = this.option.beforeAdd(this.owner, row, this);
                        if (result != undefined && !result) {
                            return false;
                        }
                    }
                    if (this.option.saveUrl && this.option.saveUrl.length > 0) {
                        this.loading = true;
                        // 调用接口提交
                        request({
                            url: this.option.saveUrl,
                            method: "post",
                            data: row,
                        })
                            .then((res) => {
                                this.$set(row, STR_IS_EDIT, false);
                                this.$set(row, "unSubmit", false);
                                this.saveRowToValue(row, index);
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    } else {
                        // 保存前先将备份移除掉
                        if (this.backupList) {
                            for (let i = 0; i < this.backupList.length; i++) {
                                let backupRow = this.backupList[i];
                                if (backupRow[STR_ROW_KEY] !== row[STR_ROW_KEY]) {
                                    continue;
                                }
                                this.backupList.splice(i, 1);
                            }
                        }
                        // 修改行的编辑状态
                        this.$set(row, STR_IS_EDIT, false);
                        this.saveRowToValue(row, index);
                    }
                }
            });
        },
        // 处理行到原始数据中
        saveRowToValue(row, index) {
            // 从所有数据中查看该 key 是否存在（存在为编辑保存，不存在为新增保存）
            let findIndex = this.value.findIndex(
                (item) => item[STR_ROW_KEY] === row[STR_ROW_KEY]
            );
            if (findIndex > -1) {
                this.value.splice(findIndex, 1, row);
            } else {
                this.value.unshift(Object.assign({}, row));
            }
            this.$forceUpdate();
            this.$emit("saved", row, index);
            if (this.option.rowSaved) {
                this.option.rowSaved(this.root);
            }
        },
        // 行编辑
        rowEdit(scope) {
            if (this.option.children) {
                // 对话框编辑
                this.dialogEdit.group = this.option.children;
                this.dialogEdit.isNew = false;
                this.dialogEdit.isView = false;
                this.dialogEdit.isEdit = true;
                this.dialogEdit.dataSource = Object.assign({}, scope.row);
                this.dialogEdit.visible = true;
                this.dialogEdit.index =
                    scope.$index +
                    this.pageOption.pageSize * (this.pageOption.pageNum - 1);
            } else {
                // 行内编辑
                this.$emit("before-edit", scope.row);
                //编辑行
                let hasEdit = this.hasEdit();

                if (!hasEdit) {
                    let loadingCount = 0;
                    this.loading = true;
                    if (this.column) {
                        this.column.forEach((col) => {
                            if (!col.dicData || col.dicData.length === 0) {
                                // 调用接口，获取数据源
                                if (col.dicUrl) {
                                    loadingCount++;
                                    // 拼接接口参数
                                    let requestUrl = col.dicUrl;

                                    if (col.search && (this.owner || this.form)) {
                                        requestUrl += "?";
                                        let params = [];
                                        // 如果在上级没有找到对应字段，就往上上级找
                                        for (let key in col.search) {
                                            if (this.owner) {
                                                if (this.owner[key]) {
                                                    params.push(key + "=" + this.owner[key]);
                                                }
                                            }
                                            if (this.form) {
                                                if (this.form[key]) {
                                                    params.push(key + "=" + this.form[key]);
                                                }
                                            }
                                        }

                                        requestUrl += params.join("&");
                                    }
                                    request({
                                        url: requestUrl,
                                        method: "get",
                                    })
                                        .then((res) => {
                                            if (typeof (col.isOther) != "undefined" && !col.isOther) {
                                                let data = res.data.data.filter(item => item[col.props['value']] == scope.row[col.prop]);
                                                this.$set(col, "dicData", data);
                                            } else {
                                                this.$set(col, "dicData", res.data.data);
                                            }
                                        })
                                        .finally(() => {
                                            loadingCount--;
                                        });
                                }
                            }
                        });
                    }
                    let self = this;
                    let interval = setInterval(function () {
                        if (loadingCount === 0) {
                            // 先备份行，再修改编辑状态
                            if (!scope.row[STR_ROW_KEY]) {
                                self.$set(scope.row, STR_ROW_KEY, self.guid());
                            }

                            self.backupList.push(Object.assign({isEdit: false}, scope.row));
                            // 修改编辑状态
                            self.$set(scope.row, STR_IS_EDIT, true);
                            self.$set(scope.row, STR_HAS_EDIT, true);

                            // 触发组件change事件
                            if (self.option.column) {
                                self.option.column.forEach((col) => {
                                    if (col.change) {
                                        let val = scope.row[col.prop];
                                        self.onChange(val, null, col, scope, false);
                                    }
                                });
                            }
                            self.loading = false;
                            clearInterval(interval);
                        }
                    });
                }
            }
        },
        // 行查看
        rowView(scope) {
            if (this.option.children) {
                // 对话框编辑
                this.dialogEdit.group = this.option.children;
                this.dialogEdit.isNew = false;
                this.dialogEdit.isView = true;
                this.dialogEdit.isEdit = false;
                this.dialogEdit.dataSource = Object.assign({}, scope.row);
                this.dialogEdit.visible = true;
                this.dialogEdit.index =
                    scope.$index +
                    this.pageOption.pageSize * (this.pageOption.pageNum - 1);
            }
        },
        // 行删除
        rowDel(scope) {
            let row = scope.row;
            let findIndex = this.value.findIndex(
                (item) => item[STR_ROW_KEY] === row[STR_ROW_KEY]
            );
            let sourceRow = Object.assign({}, this.value[findIndex]);
            this.$set(sourceRow, STR_HAS_EDIT, true);
            if (this.option.del) {
                if (findIndex === -1 || !row[this.option.del.id]) {
                    this.rowDelete(findIndex, sourceRow);
                } else {
                    this.$confirm("确定删除当前数据？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    }).then(() => {
                        this.loading = true;
                        let ids = row[this.option.del.id];
                        // 调用接口提交
                        request({
                            url: this.option.del.url,
                            method: "post",
                            params: {
                                ids,
                            },
                        })
                            .then((res) => {
                                this.rowDelete(findIndex, sourceRow);
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    });
                }
            } else {
                this.rowDelete(findIndex, sourceRow);
            }
        },
        // 处理行删除，从原数据中移除行
        rowDelete(findIndex, sourceRow) {
            if (findIndex > -1) {
                // 从所有数据中删除
                this.value.splice(findIndex, 1);
            }
            // 如果有index列，调整index列的值
            for (let i = 0; i < this.value.length; i++) {
                let row = this.value[i];
                // 找到index列
                for (let j = 0; j < this.column.length; j++) {
                    let col = this.column[j];
                    if (col.type !== "index") {
                        continue;
                    }
                    if (!row[col.prop] || !sourceRow[col.prop]) {
                        continue;
                    }
                    if (row[col.prop] > sourceRow[col.prop]) {
                        row[col.prop]--;
                    }
                }
            }
            // 重置表格中得数据
            this.currentChange(this.pageOption.pageNum);
            this.$emit("deleted", sourceRow, findIndex);
        },
        // 行复制
        rowCopy(index) {
            let row = this.value[index];
            if (row.isEdit) {
                // 如果当前行为编辑状态，则自动保存后再复制
                this.validate(row, index, (valid) => {
                    if (valid) {
                        this.$set(row, STR_IS_EDIT, false);
                        this.$forceUpdate();
                        this.$emit("saved", row);
                        this.copyItem(row, index);
                    }
                });
                return;
            } else {
                // 非编辑状态直接复制
                this.copyItem(row, index);
            }
        },
        // 复制指定行的数据
        copyItem(row, index) {
            let obj = {
                isEdit: false,
            };
            this.$set(obj, STR_ROW_KEY, this.guid());
            this.option.column.forEach((col) => {
                this.$set(obj, col.prop, row[col.prop]);
                if (col.show) {
                    col.show.forEach((item) => {
                        this.$set(obj, item, row[item]);
                    });
                }
            });
            if (this.tableData.length >= this.pageOption.pageSize) {
                this.value.splice(index + 1, 0, obj);
                this.tableData.splice(this.tableData.length - 1, 1);
            } else {
                this.tableData.unshift(obj);
                this.value.splice(0, 0, obj);
            }
        },
        // 行向上移动
        rowMoveup(row, index) {
            //行上移
            if (index === 0) {
                return;
            }
            // 判断是否有索引行
            let filterList = this.column.filter((col) => {
                return col.type === "index";
            });
            if (filterList && filterList.length > 0) {
                let indexObj = filterList[0];
                let findObj = this.value[index];
                let end_index = index;
                let start_index = index - 1;

                for (let i = start_index; i < end_index; i++) {
                    let obj = this.value[i];
                    obj[indexObj.prop] = obj[indexObj.prop] + 1;
                }
                findObj[indexObj.prop] = findObj[indexObj.prop] - 1;
                this.$set(findObj, STR_HAS_EDIT, true);
                this.value = this.value.sort(function (a, b) {
                    return a[indexObj.prop] - b[indexObj.prop];
                });
            }
        },
        // 行向下移动
        rowMovedown(row, index) {
            //行下移
            if (index === this.value.length - 1) {
                return;
            }
            // 判断是否有索引行
            let filterList = this.column.filter((col) => {
                return col.type === "index";
            });
            if (filterList && filterList.length > 0) {
                let indexObj = filterList[0];
                let findObj = this.value[index];
                let end_index = index;
                let start_index = index + 1;

                for (let i = start_index; i > end_index; i--) {
                    let obj = this.value[i];
                    obj[indexObj.prop] = obj[indexObj.prop] - 1;
                }
                findObj[indexObj.prop] = findObj[indexObj.prop] + 1;
                this.$set(findObj, STR_HAS_EDIT, true);
                this.value = this.value.sort(function (a, b) {
                    return a[indexObj.prop] - b[indexObj.prop];
                });
            }
        },
        // 递归查找对象
        findObj(val, prop, dataSource) {
            for (let i = 0; i < dataSource.length; i++) {
                let data = dataSource[i];
                if (data[prop] === val) {
                    return data;
                } else {
                    if (data.children && data.children.length > 0) {
                        data = this.findObj(val, prop, data.children);
                        if (data) {
                            return data;
                        }
                    }
                }
            }
        },
        // 选择框值变化事件处理
        onChange(val, obj, col, scope, clear = true) {
            if (col) {
                // 设置表单属性
                if (obj) {
                    if (col.props && obj[col.props.value]) {
                        this.$set(scope.row, col.prop, obj[col.props.value]);
                    }
                } else {
                    if (col.dicData) {
                        obj = this.findObj(
                            val,
                            col.props ? col.props.value : "value",
                            col.dicData
                        );
                    }
                }
                // 级联不为空的情况下
                if (col.cascaderItem) {
                    if (clear) {
                        this.clearItem(col.cascaderItem, scope.row);
                    }
                    // 获取当前列级联列
                    col.cascader = [];
                    this.column.forEach((column) => {
                        if (col.cascaderItem.indexOf(column.prop) > -1) {
                            col.cascader.push(column);
                            // 清空子级表格显示的值
                            // if (column.show) {
                            //     column.show.forEach(item => {
                            //         this.$set(scope.row, item, '');
                            //         this.onChange('', null, column, scope, true);
                            //     });
                            // }
                        }
                    });
                }
                // 触发事件
                if (col.change) {
                    col.change(val, obj, scope, this);
                }
            }
            this.procSearchData(this.column, scope.row);
        },
        // 处理搜索条件
        procSearchData(columns, row) {
            let searchColumnList = [];
            columns.forEach((column) => {
                if (column.search) {
                    searchColumnList.push(column);
                }
                if (column.children) {
                    column.children.forEach((child) => {
                        if (child.search) {
                            searchColumnList.push(child);
                        }
                    });
                }
            });
            if (searchColumnList && searchColumnList.length > 0) {
                searchColumnList.forEach((col) => {
                    if (!col["searchData"]) {
                        this.$set(col, "searchData", {});
                    }
                    for (let key in col.search) {
                        const value = row[key];
                        if (value !== undefined) {
                            this.$set(col.searchData, key, value);
                        } else {
                            this.$set(col.searchData, key, col.search[key]);
                        }
                    }
                });
            }
        },
        // 清空数据源/值
        clearItem(items, row) {
            if (!items) {
                return;
            }
            items.forEach((item) => {
                // 清空字段的值
                if (row[item]) {
                    this.$set(row, item, "");
                }

                // 清空数据源的值
                this.column.forEach((column) => {
                    // 清空子级
                    if (column.prop === item) {
                        if (column["dicData"]) {
                            this.$set(column, "dicData", []);
                        }
                        if (column.show) {
                            for (let i = 0; i < column.show.length; i++) {
                                this.$set(column, column.show[i], "");
                            }
                        }
                        if (column.cascaderItem) {
                            this.clearItem(column.cascaderItem, row);
                        }
                    }
                });
            });
        },
        // 对话框的回调事件
        callbackEdit(callback) {
            if (callback.success) {
                let data = callback.data;
                this.$set(data, STR_HAS_EDIT, true);
                if (!this.dialogEdit.isView && !this.dialogEdit.dataSource) {
                    // 如果存在索引行，则给索引行赋值
                    let filterList = this.column.filter((col) => {
                        return col.type === "index";
                    });
                    if (filterList && filterList.length > 0) {
                        this.$set(
                            data,
                            filterList[0].prop,
                            this.indexMethod(this.value.length)
                        );
                    }

                    this.value.push(data);
                } else if (this.dialogEdit.dataSource && !this.dialogEdit.isView) {
                    // 编辑
                    if (this.dialogEdit.index > -1) {
                        this.value.splice(this.dialogEdit.index, 1, data);
                    }
                }
            }
            this.dialogEdit.visible = callback.visible;
        },
        // 对话框打开时事件
        beforeOpen(done, type, finish) {
            if (type !== undefined && type !== "new") {
                done();
            }
        },
        // 还原或删除未保存的行
        restoreUnSaveRow() {
            if (this.value) {
                for (let i = 0; i < this.value.length; i++) {
                    let row = this.value[i];
                    if (row.isEdit) {
                        // 判断再备份中是否存在
                        let backupObj = this.backupList.find(
                            (u) => u[STR_ROW_KEY] === row[STR_ROW_KEY]
                        );
                        if (!backupObj) {
                            // 在备份 backupList 中不存在的表示为新增的行，可以删除掉
                            this.value.splice(i, 1);
                        } else {
                            // 在备份 backupList 中存在的表示为修改的行，需要还原数据
                            this.value.splice(i, 1, Object.assign({}, backupObj));
                        }
                    }
                }
            }
            // 清空备份
            this.backupList = [];
        },
        // 生成唯一id
        guid() {
            return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(
                /[xy]/g,
                function (c) {
                    let r = (Math.random() * 16) | 0,
                        v = c == "x" ? r : (r & 0x3) | 0x8;
                    return v.toString(16);
                }
            );
        },
        // 当前页变化
        currentChange(current) {
            let startIndex = (current - 1) * this.pageOption.pageSize; // 起始索引
            let endIndex = startIndex; // 结束索引
            let pageCount = 1; // 总页数
            if (this.value.length % this.pageOption.pageSize === 0) {
                pageCount = Math.floor(this.value.length / this.pageOption.pageSize);
            } else {
                pageCount =
                    Math.floor(this.value.length / this.pageOption.pageSize) + 1;
            }
            // 计算结束位置
            if (
                current < pageCount ||
                this.value.length % this.pageOption.pageSize === 0
            ) {
                endIndex = startIndex + this.pageOption.pageSize;
            } else {
                endIndex = startIndex + (this.value.length % this.pageOption.pageSize);
            }
            let data = [];
            if (startIndex < this.value.length) {
                for (let i = startIndex; i < endIndex; i++) {
                    data.push(Object.assign({}, this.value[i]));
                }
            }
            this.tableData = data;
        },
        sizeChange(size) {
            this.pageOption.pageSize = size;
            this.currentChange(1);
        },
        selectionChange(val) {
            this.selectionList = val;
            this.$emit("selection-change", val);
        },
        menuBtnClick(col, scope) {
            if (col.click) {
                col.click(scope, this);
            }
        },
        getHide(col) {
            if (col.hide !== undefined) {
                if (typeof col.hide === "function") {
                    return col.hide(this.owner);
                } else {
                    return !col.hide;
                }
            } else {
                return true;
            }
        },
    },
};
</script>
<style lang="scss">

.table-style .el-table-column--selection .cell {
    padding-right: 10px;
}

.table-style .operation-item {
    display: inline-block;
    margin: 0 5px;
}

.table-style .add-row {
    cursor: pointer;
}

.table-style .el-form-item {
    margin-bottom: 0px;
}

.table-style .is_error .el-input__inner {
    border-color: #f56c6c;
}

.demonstration {
    text-align: right;
    margin-top: 10px;
}

.table-style .hidden-row {
    display: none;
}

.el-pager {
    border: none;
}

input[type="number"] {
    -moz-appearance: textfield;
}

input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
</style>
