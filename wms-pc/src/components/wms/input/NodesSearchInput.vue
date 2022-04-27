<template>
    <div class="el-form-item el-form-item--mini">
        <label class="el-form-item__label">{{ label }}</label>
        <el-input v-model="val"
                  class="d-input"
                  size="mini"
                  @input="onInput">
        </el-input>
    </div>
</template>

<script>
export default {
    name: "NodesSearchInput",
    model: {
        prop: 'modelVal',
        event: 'modelValInput'
    },
    props: {
        label: {type: String, require: true}
    },
    data() {
        return {
            val: ''
        }
    },
    methods: {
        onInput(val) {
            this.$emit('modelValInput', val);
        }
    }
}
</script>

<style scoped>

</style>
