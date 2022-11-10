<template>
    <el-select
        v-model="val"
        :multiple="true"
        :collapse-tags="collapse"
        filterable
        :placeholder="this.msg"
        :size="size"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.serialNumber"
            :label="item.serialNumber"
            :value="item.serialNumber">
        </el-option>
        <el-checkbox @change="selectAll">全选</el-checkbox>
    </el-select>

</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getSerialListByReceiveDetailId} from "@/api/wms/instock/receive";
import func from "@/util/func";


export default {
    name: "NodesSerialQuery",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array],
        snCodeNum: 0,
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
        receiveDetailId: {String},
        // 多选时是否将选中值按文字的形式展示
        collapse: {type: Boolean, require: false, default: () => true}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: [],
            msg: "请选择"
        }
    },
    created() {
        if (func.isNotEmpty(this.receiveDetailId)) {
            this.getDataSource()
        }
    },

    watch: {
        selectVal(newVal) {
            if (newVal.length > this.snCodeNum) {
                newVal.pop()
                this.$message.warning("最多选择" + this.snCodeNum + "2个序列号")
            } else {
                this.val = newVal;
            }

        },
        receiveDetailId() {
            this.getDataSource()
        }
    },
    methods: {
        async getDataSource() {
            const response = await getSerialListByReceiveDetailId(this.receiveDetailId);
            this.dataSource = response.data.data;
            if (this.dataSource.length === 0) {
                this.msg = "该批次没有序列号"
            }
        },
        onChange(val) {
            this.$emit('selectValChange', val);

        },
        selectAll() {
            if (this.val.length === this.dataSource.length) {
                this.val = []
            } else {
                this.val = this.dataSource.map(e => e.serialNumber)
            }
            this.$emit('selectValChange', this.val);
        },
    }
}
</script>

<style scoped>

</style>
