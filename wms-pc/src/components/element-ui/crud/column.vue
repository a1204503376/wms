<template>
    <div>
        <slot name="header"></slot>
        <!-- 动态列 -->
        <template v-for="(column, index) in columnOption">
            <column-dynamic v-if="column.children && column.children.length>0"
                            :key="column.label"
                            :columnOption="column">
                <template v-for="item in crud.mainSlot"
                          slot-scope="scope"
                          :slot="item.prop">
                    <slot v-bind="scope"
                          :name="item.prop"></slot>
                </template>
                <template v-for="item in crud.headerSlot"
                          slot-scope="scope"
                          :slot="crud.getSlotName(item,'H')">
                    <slot v-bind="scope"
                          :name="crud.getSlotName(item,'H')"></slot>
                </template>
                <template v-for="item in crud.mainSlot"
                          slot-scope="scope"
                          :slot="crud.getSlotName(item,'F')">
                    <slot v-bind="scope"
                          :name="crud.getSlotName(item,'F')"></slot>
                </template>
            </column-dynamic>
            <el-table-column v-else-if="getHide(column)"
                             :key="index"
                             :align="column.align || tableOption.align"
                             :filter-multiple="vaildData(column.filterMultiple, true)"
                             :fixed="column.fixed"
                             :header-align="column.headerAlign || tableOption.headerAlign"
                             :label="column.aliasName || column.label"
                             :min-width="getColumnMinWidth(column)"
                             :prop="column.prop"
                             :render-header="column.renderHeader"
                             :show-overflow-tooltip="column.overHidden !== false"
                             filter-placement="bottom-end">
                <template slot="header" slot-scope="scope">
                    {{ column.aliasName || column.label }}
                    <template v-if="column.search != false">
                        <!-- 日期范围 -->
                        <template v-if="getColumnSearchType(column) == 'date-picker'">
                            <el-date-picker
                                v-model="column.$search"
                                :format="column.format || 'yyyy-MM-dd 00:00:00'"
                                :picker-options="pickerOptions"
                                :size="config.controlSize"
                                :value-format="column.valueFormat || 'yyyy-MM-dd 00:00:00'"
                                align="right"
                                end-placeholder="结束日期"
                                range-separator="至"
                                start-placeholder="开始日期"
                                style="width:100%;"
                                type="daterange"
                                unlink-panels>
                            </el-date-picker>
                        </template>
                        <!-- 下拉列表框 -->
                        <!-- 暂时不支持多选，需要后台支持 -->
                        <!--                                multiple-->
                        <!--                                collapse-tags-->
                        <template v-else-if="getColumnSearchType(column) == 'select'">
                            <el-select
                                v-model="column.$search"
                                :size="config.controlSize"
                                clearable
                                filterable
                                multiple
                                placeholder=""
                                style="width: 100%"
                                @change="onChange"
                            >
                                <el-option
                                    v-for="item in getColumnData(column)"
                                    :key="column.props ? item[column.props.value] : item['value']"
                                    :label="column.props ? item[column.props.label] : item['label']"
                                    :value="column.props ? item[column.props.value] : item['value']"
                                ></el-option>
                            </el-select>
                        </template>
                        <!-- 下拉表格-用户 -->
                        <template v-else-if="getColumnSearchType(column) == 'select-table-user'">
                            <select-table-user
                                v-model="column.$search"
                                :data="getColumnData(column)"
                                :size="config.controlSize"
                            ></select-table-user>
                        </template>
                        <!-- 下拉树 -->
                        <template v-else-if="getColumnSearchType(column) == 'select-tree'">
                            <select-tree
                                v-model="column.$search"
                                :accordion="true"
                                :clearable="true"
                                :options="getColumnData(column)"
                                :props="column.props"
                                :size="config.controlSize"
                                style="width:100%;"
                            />
                        </template>
                        <!-- 输入框 -->
                        <template v-else>
                            <el-input
                                v-model="column.$search"
                                :size="config.controlSize"
                                clearable
                                show-word-limit
                            >
                            </el-input>
                        </template>
                    </template>
                </template>
                <template slot-scope="scope">
                    <slot :name="column.prop"
                          v-if="column.slot"
                          v-bind="scope.row"
                    ></slot>
                    <span v-else-if="column.view">
                        <el-button
                            :size="config.controlSize"
                            type="text"
                            @click="crud.rowView(scope.row, scope.$index)"
                        >{{ scope.row[column.prop] }}</el-button>
                    </span>
                    <span v-else v-html="handleDetail(scope.row, column)"></span>
                </template>
            </el-table-column>
        </template>
        <slot name="footer"></slot>
    </div>
</template>

<script>
import columnDynamic from "./column-dynamic";
import config from "./config";
import selectTableUser from "../select/select-table-user";
import selectTree from "../select/select-tree";

export default {
    name: "column",
    inject: ["crud"],
    provide() {
        return {
            crud: this.crud,
            dynamic: this
        }
    },
    components: {
        columnDynamic,
        selectTableUser,
        selectTree
    },
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
            drawer: false,
            config: config,
            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
        }
    },
    mounted() {
    },
    methods: {
        getColumnData(column) {
            return this.crud.DIC[column.prop] || [];
        },
        getColumnProp(column, type) {
            if (type === 'filterMethod') {
                return this.crud.default[column.prop] ? this.crud.default[column.prop].filters : false;
            }
            if (this.crud.isMobile && ['fixed'].includes(type)) {
                return false;
            }
            let result = this.crud.default[column.prop] ? this.crud.default[column.prop][type] : false;
            if (type == 'width' && result == 0) {
                return undefined
            } else {
                return result;
            }
        },
        getColumnWidth(column) {
            let canvas = document.createElement('canvas');
            let context = canvas.getContext('2d');
            context.font = '14px Chinese Quote';
            let max_width = 0;
            for (let i = 0; i < this.crud.data.length; i++) {
                let row = this.crud.data[i];
                let dimension = context.measureText(this.handleDetail(row, column));
                if (dimension.width <= max_width) {
                    continue;
                }
                max_width = dimension.width;
            }
            return max_width + 30;
        },
        getColumnMinWidth(column) {
            let min_width = -1;
            if (column) {
                if ((!column.width || column.width < 240) && column.type == 'date-picker') {
                    min_width = 240;
                } else if ((!column.width || column.width < 120) && (['select', 'select-tree'].includes(column.type))) {
                    min_width = 120;
                }
            }
            if (min_width <= 0) {
                min_width = column.width || 100;
            }
            let max_width = this.getColumnWidth(column);
            if (min_width <= 0 && max_width <= 0) {
                return 100;
            } else if (min_width > max_width) {
                return min_width;
            } else {
                return max_width;
            }
        },
        getColumnSearchType(column) {
            if (!column) {
                return '';
            }
            return column.type;
        },
        vaildLabel(column, row, val) {
            if (column.rules && row.$cellEdit) {
                return val
            }
        },
        vaildColumn(column) {
            return column.hide !== true;
        },
        //表格筛选逻辑
        handleFiltersMethod(value, row, column) {
            const columnNew = this.columnOption.filter(
                ele => ele.prop === column.property
            )[0];
            if (typeof columnNew.filtersMethod === "function") {
                return columnNew.filtersMethod(value, row, columnNew);
            } else {
                return row[columnNew.prop] === value;
            }
        },
        columnChange(index, row, column) {
            if (this.validatenull(this.count[column.prop])) this.count[column.prop] = 0
            this.count[column.prop] = this.count[column.prop] + 1;
            if (column.cascader) this.handleChange(index, row)
            if (this.count[column.prop] % 3 === 0 && typeof column.change === 'function' && column.cell === true) {
                column.change({row, column, index: row.$index, value: row[column.prop]})
            }
        },
        getArrayValue(row, column, array) {
            let value = '';
            let property = row[column.prop];
            if (property && !(property instanceof String)) {
                let valueList = [];
                if (typeof property == 'string') {
                    valueList = property.split(',');
                } else {
                    valueList.push(property);
                }
                let resultList = [];
                for (let i = 0; i < valueList.length; i++) {
                    let key = valueList[i];
                    let data = this.getData(row, column, array, key);
                    if (data) {
                        resultList.push(data[column.props.label]);
                    }
                }
                if (resultList.length > 0) {
                    value = resultList.join(',');
                }
            } else {
                value = row[column.props.value];
            }
            return value;
        },
        getData(row, column, array, key){
            for (let i = 0; i < array.length; i++) {
                let data = array[i];
                if (data.children && data.children.length > 0 && data[column.props.value] != key) {
                    return this.getData(row, column, data.children, key);
                }
                if (data[column.props.value] == key) {
                    return data;
                }
            }
        },
        handleDetail(row, column) {
            if (['select', 'select-table-user', 'select-tree'].includes(column.type)) {
                let array = this.crud.DIC[column.prop];
                if (array) {
                    return this.getArrayValue(row, column, array);
                }
            } else {
                return row[column.prop];
            }
            return '';
        },
        corArray(list, separator = DIC_SPLIT) {
            if (this.validatenull(list)) {
                return []
            } else if (!Array.isArray(list)) {
                return list.split(separator);
            }
            return list
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
        onChange() {
            let self = this;
            setTimeout(() => {
                self.crud.doLayout();
            }, 100);
        }
    }
}
</script>

<style scoped>

</style>
