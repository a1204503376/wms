<template>
    <span>
        <el-input
                placeholder="请选择用户"
                v-model="text"
                readonly
                class="select-input"
                suffix-icon="el-icon-user-solid"
                v-on:click.native="visible=true">
        </el-input>
        <el-dialog
                title="用户选择"
                :visible.sync="visible"
                :close-on-click-modal="false"
                :modal="false"
                @close="cancelUser"
                option="option"
                data="data"
                width="80%"
                top="3vh"
        >
            <el-form :model="searchData" label-position="right" label-width="80px" ref="newForm">
                <div class="dialog__body">
                    <el-row :gutter="20">
                        <el-col :span="4">
                            <el-form-item label="所属租户" prop="tenantId">
                                <treeSelect
                                        placeholder="请选择所属租户"
                                        v-model="searchData.tenantId"
                                        :props="selectTreeOptions.props"
                                        :options="dic"
                                        :value="selectTreeOptions.valueId"
                                        :clearable="selectTreeOptions.isClearable"
                                        :accordion="selectTreeOptions.isAccordion"
                                        />
                            </el-form-item>
                        </el-col>
                        <el-col :span="4">
                            <el-form-item label="用户昵称" prop="name">
                                <el-input type="text" v-model="searchData.name" placeholder="请输入用户昵称"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4">
                            <el-form-item label="用户姓名" prop="realName">
                                <el-input type="text" v-model="searchData.realName" placeholder="请输入用户姓名"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4">
                            <el-form-item label="用户角色" prop="roleId">
                                <treeSelect
                                        placeholder="请选择所属角色"
                                        v-model="searchData.roleId"
                                        :props="selectTreeOptions.props"
                                        :options="role"
                                        :value="selectTreeOptions.valueId"
                                        :clearable="selectTreeOptions.isClearable"
                                        :accordion="selectTreeOptions.isAccordion"
                                        />
                            </el-form-item>
                        </el-col>
                        <el-col :span="4">
                            <el-form-item label="用户机构" prop="deptId">
                                <treeSelect
                                        placeholder="请选择用户所属机构"
                                        v-model="searchData.deptId"
                                        :props="selectTreeOptions.props"
                                        :options="dept"
                                        :value="selectTreeOptions.valueId"
                                        :clearable="selectTreeOptions.isClearable"
                                        :accordion="selectTreeOptions.isAccordion"
                                        />
                            </el-form-item>
                        </el-col>
                        <el-col :span="4">
                            <el-button type="primary" size="small" @click="searchBtn(searchData)">查 询</el-button>
                            <el-button type="danger" size="small" @click="searchReset">重 置</el-button>
                        </el-col>
                    </el-row>
                </div>
                 <el-row>
                <el-col :span="24">
                    <el-table
                    :data="gridData"
                    border
                    @current-change="handleCurrentChange"
                    @cell-dblclick="dblhandleCurrentChange"
                    highlight-current-row
                    ref="singleTable"
                    :header-cell-style="{'background-color': '#fafafa'}"
                    height="400px"
                    max-height="400px"
                    style="cursor:pointer;"
                    overflow="auto"
                    >
                    <el-table-column property="tenantName" label="所属租户" align="center"></el-table-column>
                    <el-table-column property="name" label="用户昵称" align="center"></el-table-column>
                    <el-table-column property="realName" label="用户姓名" align="center"></el-table-column>
                    <el-table-column property="roleName" label="用户角色" width="150" align="center"></el-table-column>
                    <el-table-column property="deptName" label="机构" width="200" align="center"></el-table-column>
                    </el-table>
                </el-col>
              </el-row>
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-pagination
                        style="float:left"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange2"
                        :page-sizes="[10, 20, 30, 40]"
                        :page-size="100"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="pageTotal"
                ></el-pagination>
                <el-button @click="cancelUser">取 消</el-button>
                <el-button type="primary" @click="addUser">确 定</el-button>
            </div>
        </el-dialog>
    </span>
</template>

<script>
    import treeSelect from "./treeSelect";
    import request from '@/router/axios'; //引入请求
    import {getDeptTree} from "@/api/core/dept";
    import {getRoleTree} from "@/api/core/role";

    export default {
        name: "selectUser",
        components: {treeSelect},
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {},
        data() {
            return {
                text: '',
                value:'',
                dic: [],
                role: [],
                dept: [],
                visible: false,
                currentRow: null, //选中货主
                pageTotal: 0, //数据总数量
                gridData: [],
                gridData2: [],
                searchData: {
                    tenantId: "",
                    name: "",
                    realName: "",
                    roleId: "",
                    deptId: ""
                },
                selectTreeOptions: {
                    isClearable: false,      // 可清空（可选）
                    isAccordion: true,      // 可收起（可选）
                    valueId: 20,            // 初始ID（可选）
                    props: {                // 配置项（必选）
                        value: 'value',
                        label: 'label',
                        children: 'children'
                        // disabled:true
                    },
                },
            };
        },
        created() {
            this.defaultRender();
            this.getData();
        },
        updated() {
            if (this.$refs.singleTable && this.gridData && this.gridData.length > 0) {
                this.$refs.singleTable.setCurrentRow(this.gridData[0]);
            }
        },
        watch: {
            'searchData.tenantId'() {
                this.searchData.roleId = '';
                this.searchData.deptId = '';

                if (this.searchData.tenantId !== '') {
                    getDeptTree(this.searchData.tenantId).then(res => {
                        this.dept = res.data.data.map(org => this.mapTree(org));
                    });
                    getRoleTree(this.searchData.tenantId).then(res => {
                        this.role = res.data.data.map(org => this.mapTree(org));
                    });
                }
            }
        },
        methods: {
            searchReset() {
                this.defaultRender();
                this.searchData.tenantId = "";
                this.searchData.name = "";
                this.searchData.realName = "";
                this.searchData.roleId = "";
                this.searchData.deptId = "";
            },
            mapTree(org) {
                const haveChildren =
                    Array.isArray(org.children) && org.children.length > 0;
                return {
                    //分别将我们查询出来的值做出改变他的key
                    label: org.title,
                    value: org.value,
                    data: {...org},
                    //判断它是否存在子集，若果存在就进行再次进行遍历操作，知道不存在子集便对其他的元素进行操作
                    children: haveChildren ? org.children.map(i => this.mapTree(i)) : []
                };
            },
            TmapTree(org) {
                const haveChildren = Array.isArray(org.data) && org.data.length > 0;
                return {
                    //分别将我们查询出来的值做出改变他的key
                    label: org.tenantName,
                    value: org.tenantId,
                    data: {...org},
                    //判断它是否存在子集，若果存在就进行再次进行遍历操作，知道不存在子集便对其他的元素进行操作
                    children: haveChildren ? org.data.map(i => this.mapTree(i)) : []
                };
            },
            //获取所属租户名称列表
            getData() {
                request({
                    url: "/blade-system/tenant/select",
                    method: "get"
                }).then(res => {
                    let data = res.data.data;
                    if (Array.isArray(data)) {
                        this.dic = data.map(org => this.TmapTree(org));
                    }
                });
            },
            handleSizeChange(val) {
                request({
                    url: "/blade-user/list?size=" + val,
                    method: "get"
                }).then(res => {
                    this.gridData = res.data.data.records;
                });
            },
            handleCurrentChange2(val) {
                request({
                    url: "/blade-user/list?current=" + val,
                    method: "get"
                }).then(res => {
                    this.gridData = res.data.data.records;
                });
            },
            //获取选中用户的数据
            handleCurrentChange(val) {
                this.currentRow = val;
            },
            //默认渲染页面
            defaultRender() {
                request({
                    url: "/blade-user/list",
                    method: "get"
                }).then(res => {
                    this.gridData = res.data.data;
                    this.gridData2 = res.data.data;
                    this.pageTotal = res.data.data.length;
                });
            },
            //取消按钮事件
            cancelUser() {
                this.visible = false;
            },
            //双击选中
            dblhandleCurrentChange() {
                this.addUser();
            },
            //确定
            addUser() {
                this.visible = false;
                if (this.currentRow) {
                    this.value = this.currentRow.id;
                    this.text = this.currentRow.realName;
                    this.$emit("change", this.currentRow.id);
                }
            },
            //模糊搜索
            searchBtn({tenantId, name, realName, roleId, deptId}) {
                var aa = this.gridData2.filter(item => {
                    let matchTenantId = true; //所属租户 筛选
                    let matchName = true; // 用户昵称 筛选
                    let matchRealName = true; // 用户姓名 筛选
                    let matchRoleId = true; // 用户角色 筛选
                    let matchdeptId = true; // 用户机构 筛选
                    //所属租户查询
                    if (tenantId) {
                        const keyTenantId = tenantId
                            .toUpperCase()
                            .replace(" ", "")
                            .split("");
                        matchTenantId = keyTenantId.every(key =>
                            item.tenantId.toUpperCase().includes(key)
                        );
                    }
                    // 用户昵称模糊搜索;
                    if (name) {
                        const keyName = name
                            .toUpperCase()
                            .replace(" ", "")
                            .split("");
                        matchName = keyName.every(key =>
                            item.name.toUpperCase().includes(key)
                        );
                    }
                    // 用户姓名模糊搜索;
                    if (realName) {
                        const keyRname = realName
                            .toUpperCase() // 转大写
                            .replace(" ", "") // 删掉空格
                            .split(""); // 切割成 单个字
                        matchRealName = keyRname.every(key =>
                            item.realName.toUpperCase().includes(key)
                        );
                    }
                    // 用户角色模糊搜索;
                    if (roleId) {
                        const keyRoleId = roleId
                            .toUpperCase()
                            .replace(" ", "")
                            .split("");
                        matchRoleId = keyRoleId.every(key =>
                            item.roleId.toUpperCase().includes(key)
                        );
                    }
                    // 用户机构模糊搜索;
                    if (deptId) {
                        const keydeptId = deptId
                            .toUpperCase()
                            .replace(" ", "")
                            .split("");
                        matchdeptId = keydeptId.every(key =>
                            item.deptId.toUpperCase().includes(key)
                        );
                    }
                    return (
                        matchTenantId &&
                        matchName &&
                        matchRealName &&
                        matchRoleId &&
                        matchdeptId
                    );
                });
                this.gridData = aa;
            }
        }
    };
</script>

<style lang="scss">
    .select-input .el-input__inner {
        cursor: pointer;
    }
</style>
