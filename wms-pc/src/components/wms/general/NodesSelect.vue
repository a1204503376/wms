<template>
    <el-select
        v-model="val"
        :multiple="multiple"
        collapse-tags
        placeholder="请选择"
        size="mini"
        style="width:100%;"
        :value-key="valueKey"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.value"
            :label="item[labelName]"
            :value="item">
            <span style="float: right" v-if="isCustomTemplate">{{ item[labelName] }}</span>
            <span style="float: left; color: #8492a6; font-size: 13px" v-if="isCustomTemplate">{{ item[valueName] }}</span>
        </el-option>
    </el-select>
</template>

<script>

import func from "@/util/func";

export default {
    name: "NodesSelect",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        // 选中的数据
        selectVal: [Number, Array, String, Object],
        // 是否多选 false:单选 默认为多选
        multiple: {type: Boolean, required: false, default: true},
        // 数据源
        dataSource: {type: Array, required: true},
        // 唯一标识
        valueKey:{type: String, required: false,default:()=>'value'},
        // label属性名
        labelName:{type: String, required: false,default:()=>'label'},
        // value属性名
        valueName:{type: String, required: false,default:()=>'value'},
        // value和name是否都显示，true:都显示 默认为不都显示
        isCustomTemplate:{type: Boolean, required: false,default:()=>false},
    },
    data() {
        return {
            val: this.selectVal
        }
    },
    created() {
        if (func.isNumber(this.val) && this.val <= 0) {
            this.val = '';
        }
    },
    methods: {
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
