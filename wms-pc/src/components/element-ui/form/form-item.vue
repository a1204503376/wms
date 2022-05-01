<template>
    <el-row v-if="form.getHide(item)">
        <!-- 循环分组下所有字段 -->
        <template v-for="col in item.column">
            <el-col v-if="col.row" :span="24"></el-col>
            <el-col v-if="form.getHide(col)"
                    :xs="col.span || 24"
                    :sm="col.span || 12"
                    :md="col.span || 12"
                    :lg="col.span || 8"
                    :xl="col.span || 8"
                    :offset="col.offset || 0"
                    :push="col.push || 0"
                    :pull="col.pull || 0">
                <!-- 子表格 -->
                <template v-if="col.type === 'dynamic'">
                    <nodes-table v-model="form.form[col.prop]"
                                 :ref="'dynamic_' + col.prop"
                                 :formData="form.form"
                                 :option="col['$option']"
                                 @selection-change="(val)=>onDynamicSelectChange(val, col)"
                    ></nodes-table>
                </template>
                <!-- 按钮 -->
                <template v-else-if="col.type === 'button'">
                    <el-button size="medium"
                               :type="col.buttonType === undefined ? 'primary' : col.buttonType"
                               :plain="col.plain === undefined ? false : col.plain"
                               :disabled="getDisabled(col)"
                               @click="onClick(col)">
                        {{ col.label }}
                    </el-button>
                </template>
                <template v-else>
                    <el-form-item :prop="col.prop" :label="col.label"
                                  :rules="col.rules"
                                  :show-message="true"
                                  :class="{'multiple-item': (col.rules && col.label.length > 4) || col.label.length > 5}"
                    >
                        <slot name="form-body" v-bind="col"></slot>
                        <!-- 下拉列表框 -->
                        <template v-if="col.type === 'select'">
                            <el-select v-if="form.getHide(col)"
                                       v-model="form.form[col.prop]"
                                       filterable
                                       :clearable="col.clearable === undefined ? true : col.clearable"
                                       collapse-tags
                                       :multiple="col.multiple"
                                       :allow-create="col.allowCreate"
                                       :readonly="col.readonly"
                                       :disabled="getDisabled(col)"
                                       :placeholder="col.placeholder || '请选择'"
                                       style="width:100%;"
                                       @change="(val)=>selectChange(val, null, col)">
                                <el-option
                                    v-for="item in col.dicData"
                                    :key="col.props ? item[col.props.value] : item['value']"
                                    :value="col.props ?item[col.props.value] : item['value']"
                                    :label="col.props ?item[col.props.label] : item['label']"
                                ></el-option>
                            </el-select>
                        </template>
                        <!-- 下拉表格-报表 -->
                        <!--                        <template v-else-if="col.type === 'selectReport'">-->
                        <!--                            <select-report v-model="form.form[col.prop]"-->
                        <!--                                           @change="(val, obj)=>selectChange(val, obj, col)"></select-report>-->
                        <!--                        </template>-->
                        <!-- 下拉表格-物品 -->
                        <template v-else-if="col.type === 'select-table-sku'">
                            <select-table-sku
                                v-model="form.form[col.prop]"
                                @change="(val, obj)=>selectChange(val, obj, col)"
                            ></select-table-sku>
                        </template>
                        <!-- 下拉表格-企业 -->
                        <template v-else-if="col.type === 'select-table-enterprise'">
                            <select-table-enterprise
                                v-model="form.form[col.prop]"
                                :before-open="(val)=>col.beforeOpen(val, form.form)"
                                @change="(val, obj)=>selectChange(val, obj, col)"
                            ></select-table-enterprise>
                        </template>
                        <!-- 下拉表格-包装 -->
                        <template v-else-if="col.type === 'select-table-sku-package'">
                            <select-table-sku-package
                                v-model="form.form[col.prop]"
                                @change="(val, obj)=>selectChange(val, obj, col)"
                            ></select-table-sku-package>
                        </template>
                        <!-- 下拉列表框（树形）-->
                        <template v-else-if="col.type === 'select-tree'">
                            <select-tree
                                v-model="form.form[col.prop]"
                                :placeholder="col.placeholder || '请选择'"
                                :props="col.props"
                                :options="col.dicData"
                                :clearable="col.clearable"
                                :accordion="col.accordion"
                                :disabled="getDisabled(col)"
                                style="width:100%;"
                                @change="(val,obj)=>selectChange(val, obj, col)"
                            />
                        </template>
                        <!-- 单选框 -->
                        <template v-else-if="col.type === 'radio'">
                            <el-radio-group v-if="form.getHide(col)"
                                            v-model="form.form[col.prop]"
                                            :disabled="getDisabled(col)"
                                            @change="(val)=>selectChange(val, null, col)"
                            >
                                <el-radio v-for="item in col.dicData"
                                          :label="col.props ? item[col.props.value] : item['value']">
                                    {{
                                        col.props ? item[col.props.label] :
                                            item['label']
                                    }}
                                </el-radio>
                            </el-radio-group>
                        </template>
                        <!-- 开关 -->
                        <template v-else-if="col.type === 'switch'">
                            <el-switch
                                v-model="form.form[col.prop]"
                                :active-color="col.activeColor"
                                :inactive-color="col.inactiveColor"
                                :active-text="col.activeText"
                                :inactive-text="col.inactiveText"
                                :active-value="col.activeValue"
                                :inactive-value="col.inactiveValue"
                            >
                            </el-switch>
                        </template>
                        <!-- 复选框 -->
                        <template v-else-if="col.type === 'check'">
                            <el-checkbox v-model="form.form[col.prop]"
                                         :label="col.text"
                                         :disabled="getDisabled(col)"
                                         border
                                         size="medium"></el-checkbox>
                        </template>
                        <!-- 复选框组 -->
                        <template v-else-if="col.type === 'checkGroup'">
                            <el-checkbox-group v-if="form.getHide(col)"
                                               v-model="form.form[col.prop]"
                                               :disabled="getDisabled(col)"
                                               @change="selectChange($event, null, col)">
                                <template v-for="item in col.dicData">
                                    <el-checkbox
                                        :key="col.props ? item[col.props.value] : item['value']"
                                        :label="col.props ? item[col.props.value] : item['value']">
                                        {{
                                            col.props ? item[col.props.label] :
                                                item['label']
                                        }}
                                    </el-checkbox>
                                </template>
                            </el-checkbox-group>
                        </template>
                        <!-- 日期框(限制最小日期为当天) -->
                        <template v-else-if="col.type === 'min-date'">
                            <el-date-picker v-model="form.form[col.prop]"
                                            type="date"
                                            :placeholder="col.placeholder || '选择日期'"
                                            :disabled="getDisabled(col)"
                                            :readonly="col.readonly"
                                            :picker-options="pickerOptions"
                                            :value-format="col.format"
                                            style="width:100%;">
                            </el-date-picker>
                        </template>
                        <!-- 日期框 -->
                        <template v-else-if="col.type === 'date'">
                            <el-date-picker v-model="form.form[col.prop]"
                                            type="date"
                                            :placeholder="col.placeholder || '选择日期'"
                                            :disabled="getDisabled(col)"
                                            :readonly="col.readonly"
                                            :value-format="col.format === undefined ? 'yyyy-MM-dd' : col.format"
                                            style="width:100%;">
                            </el-date-picker>
                        </template>
                        <!-- 日期时间框 -->
                        <template v-else-if="col.type === 'datetime'">
                            <el-date-picker
                                v-model="form.form[col.prop]"
                                type="datetime"
                                :placeholder="col.placeholder || '选择日期时间'"
                                :disabled="getDisabled(col)"
                                :readonly="col.readonly"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                style="width:100%;">
                            </el-date-picker>
                        </template>
                        <!-- 文本域 -->
                        <template v-else-if="col.type === 'textarea'">
                            <el-input
                                type="textarea"
                                autosize
                                :disabled="getDisabled(col)"
                                :readonly="col.readonly"
                                :rows="col.rows || 2"
                                :autosize="{ minRows: 2, maxRows: 4}"
                                :placeholder="col.placeholder || (col.readonly || col.disabled ? '' : '请输入')"
                                :maxlength="col.maxlength"
                                :minlength="col.minlength"
                                v-model="form.form[col.prop]"
                                show-word-limit>
                            </el-input>
                        </template>
                        <!-- 菜单图标 -->
                        <template v-else-if="col.type === 'icon'">
                            <icon v-model="form.form[col.prop]" icontitleText="图标选择"
                            ></icon>
                        </template>
                        <!-- 计数器 -->
                        <template v-else-if="col.type == 'inputNumber'">
                            <el-input-number
                                :placeholder="col.placeholder || (col.readonly || col.disabled ? '' : '请输入')"
                                v-model="form.form[col.prop]"
                                size="medium"
                                controls-position="right"
                                :disabled="getDisabled(col)"
                                :min="col.min !== undefined ? col.min : 0"
                                :max="col.max !== undefined ? col.max : 2147483647"
                                :step="col.step"
                                :precision="col.precision"
                                style="width:100%;"
                            >
                            </el-input-number>
                        </template>
                        <template v-else-if="col.type == 'number'">
                            <el-input
                                :placeholder="col.placeholder || (col.readonly || col.disabled ? '' : '请输入')"
                                v-model.number="form.form[col.prop]"
                                @input="(val)=>inputNumber(val, col)"
                                clearable
                                :readonly="col.readonly"
                                :disabled="getDisabled(col)"
                                :maxlength="col.maxlength"
                                :minlength="col.minlength"
                                show-word-limit>
                                <template v-if="col.append"
                                          slot="append"
                                          style="width:40px;">
                                    {{ col.append }}
                                </template>
                            </el-input>
                        </template>
                        <!-- 默认：输入框input -->
                        <template v-else>
                            <el-input
                                :placeholder="col.placeholder || (col.readonly || col.disabled ? '' : '请输入')"
                                v-model="form.form[col.prop]"
                                clearable
                                :readonly="col.readonly"
                                :disabled="getDisabled(col)"
                                :maxlength="col.maxlength"
                                :minlength="col.minlength"
                                show-word-limit>
                            </el-input>
                        </template>
                    </el-form-item>
                </template>
            </el-col>
        </template>
    </el-row>
</template>

<script>
import selectTableSku from "../select/select-table-sku";
import selectTableEnterprise from "../select/select-table-enterprise";
import columnDynamic from "@/components/element-ui/crud/column-dynamic";
import selectTableUser from "@/components/element-ui/select/select-table-user";
import selectTableSkuPackage from "@/components/element-ui/select/select-table-sku-package";
import selectTree from "@/components/element-ui/select/select-tree";
import nodesTable from "@/components/element-ui/table/index";
import icon from "@/components/nodes/icon";

export default {
    name: "form-item",
    inject: ["form"],
    provide() {
        return {
            form: this.form,
            formItem: this
        }
    },
    props: {
        item: {
            type: Object, default: () => {
            }
        }
    },
    watch: {
        item: {
            handler: function () {
                this.columns = [];
                this.item.column.forEach(col => {
                    if (!col.children) {
                        return;
                    }
                    col.children.forEach(u => {
                        this.columns.push(u);
                    })
                })
            }
        }
    },
    data() {
        return {
            pickerOptions: {
                disabledDate: function (date) {
                    return date.getTime() < Date.now() - 8.64e7;
                }
            },
            columns: [],
        }
    },
    components: {
        columnDynamic,
        nodesTable,
        selectTableUser,
        selectTree,
        selectTableSku,
        selectTableEnterprise,
        selectTableSkuPackage,
        icon
    },
    methods: {
        // 获取禁用状态
        getDisabled(col) {
            if (this.isView) {
                return true;
            }
            if (col.disabled !== undefined) {
                if (typeof col.disabled === 'function') {
                    return col.disabled(this.form.form, this.form);
                } else {
                    return col.disabled;
                }
            } else {
                return false;
            }
        },
        // 按钮点击事件
        onClick(col) {
            if (col.click) {
                col.click(this.form);
            }
        },
        // 选择框的值发生变化后
        selectChange(val, obj, col, clear = true) {
            if (col) {
                // 设置表单属性
                if (obj) {
                    if (col.props && obj[col.props.value]) {
                        this.$set(this.form.form, col.prop, obj[col.props.value]);
                    }
                } else {
                    if (col.dicData) {
                        obj = this.findObj(val, col.props ? col.props.value : 'value', col.dicData);
                    }
                }
                if (!obj) {
                    clear = true;
                }
                // 级联不为空的情况下
                if (col.cascaderItem) {
                    if (clear) {
                        this.clearItem(col.cascaderItem);
                    }
                    // 获取当前列级联列
                    col.cascader = [];
                    this.form.columns.forEach(column => {
                        if (col.cascaderItem.indexOf(column.prop) > -1) {
                            col.cascader.push(column);
                        }
                    });
                }
                if (col.change) {
                    col.change(val, obj, col, this.form.form, this);
                }
            }
            this.procSearchData(this.item.column);
        },
        findObj(val, prop, dataSource) {
            for (let i = 0; i < dataSource.length; i++) {
                let data = dataSource[i];
                if (val instanceof Array) {
                    let resultData = [];
                    for (let j = 0; j < val.length; j++) {
                        let result = this.findObj(val[j], prop, dataSource);
                        if (result) {
                            resultData.push(result);
                        }
                    }
                    return resultData;
                } else if (data[prop] === val) {
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
        clearItem(items) {
            if (!items) {
                return;
            }
            items.forEach(item => {
                // 清空字段的值
                if (this.form.form[item]) {
                    if (this.form.form[item] instanceof Array) {
                        this.$set(this.form.form, item, []);
                    } else {
                        this.$set(this.form.form, item, '');
                    }
                }
                // 清空数据源的值
                this.columns.forEach(column => {
                    // 清空子级
                    if (column.prop === item) {
                        if (column['dicData']) {
                            this.$set(column, 'dicData', []);
                        }
                        if (column.cascaderItem) {
                            this.clearItem(column.cascaderItem);
                        }
                    }
                });
            });
            this.$forceUpdate();
        },
        procSearchData(columns) {

            let searchColumnList = [];
            columns.forEach(column => {
                if (column.search) {
                    searchColumnList.push(column);
                }
                if (column.children) {
                    column.children.forEach(child => {
                        if (child.search) {
                            searchColumnList.push(child);
                        }
                    });
                }
            });
            if (searchColumnList && searchColumnList.length > 0) {
                searchColumnList.forEach(col => {
                    if (!col['searchData']) {
                        this.$set(col, 'searchData', {});
                    }
                    for (let key in col.search) {
                        let value = this.form.form[key];
                        if (value !== undefined) {
                            this.$set(col.searchData, key, value);
                        } else {
                            this.$set(col.searchData, key, col.search[key]);
                        }
                    }
                });
            }
        },
        onDynamicSelectChange(val, col){
            if (col.selectionChange) {
                col.selectionChange(val, this.form.form);
            }
        }
    }
}
</script>

<style scoped>

</style>
