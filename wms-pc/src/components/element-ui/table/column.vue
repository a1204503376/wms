<template>
    <div>
        <slot name="header"></slot>
        <!-- 动态列 -->
        <template v-for="(col, index) in columnOption">
            <el-table-column
                :key="index"
                :align="col.align || tableOption.align"
                :fixed="col.fixed"
                :width="col.width"
                :header-align="col.headerAlign || tableOption.headerAlign"
                :label="col.aliasName || col.label"
                :prop="col.prop"
                :class-name="'is_' + col.prop"
                :render-header="col.renderHeader"
                :show-overflow-tooltip="col.overHidden !== false"
                filter-placement="bottom-end">
                <template slot-scope="scope">
                    <slot :name="col.prop"
                          v-if="col.slot"
                          v-bind="scope.row"
                    ></slot>
                    <template v-else-if="columnOption.edit != false" slot-scope="scope">
                        <!-- 普通文本 -->
                        <template v-if="col.type == 'text'">
                            <template>
                                <span>{{ scope.row[col.prop] }}</span></template>
                        </template>
                        <!-- 物品 -->
                        <template v-else-if="col.type == 'selectSku'">
                            <template v-if="scope.row.$edit">
                                <select-sku-table v-model="scope.row[col.prop]"
                                                  style="width:100%;"
                                                  :disabled="col.disabled"
                                                  :before-open="(val)=>col.beforeOpen(val, table.formData, scope)"
                                                  @change="(val, obj)=>onChange(val, obj, col, scope)"
                                ></select-sku-table>
                                <!--                                <SelectSku-->
                                <!--                                    v-model="scope.row[col.prop]"-->
                                <!--                                    :before-open="col.beforeOpen(scope)"-->
                                <!--                                    :search="col.searchData ? col.searchData : {}"-->
                                <!--                                    :text="-->
                                <!--                    col.props ? scope.row[col.props.label] : scope.row[col.prop]-->
                                <!--                  "-->
                                <!--                                    size="medium"-->
                                <!--                                    @change="(val, obj) => onChange(val, obj, col, scope)"-->
                                <!--                                ></SelectSku>-->
                            </template>
                            <template v-else>
                                <span>
                                    {{
                                        getTableContent(scope, col)
                                    }}
                                </span>
                            </template>
                        </template>
                        <!-- 物品包装 -->
                        <template v-else-if="col.type == 'selectSkuPackage'">
                            <template v-if="scope.row.$edit">
                                <select-table-sku-package v-model="scope.row[col.prop]"
                                                          style="width:100%;"
                                                          :disabled="col.disabled"
                                                          @change="(val, obj)=>onChange(val, obj, col, scope)"
                                ></select-table-sku-package>
                                <!--                                <selectSkuPackage-->
                                <!--                                    v-model="scope.row[col.prop]"-->
                                <!--                                    :disabled="col.disabled == undefined ? false : col.disabled"-->
                                <!--                                    :search="col.searchData ? col.searchData : {}"-->
                                <!--                                    :text="-->
                                <!--                    col.props ? scope.row[col.props.label] : scope.row[col.prop]-->
                                <!--                  "-->
                                <!--                                    size="medium"-->
                                <!--                                    @change="(val, obj) => onChange(val, obj, col, scope)"-->
                                <!--                                ></selectSkuPackage>-->
                            </template>
                            <template v-else>
                                <span>
                                    {{
                                        getTableContent(scope, col)
                                    }}
                                </span>
                            </template>
                        </template>
                        <!-- 来往企业 -->
                        <template v-else-if="col.type == 'selectEnterprise'">
                            <template v-if="scope.row.$edit">
                                <!-- <selectEnterprise
                                                        v-model="scope.row[col.prop]"
                                                        :text="scope.row[col.prop+'_text']"
                                                        :search="col.searchData ? col.searchData : {}"
                                                        size="medium"
                                                        @change="(val, obj)=>onChange({id: val, obj, scope, type: 'selectEnterprise'})"
                                                ></selectEnterprise> -->
                                <SelectEnterpriseTable
                                    v-model="scope.row[col.prop]"
                                    style="width:100%;"
                                    :search="col.searchData ? col.searchData : {}"
                                    :text="col.props ? scope.row[col.props.label] : scope.row[col.prop]"
                                    :disabled="col.disabled"
                                    size="medium"
                                    @change="(val, obj) => onChange(val, obj, col, scope)"
                                ></SelectEnterpriseTable>
                            </template>
                            <template v-else>
                                <span>
                                    {{
                                        getTableContent(scope, col)
                                    }}
                                </span>
                            </template>
                        </template>
                        <!-- 下拉框 -->
                        <template v-else-if="col.type == 'select'">
                            <template v-if="scope.row.$edit">
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
                                <span>
                                    {{
                                        getTableContent(scope, col)
                                    }}
                                </span>
                            </template>
                        </template>
                        <!-- 下拉列表框（树形）-->
                        <template v-else-if="col.type === 'select-tree'">
                            <template v-if="scope.row.$edit">
                                <select-tree
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
                                <span>
                                    {{
                                        getTableContent(scope, col)
                                    }}
                                </span>
                            </template>
                        </template>
                        <!-- 计数器 -->
                        <template v-else-if="col.type == 'inputNumber'">
                            <template v-if="scope.row.$edit || col.isEdit">
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
                            <template v-if="scope.row.$edit">
                                <el-switch
                                    v-model="scope.row[col.prop]"
                                    :active-text="col.activeText || '是'"
                                    :active-value="col.activeValue || 1"
                                    :inactive-text="col.inactiveText || '否'"
                                    :inactive-value="col.inactiveValue || 0"
                                    size="medium"
                                    @change="(val)=>onChange(val, undefined, col, scope)"
                                ></el-switch>
                            </template>
                            <template v-else>
                                <span>{{
                                        scope.row[col.prop] == (col.activeValue || 1) ? (col.activeText || '是')
                                            : (col.inactiveText || '否')
                                    }}</span>
                            </template>
                        </template>
                        <!-- 日期 -->
                        <template v-else-if="col.type == 'date'">
                            <template v-if="scope.row.$edit">
                                <el-date-picker
                                    v-model="scope.row[col.prop]"
                                    :format="col.format ? col.format : 'yyyy-MM-dd'"
                                    :value-format="col.valueFormat ? col.valueFormat : 'yyyy-MM-dd'"
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
                            <template v-if="scope.row.$edit">
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
                    <span v-else>{{ scope.row[column.prop] }}</span>
                </template>
            </el-table-column>
        </template>
        <slot name="footer"></slot>
    </div>
</template>

<script>
import columnDefault from "./column-default";
import SelectSkuTable from "../select/select-table-sku";
import SelectTableSkuPackage from "../select/select-table-sku-package";
import SelectEnterpriseTable from "../select/select-table-enterprise";
import selectTree from "../select/select-tree";

export default {
    name: "column",
    components: {
        SelectTableSkuPackage,
        SelectSkuTable,
        columnDefault,
        SelectEnterpriseTable,
        selectTree,
    },
    inject: ["table"],
    props: {
        tableOption: {
            type: Object,
            default:
                () => {
                    return {};
                }
        },
        columnOption: {
            type: Array,
            default:
                () => {
                    return [];
                }
        }
    },
    data() {
        return {
            isInit: false,
        };
    },
    mounted() {
    },
    methods: {
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
                        this.clearItem(col.cascaderItem, scope);
                    }
                    // 获取当前列级联列
                    col.cascader = [];
                    this.columnOption.forEach((column) => {
                        if (col.cascaderItem.indexOf(column.prop) > -1) {
                            col.cascader.push(column);
                            // 清空子级表格显示的值
                            if (column.show) {
                                column.show.forEach(item => {
                                    this.$set(scope.row, item, undefined);
                                    this.onChange(undefined, undefined, column, scope, true);
                                });
                            }
                        }
                    });
                }
                // 触发事件
                if (col.change) {
                    col.change(val, obj, scope, this);
                }
            }
            this.procSearchData(this.columnOption, scope.row);
            this.table.onDataChange();
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
        clearItem(items, scope) {
            if (!items) {
                return;
            }
            items.forEach((item) => {
                // 清空字段的值
                if (scope.row[item]) {
                    // this.$set(scope.row, item, "");
                    scope.row[item] = undefined;
                }
                // 清空数据源的值
                this.columnOption.forEach((column) => {
                    // 清空子级
                    if (column.prop === item) {
                        // this.onChange(undefined, undefined, column, scope);
                        if (column["dicData"]) {
                            column.dicData = [];
                            // this.$set(column, "dicData", []);
                        }
                        if (column.show) {
                            for (let i = 0; i < column.show.length; i++) {
                                this.$set(column, column.show[i], "");
                            }
                        }
                        if (column.cascaderItem) {
                            this.clearItem(column.cascaderItem, scope);
                        }
                    }
                });
            });
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
        getTableContent(scope, col) {
            if (col.show && col.show.length > 0) {
                return scope.row[col.show[0]];
            } else if (col.props && col.props.label) {
                return scope.row[col.props.label];
            } else {
                return scope.row[col.prop];
            }
        },
    }
}
</script>

<style scoped>

</style>
