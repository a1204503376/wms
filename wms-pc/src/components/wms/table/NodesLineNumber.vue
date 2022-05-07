<template>
    <div>
        {{ lineNumber }}
    </div>
</template>

<script>
export default {
    name: "NodesLineNumber",
    props: {
        index: Number
    },
    computed: {
        lineNumber: function () {
            let val = (this.index + 1) * 10;
            this.$emit('change',val);
            return val;
        }
    }
}
</script>

<style scoped>

</style>
