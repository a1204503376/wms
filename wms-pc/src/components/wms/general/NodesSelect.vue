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

                <span style="float: left" v-if="isCustomTemplate">{{ item[labelName] }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px" v-if="isCustomTemplate">{{ item[valueName] }}</span>

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
        selectVal: [Number, Array, String],
        multiple: {type: Boolean, required: false, default: true},
        dataSource: {type: Array, required: true},
        valueKey:{type: String, required: false,default:()=>'value'},
        labelName:{type: String, required: false,default:()=>'label'},
        valueName:{type: String, required: false,default:()=>'value'},
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
