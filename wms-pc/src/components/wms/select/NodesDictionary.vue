<template>
    <el-select
        v-model="val"
        :code="code"
        :clearable="true"
        :disabled="disabled"
        :multiple="multiple"
        collapse-tags
        :size="size"
        value-key="value"
        @change="onChange">
        <el-option
            v-for="item in this.dataSource"
            :key="item.dictKey"
            :label="item.dictValue"
            :value="item.dictKey">
            <span style="float: left">{{ item.dictValue }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.dictKey }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getDictByCode} from "@/api/core/dict";

export default {
    name: "NodesDictionary",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, Number, String],
        //是否有默认值 true:有默认值  默认为false
        defaultValue: {type: Boolean, required: false, default: () => false},
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
        // 是否只读
        disabled: {type: Boolean, required: false, default: () => false},
        // 字典码 必填
        code: {type: String, required: true},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        selectVal(newVal) {
            this.val = newVal;
        },
    },
    async created() {
        await this.getDataSource()
        if (this.defaultValue) {
            this.val = this.dataSource[0].dictKey
            this.onChange(this.val);
        }
    },
    methods: {
        async getDataSource() {
            let {data: {data}} = await getDictByCode(this.code)
            this.dataSource = data;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
