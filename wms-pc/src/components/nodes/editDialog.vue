<template>
    <el-dialog :title="dialogTitle"
               :visible.sync="visible"
               :close-on-click-modal="false"
               @opened="dialogOpened"
               @open="dialogOpen"
               :before-close="dialogClose"
               :destroy-on-close="true"
               v-dialogDrag
               class="dialogs"
               :width="width"
               top="3vh"
               append-to-body>
        <div class="dialog-body">
            <el-row class="tabs-item">
                <el-col :span="group && group.length > 0 && group[0].label ? 3 : 0">
                    <div style="width: 100px;" v-if="group && group.length > 0 && group[0].label">
                        <ul id="tabList">
                            <template v-for="(item, ind) in group">
                                <li v-if="getGroupHide(item)"
                                    :class="{active:tabIndex === ind}"
                                    @click="changeTab(ind)">{{ item.label }}<i></i></li>
                            </template>
                        </ul>
                    </div>
                </el-col>
                <el-col :span="group && group.length > 0 && group[0].label ? 21 : 24">
                    <div class="grid-content bg-purple-light"
                         style="max-height:500px;overflow: hidden;overflow-y: scroll;">
                        <div :id="prop" v-loading="loading.content">
                            <el-form label-position="right"
                                     label-width="100px"
                                     size="medium"
                                     ref="form"
                                     :model="form"
                            >
                                <!-- 循环分组 -->
                                <template v-for="item in group">
                                    <el-row v-if="item.label && getGroupHide(item)" :gutter="20" class="tabs-item">
                                        {{ item.label }}
                                    </el-row>
                                    <el-row :gutter="20" v-if="getGroupHide(item)">
                                        <!-- 循环分组下所有字段 -->
                                        <template v-for="col in item.column">
                                            <el-col v-if="col.row" :span="24"></el-col>
                                            <el-col v-if="getHide(col)"
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
                                                    <edit-table v-model="form[col.prop]"
                                                                :ref="'dynamic_' + col.prop"
                                                                :option="col['_option']"
                                                                :owner="form"
                                                                :form="owner"
                                                                :root="root"
                                                                :height="col.height"
                                                                :row-class-name="({row, rowIndex})=>col.rowClassName && col.rowClassName(row, rowIndex)"
                                                                @before-add="(row)=>dynamicBeforeAdd(row, col)"
                                                                @before-edit="dynamicBeforeEdit($event, col.prop)"
                                                                @saved="(row, index)=> dynamicSaved(row, col.prop, {col, index})"
                                                                @deleted="(row, rowIndex, prop)=>dynamicDeleted(
                                                                            row, rowIndex, col)"
                                                                @selection-change="dynamicSelectionChange($event, col)"
                                                    ></edit-table>
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
                                                                  :show-message="false"
                                                                  :class="{'multiple-item': (col.rules && col.label.length > 4) || col.label.length > 5}"
                                                    >
                                                        <!-- 来往企业 -->
                                                        <template v-if="col.type === 'selectEnterprise'">
                                                            <select-enterprise v-model="form[col.prop]"
                                                                               :text="form[col.prop]"
                                                                               :placeholder="col.placeholder || '请选择供应商'"
                                                                               :search="col.searchData"
                                                                               :disabled="getDisabled(col)"
                                                                               @change="(val, obj)=>selectChange(val, obj, col)">
                                                            </select-enterprise>
                                                        </template>
                                                        <!-- 物品 -->
                                                        <template v-else-if="col.type === 'selectSku'">
                                                            <select-sku v-model="form[col.prop]"
                                                                        :text="form[col.props.label]"
                                                                        :placeholder="col.placeholder || '请选择物品'"
                                                                        :search="col.searchData"
                                                                        :disabled="getDisabled(col)"
                                                                        :clearable="col.clearable"
                                                                        @change="(val, obj)=>selectChange(val, obj, col)"></select-sku>
                                                        </template>
                                                        <!-- 物品包装 -->
                                                        <template v-else-if="col.type === 'selectSkuPackage'">
                                                            <select-sku-package v-model="form[col.prop]"
                                                                                :text="form[col.props.label]"
                                                                                :placeholder="col.placeholder || '请选择物品包装'"
                                                                                :search="col.searchData"
                                                                                :disabled="getDisabled(col)"
                                                                                @change="(val, obj)=>selectChange(val, obj, col)"
                                                            ></select-sku-package>
                                                        </template>
                                                        <!-- 人员 -->
                                                        <template v-else-if="col.type === 'selectUser'">
                                                            <selectUserTable v-model="form[col.prop]" size="small"
                                                                             ref="newForm"></selectUserTable>
                                                        </template>
                                                        <!-- 下拉列表框 -->
                                                        <template v-else-if="col.type === 'select'">
                                                            <el-select v-if="getHide(col)"
                                                                       v-model="form[col.prop]"
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
                                                        <template v-else-if="col.type === 'selectReport'">
                                                            <select-report v-model="form[col.prop]"></select-report>
                                                        </template>
                                                        <!-- 下拉列表框（树形）-->
                                                        <template v-else-if="col.type === 'select-tree'">
                                                            <treeSelect
                                                                v-model="form[col.prop]"
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
                                                            <el-radio-group v-if="getHide(col)"
                                                                            v-model="form[col.prop]"
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
                                                                v-model="form[col.prop]"
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
                                                            <el-checkbox v-model="form[col.prop]"
                                                                         :label="col.text"
                                                                         :disabled="getDisabled(col)"
                                                                         border
                                                                         size="medium"></el-checkbox>
                                                        </template>
                                                        <!-- 复选框组 -->
                                                        <template v-else-if="col.type === 'checkGroup'">
                                                            <el-checkbox-group v-if="getHide(col)"
                                                                               v-model="form[col.prop]"
                                                                               :disabled="getDisabled(col)"
                                                                               @change="selectChange($event, null, col)">
                                                                <template v-for="item in col.dicData">
                                                                    <el-checkbox
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
                                                            <el-date-picker v-model="form[col.prop]"
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
                                                            <el-date-picker v-model="form[col.prop]"
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
                                                                v-model="form[col.prop]"
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
                                                                :disabled="getDisabled(col)"
                                                                :readonly="col.readonly"
                                                                :rows="col.rows ? col.rows : 2"
                                                                :placeholder="col.placeholder || (col.readonly || col.disabled ? '' : '请输入')"
                                                                :maxlength="col.maxlength"
                                                                :minlength="col.minlength"
                                                                v-model="form[col.prop]"
                                                                show-word-limit>
                                                            </el-input>
                                                        </template>
                                                        <!-- 菜单图标 -->
                                                        <template v-else-if="col.type === 'icon'">
                                                            <icon v-model="form[col.prop]" :icontitleText="title"
                                                                  :isView="isView"></icon>
                                                        </template>
                                                        <!-- 计数器 -->
                                                        <template v-else-if="col.type == 'inputNumber'">
                                                            <el-input-number
                                                                :placeholder="col.placeholder || (col.readonly || col.disabled ? '' : '请输入')"
                                                                v-model="form[col.prop]"
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
                                                                v-model.number="form[col.prop]"
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
                                                                v-model="form[col.prop]"
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
                            </el-form>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>
        <div slot="footer" class="dialog-footer">
            <template v-for="button in groupButton">
                <el-button v-show="!button.hide"
                           :type="button.type"
                           :loading="button.loading"
                           @click="buttonClick(button)">
                    {{ button.label }}
                </el-button>
            </template>
            <el-button v-if="saveBtn === undefined || saveBtn"
                       type="primary"
                       @click="save"
                       :loading="loading.save"
                       v-show="!isView">保 存
            </el-button>
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
import request from '@/router/axios';
import treeSelect from "@/components/nodes/treeSelect";
import selectEnterprise from "@/components/nodes/selectEnterprise";
import selectSkuPackage from "@/components/nodes/selectSkuPackage";
import selectSku from "@/components/nodes/selectSku";
import editTable from "@/components/nodes/editTable";
import icon from "@/components/nodes/icon"
import selectReport from "@/components/nodes/selectReport";
import selectUserTable from "@/components/nodes/selectUserTable";

import {mapState} from 'vuex';

export default {
    name: "editDialog",
    props: {
        // 对话框的显隐状态
        visible: {type: Boolean, default: false},
        // 是否为新增
        isNew: {type: Boolean, default: false},
        // 是否为查看
        isView: {type: Boolean, default: false},
        // 是否为编辑
        isEdit: {type: Boolean, default: false},
        // 界面分组配置
        group: {
            type: Array, default: function () {
                return [];
            }
        },
        cloneGroup: {
            type: Array, default: function () {
                return [];
            }
        },
        groupButton: {
            type: Array, default: function () {
                return [];
            }
        },
        // 界面显示字段（group为 undefined时使用)
        column: {
            type: Array, default: function () {
                return []
            }
        },
        // 表单对象
        dataSource: {
            type: Object, default: function () {
                return {};
            }
        },
        // 对话框宽度
        width: {type: [Number, String], default: '80%'},
        // 保存按钮显隐
        saveBtn: {type: Boolean, default: true},
        // 父级对象
        owner: {type: Object, default: null},
        root: {type: Object, default: null},
        // 对话框标题
        title: {type: String, default: undefined}
    },
    components: {
        selectSku,
        selectSkuPackage,
        treeSelect,
        selectEnterprise,
        editTable,
        icon,
        selectReport,
        selectUserTable
    },
    created() {
        this.prop = 'content_' + Date.now().toString(36);
        if (this.title) {
            this.dialogTitle = this.title;
        }
    },
    mounted() {
        window.addEventListener('scroll', this.handleScroll, true);
    },
    watch: {
        form: {
            handler(newValue, oldValue) {
                this.$emit('change', newValue);
            },
            deep: true,
            immediate: true
        },
        title: {
            handler(newValue, oldValue) {
                this.dialogTitle = newValue;
            }
        },
    },
    data() {
        return {
            dialogTitle: '对话框',
            prop: 'content',
            loading: {
                content: false,
                save: false
            },
            form: {},
            tabIndex: 0,
            isHandelScroll: false,  //区分是点击切换还是滚动
            tabOffsetTop: [],
            pickerOptions: {
                disabledDate: function (date) {
                    return date.getTime() < Date.now() - 8.64e7;
                }
            },
            // 所有列（所有分组下的所有列）
            columns: [],
            callback: {
                visible: false,
                success: false,
                data: null,
                loading: function () {
                }
            },
            editType: undefined
        }
    },
    computed: {
        ...mapState(['hiddenObj', 'viewObj'])
    },
    methods: {
        // 监控滚动条
        handleScroll(event) {
            if (this.isHandelScroll) {
                return;
            }
            let num = 500; //滚动div的高度
            let scrollHeight = event.target.scrollHeight;
            let scrollTop = event.target.scrollTop;
            let countTabOffsetTop = this.tabOffsetTop.map(v => {
                return Math.abs(v - scrollTop);
            });
            let countTabOffsetTop2 = this.tabOffsetTop.map(v => {
                return (v - scrollTop);
            });
            let minNum = Math.min.apply(Math, countTabOffsetTop);
            let index = countTabOffsetTop.indexOf(minNum);

            if (countTabOffsetTop[index] < 50 && countTabOffsetTop2[index] >= 0) {
                this.tabIndex = index;
            }
            if (Math.abs(scrollTop - (scrollHeight - num)) <= 50) { //处理最后一个tab
                this.tabIndex = this.tabOffsetTop.length - 1;
            }
        },
        // 切换左侧tab
        changeTab(index) {
            this.isHandelScroll = true;
            this.tabIndex = index;
            document.querySelector('.grid-content').scrollTop = this.tabOffsetTop[index];

            setTimeout(() => {
                this.isHandelScroll = false;
            }, 200)
        },
        // 对话框打开
        dialogOpen() {
            this.beforeLoading();
            this.tabIndex = 0;

            if (this.isNew) {
                if (!this.title) {
                    this.dialogTitle = '新增';
                }
                this.editType = 'new';
            } else {
                if (this.isView) {
                    if (!this.title) {
                        this.dialogTitle = '查看';
                    }
                    this.editType = 'view';
                } else if (this.isEdit) {
                    if (!this.title) {
                        this.dialogTitle = '编辑';
                    }
                    this.editType = 'edit';
                } else {
                    this.editType = undefined;
                }
            }
            this.form = Object.assign({}, this.dataSource);
            this.$emit('change', this.form);
            // 记录对话框底部自定义按钮状态
            if (this.groupButton) {
                this.groupButton.forEach(button => {
                    this.$set(button, 'initHide', button.hide);
                });
            }
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
                        // 初始化 dicData 綁定的屬性
                        if (col.type !== 'dynamic') {
                            // 有设置默认值，并且不为新增的情况下
                            if (col.default !== undefined && this.editType === 'new') {
                                let defaultValue = null;
                                if (typeof col.default === 'function') {
                                    defaultValue = col.default(this.form, this.editType);
                                } else {
                                    defaultValue = col.default;
                                }
                                if (col.prop) {
                                    this.$set(this.form, col.prop, defaultValue);
                                    // 设置默认值后，触发 change 事件
                                    this.selectChange(defaultValue, null, col);
                                }
                            } else {
                                if (col.type === 'select' && col.multiple) {
                                    this.$set(this.form, col.prop, this.form[col.prop] ? this.form[col.prop] : []);
                                } else if (col.type === 'inputNumber' && this.editType === 'new') {
                                    this.$set(this.form, col.prop, this.form[col.prop] ? this.form[col.prop] : 0);
                                } else {
                                    this.$set(this.form, col.prop, this.form[col.prop] ? this.form[col.prop] : undefined);
                                }
                            }
                        } else {
                            let indexCol = false;
                            let viewBtn = col.column != undefined && col.column.length > 0;
                            let editBtn = !this.isView && (col.editBtn === undefined || col.editBtn);
                            let delBtn = !this.isView && (col.delBtn === undefined || col.delBtn);
                            let addBtn = !this.isView && col.addBtn;
                            let menu = viewBtn || editBtn || delBtn || (col.menuBtn != undefined && col.menuBtn.length > 0);
                            let moveBtn = !this.isView && col.moveBtn;
                            let copyBtn = !this.isView && col.copyBtn;
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
                                beforeAdd: col.beforeAdd
                            };
                            this.$set(col, '_option', option);
                            this.$set(this.form, col.prop, []);
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
                            if (!col.type) {
                                if (res.data.data[col.props.value]) {
                                    this.$set(this.form, col.prop, res.data.data[col.props.value]);
                                } else {
                                    this.$set(this.form, col.prop, res.data.data);
                                }
                            } else {
                                this.$set(col, "dicData", res.data.data);
                                if (col.defaultIndex !== undefined && res.data.data && res.data.data.length > 0 && this.editType === 'new') {
                                    // 设置默认值
                                    let defaultObj = res.data.data[col.defaultIndex];
                                    this.$set(this.form, col.prop, defaultObj[col.props.value]);
                                    this.selectChange(defaultObj[col.props.value], defaultObj, col);
                                }
                            }
                        }).finally(() => {
                            loadingCount--;
                        });
                    }
                }
            }
            let self = this;
            let interval = setInterval(function () {
                if (loadingCount === 0 && self.loading.content) {
                    if (self.editType === 'new') {
                        self.$emit('before-open', self.finishLoading, self.editType, self.dialogClose);
                        self.finishLoading(self.editType);
                    } else {
                        if (self.owner) {
                            self.form = Object.assign({}, self.dataSource);
                        }
                        self.$emit('before-open', self.finishLoading, self.editType, self.dialogClose);
                        // 循环所有 column, 存在子级的情况下，触发 change 事件
                        if (self.columns) {
                            self.columns.forEach(col => {
                                if (col.cascaderItem) {
                                    self.selectChange(self.form[col.prop], null, col, false);
                                }
                            });
                            self.procSearchData(self.columns);
                        }
                    }
                    self.loading.content = false;
                    self.$refs.form.clearValidate();
                    self.$forceUpdate();
                    clearInterval(interval);
                }
            }, 100);
        },
        // 对话框打开后
        dialogOpened() {
            // 记录tab位置
            let tabs = document.querySelectorAll('#' + this.prop + ' .tabs-item');
            if (tabs) {
                this.tabOffsetTop = [];
                tabs.forEach(item => {
                    this.tabOffsetTop.push(item.offsetTop);
                });
            }
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
            // 先移除子表格中未保存的项
            this.columns.forEach(col => {
                if (col.type === 'dynamic') {
                    let dynamicList = this.$refs['dynamic_' + col.prop];
                    if (dynamicList) {
                        dynamicList.forEach(dynamic => {
                            dynamic.restoreUnSaveRow();
                        });
                    }
                }
            });
            this.clearDataDic();
            // this.clearObj(this.form);
            this.columns = [];
            this.$refs.form.clearValidate();
            this.callback.visible = false;
            this.callback.success = false;
            this.callback.data = null;
            // 还原对话框底部自定义按钮的状态
            if (this.groupButton) {
                this.groupButton.forEach(button => {
                    this.$set(button, 'hide', button.initHide);
                });
            }
            this.$emit('callback', this.callback);
        },
        // 保存
        save() {
            this.$refs.form.validate(valid => {
                // 验证子所有子表格
                let result = true;
                this.columns.forEach(col => {
                    if (col.type === 'dynamic') {
                        if (!result) {
                            return;
                        }
                        let dynamic = this.$refs['dynamic_' + col.prop];
                        if (dynamic && dynamic.length > 0) {
                            result = !dynamic[0].hasEdit();
                        }
                    }
                });
                if (valid && result) {
                    this.callback.visible = false;
                    this.callback.success = true;
                    this.callback.data = this.form;
                    this.beforeLoading();
                    let self = this;
                    this.callback.loading = function () {
                        self.finishLoading();
                    };
                    this.$refs.form.clearValidate();
                    // this.clearDataDic();
                    this.$emit('callback', this.callback);
                } else {
                    return false;
                }
            });
        },
        // 清楚数据源/值
        clearItem(items) {
            if (!items) {
                return;
            }
            items.forEach(item => {
                // 清空字段的值
                if (this.form[item]) {
                    if (this.form[item] instanceof Array) {
                        this.$set(this.form, item, []);
                    } else {
                        this.$set(this.form, item, '');
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
        // 清空数据源
        clearDataDic() {
            if (this.columns) {
                this.columns.forEach(col => {
                    if (col.static) {
                        return;
                    }
                    col.dicData = [];
                });
            }
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
        // 选择框的值发生变化后
        selectChange(val, obj, col, clear = true) {
            if (col) {
                // 设置表单属性
                if (obj) {
                    if (col.props && obj[col.props.value]) {
                        this.$set(this.form, col.prop, obj[col.props.value]);
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
                    this.columns.forEach(column => {
                        if (col.cascaderItem.indexOf(column.prop) > -1) {
                            col.cascader.push(column);
                        }
                    });
                }
                if (col.change) {
                    col.change(val, obj, col, this.form, this);
                }
            }
            this.procSearchData(this.columns);
            if (this.$refs.form) {
                this.$refs.form.clearValidate();
            }
        },
        // 处理搜索条件
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
                        let value = this.form[key];
                        if (value !== undefined) {
                            this.$set(col.searchData, key, value);
                        } else {
                            this.$set(col.searchData, key, col.search[key]);
                        }
                    }
                });
            }
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
            if (type !== 'new') {
                this.form = Object.assign({}, this.dataSource);
            }

            return this.loading;
        },
        // 数据源（绑定的值）发生变化
        change(val) {
            this.form = Object.assign({}, val);
        },
        // 表格行添加前触发
        dynamicBeforeAdd(row, col) {
            if (col.beforeAdd) {
                col.beforeAdd(this.form, row);
            }
        },
        // 表格开始编辑前触发
        dynamicBeforeEdit(row, prop) {
            this.$emit('dynamic-before-edit', row, prop);
        },
        // 表格行保存后触发
        dynamicSaved(row, prop, data) {
            this.$emit('dynamic-saved', row, prop);
            data.col.saved && data.col.saved(row, this.form[data.col.prop], data.index);
        },
        // 表格行删除后触发
        dynamicDeleted(row, index, col) {
            if (col.delete) {
                col.delete(this.form, row);
            }
        },
        dynamicSelectionChange(val, col) {
            if (col.selectionChange) {
                col.selectionChange(val, this.form, this);
            }
        },
        // 按钮点击事件
        onClick(col) {
            if (col.click) {
                col.click(this);
            }
        },
        // 操作栏自定义按钮点击事件
        buttonClick(button) {
            if (button && button.click) {
                this.$set(button, 'loading', true);
                button.click(this);
            }
        },
        getGroupHide(group) {
            if (group.hide !== undefined) {
                if (typeof group.hide === 'function') {
                    return group.hide(this.editType, this.form);
                } else {
                    return group.hide;
                }
            } else {
                return true;
            }
        },
        inputNumber(val, col) {
            this.form[col.prop] = val.replace(/[^\d]/g, '');
        },
        clearObj(data) {
            for (let property in data) {
                if (data[property] instanceof Object) {
                    this.clearObj(data[property]);
                } else if (data[property] instanceof Array) {
                    data[property] = [];
                } else {
                    data[property] = undefined;
                }
            }
        }
    }
}
</script>

<style lang="scss" scope>
.multiple-height {
    height: 30px;
}

.el-dialog__body {
    padding: 0px !important;
}

.el-main {
    padding: 0px !important;
}

.dialog-body .el-row {
    margin: 5px 0px 5px 5px !important;
}

.el-form-item--medium .el-form-item__content, .el-form-item--medium .el-form-item__label {
    /* line-height: 18px; */
    position: relative;

}

#tabList {
    list-style: none;
    margin-top: 0;
    padding-top: 0;
    border-right: 1px solid #d2d6de;
}

#tabList li:after {
    content: '';
    display: inline-block;
    width: 100%;
}

#tabList li {
    //padding: 0px 20px;
    line-height: 35px;
    height: 35px;
    cursor: pointer;
    -moz-text-align-last: justify;
    text-justify: distribute-all-lines;
    text-align: justify;
    display: block;
    position: relative;

    i {
        display: none;
        position: absolute;
        width: 2px;
        height: 100%;
        background-color: #668eff;
        top: 0px;
        right: -2px;
    }

    &.active {
        i {
            display: block;
        }
    }

}

#content {
    position: relative;
    padding-top: 10px;
    padding-right: 10px;
    font-size: 18px;
    max-height: 500px;
}

.dialogs {
    .el-form-item--medium .el-form-item__content, .el-form-item--medium .el-form-item__label {
        line-height: 40px;
    }

}

.el-dialog__wrapper {
    overflow: none;
    overflow-y: hidden;
}

//    .el-form-item label:after {
//         content: " ";
//         display: inline-block;
//         width: 100%;
//     }

//     .el-form-item__label {
//         text-align: justify;
//         height: 20px;
//     }
//     /* 这里去除必选字段的*,这个符号会造成一定影响,去掉之后我用了li列表进行定位,在前面加上" * ". */
//     .el-form-item.is-required .el-form-item__label:before {
//         content: none !important;
//     }
</style>

