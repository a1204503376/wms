<template>
    <el-select ref="select"
               :value="valueTitle"
               :clearable="clearable"
               :size="size"
               :disabled="disabled"
               @clear="clearHandle">
        <el-input v-model="filterText" :size="size" placeholder="请输入关键字"></el-input>
        <el-option :value="valueTitle" :label="valueTitle" class="options">
            <el-tree
                    id="tree-option"
                    ref="selectTree"
                    class="filter-tree"
                    default-expand-all
                    :accordion="accordion"
                    :data="options"
                    :props="props"
                    :node-key="props.value"
                    :default-expanded-keys="defaultExpandedKey"
                    :filter-node-method="filterNode"
                    @node-click="handleNodeClick">
            </el-tree>
        </el-option>
    </el-select>
</template>

<script>
    export default {
        name: "el-tree-select",
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            // 配置项
            props: {
                type: Object,
                // eslint-disable-next-line vue/require-valid-default-prop
                default: () => ({
                    value: 'id',             // ID字段名
                    label: 'title',         // 显示名称
                    children: 'children'    // 子级字段名
                })
            },
            // 选项列表数据(树形结构的对象数组)
            // eslint-disable-next-line vue/require-valid-default-prop
            options: {
                type: Array, default: function () {
                    return [];
                }
            },
            // 初始值
            value: {type: String, default: ''},
            size: {type: String, default: "medium"},
            // 可清空选项
            clearable: {type: Boolean, default: true},
            // 自动收起
            accordion: {type: Boolean, default: false},
            disabled: {type: Boolean, default: false}
        },
        data() {
            return {
                valueId: null,
                valueTitle: '',
                filterText: '',
                defaultExpandedKey: []
            }
        },
        mounted() {
            this.valueId = this.value;    // 初始值
            this.initHandle();
        },
        methods: {
            filterNode(value, data) {
                if (!value) return true;
                if (!data) return false;
                return data[this.props.label].indexOf(value) !== -1;
            },
            // 初始化值
            initHandle() {
                if(!this.value || !this.$refs.selectTree) {
                    return;
                }
                let node = this.findObj(this.value, this.options);
                if (node) {
                    this.valueTitle = node[this.props.label];
                    this.$refs.selectTree.setCurrentKey(this.value);      // 设置默认选中
                }
                this.initScroll();
            },
            findObj(val, dataSource) {
                for (let i = 0; i < dataSource.length; i++) {
                    let data = dataSource[i];
                    if (data[this.props.value] === val) {
                        return data;
                    } else {
                        if (data[this.props.children] && data[this.props.children].length > 0) {
                            data = this.findObj(val, data[this.props.children]);
                            if (data) {
                                return data;
                            }
                        }
                    }
                }
            },
            // 初始化滚动条
            initScroll() {
                this.$nextTick(() => {
                    let scrollWrap = document.querySelectorAll('.el-scrollbar .el-select-dropdown__wrap')[0];
                    let scrollBar = document.querySelectorAll('.el-scrollbar .el-scrollbar__bar');
                    scrollWrap.style.cssText = 'margin: 0px; max-height: none; overflow: hidden;';
                    scrollBar.forEach(ele => {
                        ele.style.width = 0;
                    });
                })
            },
            // 切换选项
            handleNodeClick(node) {
                this.valueTitle = node[this.props.label];
                this.valueId = node[this.props.value];
                this.$emit('change', this.valueId);
                this.defaultExpandedKey = [];
                // 隐藏下拉框
                if (this.$refs.select) {
                    this.$refs.select.blur();
                }
            },
            // 清除选中
            clearHandle() {
                this.valueTitle = '';
                this.valueId = null;
                this.defaultExpandedKey = [];
                this.clearSelected();
                this.$emit('change', null);
            },
            // 清空选中样式
            clearSelected() {
                let allNode = document.querySelectorAll('#tree-option .el-tree-node');
                allNode.forEach((element) => element.classList.remove('is-current'));
            }
        },
        watch: {
            value:{
                handler: function(newValue, oldValue) {
                    this.valueId = newValue;
                    if (!this.valueId) {
                        this.valueTitle = '';
                        this.defaultExpandedKey = [];
                        this.clearSelected();
                    }
                    this.initHandle();
                }
            },
            filterText(val) {
                this.$refs.selectTree.filter(val);
            },
            options: {
                handler: function (newValue, oldValue) {
                    this.initHandle();
                },
                deep: true,
                immediate: true
            }
        },
    }
</script>

<style scoped>
    .el-scrollbar .el-scrollbar__view .el-select-dropdown__item {
        height: auto;
        max-height: 210px;
        padding: 0;
        overflow: hidden;
        overflow-y: auto;
    }

    .el-select-dropdown__item.selected {
        font-weight: normal;
    }

    ul li >>> .el-tree .el-tree-node__content {
        height: auto;
        padding: 0 20px;
    }

    .el-tree-node__label {
        font-weight: normal;
    }

    .el-tree >>> .is-current .el-tree-node__label {
        color: #409EFF;
        font-weight: 700;
    }

    .el-tree >>> .is-current .el-tree-node__children .el-tree-node__label {
        color: #606266;
        font-weight: normal;
    }

</style>
