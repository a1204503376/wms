<template>
    <div class="basic-container"
         :class="{'basic-container--block':block}">
        <el-card>
            <slot></slot>
        </el-card>
    </div>
</template>

<script>
export default {
    name: "basicContainer",
    props: {
        radius: {
            type: [String, Number],
            default: 10
        },
        background: {
            type: String
        },
        block: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        styleName() {
            return {
                borderRadius: this.setPx(this.radius),
                background: this.background,
            }
        }
    }
};
</script>

<style lang="scss">
.basic-container {
    padding: 10px 6px;
    box-sizing: border-box;

    &--block {
        height: 100%;

        .el-card {
            height: 100%;
        }
    }

    .el-card {
        width: 100%;
        margin: 0px !important;
    }

    &:first-child {
        padding-top: 0;
    }
}
</style>
