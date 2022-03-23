<template>
    <span>
        <el-table
            :id="tableOption.prop"
            ref="nodesTable"
            v-loading="loading"
            :data="tableData"
            :height="tableOption.height || 220"
            border
            class="table-style"
            show-overflow-tooltip
            style="width: 100%"
            @selection-change="selectionChange"
            @row-click="rowClick"
            @cell-dblclick="onCellDblClick"
        >
        <!-- 暂无数据提醒 -->
        <template slot="empty">
            <div class="nodes-crud__empty">
                <slot v-if="$slots.empty"
                      name="empty"></slot>
                <avue-empty v-else
                            :desc="(option && option.emptyText) || '暂无数据'"
                            image="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjQiIGhlaWdodD0iNDEiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CiAgPGcgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoMCAxKSIgZmlsbD0ibm9uZSIgZmlsbC1ydWxlPSJldmVub2RkIj4KICAgIDxlbGxpcHNlIGZpbGw9IiNGNUY1RjUiIGN4PSIzMiIgY3k9IjMzIiByeD0iMzIiIHJ5PSI3Ii8+CiAgICA8ZyBmaWxsLXJ1bGU9Im5vbnplcm8iIHN0cm9rZT0iI0Q5RDlEOSI+CiAgICAgIDxwYXRoIGQ9Ik01NSAxMi43Nkw0NC44NTQgMS4yNThDNDQuMzY3LjQ3NCA0My42NTYgMCA0Mi45MDcgMEgyMS4wOTNjLS43NDkgMC0xLjQ2LjQ3NC0xLjk0NyAxLjI1N0w5IDEyLjc2MVYyMmg0NnYtOS4yNHoiLz4KICAgICAgPHBhdGggZD0iTTQxLjYxMyAxNS45MzFjMC0xLjYwNS45OTQtMi45MyAyLjIyNy0yLjkzMUg1NXYxOC4xMzdDNTUgMzMuMjYgNTMuNjggMzUgNTIuMDUgMzVoLTQwLjFDMTAuMzIgMzUgOSAzMy4yNTkgOSAzMS4xMzdWMTNoMTEuMTZjMS4yMzMgMCAyLjIyNyAxLjMyMyAyLjIyNyAyLjkyOHYuMDIyYzAgMS42MDUgMS4wMDUgMi45MDEgMi4yMzcgMi45MDFoMTQuNzUyYzEuMjMyIDAgMi4yMzctMS4zMDggMi4yMzctMi45MTN2LS4wMDd6IiBmaWxsPSIjRkFGQUZBIi8+CiAgICA8L2c+CiAgPC9nPgo8L3N2Zz4K"
                            size="50"></avue-empty>
            </div>
        </template>
        <column ref="column"
                :columnOption="tableOption.column"
                :tableOption="tableOption">
            <template v-for="item in tableOption.column"
                      slot-scope="scope"
                      :slot="item.prop">
                <slot v-bind="scope"
                      :name="item.prop"></slot>
            </template>
            <column-default ref="columnDefault"
                            slot="header"
                            :tableOption="tableOption"></column-default>
            <column-menu slot="footer"
                         :tableOption="tableOption">
                <template slot="menu"
                          slot-scope="scope">
                    <slot v-bind="scope"
                          name="menu"></slot>
                </template>
                <template slot="menuBtn"
                          slot-scope="scope">
                    <slot v-bind="scope"
                          name="menuBtn"></slot>
                </template>
            </column-menu>
        </column>
    </el-table>
        <nodes-dialog
            ref="nodesDialog"
            :dataSource="dialogEdit.dataSource"
            :group="dialogEdit.group"
            :isEdit="dialogEdit.isEdit"
            :isNew="dialogEdit.isNew"
            :isView="dialogEdit.isView"
            :owner="formData"
            :width="dialogEdit.width"
            @callback="callbackEdit"
            @before-open="beforeOpen"
        ></nodes-dialog>
    </span>
</template>

<script>
import Column from "./column";
import ColumnDefault from "./column-default";
import ColumnMenu from "./column-menu";
import nodesDialog from "../dialog/index"
import request from '@/router/axios';
import schema from "async-validator";
import Sortable from 'sortablejs';

export default {
    name: "nodes-table",
    components: {
        Column,
        ColumnMenu,
        ColumnDefault,
        nodesDialog,
    },
    provide() {
        return {
            table: this
        };
    },
    model: {
        prop: "data",
        event: "data-change",
    },
    watch: {
        data: {
            handler: function (nv, ov) {
                if (JSON.stringify(nv) == JSON.stringify(ov)) {
                    return;
                }
                let data = [];
                nv.forEach(row => {
                    let findObj = this.tableData.find(u => u.$id == row.$id);
                    let obj = findObj || row;
                    if (!obj.$id) {
                        this.$set(obj, '$id', this.guid());
                    }
                    data.push(obj);
                });
                if (!this.validatenull(this.formData)) {
                    this.addDefaultRow(false, data);
                }
                this.tableData = data;
            },
            deep: true,
            immediate: true
        },
        formData: {
            handler: function (nv, ov) {
                if (!this.validatenull(nv)) {
                    this.addDefaultRow(true, this.tableData);
                }
            },
        },
        tableData: {
            handler: function (nv, ov) {
                this.doLayout();
            },
        },
        option: {
            handler: function (nv, ov) {
                if (JSON.stringify(nv) == JSON.stringify(ov)) {
                    return;
                }
                this.tableOption = this.option;//this.deepClone(this.option);
                if (!this.validatenull(this.formData)) {
                    this.addDefaultRow(true, this.tableData);
                }
            },
            deep: true,
            immediate: true
        }
    },
    props: {
        formData: {
            type: Object, default: function () {
                return {};
            }
        },
        option: {
            type: Object, default: function () {
                return undefined;
            }
        },
        data: {
            type: Array, default: function () {
                return [];
            }
        },
    },
    data() {
        return {
            loading: false,
            tableData: [],
            selectionList: [],
            addingRow: false,
            tableOption: {},
            dialogEdit: {
                visible: false,
                isNew: false,
                isView: false,
                isEdit: false,
                group: [],
                dataSource: null,
                index: -1,
            },
        }
    },
    mounted() {
        this.$nextTick(() => {
            if (this.option && this.option.moveBtn) {
                this.rowDrop();
            }
        })
    },
    methods: {
        doLayout() {
            this.$nextTick(()=>{
                if (this.$refs.nodesTable) {
                    this.$refs.nodesTable.doLayout();
                }
            });
        },
        /**
         * 当选择项发生变化时会触发该事件
         * @param val 选中项
         */
        selectionChange(val) {
            this.selectionList = val;
            this.$emit("selection-change", val);
        },
        /**
         * 当某一行被点击时会触发该事件
         * @param row
         * @param column
         * @param event
         */
        rowClick(row, column, event) {
            if (row.$edit) {
                return;
            }
            let editRowIndex = this.tableData.findIndex(item => item.$edit == true);
            if (editRowIndex > -1) {
                let editRow = this.tableData[editRowIndex];
                this.validateRow(editRow, editRowIndex, (valid) => {
                    if (valid) {
                        this.tableData.forEach(item => {
                            this.$set(item, '$edit', item.$id == row.$id);
                        });
                    }
                    return valid;
                });
            }
            // if (this.tableOption.edit != false) {
            //     row.$edit = true;
            // }
        },
        /**
         * 增加默认行
         */
        addDefaultRow(isEdit, data) {
            if (!this.tableOption || !this.tableOption.column ||
                (this.tableOption.children && this.tableOption.children.length > 0) ||
                this.hasEmptyRow(data) || this.tableOption.edit == false) {
                return;
            }
            if (this.addingRow) {
                return;
            }
            this.addingRow = true;
            let default_row = {
                $id: this.guid(),
                $edit: isEdit,
                $default: {},
            };
            let loadingCount = 0;
            this.tableOption.column.forEach(col => {
                if (col.type === "index") {
                    this.$set(
                        default_row,
                        col.prop,
                        this.$refs.columnDefault.getTableIndex(data.length)
                    );
                }
                // if (!col.static) {
                //     this.$set(col, "dicData", []);
                // }
                // 调用接口，获取数据源
                if (col.dicUrl && (!col.dicData || col.dicData.length == 0)) {
                    loadingCount++;
                    // 拼接接口参数
                    let requestUrl = col.dicUrl;
                    if (col.search && (this.formData)) {
                        requestUrl += "?";
                        let params = [];
                        // 如果在上级没有找到对应字段，就往上上级找
                        for (let key in col.search) {
                            if (this.formData && this.formData[key]) {
                                params.push(key + "=" + this.formData[key]);
                            }
                            if (this.form && this.form[key]) {
                                params.push(key + "=" + this.form[key]);
                            }
                        }

                        requestUrl += params.join("&");
                    }
                    request({
                        url: requestUrl,
                        method: "get",
                    }).then((res) => {
                        this.$set(col, "dicData", res.data.data);
                    }).finally(() => {
                        loadingCount--;
                    });
                }
                if (col.default !== undefined) {
                    let value;
                    if (typeof col.default === "function") {
                        value = col.default(data);
                    } else {
                        value = col.default;
                    }
                    this.$set(default_row, col.prop, value);
                } else {
                    if (col.type === "inputNumber") {
                        this.$set(default_row, col.prop, col.min ? col.min : 0);
                    } else {
                        this.$set(default_row, col.prop, "");
                    }
                }
            });
            let self = this;
            let interval = setInterval(function () {
                if (loadingCount === 0) {
                    data.push(default_row);
                    if (self.option.rowAdd) {
                        self.option.rowAdd(default_row, self.root);
                    }
                    self.loading = false;
                    default_row.$default = JSON.parse(JSON.stringify(default_row));
                    delete default_row.$default.$default;
                    delete default_row.$default.$edit;
                    self.addingRow = false;
                    if (data.findIndex(row => row.$edit) < 0 && data.length > 0) {
                        data[data.length - 1].$edit = true;
                    }
                    self.tableData = data;
                    clearInterval(interval);
                }
            });
        },
        /**
         * 处理行删除中的逻辑
         * @param row
         */
        rowDel(scope) {
            let row = scope.row;
            if ((!this.tableOption.children || this.tableOption.children.length == 0) &&
                (this.isEmptyRow(row) || this.tableData.length == 1)) {
                return;
            }
            if (this.tableOption.del) {
                if (scope.$index === -1 || !row[this.tableOption.del.id]) {
                    this.rowDelete(scope.$index, scope.row);
                } else {
                    this.$confirm("确定删除当前数据？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    }).then(() => {
                        this.loading = true;
                        let ids = row[this.tableOption.del.id];
                        // 调用接口提交
                        request({
                            url: this.tableOption.del.url,
                            method: "post",
                            params: {
                                ids,
                            },
                        }).then((res) => {
                            this.rowDelete(scope.$index, scope.row);
                        }).finally(() => {
                            this.loading = false;
                        });
                    });
                }
            } else {
                this.rowDelete(scope.$index, scope.row);
            }
        },
        /**
         * 删除表格数据中指定行
         * @param index 索引
         * @param row 行
         */
        rowDelete(index, row) {
            if (index > -1) {
                // 从所有数据中删除
                this.tableData.splice(index, 1);
            }
            // 如果有index列，调整index列的值
            for (let i = 0; i < this.tableData.length; i++) {
                let findRow = this.tableData[i];
                // 找到index列
                for (let j = 0; j < this.tableOption.column.length; j++) {
                    let col = this.tableOption.column[j];
                    if (col.type !== "index") {
                        continue;
                    }
                    if (!row[col.prop] || !row[col.prop]) {
                        continue;
                    }
                    if (row[col.prop] > row[col.prop]) {
                        row[col.prop]--;
                    }
                }
            }
            // 触发删除后的事件
            if (this.option.onDeleted) {
                this.option.onDeleted(this.formData, row);
            }
            this.onDataChange();
        },
        /**
         * 复制行
         * @param index 索引
         * @param row 行
         */
        rowCopy(index, row) {
            if (row.$edit) {
                // 如果当前行为编辑状态，则自动保存后再复制
                this.validateRow(row, index, (valid) => {
                    if (valid) {
                        this.copyItem(row, index);
                    }
                });
                return;
            } else {
                // 非编辑状态直接复制
                this.copyItem(row, index);
            }
        },
        /**
         * 复制指定行的数据
         * @param row
         * @param index
         */
        copyItem(row, index) {
            let obj = {
                $edit: false,
                $id: this.guid(),
            };
            let data = Object.assign(this.deepClone(row), obj);
            this.tableData.splice(0, 0, data);
        },
        /**
         * 行编辑
         * @param scope
         */
        rowEdit(scope) {
            this.$refs.nodesDialog.open(
                this.tableOption.children,
                this.deepClone(scope.row),
                'edit',
                this.tableOption.name,
                scope.$index);
        },
        /**
         * 验证是否为空行(只用于判断可编辑行)
         * @param row
         * @returns {boolean}
         */
        isEmptyRow(row) {
            if (!row.$default) {
                return false;
            }
            for (let property in row.$default) {
                let col = this.tableOption.column.find(u => u.prop == property);
                if (col && col.type == 'text') {
                    continue;
                }
                if (row[property] != row.$default[property]) {
                    return false;
                }
            }
            return true;
        },
        hasEmptyRow(data) {
            if (!data || data.length == 0) {
                return false;
            }
            let result = false;
            for (let i = 0; i < data.length; i++) {
                let row = data[i];
                if (this.isEmptyRow(row)) {
                    result = true;
                    break;
                }
                result = false;
            }
            return result;
        },
        onDataChange() {
            let data = [];
            this.tableData.forEach((row) => {
                if (this.isEmptyRow(row)) {
                    return;
                }
                let obj = this.deepClone(row);
                delete obj.$default;
                delete obj.$edit;
                data.push(obj);
            });
            this.$emit('data-change', data);
            this.doLayout();
        },
        onCellDblClick(row, column, cell, event) {
            // 如果当前行为编辑状态，需要从数据源里取数据
            let content = row[column.property];
            if (row.$edit) {
                return;
                let col = this.option.column.find(item => item.prop == column.property);
                if (col && col.props) {
                    let dicData = col.dicData.find(item => item.value = content);
                    if (dicData) {
                        content = dicData[col.props.label];
                    }
                }
            }
            if (!content || content.length < 1) {
                return;
            }
            this.$Clipboard({
                text: content
            }).then(() => {
                this.$message({message: '复制内容：' + content, type: 'success'});
            }).catch(() => {
                this.$message({message: '复制失败！：', type: 'error'});
            });
        },
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
        /**
         * 验证数据
         */
        validateRow(row, index, callback) {
            if (this.isEmptyRow(row)) {
                callback && callback(true);
                return;
            }
            let rules = {};
            this.tableOption.column.forEach((col) => {
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
            validator.validate(row).then(() => {
                    callback && callback(true);
                }
            ).catch(({errors}) => {
                    errors.forEach((v) => {
                        for (let i of columns) {
                            if (i.classList.contains("is_" + v.field)) {
                                i.classList.add("is_error");
                            }
                        }
                    });
                    callback && callback(false);
                }
            );
        },
        /**
         * 验证数据
         * @param callback
         */
        validate(callback) {
            let editRowIndex = this.tableData.findIndex(item => item.$edit == true);
            if (editRowIndex > -1) {
                let editRow = this.tableData[editRowIndex];
                this.validateRow(editRow, editRowIndex, (valid) => {
                    if (valid) {
                        callback && callback(valid);
                    }
                });
            }
        },
        beforeOpen(done, type, finish) {
            if (type !== undefined && type !== "new") {
                done();
            }
        },
        callbackEdit(callback) {
            if (callback.success) {
                let data = callback.data;
                if (callback.type == 'new') {
                    // 如果存在索引行，则给索引行赋值
                    let filterList = this.tableOption.column.filter((col) => {
                        return col.type === "index";
                    });
                    if (filterList && filterList.length > 0) {
                        this.$set(
                            data,
                            filterList[0].prop,
                            this.$refs.columnDefault.getTableIndex(this.tableData.length)
                        );
                    }
                    this.tableData.push(data);
                } else {
                    // 编辑
                    if (callback.index > -1) {
                        this.tableData.splice(callback.index, 1, data);
                    }
                }
            }
            callback.loading();
            this.dialogEdit.visible = callback.visible;
            this.onDataChange();
        },
        rowDrop() {
            const el = document.querySelectorAll(
                ".table-style .el-table__body-wrapper > table > tbody"
            )[0];
            Sortable.create(el, {
                disabled: !this.option.moveBtn, // 拖拽不可用? false 启用
                ghostClass: 'sortable-ghost', //拖拽样式
                animation: 150, // 拖拽延时，效果更好看
                onEnd: (e) => { // 拖拽结束时的触发
                    let arr = this.tableData; // 获取表数据
                    // 数据处理，获取最新的表格数据
                    arr.splice(e.newIndex, 0, arr.splice(e.oldIndex, 1)[0]);
                    arr.forEach((value, index) => {
                        value.order = index;
                    });
                    this.$nextTick(function () {
                        this.tableData = arr;
                        this.rowDrop();
                    })
                },
            });
        },
    }
}
</script>

<style scoped>
.table-style .is_error .el-input__inner {
    border-color: #f56c6c;
}
</style>
