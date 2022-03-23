<template>
    <div>
        <!-- 索引列 -->
        <el-table-column
            :align="tableOption.align || 'center'"
            :index="getTableIndex"
            fixed="left"
            type="index"
            :width="tableOption.children && tableOption.children.length > 0 ? 80 : 55"
        >
            <template slot="header">
                <el-tooltip
                    v-if="tableOption.children && tableOption.children.length > 0"
                    :enterable="false"
                    content="新增"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-plus"
                        size="mini"
                        style="padding: 4px"
                        type="primary"
                        @click="onAdd"
                    ></el-button>
                </el-tooltip>
                <el-tooltip
                    v-if="tableOption.delBtn === undefined ? true : tableOption.delBtn"
                    :enterable="false"
                    content="批量删除"
                    effect="dark"
                    placement="top"
                >
                    <el-button
                        circle
                        icon="el-icon-delete"
                        size="mini"
                        style="padding: 4px"
                        type="danger"
                        plain
                        @click="onMultiDelete"
                    ></el-button>
                </el-tooltip>
                <span v-else> # </span>
            </template>
        </el-table-column>
        <!-- 选择列 -->
        <el-table-column
            v-if="tableOption.selection == undefined ? true : tableOption.selection"
            :align="tableOption.align || 'center'"
            fixed="left"
            type="selection"
            width="55"
        >
        </el-table-column>
    </div>
</template>

<script>
export default {
    name: "column-default",
    inject: ["table"],
    props: {
        tableOption: {
            type: Object, default: () => {
            }
        }
    },
    methods: {
        getTableIndex(index) {
            // 表格序号显示规则
            return index + 1;
        },
        onAdd(){
            this.table.$refs.nodesDialog.open(this.tableOption.children, {}, 'new', this.table.option.name);
        },
        onMultiDelete() {
            if (this.table.selectionList.length == 0) {
                return;
            }
            this.$confirm("确定删除当前选中数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                this.table.loading = true;
                this.table.selectionList.forEach(row => {
                    let index = this.table.tableData.indexOf(row);
                    if (index < 0) {
                        return;
                    }
                    this.table.tableData.splice(index, 1);
                    // 如果有index列，调整index列的值
                    for (let i = 0; i < this.table.tableData.length; i++) {
                        let findRow = this.table.tableData[i];
                        // 找到index列
                        for (let j = 0; j < this.table.tableOption.column.length; j++) {
                            let col = this.table.tableOption.column[j];
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
                    if (this.table.option.onDeleted) {
                        this.table.option.onDeleted(this.table.formData, row);
                    }
                });
                this.table.onDataChange();
                this.table.loading = false;
            });
        }
    }
}
</script>

<style scoped>

</style>
