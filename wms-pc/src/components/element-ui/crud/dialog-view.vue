<template>
    <el-drawer
        :title="title"
        :visible.sync="visible"
        :destroy-on-close="true"
        class="view-dialog"
        size="50%"
        append-to-body
        @closed="onClosed">
        <span style="padding:10px;">
            <el-row v-for="group in groupList">
                <el-col :span="24">
                    <h4 style="line-height:30px;margin-top:20px;">{{ group.label }}</h4>
                    <el-descriptions v-if="group.type != 'dynamic'"
                                     :column="2"
                                     border>
                        <template v-for="column in group.column">
                            <el-descriptions-item v-if="column.view != false"
                                                  :label="column.label"
                                                  label-class-name="my-label"
                            >
                                <div @dblclick="onCopy(undefined, column)">
                                    {{ getTableContent(data, column) }}
                                </div>
                            </el-descriptions-item>
                        </template>
                    </el-descriptions>
                    <el-table v-else
                              :data="data[group.prop]"
                              border
                              style="width: 100%"
                              height="220"
                              header-cell-class-name="headerCell"
                              @cell-dblclick="(row, tableColumn)=>onCopy(row, tableColumn, group.column)"
                    >
                        <el-table-column v-for="col in group.column"
                                         :prop="col.prop"
                                         :label="col.label"
                                         :width="col.width"
                                         :show-overflow-tooltip="col.overHidden !== false"
                        >
                            <template slot-scope="scope">
                                <span>
                                    {{ getTableContent(scope.row, col) }}
                                </span>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </span>
    </el-drawer>
</template>

<script>

export default {
    name: 'dialog-view',
    data() {
        return {
            visible: false,
            title: '查看',
            groupList: [],
            data: {},
        }
    },
    methods: {
        getTableContent(row, col) {
            if (col.show && col.show.length > 0) {
                return row[col.show[0]];
            } else if (col.props && col.props.label) {
                return row[col.props.label];
            } else {
                return row[col.prop];
            }
        },
        open(group, data, title) {
            this.title = title + '-查看';
            // 处理下group，子级包含多项，并且有子表格的情况下显示会异常
            group.forEach(g => {
                if (!g.column || g.column.length == 0) {
                    return;
                }
                let new_group = {
                    label: g.label,
                    column: []
                };
                for (let i = 0; i < g.column.length; i++) {
                    let col = g.column[i];
                    if (col.view == false) {
                        continue;
                    }
                    if (col.type == 'dynamic') {
                        let col_group = {
                            label: new_group.label,
                            type: 'dynamic',
                            prop: col.prop,
                            column: this.deepClone(col.children)
                        }
                        this.groupList.push(col_group);
                    } else {
                        new_group.column.push(this.deepClone(col));
                    }
                }
                if (new_group.column.length > 0) {
                    this.groupList.push(new_group);
                }
            });
            this.data = data;
            this.visible = true;
        },
        onCopy(row, column, columnList) {
            let col = columnList ? columnList.find(u => u.prop == column.property) : column;
            if (!col) {
                return;
            }
            if (!row) {
                row = this.data;
            }
            let content = this.getTableContent(row, col);
            if (content && content.length > 0) {
                this.$Clipboard({
                    text: content
                }).then(() => {
                    this.$message({message: '复制内容：' + content, type: 'success'});
                }).catch(() => {
                    this.$message({message: '复制失败！：', type: 'error'});
                });
            }
        },
        onClosed(){
            this.groupList = [];
        }
    }
}
</script>
<style lang="scss">
.el-drawer__header {
    margin-bottom: 0px !important;
}

.el-drawer__body {
    margin-left: 20px;
    margin-right: 20px;
}

.el-descriptions__title {
    font-size: 13px;
    font-weight: 500;
}

.my-label {
    background: #f2f2f2;
}

.view-dialog .el-table__cell .cell {
    line-height: 36px;
}

.view-dialog .el-table .headerCell {
    padding: 0px;
    background-color: #f2f2f2 !important;
}

.view-dialog .el-table {
    margin-top: 0px !important;
}
</style>
