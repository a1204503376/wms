<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        collapse-tags
        placeholder="请选择"
        :size="size"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.lineNo"
            :label="item.skuCode"
            :value="item.lineNo">
            <span style="float: left">{{ item.lineNo }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.skuCode }}</span>
        </el-option>
    </el-select>

</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getLineNoAndSkuSelectList} from "@/api/wms/outstock/soHeader";


export default {
    name: "NodesOutStock",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        soBillId: {type: String, required: false},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    async created() {
        await this.getLineNoAndSkuSelectList(this.soBillId)

    },
    watch: {
        selectVal(newVal) {
            this.val = newVal;
        }
    },
    methods: {
        async getLineNoAndSkuSelectList(soBillId) {
            const response = await getLineNoAndSkuSelectList(soBillId);
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
