<template>
    <!-- 操作栏 -->
    <el-table-column v-if="vaildData(tableOption.menu, true)"
                     :align="tableOption.menuAlign || 'center'"
                     :label="tableOption.menuTitle || '操作'"
                     :width="tableOption.menuWidth || menuWidth"
                     :fixed="vaildData(tableOption.menuFixed, 'right')"
                     prop="menu"
    >
        <template slot-scope="scope">
            <!-- 编辑 -->
            <template v-if="tableOption.editBtn">
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="编辑"
                    effect="dark"
                    placement="top"
                >
                    <el-button type="text" @click="table.rowEdit(scope)">
                        <i class="el-icon-edit"></i>
                    </el-button>
                </el-tooltip>
            </template>
            <!-- 查看 -->
<!--            <el-tooltip-->
<!--                v-if="tableOption.viewBtn"-->
<!--                :enterable="false"-->
<!--                class="item"-->
<!--                content="查看"-->
<!--                effect="dark"-->
<!--                placement="top"-->
<!--            >-->
<!--                <el-button type="text" @click="table.rowView(scope)">-->
<!--                    <i class="el-icon-view"></i>-->
<!--                </el-button>-->
<!--            </el-tooltip>-->
            <!-- 删除 -->
            <el-tooltip
                v-if="tableOption.delBtn"
                :enterable="false"
                class="item"
                content="删除"
                effect="dark"
                placement="top"
            >
                <el-button type="text" @click="table.rowDel(scope)">
                    <i class="el-icon-delete"></i>
                </el-button>
            </el-tooltip>
            <!-- 复制 -->
            <el-tooltip
                v-if="tableOption.copyBtn"
                :enterable="false"
                class="item"
                content="复制"
                effect="dark"
                placement="top"
            >
                <el-button type="text" @click="table.rowCopy(scope.$index, scope.row)">
                    <i class="el-icon-copy-document"></i>
                </el-button>
            </el-tooltip>
            <!-- 调整顺序 -->
<!--            <template v-if="tableOption.moveBtn">-->
<!--                <el-button v-if="!scope.row.isEdit" type="text">-->
<!--                    <i-->
<!--                        class="el-icon-caret-top"-->
<!--                        @click="rowMoveup(scope.row, scope.$index)"-->
<!--                    ></i>-->
<!--                </el-button>-->
<!--                <el-button v-if="!scope.row.isEdit" type="text">-->
<!--                    <i-->
<!--                        class="el-icon-caret-bottom"-->
<!--                        @click="rowMovedown(scope.row, scope.$index)"-->
<!--                    ></i>-->
<!--                </el-button>-->
<!--            </template>-->
            <!-- 自定义按钮 -->
            <template v-for="menuBtn in tableOption.menuBtn">
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
</template>

<script>
export default {
    name: "column-menu",
    inject: ["table"],
    props: {
        tableOption: {
            type: Object,
            default: () => {
                return {};
            }
        }
    },
    data(){
        return {
            menuWidth: 80
        }
    },
    activated() {
    },
    mounted(){
        this.$nextTick(()=>{
            this.menuWidth = 0;
            if (this.tableOption.editBtn) {
                this.menuWidth += 30;
            }
            if (this.tableOption.viewBtn) {
                this.menuWidth += 30;
            }
            if (this.tableOption.delBtn) {
                this.menuWidth += 30;
            }
            // if (this.tableOption.moveBtn) {
            //     this.menuWidth += 60;
            // }
            if (this.tableOption.copyBtn) {
                this.menuWidth += 30;
            }
            if (this.tableOption.menuBtn && this.tableOption.menuBtn.length > 0) {
                this.menuWidth += this.tableOption.menuBtn.length * 30;
            }
            if (this.menuWidth < 80) {
                this.menuWidth = 80;
            }
        });
    }
}
</script>

<style scoped>

</style>
