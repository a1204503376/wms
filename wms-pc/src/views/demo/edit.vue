<template>
    <basic-container>
        <el-container v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>收货单</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="单据编码" show-message>
                                <el-input v-model="form.params.asnBillNo"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="入库方式" show-message>
                                <nodes-in-store-mode v-model="form.params.inStorageType"
                                                     :multiple="false"></nodes-in-store-mode>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="单据类型" show-message>
                                <nodes-in-store-type v-model="form.params.billTypeCd"
                                                     :multiple="false"></nodes-in-store-type>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>明细</h3>
                    </el-row>
                    <el-row>
                        <el-col>
                            <el-table
                                ref="table"
                                :data="table.data"
                                border
                                highlight-current-row
                                size="mini">
                                    <el-table-column
                                        prop="skuCode"
                                        label="物品编码"
                                        show-overflow-tooltip>
                                        <template v-slot="{row}">
                                            <el-input v-model="row.skuCode"></el-input>
                                        </template>
                                    </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
                    <el-button
                        type="primary"
                        @click="onSubmit">保 存
                    </el-button>
                    <el-button @click="onClose">关 闭</el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesInStoreType from "@/components/wms/select/NodesInStoreType";

export default {
    name: "edit",
    components: {NodesInStoreType, NodesInStoreMode},
    props: ['id'],
    data() {
        return {
            loading: false,
            form: {
                params: {
                    asnBillNo: '',
                    inStorageType: [],
                    billTypeCd: 0,
                }
            },
            table: {
                data: [
                    {
                        skuCode: 'DML001'
                    },
                    {
                        skuCode: 'DML002'
                    },
                ]
            }
        }
    },
    created() {
        console.log(this.id)
    },
    methods: {
        onSubmit() {
        },
        onClose() {
            this.$router.$avueRouter.closeTag(this.$route.path);
            this.$router.back();
        }
    }
}
</script>

<style scoped>
.table .thead {
    width: 100%;
    display: flex;
    border-bottom: 1px solid #ccc;
    border-bottom-color: rgba(205, 206, 207, 0.5);
    align-items: center;
    height: 45px;
    background-color: rgba(189, 188, 188, 0.1);
}

.table .tbody {
    font-weight: lighter;
}

.tbody .tr, .thead .tr {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 10px;
    font-weight: bolder;

}

.tbody .tr {
    height: 40px;
    border-bottom: 1px solid #ccc;
    border-bottom-color: rgba(205, 206, 207, 0.5);
}

.tr .td, .tr .th {
    flex: 8;
}

.tr .edit {
    flex: 1;
    cursor: pointer;
    color: #FF7002;
}

.thead .tr .edit {
    display: flex;
    justify-content: center;
    cursor: none;
    color: #000;
}


</style>
