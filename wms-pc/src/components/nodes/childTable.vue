<template>
    <div class="dynamic-table-box">
        <el-table
            class="table-style"
            id="dynamicTable"
            ref="dynamicTable"
            :data="tableData"
            border
            height="218px"
            style="width: 100%"
            show-overflow-tooltip
        >
            <el-table-column
                type="index"
                :index="indexMethod"
                v-if="option.index"
                :align="option.align"
                width="55"
            >
                <template slot="header">{{ option.indexLabel || '#' }}</template>
            </el-table-column>

            <el-table-column v-if="option.selection" :align="option.align" type="selection"
                             width="55"></el-table-column>

            <template v-for="col in column">
                <el-table-column
                    :align="option.align"
                    :prop="col.prop"
                    :label="col.label"
                    :key="col.prop"
                    :width="col.width"
                    :class-name="'is_'+col.prop"
                    :show-overflow-tooltip="true"
                >
                    <template slot-scope="scope">
                        <template v-if="col.type">
                            <!-- 文本框 -->
                            <template v-if="col.type == 'input'">
                                <template v-if="scope.row.isEdit">
                                    <template v-if="col.dialogType == 'selectSkuPackage'">
                                        <selectSkuPackage
                                            v-model="scope.row[col.prop]"
                                            :text="scope.row[col.prop+'_text']"
                                            size="medium"
                                            @change="(val, obj)=>skuPackageChange({id: val, obj, scope, type: 'skuPackageChange'})"
                                        ></selectSkuPackage>
                                    </template>
                                    <template v-else-if="col.dialogType == 'SelectSku'">
                                        <SelectSku
                                            v-model="scope.row[col.prop]"
                                            :text="scope.row[col.prop+'_text']"
                                            :search="search"
                                            size="medium"
                                            @change="(val, obj)=>skuPackageChange({id: val, obj, scope, type: 'SelectSku'})"
                                        ></SelectSku>
                                    </template>
                                    <template v-else-if="col.dialogType == 'selectEnterprise'">
                                        <selectEnterprise
                                            v-model="scope.row[col.prop]"
                                            :text="scope.row[col.prop+'_text']"
                                            :search="search"
                                            size="medium"
                                            @change="(val, obj)=>skuPackageChange({id: val, obj, scope, type: 'selectEnterprise'})"
                                        ></selectEnterprise>
                                    </template>
                                </template>
                                <template v-else>
                                    <template v-if="col.dialogType == 'selectSkuPackage'">
                                        <span>{{ col.prop && col.prop.length === 1 ? scope.row[col.prop[0]] : scope.row[col.prop + '_text'] }}</span>
                                        <!-- <template v-if="col.dialogType == 'selectSkuPackage'">
                                          <selectSkuPackage
                                            :disabled="true"
                                            :text="scope.row[col.prop+'_text']"
                                            v-model="scope.row[col.prop]"
                                            size="medium"
                                          ></selectSkuPackage>
                                        </template> -->
                                    </template>
                                    <template v-else-if="col.dialogType == 'SelectSku'">
                                        <SelectSku
                                            :disabled="true"
                                            :text="scope.row[col.prop+'_text']"
                                            v-model="scope.row[col.prop]"
                                            size="medium"
                                        ></SelectSku>
                                    </template>
                                    <template v-else-if="col.dialogType == 'selectEnterprise'">
                                        <selectEnterprise
                                            :disabled="true"
                                            :text="scope.row[col.prop+'_text']"
                                            v-model="scope.row[col.prop]"
                                            size="medium"
                                        ></selectEnterprise>
                                    </template>
                                </template>
                            </template>
                            <!-- 计数器 -->
                            <template v-if="col.type == 'inputnumber'">
                                <template v-if="scope.row.isEdit">
                                    <el-input-number
                                        v-model="scope.row[col.prop]"
                                        size="medium"
                                        controls-position="right"
                                        :min="0"
                                        :max="10"
                                    ></el-input-number>
                                </template>
                                <template v-else>{{ scope.row[col.prop] }}</template>
                            </template>
                            <!-- 下拉框 -->
                            <template v-if="col.type == 'select'">
                                <template v-if="scope.row.isEdit">
                                    <el-select
                                        v-model="scope.row[col.prop]"
                                        placeholder="请选择"
                                        size="medium"
                                        clearable
                                        @change="selectChanged($event, scope)"
                                    >
                                        <el-option
                                            v-for="item in col.dicData"
                                            :key="item.value"
                                            :label="item.label"
                                            :value="item.value"
                                        ></el-option>
                                    </el-select>
                                </template>
                                <template v-else show-overflow-tooltip>
                                    <template v-for="item in col.dicData">
                                        <span v-if="item.value === scope.row[col.prop]"
                                              :key="item.value">{{ item.label }}</span>
                                    </template>
                                </template>
                            </template>
                            <template v-if="col.type == 'input-select'">
                                <template v-if="scope.row.isEdit">
                                    <el-select v-model="scope.row[col.prop]"
                                               clearable
                                               @change="selectChanged($event, scope)"
                                               placeholder="请选择" size="medium">
                                        <el-option
                                            v-for="(item, index) in scope.row.option.dicData[col.prop]"
                                            :key="index"
                                            :label="item.label"
                                            :value="item.value"
                                        ></el-option>
                                    </el-select>
                                </template>
                                <template v-else show-overflow-tooltip>
                                    <template v-if="scope.row.option.dicData[col.prop].length">
                                        <template v-for="item in scope.row.option.dicData[col.prop]">
                                            <span v-if="item.value == scope.row[col.prop]"
                                                  :key="item.value">{{ item.label }}</span>
                                        </template>
                                    </template>
                                    <template v-else>
                                        <span> {{ scope.row[col.prop + '_text'] }}</span>
                                    </template>

                                </template>
                            </template>
                            <!-- switch开关 -->
                            <template v-if="col.type == 'switch'">
                                <template v-if="scope.row.isEdit">
                                    <el-switch
                                        v-model="scope.row[col.prop]"
                                        :active-text="col.dicData[0].label"
                                        :inactive-text="col.dicData[1].label"
                                        :active-value="col.dicData[0].value"
                                        :inactive-value="col.dicData[1].value"
                                    ></el-switch>
                                </template>
                                <template v-else>
                                    <template v-for="item in col.dicData">
                                        <span v-if="item.value == scope.row[col.prop]"
                                              :key="item.value">{{ item.label }}</span>
                                    </template>
                                </template>
                            </template>

                            <!-- 日期 -->
                            <template v-if="col.type == 'date'">
                                <template v-if="scope.row.isEdit">
                                    <el-date-picker
                                        v-model="scope.row[col.prop]"
                                        type="date"
                                        placeholder="选择日期"
                                        format="yyyy-MM-dd"
                                        value-format="yyyy-MM-dd"
                                    ></el-date-picker>
                                </template>
                                <template v-else>{{ scope.row[col.prop] }}</template>
                            </template>
                        </template>

                        <!-- 普通文本 -->
                        <template v-else>{{ scope.row[col.prop] }}</template>
                    </template>
                </el-table-column>
            </template>

            <el-table-column
                v-if="option.menu !== undefined ? option.menu : true"
                :align="option.align"
                fixed="right"
                label="操作"
                :width="option.operationWidth || 200"
            >
                <template slot="header">
                    <div style="float: left;">操作</div>
                    <div style="cursor:pointer" @click="rowAdd" v-if="dialogType != '查看'">添加</div>
                </template>
                <!-- v-if="dialogType != '查看'" -->
                <template slot-scope="scope" v-if="dialogType != '查看'">
                    <el-button type="text" size="medium">
                        <template v-if="scope.row.isEdit">
                            <!-- <i class="el-icon-circle-check" @click="rowSave(scope)"></i> -->
                            <el-tooltip class="item" :enterable="false" effect="dark" content="保存" placement="top">
                                <el-button type="text" size="medium" @click="rowSave(scope)">
                                    <i class="el-icon-circle-check"></i>
                                </el-button>
                            </el-tooltip>
                        </template>
                        <template v-else>
                            <!-- <i class="el-icon-edit" @click="rowEdit(scope.row)"></i> -->
                            <el-tooltip class="item" :enterable="false" effect="dark" content="编辑" placement="top">
                                <el-button type="text" size="medium" @click="rowEdit(scope)">
                                    <i class="el-icon-edit"></i>
                                </el-button>
                            </el-tooltip>
                        </template>
                    </el-button>
                    <el-tooltip class="item" :enterable="false" effect="dark" content="删除" placement="top">
                        <el-button type="text" size="medium" @click="rowDel(scope.row)">
                            <i class="el-icon-delete"></i>
                        </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" :enterable="false" effect="dark" content="复制" placement="top">
                        <el-button type="text" size="medium" @click="rowCopy(scope.$index)">
                            <i class="el-icon-copy-document"></i>
                        </el-button>
                    </el-tooltip>

                    <template v-if="option.moveBtn && tableData.length != 1">
                        <el-button type="text" size="medium" v-if="!scope.row.isEdit && scope.$index != 0">
                            <i class="el-icon-caret-top" @click="rowMoveup(scope.row, scope.$index)"></i>
                        </el-button>
                        <el-button
                            type="text"
                            size="medium"
                            v-if="!scope.row.isEdit && scope.$index != tableData.length - 1"
                        >
                            <i class="el-icon-caret-bottom" @click="rowMovedown(scope.row, scope.$index)"></i>
                        </el-button>
                    </template>

                    <slot name="operation" :rowData="{row: scope.row, index: scope.$index}"></slot>
                </template>
            </el-table-column>
        </el-table>
        <div class="demonstration">
            <el-pagination
                :page-size="pageOption.pageSize"
                :current-page.sync="pageOption.pageNum"
                layout="total, prev, pager, next"
                :total="pageOption.total"
            ></el-pagination>
        </div>
    </div>
</template>

<script>
import schema from "async-validator";
import {getDetail} from "@/api/wms/basedata/skupackage";

export default {
    name: "dynamicTable",
    components: {
        SelectSku: () => import("@/components/nodes/selectSku"),
        selectSkuPackage: () =>
            import("@/components/nodes/selectSkuPackage"),
        selectEnterprise: () => import("@/components/nodes/selectEnterprise")
    },
    props: {
        value: {
            type: Array,
            required: true,
            default: () => []
        },
        disabled: {type: Boolean, default: false},
        option: {
            type: Object,
            required: true,
            default: () => {
            }
        },
        search: {
            type: Object,
            default: () => {
            }
        },
        dialogType: {
            type: String
        },
    },
    data() {
        return {
            tableData: [], //表格数据
            column: [], //表头数据
            rules: {}, //验证规则
            //dicData: {}
            // 分页组建配置
            pageOption: {
                pageNum: 1,
                pageSize: 5,
                total: 0
            }
        };
    },
    created() {
        this.initData();
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
        }
    },
    watch: {
        value() {
            this.initData();
        },
        option() {
            this.initData();
        }
    },
    methods: {
        tableRowClassName({rowIndex}) {
            let index = rowIndex || rowIndex + 1;
            if (index >= this.minPageSize && index < this.maxPageSize) {
                return "";
            }
            return "hidden-row";
        },
        initData() {
            //初始化数据
            this.initTableData();
            this.initColumn();
            this.initRules();
        },
        initTableData() {
            this.tableData = this.value;
            this.$set(this.pageOption, 'total', this.tableData.length);
        },
        initColumn() {
            this.column = this.option.column;
        },
        initRules() {
            this.rules = this.option.rules || {};
        },

        returnBackData() {
            //更新数据
            this.$emit("input", this.tableData);
        },
        indexMethod(index) {
            //表格序号显示规则
            return index + 1;
        },
        hasEdit() {
            //判断当前是否有编辑项
            let index = this.tableData.findIndex(v => {
                return v.isEdit;
            });
            if (index >= 0) {
                this.validate({row: this.tableData[index], $index: index}, valid => {
                    if (valid) {
                        //this.$message.warning("请保存数据");
                    }
                });
            }
            return index >= 0 ? true : false;
        },
        rowAdd() {
            //新增行（报漏到父组件来完成）
            let hasEdit = this.hasEdit();
            if (hasEdit) {
                //this.$message.warning('请保存数据')
            } else {
                this.$emit("rowAdd");
            }
        },
        inputDialog(scope) {
            //文本框选择
            this.$emit("inputDialog", scope);
        },
        validate({row, $index}, callBack) {
            //验证表单
            const validator = new schema(this.rules);
            let columns = document
                .querySelectorAll("#dynamicTable .el-table__body-wrapper tbody tr")
                .item($index)
                .querySelectorAll("td");
            for (let i of columns) {
                i.classList.remove("is_error");
            }
            validator
                .validate(row)
                .then(() => {
                    callBack && callBack(true);
                })
                .catch(({errors}) => {
                    errors.forEach(v => {
                        for (let i of columns) {
                            if (i.classList.contains("is_" + v.field)) {
                                i.classList.add("is_error");
                            }
                        }
                    });
                    callBack && callBack(false);
                });
        },
        rowSave(scope) {
            //保存行（进行校验）
            this.validate(scope, valid => {
                if (valid) {
                    scope.row.isEdit = false;
                    this.$emit("rowSave", scope.row);
                } else {
                    //this.$message.error("请完善数据");
                }
            });
        },
        rowEdit(scope) {
            //编辑行
            let hasEdit = this.hasEdit();
            if (hasEdit) {
                //this.$message.warning('请保存数据')
            } else {
                scope.row.isEdit = true;
                this.$emit('rowEdit', scope);
            }
        },
        rowDel(row, index) {
            //alert(111)
            //删除行
            //this.tableData.splice(index, 1);
            this.$emit("rowDel", row, index);
        },
        rowCopy(index) {
            //复制行
            this.$emit('rowCopy', index);
        },
        rowMoveup(row, index) {
            //行上移
            if (index == 0) return;
            let tableData = this.tableData;
            tableData.splice(index, 1);
            tableData.splice(index - 1, 0, row);
        },
        rowMovedown(row, index) {
            //行下移
            if (index == this.tableData.length - 1) return;
            let tableData = this.tableData;
            tableData.splice(index, 1);
            tableData.splice(index + 1, 0, row);
        },
        //下拉select
        selectChanged(id, scope) {
            this.$emit("selectChanged", {id, scope});
        },
        skuPackageChange({id, obj, scope, type}) {
            if (id) {
                switch (type) {
                    case "skuPackageChange": //包装
                        getDetail(id).then(res => {
                            let dicData = [];
                            if (
                                res.data.data &&
                                res.data.data.skuPackageDetailVOList instanceof Array
                            ) {
                                res.data.data.skuPackageDetailVOList.forEach(item => {
                                    //if (item.skuLevel == "1") {
                                    dicData.push({
                                        label: item.wsuName,
                                        value: item.wsuId
                                    });
                                    //  }
                                });
                            }
                            var text = obj.wspName;
                            this.$emit("inputDialog", {scope, dicData, text, obj});
                        });
                        break;

                    case "SelectSku": //替代物品
                        var text = obj.skuName;
                        this.$emit("inputDialog", {scope, text, obj});
                        break;

                    case "selectEnterprise": //供应商
                        var text = obj.enterpriseName;
                        this.$emit("inputDialog", {scope, text, obj});
                        break;
                }
            }
        }
    }
};
</script>

<style>
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
</style>
