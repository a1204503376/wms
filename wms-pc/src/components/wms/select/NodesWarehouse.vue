<template>
    <el-select
        v-model="val"
        collapse-tags
        placeholder="请选择"
        :multiple="multiple"
        size="mini"
        style="width:100%;"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.whId"
            :label="item.whName"
            :value="item.whId">
            <span style="float: left">{{ item.whCode }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.whName }}</span>
        </el-option>
    </el-select>

</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getWarehouseSelectResponseList} from "@/api/wms/warehouse/warehouse";


export default {
    name: "NodesWarehouse",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false}
    },
    data() {
        return {
            val:this.selectVal,
            dataSource: []
        }
    },
    async created() {
        await this.getDataSource();
    },
    methods: {
        async getDataSource() {
            const response = await getWarehouseSelectResponseList();
            this.dataSource = response.data.data;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
